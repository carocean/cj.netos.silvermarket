package cj.netos.silvermarket.bs;

import java.util.List;

import cj.netos.silvermarket.args.MarketInfo;
import cj.studio.ecm.net.CircuitException;

public interface IMarketInfoBS {
	static String TABLE_Market_INFO = "markets";

	void saveMarket(MarketInfo info) throws CircuitException;

	void updateMarketName(String market, String name);

	void updateMarketPresident(String market, String president);

	void updateMarketCompany(String market, String company);

	MarketInfo getMarketInfo(String market);

	List<MarketInfo> pageMarketInfo(int currPage, int pageSize);

	boolean existsMarketName(String name);

	boolean existsMarketCode(String market);

	boolean isExpired(String market) throws CircuitException;

}
