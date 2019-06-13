package cj.netos.silvermarket.bs;

import java.math.BigDecimal;
import java.util.Map;

public interface IMarketBuyOrderBS {
	static String TABLE_buyorders = "orders.buies";

	Map<String, Object> buyOrder(String market, String buyer, BigDecimal amount);

}
