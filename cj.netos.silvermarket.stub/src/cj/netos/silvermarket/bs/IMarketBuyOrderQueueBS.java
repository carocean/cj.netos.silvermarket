package cj.netos.silvermarket.bs;

import java.math.BigDecimal;
import java.util.List;

import cj.netos.silvermarket.args.BuyOrderStock;

public interface IMarketBuyOrderQueueBS {
	static String TABLE_queue_buyOrder = "queue.buyorders";

	void offer(String market, BuyOrderStock buyOrderStock);

	BuyOrderStock peek(String market);

	void remove(String market);

	void remove(String market, String stockno);

	void updateAmount(String market, String stockno, BigDecimal amount);

	List<BuyOrderStock> listFiveBuyingWindow(String market);
}
