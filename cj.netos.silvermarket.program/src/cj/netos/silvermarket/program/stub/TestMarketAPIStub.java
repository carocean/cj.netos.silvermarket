package cj.netos.silvermarket.program.stub;

import java.math.BigDecimal;

import cj.netos.silvermarket.bs.IMarketBalanceBS;
import cj.netos.silvermarket.bs.ITestMarketAPIStub;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;
@CjService(name="/test.service")
public class TestMarketAPIStub extends GatewayAppSiteRestStub implements ITestMarketAPIStub{
	@CjServiceRef(refByName = "MarketEngine.marketBalanceBS")
	IMarketBalanceBS marketBalanceBS;
	@Override
	public void changeStockPrice(String market, BigDecimal stockPrice) {
		marketBalanceBS.updateStockprice(market, stockPrice);
	}

}
