package cj.netos.silvermarket.args;

import java.math.BigDecimal;

/**
 * 委托卖出存量单<br>
 * 是卖出队列中的行
 * 
 * @author caroceanjofers
 *
 */
public class SellOrderStock {
	String no;
	String orderno;
	String seller;
	Stock stock;
	long otime;//委托时间
	BigDecimal stockPrice;

	public SellOrderStock() {
	}

	public SellOrderStock(SellOrder order) {
		this.orderno = order.orderno;
		this.otime = order.ctime;
		this.seller = order.seller;
		this.stock = order.stock;
		this.stockPrice = order.stockPrice;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public long getOtime() {
		return otime;
	}
	public void setOtime(long otime) {
		this.otime = otime;
	}

	public BigDecimal getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(BigDecimal stockPrice) {
		this.stockPrice = stockPrice;
	}

}
