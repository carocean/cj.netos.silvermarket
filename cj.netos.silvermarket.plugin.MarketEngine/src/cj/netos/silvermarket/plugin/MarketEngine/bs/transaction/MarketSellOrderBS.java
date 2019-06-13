package cj.netos.silvermarket.plugin.MarketEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.silvermarket.args.SellOrder;
import cj.netos.silvermarket.args.SellOrderStock;
import cj.netos.silvermarket.args.Stock;
import cj.netos.silvermarket.bs.IMarketBalanceBS;
import cj.netos.silvermarket.bs.IMarketSellOrderBS;
import cj.netos.silvermarket.bs.IMarketSellOrderQueueBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "transaction#marketSellOrderBS")
public class MarketSellOrderBS implements IMarketSellOrderBS {
	@CjServiceRef
	IMarketStore marketStore;

	@CjServiceRef
	IMarketBalanceBS marketBalanceBS;
	@CjServiceRef
	IMarketSellOrderQueueBS marketSellOrderQueueBS;

	// 增加委托卖单流水，同时发给卖方队列
	@Override
	public Map<String, Object> sellOrder(String market, String seller, Stock stock) {
		SellOrder order = new SellOrder();
		order.setCtime(System.currentTimeMillis());
		order.setSeller(seller);
		order.setStock(stock);

		BigDecimal stockPrice = marketBalanceBS.getStockPrice(market);
		order.setStockPrice(stockPrice);

		String orderno = marketStore.market(market).saveDoc(TABLE_sellorders, new TupleDocument<>(order));
		order.setOrderno(orderno);

		SellOrderStock sellOrderStock = new SellOrderStock(order);
		marketSellOrderQueueBS.offer(market,sellOrderStock);
		
		Map<String, Object> map = new HashMap<>();
		map.put("order", order);
		return map;
	}

}
