package cj.netos.silvermarket.plugin.MarketEngine.bs;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.silvermarket.args.MarketInfo;
import cj.netos.silvermarket.bs.IMarketInfoBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;

@CjService(name = "marketInfoBS")
public class MarketInfoBS implements IMarketInfoBS {
	@CjServiceRef
	IMarketStore marketStore;
	@Override
	public boolean existsMarketName(String name) {
		String where = String.format("{'tuple.name':'%s'}", name);
		return marketStore.home().tupleCount(TABLE_Market_INFO, where) > 0;
	}

	@Override
	public void saveMarket(MarketInfo info) throws CircuitException {
		if (existsMarketName(info.getName())) {
			throw new CircuitException("405", "已存在市场名为：" + info.getName());
		}
		info.setCode(null);
		String id =  marketStore.home().saveDoc(TABLE_Market_INFO, new TupleDocument<>(info));
		info.setCode(id);
	}

	@Override
	public boolean existsMarketCode(String market) {
		String where = String.format("{'_id':ObjectId('%s')}", market);
		return  marketStore.home().tupleCount(TABLE_Market_INFO, where) > 0;
	}

	@Override
	public MarketInfo getMarketInfo(String market) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'_id':ObjectId('%s')}",
				TABLE_Market_INFO, MarketInfo.class.getName(), market);
		IQuery<MarketInfo> q =  marketStore.home().createQuery(cjql);
		IDocument<MarketInfo> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setCode(doc.docid());
		return doc.tuple();
	}

	@Override
	public List<MarketInfo> pageMarketInfo(int currPage, int pageSize) {
		String cjql = String.format("select {'tuple':'*'}.limit(%s).skip(%s) from tuple %s %s where {}", pageSize,
				currPage, TABLE_Market_INFO, MarketInfo.class.getName());
		IQuery<MarketInfo> q =  marketStore.home().createQuery(cjql);
		List<IDocument<MarketInfo>> docs = q.getResultList();
		List<MarketInfo> list = new ArrayList<MarketInfo>();
		for (IDocument<MarketInfo> doc : docs) {
			doc.tuple().setCode(doc.docid());
			list.add(doc.tuple());
		}
		return list;
	}

	@Override
	public void updateMarketName(String market, String name) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.name':'%s'}}", name));
		 marketStore.home().updateDocOne(TABLE_Market_INFO, filter, update);
	}

	@Override
	public void updateMarketPresident(String market, String president) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.president':'%s'}}", president));
		 marketStore.home().updateDocOne(TABLE_Market_INFO, filter, update);
	}

	@Override
	public void updateMarketCompany(String market, String company) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.company':'%s'}}", company));
		 marketStore.home().updateDocOne(TABLE_Market_INFO, filter, update);
	}

	@Override
	public boolean isExpired(String market) throws CircuitException {
		MarketInfo info = getMarketInfo(market);
		if (info == null) {
			throw new CircuitException("404",
					String.format("The marketno %s of silvermarket does not exist. ", market));
		}
		return info.getExpiredTime() <= System.currentTimeMillis();
	}
}
