package cj.netos.silvermarket.program.reactor.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cj.netos.inform.Informer;
import cj.netos.silvermarket.args.Stock;
import cj.netos.silvermarket.bs.IMarketSellOrderBS;
import cj.netos.silvermarket.program.IMarketCacher;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.ecm.net.io.MemoryOutputChannel;
import cj.studio.util.reactor.Event;
import cj.studio.util.reactor.IPipeline;
import cj.studio.util.reactor.IValve;
import cj.ultimate.util.StringUtil;

@CjService(name = "transaction.sellOrder")
public class SellOrderValve implements IValve {
	@CjServiceRef
	IMarketCacher marketCacher;
	@CjServiceRef(refByName = "MarketEngine.transaction#marketSellOrderBS")
	IMarketSellOrderBS marketSellOrderBS;
	@CjServiceRef(refByName = "$.netos.informer")
	Informer informer;

	@Override
	public void flow(Event e, IPipeline pipeline) throws CircuitException {
		String seller = (String) e.getParameters().get("seller");
		String informAddress = (String) e.getParameters().get("informAddress");
		@SuppressWarnings("unchecked")
		List<Stock> stocks = (List<Stock>) e.getParameters().get("stocks");
		BigDecimal sellingPrice = (BigDecimal) e.getParameters().get("sellingPrice");
		Map<String, Object> map = marketSellOrderBS.sellOrder(e.getKey(), seller, stocks, sellingPrice);
		if (!StringUtil.isEmpty(informAddress)) {
			Frame f = informer.createFrame(informAddress, map);
			MemoryOutputChannel oc = new MemoryOutputChannel();
			Circuit c = new Circuit(oc, "http/1.1 200 ok");
			informer.inform(f, c);
		}
	}

}
