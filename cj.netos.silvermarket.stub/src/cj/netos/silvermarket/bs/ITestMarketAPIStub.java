package cj.netos.silvermarket.bs;

import java.math.BigDecimal;

import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubService;

@CjStubService(bindService = "/test.service", usage = "用于测试api")
public interface ITestMarketAPIStub {
	@CjStubMethod(usage = "xxx")
	void changeStockPrice(@CjStubInParameter(key = "market", usage = "xxx") String market,
			@CjStubInParameter(key = "stockPrice", usage = "xxx") BigDecimal stockPrice);
}
