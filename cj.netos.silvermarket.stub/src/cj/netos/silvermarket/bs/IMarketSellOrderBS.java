package cj.netos.silvermarket.bs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cj.netos.silvermarket.args.Stock;
import cj.studio.ecm.net.CircuitException;

public interface IMarketSellOrderBS {
	static String TABLE_sellorders="orders.sells";
	Map<String,Object> sellOrder(String market, String seller, List<Stock> stocks,BigDecimal sellingPrice) throws CircuitException;
}
