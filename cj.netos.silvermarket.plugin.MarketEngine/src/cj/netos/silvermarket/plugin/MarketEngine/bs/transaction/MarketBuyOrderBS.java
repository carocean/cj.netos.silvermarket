package cj.netos.silvermarket.plugin.MarketEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.silvermarket.args.BuyOrder;
import cj.netos.silvermarket.args.BuyOrderStock;
import cj.netos.silvermarket.bs.IMarketBalanceBS;
import cj.netos.silvermarket.bs.IMarketBuyOrderBS;
import cj.netos.silvermarket.bs.IMarketBuyOrderQueueBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "transaction#marketBuyOrderBS")
public class MarketBuyOrderBS implements IMarketBuyOrderBS {
	@CjServiceRef
	IMarketStore marketStore;

	@CjServiceRef
	IMarketBalanceBS marketBalanceBS;
	@CjServiceRef
	IMarketBuyOrderQueueBS marketBuyOrderQueueBS;

	@Override
	public Map<String, Object> buyOrder(String market, String buyer, BigDecimal amount) {
		BuyOrder order = new BuyOrder();
		order.setAmount(amount);
		order.setBuyer(buyer);
		order.setOtime(System.currentTimeMillis());

		String no = marketStore.market(market).saveDoc(TABLE_buyorders, new TupleDocument<>(order));
		order.setNo(no);

		BuyOrderStock buyOrderStock = new BuyOrderStock(order);
		marketBuyOrderQueueBS.offer(market,buyOrderStock);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order", order);
		return map;
	}

}
