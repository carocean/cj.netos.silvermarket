package cj.netos.silvermarket.bs;

import java.math.BigDecimal;

import cj.netos.silvermarket.args.BondBankBalance;
import cj.netos.silvermarket.args.MarketBalance;

public interface IMarketBalanceBS {
	static String TABLE_MarketBalance = "balance.market";
	static String TABLE_BondbankBalance = "balance.market.bondbank";

	/**
	 * 帑银现价，可能为空（当市场没有一笔成交时便没有现价）
	 * 
	 * @param market
	 * @return
	 */
	BigDecimal getStockPrice(String market);

	void updateStockprice(String market, BigDecimal stockprice);

	BigDecimal getBondQuantitiesBalance(String market);

	BigDecimal getBondAmountBalance(String market);

	BigDecimal getStockQuantitiesBalance(String market);

	BigDecimal addBondQuantitiesBalance(String market, BigDecimal bondQuantities);

	BigDecimal addBondAmountBalance(String market, BigDecimal bondAmount);

	BigDecimal addStockQuantitiesBalance(String market, BigDecimal stockQuantities);

	void decBondQuantitiesBalance(String market, BigDecimal bondQuantities);

	void decBondAmountBalance(String market, BigDecimal bondAmount);

	void decStockQuantitiesBalance(String market, BigDecimal stockQuantities);

	/**
	 * 获取债券银行在指定帑银交易市场的市盈率（该市盈率随着商户对市盈率的抄作而变化，如交保证金，或按金证银行方式）
	 */
	BigDecimal getTTM(String market, String bondbank);

	void updateTTM(String market, String bondbank, BigDecimal ttm);

	BigDecimal getBondQuantitiesBalance(String market, String bondbank);

	BigDecimal getBondAmountBalance(String market, String bondbank);

	BigDecimal getStockQuantitiesBalance(String market, String bondbank);

	BigDecimal addBondQuantitiesBalance(String market, String bondbank, BigDecimal bondQuantities);

	BigDecimal addBondAmountBalance(String market, String bondbank, BigDecimal bondAmount);

	BigDecimal addStockQuantitiesBalance(String market, String bondbank, BigDecimal stockQuantities);

	void decBondQuantitiesBalance(String market, String bondbank, BigDecimal bondQuantities);

	void decBondAmountBalance(String market, String bondbank, BigDecimal bondAmount);

	void decStockQuantitiesBalance(String market, String bondbank, BigDecimal stockQuantities);

	MarketBalance getMarketBalance(String market);

	BondBankBalance getBondbankBalance(String market, String bondbank);
}
