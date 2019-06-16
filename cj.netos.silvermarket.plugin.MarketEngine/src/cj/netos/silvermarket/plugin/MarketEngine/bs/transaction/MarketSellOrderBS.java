package cj.netos.silvermarket.plugin.MarketEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.dealmaking.args.SellOrderStock;
import cj.netos.dealmaking.stub.IDealmakingQueueStub;
import cj.netos.silvermarket.args.SellOrder;
import cj.netos.silvermarket.args.Stock;
import cj.netos.silvermarket.bs.IMarketBalanceBS;
import cj.netos.silvermarket.bs.IMarketPropertiesBS;
import cj.netos.silvermarket.bs.IMarketSellOrderBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.netos.silvermarket.util.BigDecimalConstants;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.annotation.CjStubRef;

@CjBridge(aspects = "@rest")
@CjService(name = "transaction#marketSellOrderBS")
public class MarketSellOrderBS implements IMarketSellOrderBS,BigDecimalConstants {
	@CjServiceRef
	IMarketStore marketStore;

	@CjServiceRef
	IMarketBalanceBS marketBalanceBS;
	@CjStubRef(remote = "rest://backend/dealmaking/", stub = IDealmakingQueueStub.class)
	IDealmakingQueueStub dealmakingQueueStub;
	@CjServiceRef
	IMarketPropertiesBS marketPropertiesBS;
	// 增加委托卖单流水，同时发给卖方队列
	@Override
	public Map<String, Object> sellOrder(String market, String seller, List<Stock> stocks,BigDecimal sellingPrice) throws CircuitException {
		SellOrder order = new SellOrder();
		order.setCtime(System.currentTimeMillis());
		order.setSeller(seller);
		order.setStocks(stocks);
		order.setSellingPrice(sellingPrice);

		String orderno = marketStore.market(market).saveDoc(TABLE_sellorders, new TupleDocument<>(order));
		order.setOrderno(orderno);

		SellOrderStock stock = new SellOrderStock();
		stock.setOrderno(orderno);
		stock.setOtime(order.getCtime());
		stock.setSeller(seller);
		stock.setSellingPrice(order.getSellingPrice());
		BigDecimal feeRate=defaultFreeRate(marketPropertiesBS, market);
		stock.setFeeRate(feeRate);
		List<cj.netos.dealmaking.args.Stock> list = new ArrayList<>();
		for (Stock s : order.getStocks()) {
			cj.netos.dealmaking.args.Stock ds = new cj.netos.dealmaking.args.Stock(s.getIssueno(), s.getQuantities());
			list.add(ds);
		}
		stock.setStocks(list);
		try {
			this.dealmakingQueueStub.putSellingQueue(market, stock);
		} catch (Exception e) {
			CircuitException ce = CircuitException.search(e);
			String status = "";
			String message = String.format("提交到撮合交易引擎出错，原因：%s", e);
			if (ce != null) {
				status = ce.getStatus();
			} else {
				status = "500";
			}
			updateStatus(market, order.getOrderno(), status, message);
			order.setStatus(status);
			order.setMessage(message);
			CJSystem.logging().error(getClass(), message);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("order", order);
		return map;
	}

	private void updateStatus(String market, String orderno, String status, String message) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", orderno));
		Bson update = Document
				.parse(String.format("{'$set':{'tuple.message':'%s','tuple.status':'%s'}}", message, status));
		marketStore.market(market).updateDocOne(TABLE_sellorders, filter, update);
	}

}
