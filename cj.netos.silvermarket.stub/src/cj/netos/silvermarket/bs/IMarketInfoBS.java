package cj.netos.silvermarket.bs;

import cj.netos.silvermarket.args.MarketInfo;

public interface IMarketInfoBS {

	boolean isExpired(String market);

	MarketInfo getMarketInfo(String market);

}
