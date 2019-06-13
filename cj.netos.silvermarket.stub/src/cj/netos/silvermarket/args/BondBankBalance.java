package cj.netos.silvermarket.args;

import java.math.BigDecimal;

/**
 * 商户的债券银行在帑银交易市场的余额
 * 
 * @author caroceanjofers
 *
 */
public class BondBankBalance {
	String bondbank;
	BigDecimal ttm;// 商户银行市盈率，商户抄市盈市盈率会经常变动，因此放到余额表中
	BigDecimal bondQuantitiesBalance;// 发债总量余额
	BigDecimal bondAmountBalance;// 发债总金额，每单债*面值
	BigDecimal stockQuantitiesBalance;// 帑银总量余额
	
	public String getBondbank() {
		return bondbank;
	}

	public void setBondbank(String bondbank) {
		this.bondbank = bondbank;
	}
	
	public BigDecimal getTtm() {
		return ttm;
	}

	public void setTtm(BigDecimal ttm) {
		this.ttm = ttm;
	}

	public BigDecimal getBondQuantitiesBalance() {
		return bondQuantitiesBalance;
	}

	public void setBondQuantitiesBalance(BigDecimal bondQuantitiesBalance) {
		this.bondQuantitiesBalance = bondQuantitiesBalance;
	}

	public BigDecimal getBondAmountBalance() {
		return bondAmountBalance;
	}

	public void setBondAmountBalance(BigDecimal bondAmountBalance) {
		this.bondAmountBalance = bondAmountBalance;
	}

	public BigDecimal getStockQuantitiesBalance() {
		return stockQuantitiesBalance;
	}

	public void setStockQuantitiesBalance(BigDecimal stockQuantitiesBalance) {
		this.stockQuantitiesBalance = stockQuantitiesBalance;
	}

}
