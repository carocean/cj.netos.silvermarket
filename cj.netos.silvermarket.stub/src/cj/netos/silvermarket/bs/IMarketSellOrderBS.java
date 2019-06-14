package cj.netos.silvermarket.bs;

import java.util.List;
import java.util.Map;

import cj.netos.silvermarket.args.Stock;

public interface IMarketSellOrderBS {
	static String TABLE_sellorders="orders.sells";
	Map<String,Object> sellOrder(String market, String seller, List<Stock> stocks);
}
