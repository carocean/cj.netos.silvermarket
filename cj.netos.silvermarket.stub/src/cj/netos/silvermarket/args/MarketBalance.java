package cj.netos.silvermarket.args;
//市场余额表

import java.math.BigDecimal;

public class MarketBalance {
	BigDecimal stockPrice;// 帑银现价
	BigDecimal bondQuantitiesBalance;//发债总量余额
	BigDecimal bondAmountBalance;//发债总金额，每单债*面值
	BigDecimal stockQuantitiesBalance;//帑银总量余额
	public BigDecimal getStockPrice() {
		return stockPrice;
	}
	public void setStockPrice(BigDecimal stockPrice) {
		this.stockPrice = stockPrice;
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
