package cj.netos.silvermarket.program.stub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cj.netos.silvermarket.args.BState;
import cj.netos.silvermarket.args.MarketInfo;
import cj.netos.silvermarket.args.MarketState;
import cj.netos.silvermarket.bs.IMarketInfoBS;
import cj.netos.silvermarket.bs.IMarketStateBS;
import cj.netos.silvermarket.stub.IMarketManagerStub;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;
import cj.ultimate.util.StringUtil;

@CjService(name = "/manager.service")
public class MarketManagerStub extends GatewayAppSiteRestStub implements IMarketManagerStub {
	@CjServiceRef(refByName = "MarketEngine.marketInfoBS")
	IMarketInfoBS marketInfoBS;

	@CjServiceRef(refByName = "MarketEngine.marketStateBS")
	IMarketStateBS marketStateBS;

	@Override
	public String registerMarket(String marketName, String president, String company, String expiredDate)
			throws CircuitException {
		if (StringUtil.isEmpty(marketName)) {
			throw new CircuitException("404", String.format("市场名为空"));
		}
		if (StringUtil.isEmpty(president)) {
			throw new CircuitException("404", String.format("场长为空"));
		}
		if (StringUtil.isEmpty(expiredDate)) {
			throw new CircuitException("404", String.format("到期日期为空"));
		}

		MarketInfo info = new MarketInfo();
		info.setCode(null);
		info.setPresident(president);
		info.setCompany(company);
		info.setName(marketName);
		info.setCtime(System.currentTimeMillis());
		MarketState state = new MarketState();
		state.setState(BState.opened);
		info.setBstate(state.getState());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date expire = null;
		try {
			expire = sdf.parse(expiredDate);
		} catch (ParseException e) {
			throw new CircuitException("500", e);
		}
		info.setExpiredTime(expire.getTime());

		marketInfoBS.saveMarket(info);
		// 插入营业状态为正常
		state.setBank(info.getCode());
		state.setCtime(System.currentTimeMillis());
		marketStateBS.save(state);
		return info.getCode();
	}

	@Override
	public void updateMarketName(String market, String name) throws CircuitException {
		this.marketInfoBS.updateMarketName(market, name);
	}

	@Override
	public void updateMarketPresident(String market, String president) throws CircuitException {
		this.marketInfoBS.updateMarketPresident(market, president);
	}

	@Override
	public void updateMarketCompany(String market, String company) throws CircuitException {
		this.marketInfoBS.updateMarketCompany(market, company);
	}

	@Override
	public void revokeMarket(String market) throws CircuitException {
		this.marketStateBS.revokeMarket(market);
	}

	@Override
	public void freezeMarket(String market) {
		this.marketStateBS.freezeMarket(market);
	}

	@Override
	public void closedMarket(String market) {
		this.marketStateBS.closedMarket(market);
	}

	@Override
	public void resumeMarket(String market) {
		this.marketStateBS.resumeMarket(market);
	}

	@Override
	public MarketInfo getMarketInfo(String market) {
		return marketInfoBS.getMarketInfo(market);
	}

	@Override
	public List<MarketInfo> pageMarketInfo(int currPage, int pageSize) {
		return this.marketInfoBS.pageMarketInfo(currPage, pageSize);
	}

	@Override
	public BState getMarketState(String market) {
		return this.marketStateBS.getState(market).getState();
	}

}
