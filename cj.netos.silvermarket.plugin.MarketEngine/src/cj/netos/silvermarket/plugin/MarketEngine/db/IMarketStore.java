package cj.netos.silvermarket.plugin.MarketEngine.db;

import cj.lns.chip.sos.cube.framework.ICube;

public interface IMarketStore {

	ICube market(String market);

	ICube home();

}