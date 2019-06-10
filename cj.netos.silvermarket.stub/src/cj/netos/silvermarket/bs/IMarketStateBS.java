package cj.netos.silvermarket.bs;

import cj.netos.silvermarket.args.MarketState;

public interface IMarketStateBS {

	void save(MarketState state);

	MarketState getState(String market);

}
