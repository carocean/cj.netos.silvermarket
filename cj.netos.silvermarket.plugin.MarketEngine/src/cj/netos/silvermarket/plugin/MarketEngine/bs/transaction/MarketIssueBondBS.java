package cj.netos.silvermarket.plugin.MarketEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.UpdateOptions;

import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.silvermarket.args.BondBankBalance;
import cj.netos.silvermarket.args.IssueBill;
import cj.netos.silvermarket.args.MarketBalance;
import cj.netos.silvermarket.args.SellOrder;
import cj.netos.silvermarket.args.Stock;
import cj.netos.silvermarket.bs.IMarketBalanceBS;
import cj.netos.silvermarket.bs.IMarketInfoBS;
import cj.netos.silvermarket.bs.IMarketIssueBondBS;
import cj.netos.silvermarket.bs.IMarketPropertiesBS;
import cj.netos.silvermarket.bs.IMarketSellOrderBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.netos.silvermarket.util.BigDecimalConstants;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "transaction#issueOrderBS")
public class MarketIssueBondBS implements IMarketIssueBondBS, BigDecimalConstants {
	@CjServiceRef
	IMarketPropertiesBS marketPropertiesBS;
	@CjServiceRef
	IMarketInfoBS marketInfoBS;
	@CjServiceRef
	IMarketBalanceBS marketBalanceBS;
	@CjServiceRef
	IMarketStore marketStore;
	@CjServiceRef(refByName = "transaction#marketSellOrderBS")
	IMarketSellOrderBS marketSellOrderBS;
	@Override
	public Map<String, Object> issueOrder(String market, String issuer, String bondbank, BigDecimal bondQuantities,
			BigDecimal bondFaceValue) {
		IssueBill bill = new IssueBill();
		bill.setBondBank(bondbank);
		bill.setBondFaceValue(bondFaceValue);
		bill.setBondQuantities(bondQuantities);
		bill.setCtime(System.currentTimeMillis());
		bill.setIssuer(issuer);

		// 以下是计算
		BigDecimal stockprice = marketBalanceBS.getStockPrice(market);
		BigDecimal ttm = this.marketBalanceBS.getTTM(market, bondbank);// 求商户市盈率
		if (stockprice == null || stockprice.compareTo(new BigDecimal(0)) == 0) {
			// 当市场还未有一笔成交时帑银价为空，此以按第一个发行者的(面值*(defaultFirstIssueBillOfMarketMultiplyingPower))作为初始市场价
			stockprice = bondFaceValue
					.multiply(defaultFirstIssueBillOfMarketMultiplyingPower(marketPropertiesBS, market));
			this.marketBalanceBS.updateStockprice(market, stockprice);
		}
		bill.setTtm(ttm);
		bill.setIssueprice(stockprice);

		BigDecimal stockQuantities = (bondQuantities.multiply(bondFaceValue).multiply(new BigDecimal(1).add(ttm)))
				.divide(stockprice, BigDecimalConstants.scale, BigDecimalConstants.roundingMode);
		bill.setStockQuantities(stockQuantities);

		BigDecimal bondsOfStockRate = bondQuantities.divide(stockQuantities, BigDecimalConstants.scale,
				BigDecimalConstants.roundingMode);
		bill.setBondsOfStockRate(bondsOfStockRate);

		BigDecimal stockPrincipal = (bondQuantities.multiply(bondFaceValue)).divide(stockQuantities,
				BigDecimalConstants.scale, BigDecimalConstants.roundingMode);
		bill.setStockPrincipal(stockPrincipal);

		String issueno = marketStore.market(market).saveDoc(TABLE_IssueBill, new TupleDocument<>(bill));
		bill.setIssueno(issueno);

		this.marketBalanceBS.addBondQuantitiesBalance(market, bondQuantities);
		this.marketBalanceBS.addBondAmountBalance(market, bondQuantities.multiply(bondFaceValue));
		this.marketBalanceBS.addStockQuantitiesBalance(market, stockQuantities);
		this.marketBalanceBS.addBondQuantitiesBalance(market, bondbank, bondQuantities);
		this.marketBalanceBS.addBondAmountBalance(market, bondbank, bondQuantities.multiply(bondFaceValue));
		this.marketBalanceBS.addStockQuantitiesBalance(market, bondbank, stockQuantities);
		
		Stock stock=new Stock(bill.getIssueno(),stockQuantities);
		Map<String,Object> result=marketSellOrderBS.sellOrder(market, issuer, stock);
		SellOrder sellorder=(SellOrder)result.get("order");
		bill.setSellOrderno(sellorder.getOrderno());
		
		updateSellOrderno(market,bill.getIssueno(),bill.getSellOrderno());
		
		MarketBalance marketBalance=this.marketBalanceBS.getMarketBalance(market);
		bill.setMarketBalance(marketBalance);
		setMarketBalanceIntoIssueBill(market,bill.getIssueno(),marketBalance);
		
		BondBankBalance bondbankBalance=this.marketBalanceBS.getBondbankBalance(market,bondbank);
		bill.setBondbankBalance(bondbankBalance);
		setBondbankBalanceIntoIssueBill(market,bill.getIssueno(),bondbankBalance);
		
		// 通知返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bill", bill);
		return map;
	}
	private void setBondbankBalanceIntoIssueBill(String market, String issueno, BondBankBalance bondbankBalance) {
		Bson filter = Document.parse(String.format("{'tuple.issueno':'%s'}",issueno));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondbankBalance':'%s'}}", bondbankBalance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_IssueBill, filter, update, op);
	}
	private void setMarketBalanceIntoIssueBill(String market, String issueno,MarketBalance marketBalance) {
		Bson filter = Document.parse(String.format("{'tuple.issueno':'%s'}",issueno));
		Bson update = Document.parse(String.format("{'$set':{'tuple.marketBalance':'%s'}}", marketBalance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_IssueBill, filter, update, op);
	}
	private void updateSellOrderno(String market,String issueno, String sellOrderno) {
		Bson filter = Document.parse(String.format("{'tuple.issueno':'%s'}",issueno));
		Bson update = Document.parse(String.format("{'$set':{'tuple.sellOrderno':'%s'}}", sellOrderno));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_IssueBill, filter, update, op);		
	}

}
