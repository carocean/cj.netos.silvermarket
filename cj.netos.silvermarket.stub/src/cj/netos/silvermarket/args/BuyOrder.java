package cj.netos.silvermarket.args;
/**
 * 买入委托流水单
 * @author caroceanjofers
 *
 */

import java.math.BigDecimal;

public class BuyOrder {
	String no;
	String buyer;
	BigDecimal amount;
	long otime;//委托买入时间
	BigDecimal buyingPrice;//用于申购的报价：一般指最高买入价
	public BigDecimal getBuyingPrice() {
		return buyingPrice;
	}
	public void setBuyingPrice(BigDecimal buyingPrice) {
		this.buyingPrice = buyingPrice;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public long getOtime() {
		return otime;
	}
	public void setOtime(long otime) {
		this.otime = otime;
	}
	
}
