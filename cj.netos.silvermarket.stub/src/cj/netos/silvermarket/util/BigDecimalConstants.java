package cj.netos.silvermarket.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cj.netos.silvermarket.args.MarketProperty;
import cj.netos.silvermarket.bs.IMarketPropertiesBS;
import cj.ultimate.util.StringUtil;

public interface BigDecimalConstants {
	static int scale = 16;// 小数位数为16
	static RoundingMode roundingMode = RoundingMode.FLOOR;
	static String default_ttm = "0.334";// 默认市盈率
	static String default_feeRate="0.05";//默认手续费率;
	static String default_firstIssueBillOfMarket_multiplying_power="1000.0012";//初始市场价，按此倍率放大的目的是帑银有了几元几元的价格，更加好看
	default BigDecimal defaultFirstIssueBillOfMarketMultiplyingPower(IMarketPropertiesBS marketPropertiesBS, String market) {
		String strbondRate = marketPropertiesBS.get(market,
				MarketProperty.CONSTANS_KEY_default_firstIssueBillOfMarket_multiplying_power);
		if (StringUtil.isEmpty(strbondRate)) {
			strbondRate = default_firstIssueBillOfMarket_multiplying_power + "";
		}
		return new BigDecimal(strbondRate).setScale(scale, roundingMode);
	}
	/**
	 * 默认的手续费率，是市场向交易双方征收
	 * @param marketPropertiesBS
	 * @param market
	 * @return
	 */
	default BigDecimal defaultFreeRate(IMarketPropertiesBS marketPropertiesBS, String market) {
		String strbondRate = marketPropertiesBS.get(market,
				MarketProperty.CONSTANS_KEY_default_freeRate);
		if (StringUtil.isEmpty(strbondRate)) {
			strbondRate = default_feeRate + "";
		}
		return new BigDecimal(strbondRate).setScale(scale, roundingMode);
	}
}
