package cj.netos.silvermarket.plugin.MarketEngine;

import cj.netos.silvermarket.dealmaking.IDealmakingEngine;
import cj.studio.ecm.IEntryPointActivator;
import cj.studio.ecm.IServiceProvider;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.ServiceCollection;
import cj.studio.ecm.context.IElement;
import cj.studio.ecm.context.INode;
import cj.studio.ecm.context.IProperty;

public class DealmakingEntrypointActivitor implements IEntryPointActivator {
	IDealmakingEngine engine;

	@Override
	public void activate(IServiceSite site, IElement args) {
		engine = (IDealmakingEngine) site.getService("dealmakingEngine");
		IProperty workThreadsProp = (IProperty) args.getNode("workThreads");
		String workThreads = workThreadsProp.getValue() == null ? "8" : ((INode) workThreadsProp.getValue()).getName();

		IProperty dealmakingStrategyProp = (IProperty) args.getNode("dealmakingStrategy");
		String dealmakingStrategy = dealmakingStrategyProp.getValue() == null ? ""
				: ((INode) dealmakingStrategyProp.getValue()).getName();
		IServiceProvider parent = new IServiceProvider() {

			@Override
			public <T> ServiceCollection<T> getServices(Class<T> serviceClazz) {
				return site.getServices(serviceClazz);
			}

			@Override
			public Object getService(String serviceId) {
				if ("$.dealmakingEngine.nThreads".equals(serviceId)) {
					return Integer.valueOf(workThreads);
				}
				if ("$.dealmakingEngine.dealmakingStrategy".equals(serviceId)) {
					return dealmakingStrategy;
				}
				return site.getService(serviceId);
			}
		};
		engine.start(parent);
	}

	@Override
	public void inactivate(IServiceSite site) {
		if (engine == null) {
			return;
		}
		engine.stop();
	}

}
