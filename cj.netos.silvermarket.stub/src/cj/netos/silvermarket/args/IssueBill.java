package cj.netos.silvermarket.args;
//发行单

import java.math.BigDecimal;

public class IssueBill {
	String issueno;
	String issuer;
	long ctime;
	String bondBank;//发行方自哪个债券银行发行
	BigDecimal bondQuantities;
	BigDecimal bondFaceValue;
	BigDecimal ttm;//市盈率:是交易市场认定商户的债券银行的，也就是issuer商户的债券银行.如果不申请则默认为0。市盈率通过抄作获取，所以需要商户投资来抄
	BigDecimal issueprice;//发行时的当前价格，如果当前市场无成交价，则默认按第一个发行的面值作为市场成交起始价
	BigDecimal stockPrincipal;//股本金
	BigDecimal bondsOfStockRate;//张比
	BigDecimal stockQuantities;//帑银量
	String sellOrderno;//委托卖单号
	MarketBalance marketBalance;
	BondBankBalance bondbankBalance;
	public BondBankBalance getBondbankBalance() {
		return bondbankBalance;
	}
	public void setBondbankBalance(BondBankBalance bondbankBalance) {
		this.bondbankBalance = bondbankBalance;
	}
	public MarketBalance getMarketBalance() {
		return marketBalance;
	}
	public void setMarketBalance(MarketBalance marketBalance) {
		this.marketBalance = marketBalance;
	}
	public String getSellOrderno() {
		return sellOrderno;
	}
	public void setSellOrderno(String sellOrderno) {
		this.sellOrderno = sellOrderno;
	}
	//帑银现值的升值过程在bond.price.histories表中记录。交易市场按频率主动到对方所在的金证银行查询证券价格
	public String getIssueno() {
		return issueno;
	}
	public void setIssueno(String issueno) {
		this.issueno = issueno;
	}
	public BigDecimal getStockQuantities() {
		return stockQuantities;
	}
	public void setStockQuantities(BigDecimal stockQuantities) {
		this.stockQuantities = stockQuantities;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public String getBondBank() {
		return bondBank;
	}
	public void setBondBank(String bondBank) {
		this.bondBank = bondBank;
	}
	public BigDecimal getBondQuantities() {
		return bondQuantities;
	}
	public void setBondQuantities(BigDecimal bondQuantities) {
		this.bondQuantities = bondQuantities;
	}
	public BigDecimal getBondFaceValue() {
		return bondFaceValue;
	}
	public void setBondFaceValue(BigDecimal bondFaceValue) {
		this.bondFaceValue = bondFaceValue;
	}
	public BigDecimal getTtm() {
		return ttm;
	}
	public void setTtm(BigDecimal ttm) {
		this.ttm = ttm;
	}
	public BigDecimal getIssueprice() {
		return issueprice;
	}
	public void setIssueprice(BigDecimal issueprice) {
		this.issueprice = issueprice;
	}
	public BigDecimal getStockPrincipal() {
		return stockPrincipal;
	}
	public void setStockPrincipal(BigDecimal stockPrincipal) {
		this.stockPrincipal = stockPrincipal;
	}
	public BigDecimal getBondsOfStockRate() {
		return bondsOfStockRate;
	}
	public void setBondsOfStockRate(BigDecimal bondsOfStockRate) {
		this.bondsOfStockRate = bondsOfStockRate;
	}
	
}
