package cj.netos.silvermarket.plugin.MarketEngine.bs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.UpdateOptions;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.silvermarket.args.BuyOrderStock;
import cj.netos.silvermarket.args.SellOrderStock;
import cj.netos.silvermarket.bs.IMarketBuyOrderQueueBS;
import cj.netos.silvermarket.bs.IMarketPropertiesBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.ultimate.util.StringUtil;

@CjService(name = "marketBuyOrderQueueBS")
public class MarketBuyOrderQueueBS implements IMarketBuyOrderQueueBS {
	@CjServiceRef
	IMarketStore marketStore;
	@CjServiceRef
	IMarketPropertiesBS marketPropertiesBS;

	@Override
	public void offer(String market, BuyOrderStock buyOrderStock) {
		String value = marketPropertiesBS.get(market, "init.buyOrderQueue.isCreateIndex");
		if (StringUtil.isEmpty(value) || "false".equals(value)) {
			String result = marketStore.market(market).createIndex(TABLE_queue_buyOrder,
					Document.parse("{'tuple.buyingPrice':-1,'tuple.otime':1}"));// 价格降、委托时间降，意为：最高价且越早的越优先成交，并且价格优先于时间
			System.out.println(result);
			marketPropertiesBS.put(market, "init.sellOrderQueue.isCreateIndex", "true", "判断委托卖出队列是否已创建索引");
		}
		String id = marketStore.market(market).saveDoc(TABLE_queue_buyOrder, new TupleDocument<>(buyOrderStock));
		buyOrderStock.setNo(id);
	}

	@Override
	public BuyOrderStock peek(String market) {
		String cjql = String.format(
				"select {'tuple':'*'}.sort({'tuple.buyingPrice':-1,'tuple.otime':1}).skip(0).limit(1) from tuple %s %s where {}",
				TABLE_queue_buyOrder, SellOrderStock.class.getName(), market);
		IQuery<BuyOrderStock> q = marketStore.market(market).createQuery(cjql);
		IDocument<BuyOrderStock> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setNo(doc.docid());
		return doc.tuple();
	}

	@Override
	public void remove(String market) {
		BuyOrderStock stock = peek(market);
		if (stock == null) {
			return;
		}
		remove(market, stock.getNo());
	}

	@Override
	public void remove(String market, String stockno) {
		marketStore.market(market).deleteDocOne(TABLE_queue_buyOrder, String.format("{'_id':ObjectId('%s')}", stockno));
	}

	@Override
	public void updateAmount(String market, String stockno, BigDecimal amount) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", stockno));
		Bson update = Document.parse(String.format("{'$set':{'tuple.amount':%s}}", amount));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_queue_buyOrder, filter, update, op);
	}

	@Override
	public List<BuyOrderStock> listFiveBuyingWindow(String market) {
		String cjql = String.format(
				"select {'tuple':'*'}.sort({'tuple.buyingPrice':-1,'tuple.otime':1}).skip(0).limit(5) from tuple %s %s where {}",
				TABLE_queue_buyOrder, BuyOrderStock.class.getName(), market);
		IQuery<BuyOrderStock> q = marketStore.market(market).createQuery(cjql);
		List<IDocument<BuyOrderStock>> docs = q.getResultList();
		List<BuyOrderStock> list = new ArrayList<>();
		for (IDocument<BuyOrderStock> doc : docs) {
			BuyOrderStock stock = doc.tuple();
			stock.setNo(doc.docid());
			list.add(stock);
		}
		return list;
	}
}
