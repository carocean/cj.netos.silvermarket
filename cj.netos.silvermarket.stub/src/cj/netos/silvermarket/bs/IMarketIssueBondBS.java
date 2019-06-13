package cj.netos.silvermarket.bs;

import java.math.BigDecimal;
import java.util.Map;

public interface IMarketIssueBondBS {
	static String TABLE_IssueBill="bill.issues";
	Map<String, Object> issueOrder(String market, String issuer, String bondbank, BigDecimal bondQuantities,
			BigDecimal bondFaceValue);

	

}
