package cj.netos.silvermarket.dealmaking;

import cj.netos.silvermarket.args.DealmakingContract;
import cj.netos.silvermarket.bs.IMarketBuyOrderQueueBS;
import cj.netos.silvermarket.bs.IMarketSellOrderQueueBS;

public interface IDealmakingStrategy {

	DealmakingContract dealmaking(IMarketSellOrderQueueBS marketSellOrderQueueBS,
			IMarketBuyOrderQueueBS marketBuyOrderQueueBS);

}
