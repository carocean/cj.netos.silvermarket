package cj.netos.silvermarket.program.stub;

import java.math.BigDecimal;
import java.util.List;

import cj.netos.silvermarket.args.Stock;
import cj.netos.silvermarket.stub.IMarketTransactionStub;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceSite;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;
import cj.studio.util.reactor.Event;
import cj.studio.util.reactor.IReactor;

@CjService(name = "/transaction.service")
public class MarketTransactionStub extends GatewayAppSiteRestStub implements IMarketTransactionStub {
	@CjServiceSite
	IServiceSite site;
	IReactor reactor;

	protected IReactor getReactor() {
		if (reactor == null) {
			reactor = (IReactor) site.getService("$.reactor");
		}
		return reactor;
	}

	@Override
	public void issueOrder(String market, String issuer, String bondbank, BigDecimal bondQuantities,
			BigDecimal bondFaceValue, String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(market, "transaction.issueOrder");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("issuer", issuer);
		e.getParameters().put("bondbank", bondbank);
		e.getParameters().put("bondQuantities", bondQuantities);
		e.getParameters().put("bondFaceValue", bondFaceValue);
		reactor.input(e);
	}

	@Override
	public void buyOrder(String market, String buyer, BigDecimal amount, String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(market, "transaction.buyOrder");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("buyer", buyer);
		e.getParameters().put("amount", amount);
		reactor.input(e);
	}

	@Override
	public void sellOrder(String market, String seller, List<Stock> stocks, String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(market, "transaction.sellOrder");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("seller", seller);
		e.getParameters().put("stocks", stocks);
		reactor.input(e);
	}

	@Override
	public void exchangeOrder(String market, String buyer, List<Stock> stocks, String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(market, "transaction.exchangeOrder");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("buyer", buyer);
		e.getParameters().put("stocks", stocks);
		reactor.input(e);
	}
	@Override
	public void cancelOrder(String market, String orderno, String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(market, "transaction.cancelOrder");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("orderno", orderno);
		reactor.input(e);
	}
}
