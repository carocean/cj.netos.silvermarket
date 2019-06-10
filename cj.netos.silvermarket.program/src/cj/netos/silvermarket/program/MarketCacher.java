package cj.netos.silvermarket.program;

import java.util.HashMap;
import java.util.Map;

import cj.netos.silvermarket.args.MarketInfo;
import cj.netos.silvermarket.bs.IMarketInfoBS;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "marketCacher")
public class MarketCacher implements IMarketCacher {
	@CjServiceRef(refByName = "FSBAEngine.marketInfoBS")
	IMarketInfoBS marketInfoBS;
	Map<String, MarketInfo> marketInfos;

	public MarketCacher() {
		marketInfos = new HashMap<String, MarketInfo>();
	}


	@Override
	public MarketInfo getMarketInfo(String market) {
		MarketInfo r = marketInfos.get(market);
		if (r != null) {
			return r;
		}
		r = marketInfoBS.getMarketInfo(market);
		marketInfos.put(market, r);
		return r;
	}

}
