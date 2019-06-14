package cj.netos.silvermarket.plugin.MarketEngine.dealmaking;

import cj.netos.silvermarket.args.DealmakingContract;
import cj.netos.silvermarket.bs.IMarketBuyOrderQueueBS;
import cj.netos.silvermarket.bs.IMarketSellOrderQueueBS;
import cj.netos.silvermarket.dealmaking.IDealmakingStrategy;

public class DefaultDealmakingStrategy implements IDealmakingStrategy {

	@Override
	public DealmakingContract dealmaking(IMarketSellOrderQueueBS marketSellOrderQueueBS,
			IMarketBuyOrderQueueBS marketBuyOrderQueueBS) {
		System.out.println("..........");
		return null;
	}

}
