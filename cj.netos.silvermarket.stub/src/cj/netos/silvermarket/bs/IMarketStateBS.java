package cj.netos.silvermarket.bs;

import cj.netos.silvermarket.args.MarketState;

public interface IMarketStateBS {
	static String TABLE_MARKET_STATE="bstates";
	
	void save(MarketState state);

	MarketState getState(String market);
	void revokeMarket(String market);

	void freezeMarket(String market);

	void closedMarket(String market);

	void resumeMarket(String market);
}
