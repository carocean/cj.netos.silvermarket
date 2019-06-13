package cj.netos.silvermarket.bs;

import java.math.BigDecimal;

import cj.netos.silvermarket.args.BuyOrderStock;

public interface IMarketBuyOrderQueueBS {
	static String TABLE_queue_buyOrder = "queue.buyorders";

	void offer(String market, BuyOrderStock buyOrderStock);

	BuyOrderStock peek(String market);

	void remove(String market);

	void remove(String market, String stockno);

	BigDecimal addAmount(String market, String stockno, BigDecimal amount);

	BigDecimal decAmount(String market, String stockno, BigDecimal amount);
}
