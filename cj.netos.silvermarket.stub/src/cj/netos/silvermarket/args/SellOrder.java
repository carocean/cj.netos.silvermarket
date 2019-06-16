package cj.netos.silvermarket.args;

import java.math.BigDecimal;
import java.util.List;

//委托卖出流水单
public class SellOrder {
	String orderno;
	String seller;
	List<Stock> stocks;
	long ctime;
	BigDecimal sellingPrice;// 想要卖出的帑价，一般是不低于此价
	String status;//向撮合引擎提交状态,如果不是200则表示未提交成功，则需提交
	String message;
	public SellOrder() {
		this.status="200";
		this.message="ok";
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

	public List<Stock> getStocks() {
		return stocks;
	}
	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

}
