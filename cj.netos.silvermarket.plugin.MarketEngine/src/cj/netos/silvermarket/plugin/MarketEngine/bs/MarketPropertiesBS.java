package cj.netos.silvermarket.plugin.MarketEngine.bs;

import java.util.ArrayList;
import java.util.List;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.silvermarket.args.MarketProperty;
import cj.netos.silvermarket.bs.IMarketPropertiesBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "marketPropertiesBS")
public class MarketPropertiesBS implements IMarketPropertiesBS {
	@CjServiceRef
	IMarketStore marketStore;

	@Override
	public void remove(String market, String key) {
		marketStore.home().deleteDocOne(TABLE_KEY, String.format("{'tuple.market':'%s','tuple.key':'%s'}", market, key));
	}

	@Override
	public void put(String market, String key, String value, String desc) {
		if (containsKey(market, key)) {
			remove(market, key);
		}
		MarketProperty property = new MarketProperty(market, key, value, desc);
		marketStore.home().saveDoc(TABLE_KEY, new TupleDocument<>(property));
	}

	@Override
	public boolean containsKey(String market, String key) {
		return marketStore.home().tupleCount(TABLE_KEY, String.format("{'tuple.market':'%s','tuple.key':'%s'}", market, key)) > 0;
	}

	@Override
	public String desc(String market, String key) {
		String cjql = String.format(
				"select {'tuple.desc':1} from tuple %s %s where {'tuple.market':'%s','tuple.key':'%s'}", TABLE_KEY,
				MarketProperty.class.getName(), market, key);
		IQuery<MarketProperty> q = marketStore.home().createQuery(cjql);
		IDocument<MarketProperty> doc = q.getSingleResult();
		if (doc == null)
			return null;
		return doc.tuple().getDesc();
	}

	@Override
	public String get(String market, String key) {
		String cjql = String.format("select {'tuple.value':1} from tuple %s %s where {'tuple.market':'%s','tuple.key':'%s'}",
				TABLE_KEY, MarketProperty.class.getName(), market, key);
		IQuery<MarketProperty> q = marketStore.home().createQuery(cjql);
		IDocument<MarketProperty> doc = q.getSingleResult();
		if (doc == null)
			return null;
		return doc.tuple().getValue();
	}

	@Override
	public String[] enumKey(String market) {
		String cjql = String.format("select {'tuple.key':1} from tuple %s %s where {'tuple.market':'%s'}", TABLE_KEY,
				MarketProperty.class.getName(), market);
		IQuery<MarketProperty> q = marketStore.home().createQuery(cjql);
		List<String> list = new ArrayList<String>();
		List<IDocument<MarketProperty>> docs = q.getResultList();
		for (IDocument<MarketProperty> doc : docs) {
			list.add(doc.tuple().getKey());
		}
		return list.toArray(new String[0]);
	}

	@Override
	public String[] pageKeys(String market, int currPage, int pageSize) {
		String cjql = String.format(
				"select {'tuple.key':1}.limit(%s).skip(%s) from tuple %s %s where {'tuple.market':'%s'}", pageSize,
				currPage, TABLE_KEY, MarketProperty.class.getName(), market);
		IQuery<MarketProperty> q = marketStore.home().createQuery(cjql);
		List<String> list = new ArrayList<String>();
		List<IDocument<MarketProperty>> docs = q.getResultList();
		for (IDocument<MarketProperty> doc : docs) {
			list.add(doc.tuple().getKey());
		}
		return list.toArray(new String[0]);
	}

	@Override
	public long count(String market) {
		return marketStore.home().tupleCount(TABLE_KEY, String.format("{'tuple.market':'%s'}", market));
	}

}
