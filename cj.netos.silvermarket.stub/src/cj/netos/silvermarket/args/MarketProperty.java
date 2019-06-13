package cj.netos.silvermarket.args;

public class MarketProperty {
	public static final transient String CONSTANS_KEY_default_firstIssueBillOfMarket_multiplying_power = "default_firstIssueBillOfMarket_multiplying_power";
	String key;
	String value;
	String desc;
	String market;

	public MarketProperty() {
		// TODO Auto-generated constructor stub
	}

	public MarketProperty(String market, String key, String value,String desc) {
		this.market = market;
		this.key = key;
		this.value = value;
		this.desc=desc;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}
}
