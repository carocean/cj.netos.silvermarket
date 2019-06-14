package cj.netos.silvermarket.plugin.MarketEngine.dealmaking;

import cj.netos.silvermarket.args.DealmakingContract;
import cj.netos.silvermarket.bs.IMarketBuyOrderQueueBS;
import cj.netos.silvermarket.bs.IMarketSellOrderQueueBS;
import cj.netos.silvermarket.dealmaking.IDealmakingStrategy;
import cj.studio.ecm.EcmException;
import cj.studio.ecm.IServiceProvider;
import cj.ultimate.util.StringUtil;

public class DealmakingTask implements IDealmakingTask {
	IMarketSellOrderQueueBS marketSellOrderQueueBS;
	IMarketBuyOrderQueueBS marketBuyOrderQueueBS;
	Class<IDealmakingStrategy> defaultClazz;

	@SuppressWarnings("unchecked")
	public DealmakingTask(IServiceProvider site) {
		marketSellOrderQueueBS = (IMarketSellOrderQueueBS) site.getService("marketSellOrderQueueBS");
		marketBuyOrderQueueBS = (IMarketBuyOrderQueueBS) site.getService("marketBuyOrderQueueBS");
		String clazzStr = (String) site.getService("$.dealmakingEngine.dealmakingStrategy");
		if (!StringUtil.isEmpty(clazzStr)) {
			try {
				defaultClazz = (Class<IDealmakingStrategy>) Class.forName(clazzStr);
				if (!IDealmakingStrategy.class.isAssignableFrom(defaultClazz)) {
					throw new EcmException("类未实现撮合交易策略接口：" + clazzStr);
				}
			} catch (ClassNotFoundException e) {
				throw new EcmException(e);
			}
		}
	}

	@Override
	public DealmakingContract call() throws Exception {
		IDealmakingStrategy strategy = createDealmakingStrategy();
		return strategy.dealmaking(marketSellOrderQueueBS, marketBuyOrderQueueBS);
	}

	protected IDealmakingStrategy createDealmakingStrategy() {
		if (defaultClazz == null)
			return new DefaultDealmakingStrategy();
		try {
			return defaultClazz.newInstance();
		} catch (InstantiationException e) {
			throw new EcmException(e);
		} catch (IllegalAccessException e) {
			throw new EcmException(e);
		}
	}

}
