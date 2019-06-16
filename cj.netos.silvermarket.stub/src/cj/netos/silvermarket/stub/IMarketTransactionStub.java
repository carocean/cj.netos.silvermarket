package cj.netos.silvermarket.stub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cj.netos.silvermarket.args.Stock;
import cj.studio.gateway.stub.annotation.CjStubInContentKey;
import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubService;

@CjStubService(bindService = "/transaction.service", usage = "帑银交易市场交易存根")
public interface IMarketTransactionStub {
	@CjStubMethod(usage = "委托发行")
	void issueOrder(@CjStubInParameter(key = "market", usage = "市场编号") String market,
			@CjStubInParameter(key = "issuer", usage = "发行人，一般是债券银行的商户") String issuer,
			@CjStubInParameter(key = "bondbank", usage = "从哪个债券银行发行的") String bondbank,
			@CjStubInParameter(key = "bondQuantities", usage = "债券数量") BigDecimal bondQuantities,
			@CjStubInParameter(key = "bondFaceValue", usage = "债券面值") BigDecimal bondFaceValue,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);

	@CjStubMethod(usage = "撤单，不论买单或卖单均可撤除，只能撤回未卖出的订单")
	void cancelOrder(@CjStubInParameter(key = "market", usage = "市场编号") String market,
			@CjStubInParameter(key = "orderno", usage = "买单或卖单单号。买卖单类型在前面加#以分隔，如:buyno#99393did99393,sellno#93999slllslsl") String orderno,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);

	@CjStubMethod(usage = "委托买入")
	void buyOrder(@CjStubInParameter(key = "market", usage = "市场编号") String market,
			@CjStubInParameter(key = "buyer", usage = "委托买方") String buyer,
			@CjStubInParameter(key = "amount", usage = "申购金额") BigDecimal amount,
			@CjStubInParameter(key = "buyingPrice", usage = "委托申购价格") BigDecimal buyingPrice,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);

	@CjStubMethod(command = "post", usage = "委托卖出")
	void sellOrder(@CjStubInParameter(key = "market", usage = "市场编号") String market,
			@CjStubInParameter(key = "seller", usage = "委托卖方") String seller,
			@CjStubInContentKey(key = "stocks", elementType = Stock.class, type = ArrayList.class, usage = "要卖出的帑银") List<Stock> stocks,
			@CjStubInParameter(key = "sellingPrice", usage = "委托售价") BigDecimal sellingPrice,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);

	@CjStubMethod(command = "post", usage = "委托承兑")
	void exchangeOrder(@CjStubInParameter(key = "market", usage = "市场编号") String market,
			@CjStubInParameter(key = "buyer", usage = "委托买方") String buyer,
			@CjStubInContentKey(key = "stocks", elementType = Stock.class, type = ArrayList.class, usage = "要承兑的帑银") List<Stock> stocks,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);
}
