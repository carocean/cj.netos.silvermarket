package cj.netos.silvermarket.plugin.MarketEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.dealmaking.args.BuyOrderStock;
import cj.netos.dealmaking.stub.IDealmakingQueueStub;
import cj.netos.silvermarket.args.BuyOrder;
import cj.netos.silvermarket.bs.IMarketBalanceBS;
import cj.netos.silvermarket.bs.IMarketBuyOrderBS;
import cj.netos.silvermarket.bs.IMarketPropertiesBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.netos.silvermarket.util.BigDecimalConstants;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.annotation.CjStubRef;

@CjBridge(aspects = "@rest")
@CjService(name = "transaction#marketBuyOrderBS")
public class MarketBuyOrderBS implements IMarketBuyOrderBS,BigDecimalConstants {
	@CjServiceRef
	IMarketStore marketStore;

	@CjServiceRef
	IMarketBalanceBS marketBalanceBS;

	@CjStubRef(remote = "rest://backend/dealmaking/", stub = IDealmakingQueueStub.class)
	IDealmakingQueueStub dealmakingQueueStub;
	@CjServiceRef
	IMarketPropertiesBS marketPropertiesBS;
	@Override
	public Map<String, Object> buyOrder(String market, String buyer, BigDecimal amount,BigDecimal buyingPrice) throws CircuitException {
		BuyOrder order = new BuyOrder();
		order.setAmount(amount);
		order.setBuyer(buyer);
		order.setOtime(System.currentTimeMillis());
		order.setBuyingPrice(buyingPrice);
		
		String no = marketStore.market(market).saveDoc(TABLE_buyorders, new TupleDocument<>(order));
		order.setNo(no);

		BuyOrderStock stock = new BuyOrderStock();
		stock.setAmount(order.getAmount());
		stock.setBuyer(order.getBuyer());
		stock.setBuyingPrice(order.getBuyingPrice());
		stock.setBuyorderno(order.getNo());
		stock.setOtime(order.getOtime());
		BigDecimal feeRate=defaultFreeRate(marketPropertiesBS, market);
		stock.setFeeRate(feeRate);
		try {
			dealmakingQueueStub.putBuyingQueue(market, stock);
		} catch (Exception e) {
			CircuitException ce = CircuitException.search(e);
			String status = "";
			String message = String.format("提交到撮合交易引擎出错，原因：%s", e);
			if (ce != null) {
				status = ce.getStatus();
			} else {
				status = "500";
			}
			updateStatus(market, order.getNo(), status, message);
			order.setStatus(status);
			order.setMessage(message);
			CJSystem.logging().error(getClass(), status + " " + message);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order", order);
		return map;
	}

	private void updateStatus(String market, String orderno, String status, String message) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", orderno));
		Bson update = Document
				.parse(String.format("{'$set':{'tuple.message':'%s','tuple.status':'%s'}}", message, status));
		marketStore.market(market).updateDocOne(TABLE_buyorders, filter, update);
	}
}
