package cj.netos.silvermarket.bs;

import cj.netos.silvermarket.args.SellOrderStock;
import cj.netos.silvermarket.args.Stock;

public interface IMarketSellOrderQueueBS {
	static String TABLE_queue_sellOrder = "queue.sellorders";

	void offer(String market, SellOrderStock sellOrderStock);

	SellOrderStock peek(String market);

	void remove(String market);

	void remove(String market, String stockno);

	SellOrderStock addStock(String market, String stockno, Stock stock);

	SellOrderStock decAmount(String market, String stockno, Stock stock);

}
