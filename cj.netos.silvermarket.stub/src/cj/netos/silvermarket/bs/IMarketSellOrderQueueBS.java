package cj.netos.silvermarket.bs;

import java.util.List;

import cj.netos.silvermarket.args.SellOrderStock;
import cj.netos.silvermarket.args.Stock;

public interface IMarketSellOrderQueueBS {
	static String TABLE_queue_sellOrder = "queue.sellorders";

	void offer(String market, SellOrderStock sellOrderStock);

	SellOrderStock peek(String market);

	void remove(String market);

	void remove(String market, String stockno);

	void updateStocks(String market, String stockno, List<Stock> stocks);

	List<SellOrderStock> listFiveSellingWindow(String market);

	
}
