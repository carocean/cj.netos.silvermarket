package cj.netos.silvermarket.stub;

import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubService;

@CjStubService(bindService = "/properties.service", usage = "帑银交易市场属性")
public interface IMarketPropertiesStub {
	@CjStubMethod(usage = "添加属性")
	void put(@CjStubInParameter(key="market", usage = "交易市场编号") String bank,
			@CjStubInParameter(key = "key", usage = "key") String key,
			@CjStubInParameter(key = "value", usage = "value") String value,
			@CjStubInParameter(key = "desc", usage = "属性描述，可为空") String desc);

	@CjStubMethod(usage = "获取属性")
	String get(@CjStubInParameter(key="market", usage = "交易市场编号") String bank,
			@CjStubInParameter(key = "key", usage = "key") String key);

	@CjStubMethod(usage = "获取属性描述")
	String desc(@CjStubInParameter(key="market", usage = "交易市场编号") String bank,
			@CjStubInParameter(key = "key", usage = "key") String key);

	@CjStubMethod(usage = "移除属性")
	void remove(@CjStubInParameter(key="market", usage = "交易市场编号") String bank,
			@CjStubInParameter(key = "key", usage = "key") String key);

	@CjStubMethod(usage = "获取属性")
	String[] enumKey(String bank);

	@CjStubMethod(usage = "获取一页属性")
	String[] pageKeys(@CjStubInParameter(key="market", usage = "交易市场编号") String bank,
			@CjStubInParameter(key = "currPage", usage = "当前页") int currPage,
			@CjStubInParameter(key = "pageSize", usage = "页大小") int pageSize);

	@CjStubMethod(usage = "属性个数")
	long count(@CjStubInParameter(key="market", usage = "交易市场编号") String bank);
}
