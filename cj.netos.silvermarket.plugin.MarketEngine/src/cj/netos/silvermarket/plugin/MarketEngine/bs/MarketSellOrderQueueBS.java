package cj.netos.silvermarket.plugin.MarketEngine.bs;

import org.bson.Document;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.silvermarket.args.SellOrderStock;
import cj.netos.silvermarket.args.Stock;
import cj.netos.silvermarket.bs.IMarketPropertiesBS;
import cj.netos.silvermarket.bs.IMarketSellOrderQueueBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.ultimate.util.StringUtil;

@CjService(name = "marketSellOrderQueueBS")
public class MarketSellOrderQueueBS implements IMarketSellOrderQueueBS {
	@CjServiceRef
	IMarketStore marketStore;
	@CjServiceRef
	IMarketPropertiesBS marketPropertiesBS;

	@Override
	public void offer(String market, SellOrderStock sellOrderStock) {
		String value = marketPropertiesBS.get(market, "init.sellOrderQueue.isCreateIndex");
		if (StringUtil.isEmpty(value) || "false".equals(value)) {
			String result = marketStore.market(market).createIndex(TABLE_queue_sellOrder,
					Document.parse("{'tuple.stockPrice':1,'tuple.otime':1}"));// 价格升序、委托时间升序，意为：最低价且越早的越优先成交，并且价格优先于时间
			System.out.println(result);
			marketPropertiesBS.put(market, "init.sellOrderQueue.isCreateIndex", "true", "判断委托卖出队列是否已创建索引");
		}
		String id = marketStore.market(market).saveDoc(TABLE_queue_sellOrder, new TupleDocument<>(sellOrderStock));
		sellOrderStock.setNo(id);
	}
	@Override
	public SellOrderStock peek(String market) {
		String cjql = String.format("select {'tuple':'*'}.sort({'tuple.stockPrice':1,'tuple.otime':1}).skip(0).limit(1) from tuple %s %s where {}",
				TABLE_queue_sellOrder, SellOrderStock.class.getName(), market);
		IQuery<SellOrderStock> q = marketStore.market(market).createQuery(cjql);
		IDocument<SellOrderStock> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setNo(doc.docid());
		return doc.tuple();
	}

	@Override
	public void remove(String market) {
		SellOrderStock stock = peek(market);
		if (stock == null) {
			return;
		}
		remove(market, stock.getNo());
	}
	@Override
	public void remove(String market, String no) {
		marketStore.market(market).deleteDocOne(TABLE_queue_sellOrder, String.format("{'_id':ObjectId('%s')}", no));
	}
	@Override
	public SellOrderStock addStock(String market, String stockno, Stock stock) {
		
		return null;
	}
	@Override
	public SellOrderStock decAmount(String market, String stockno, Stock stock) {
		// TODO Auto-generated method stub
		return null;
	}

}
