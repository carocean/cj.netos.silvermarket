package cj.netos.silvermarket.bs;

import java.math.BigDecimal;
import java.util.Map;

import cj.studio.ecm.net.CircuitException;

public interface IMarketIssueBondBS {
	static String TABLE_IssueBill="bill.issues";
	Map<String, Object> issueOrder(String market, String issuer, String bondbank, BigDecimal bondQuantities,
			BigDecimal bondFaceValue) throws CircuitException;

	

}
