package cj.netos.silvermarket.dealmaking;

import cj.studio.ecm.IServiceProvider;

//撮合交易引擎
public interface IDealmakingEngine {
	void start(IServiceProvider site);
	void stop();
}
