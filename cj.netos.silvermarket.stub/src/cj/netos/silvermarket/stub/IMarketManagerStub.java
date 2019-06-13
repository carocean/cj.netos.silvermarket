package cj.netos.silvermarket.stub;

import java.util.ArrayList;
import java.util.List;

import cj.netos.silvermarket.args.BState;
import cj.netos.silvermarket.args.MarketInfo;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.annotation.CjStubCircuitStatusMatches;
import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubReturn;
import cj.studio.gateway.stub.annotation.CjStubService;

@CjStubService(bindService = "/manager.service", usage = "市场管理")
public interface IMarketManagerStub {
	@CjStubMethod(usage = "注册帑银交易市场，该方法仅是创建帑银交易市场主体信息，稍后要调用其它方法以完善资料")
	@CjStubReturn(usage = "返回市场号")
	@CjStubCircuitStatusMatches(status = {"405 error becuse exists bank`s name.","404 not found"})
	String registerMarket(@CjStubInParameter(key = "marketName", usage = "帑银交易市场名") String marketName,
			@CjStubInParameter(key = "president", usage = "行长") String president,
			@CjStubInParameter(key = "company", usage = "公司") String company,
			@CjStubInParameter(key = "expiredDate", usage = "到期日期 格式：yyyy-MM-dd HH:mm，例：2019-10-30 09:45") String expiredDate)
			throws CircuitException;

	@CjStubMethod(usage = "更新帑银交易市场名")
	void updateMarketName(@CjStubInParameter(key = "market", usage = "帑银交易市场标识编码") String market,
			@CjStubInParameter(key = "name", usage = "帑银交易市场名") String name) throws CircuitException;

	@CjStubMethod(usage = "更新帑银交易市场行长")
	void updateMarketPresident(@CjStubInParameter(key = "market", usage = "帑银交易市场标识编码") String market,
			@CjStubInParameter(key = "president", usage = "行长") String president) throws CircuitException;

	@CjStubMethod(usage = "更新帑银交易市场归属企业")
	void updateMarketCompany(@CjStubInParameter(key = "market", usage = "帑银交易市场标识编码") String market,
			@CjStubInParameter(key = "company", usage = "归属企业") String company) throws CircuitException;

	@CjStubMethod(usage = "吊销指定的帑银交易市场，吊销并不是删除，只是改变状态为吊销")
	void revokeMarket(@CjStubInParameter(key = "market", usage = "帑银交易市场代码") String market) throws CircuitException;

	@CjStubMethod(usage = "冻结指定的帑银交易市场")
	void freezeMarket(@CjStubInParameter(key = "market", usage = "帑银交易市场代码") String market);

	@CjStubMethod(usage = "暂停运行指定的帑银交易市场")
	void closedMarket(@CjStubInParameter(key = "market", usage = "帑银交易市场代码") String market);

	@CjStubMethod(usage = "恢复运行指定的帑银交易市场")
	void resumeMarket(@CjStubInParameter(key = "market", usage = "帑银交易市场代码") String market);

	@CjStubMethod(usage = "获取指定的帑银交易市场信息")
	MarketInfo getMarketInfo(@CjStubInParameter(key = "market", usage = "帑银交易市场代码") String market);

	@CjStubMethod(usage = "分页查询帑银交易市场列表")
	@CjStubReturn(elementType = ArrayList.class, type = MarketInfo.class, usage = "帑银交易市场列表")
	List<MarketInfo> pageMarketInfo(@CjStubInParameter(key = "currPage", usage = "当前页码") int currPage,
			@CjStubInParameter(key = "pageSize", usage = "页大小") int pageSize);

	@CjStubMethod(usage = "获取帑银交易市场当前状态")
	BState getMarketState(@CjStubInParameter(key = "market", usage = "帑银交易市场代码") String market);
}
