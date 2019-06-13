package cj.netos.silvermarket.program.stub;

import cj.netos.silvermarket.bs.IMarketPropertiesBS;
import cj.netos.silvermarket.stub.IMarketPropertiesStub;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;
@CjService(name="/properties.service")
public class MarketPropertiesStub extends GatewayAppSiteRestStub implements IMarketPropertiesStub {
	@CjServiceRef(refByName = "MarketEngine.marketPropertiesBS")
	IMarketPropertiesBS marketPropertiesBS;
	@Override
	public void put(String market, String key, String value,String desc) {
		marketPropertiesBS.put(market,key,value,desc);
	}

	@Override
	public String get(String market, String key) {
		return marketPropertiesBS.get(market,key);
	}

	@Override
	public String[] enumKey(String market) {
		return marketPropertiesBS.enumKey(market);
	}
	@Override
	public String desc(String market, String key) {
		return marketPropertiesBS.desc(market,key);
	}
	@Override
	public String[] pageKeys(String market, int currPage, int pageSize) {
		return marketPropertiesBS.pageKeys(market,currPage,pageSize);
	}

	@Override
	public long count(String market) {
		return marketPropertiesBS.count(market);
	}
	@Override
	public void remove(String market, String key) {
		marketPropertiesBS.remove(market, key);;
	}

}
