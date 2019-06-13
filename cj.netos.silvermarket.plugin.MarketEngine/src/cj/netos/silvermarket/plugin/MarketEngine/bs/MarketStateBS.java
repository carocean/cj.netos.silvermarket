package cj.netos.silvermarket.plugin.MarketEngine.bs;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.UpdateOptions;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.silvermarket.args.BState;
import cj.netos.silvermarket.args.MarketState;
import cj.netos.silvermarket.bs.IMarketStateBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "marketStateBS")
public class MarketStateBS implements IMarketStateBS {
	@CjServiceRef
	IMarketStore marketStore;
	@Override
	public void save(MarketState state) {
		state.setId(null);
		String id = marketStore.home().saveDoc(TABLE_MARKET_STATE, new TupleDocument<>(state));
		state.setId(id);

	}

	@Override
	public MarketState getState(String market) {
		String cjql = "select {'tuple':'*'} from tuple ?(colName) ?(colType) where {'tuple.bank':'?(bank)'}";
		IQuery<MarketState> q = marketStore.home().createQuery(cjql);
		q.setParameter("colName", TABLE_MARKET_STATE);
		q.setParameter("colType", MarketState.class.getName());
		q.setParameter("bank", market);
		IDocument<MarketState> doc = q.getSingleResult();
		if (doc == null) {
			MarketState state = new MarketState();
			state.setBank(market);
			state.setCtime(System.currentTimeMillis());
			state.setState(BState.opened);
			return state;
		}
		return doc.tuple();
	}

	@Override
	public void revokeMarket(String market) {
		Bson filter = Document.parse(String.format("{'tuple.bank':'%s'}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.state':'%s'}}", BState.revoke));
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		marketStore.home().updateDocOne(TABLE_MARKET_STATE, filter, update, uo);
	}

	@Override
	public void freezeMarket(String market) {
		Bson filter = Document.parse(String.format("{'tuple.bank':'%s'}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.state':'%s'}}", BState.freeze));
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		marketStore.home().updateDocOne(TABLE_MARKET_STATE, filter, update, uo);
	}

	@Override
	public void closedMarket(String market) {
		Bson filter = Document.parse(String.format("{'tuple.bank':'%s'}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.state':'%s'}}", BState.closed));
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		marketStore.home().updateDocOne(TABLE_MARKET_STATE, filter, update, uo);
	}

	@Override
	public void resumeMarket(String market) {
		Bson filter = Document.parse(String.format("{'tuple.bank':'%s'}", market));
		Bson update = Document.parse(String.format("{'$set':{'tuple.state':'%s'}}", BState.opened));
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		marketStore.home().updateDocOne(TABLE_MARKET_STATE, filter, update, uo);
	}

}
