package cj.netos.silvermarket.plugin.MarketEngine.bs;

import java.math.BigDecimal;
import java.util.HashMap;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.UpdateOptions;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.netos.silvermarket.args.BondBankBalance;
import cj.netos.silvermarket.args.MarketBalance;
import cj.netos.silvermarket.bs.IMarketBalanceBS;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.netos.silvermarket.util.BigDecimalConstants;
import cj.studio.ecm.EcmException;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "marketBalanceBS")
public class MarketBalanceBS implements IMarketBalanceBS {
	@CjServiceRef
	IMarketStore marketStore;

	@Override
	public BondBankBalance getBondbankBalance(String market, String bondbank) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'tuple.bondbank':'%s'}",
				TABLE_BondbankBalance, BondBankBalance.class.getName(), bondbank);
		IQuery<BondBankBalance> q = marketStore.market(market).createQuery(cjql);
		IDocument<BondBankBalance> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return null;
		}
		return doc.tuple();
	}

	@Override
	public MarketBalance getMarketBalance(String market) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {}", TABLE_MarketBalance,
				MarketBalance.class.getName());
		IQuery<MarketBalance> q = marketStore.market(market).createQuery(cjql);
		IDocument<MarketBalance> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return null;
		}
		return doc.tuple();
	}

	@Override
	public BigDecimal getStockPrice(String market) {
		String cjql = String.format("select {'tuple.stockPrice':1} from tuple %s %s where {}", TABLE_MarketBalance,
				HashMap.class.getName());
		IQuery<HashMap<String, Object>> q = marketStore.market(market).createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return null;
		}
		Object v = doc.tuple().get("stockPrice");
		if (v == null) {
			return null;
		}
		return new BigDecimal(v + "");
	}

	@Override
	public void updateStockprice(String market, BigDecimal stockprice) {
		Bson filter = Document.parse(String.format("{}"));
		Bson update = Document.parse(String.format("{'$set':{'tuple.stockPrice':%s}}", stockprice));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_MarketBalance, filter, update, op);

	}

	@Override
	public BigDecimal getBondQuantitiesBalance(String market) {
		String cjql = String.format("select {'tuple.bondQuantitiesBalance':1} from tuple %s %s where {}",
				TABLE_MarketBalance, HashMap.class.getName());
		IQuery<HashMap<String, Object>> q = marketStore.market(market).createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return new BigDecimal("0");
		}
		Object v = doc.tuple().get("bondQuantitiesBalance");
		if (v == null) {
			v = "0";
		}
		return new BigDecimal(v + "");
	}

	@Override
	public BigDecimal getBondAmountBalance(String market) {
		String cjql = String.format("select {'tuple.bondAmountBalance':1} from tuple %s %s where {}",
				TABLE_MarketBalance, HashMap.class.getName());
		IQuery<HashMap<String, Object>> q = marketStore.market(market).createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return new BigDecimal("0");
		}
		Object v = doc.tuple().get("bondAmountBalance");
		if (v == null) {
			v = "0";
		}
		return new BigDecimal(v + "");
	}

	@Override
	public BigDecimal getStockQuantitiesBalance(String market) {
		String cjql = String.format("select {'tuple.stockQuantitiesBalance':1} from tuple %s %s where {}",
				TABLE_MarketBalance, HashMap.class.getName());
		IQuery<HashMap<String, Object>> q = marketStore.market(market).createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return new BigDecimal("0");
		}
		Object v = doc.tuple().get("stockQuantitiesBalance");
		if (v == null) {
			v = "0";
		}
		return new BigDecimal(v + "");
	}

	@Override
	public BigDecimal addBondQuantitiesBalance(String market, BigDecimal bondQuantities) {
		BigDecimal balance = getBondQuantitiesBalance(market);
		balance = balance.add(bondQuantities);
		Bson filter = Document.parse(String.format("{}"));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondQuantitiesBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_MarketBalance, filter, update, op);
		return balance;
	}

	@Override
	public BigDecimal addBondAmountBalance(String market, BigDecimal bondAmount) {
		BigDecimal balance = getBondAmountBalance(market);
		balance = balance.add(bondAmount);
		Bson filter = Document.parse(String.format("{}"));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondAmountBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_MarketBalance, filter, update, op);
		return balance;
	}

	@Override
	public BigDecimal addStockQuantitiesBalance(String market, BigDecimal stockQuantities) {
		BigDecimal balance = getStockQuantitiesBalance(market);
		balance = balance.add(stockQuantities);
		Bson filter = Document.parse(String.format("{}"));
		Bson update = Document.parse(String.format("{'$set':{'tuple.stockQuantitiesBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_MarketBalance, filter, update, op);
		return balance;
	}

	@Override
	public void decBondQuantitiesBalance(String market, BigDecimal bondQuantities) {
		BigDecimal balance = getStockQuantitiesBalance(market);
		if (balance.compareTo(bondQuantities) < 0) {
			throw new EcmException(String.format("余额不足扣：%s<%s", balance, bondQuantities));
		}
		balance = balance.subtract(bondQuantities);
		Bson filter = Document.parse(String.format("{}"));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondQuantitiesBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_MarketBalance, filter, update, op);
	}

	@Override
	public void decBondAmountBalance(String market, BigDecimal bondAmount) {
		BigDecimal balance = getStockQuantitiesBalance(market);
		if (balance.compareTo(bondAmount) < 0) {
			throw new EcmException(String.format("余额不足扣：%s<%s", balance, bondAmount));
		}
		balance = balance.subtract(bondAmount);
		Bson filter = Document.parse(String.format("{}"));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondAmountBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_MarketBalance, filter, update, op);
	}

	@Override
	public void decStockQuantitiesBalance(String market, BigDecimal stockQuantities) {
		BigDecimal balance = getStockQuantitiesBalance(market);
		if (balance.compareTo(stockQuantities) < 0) {
			throw new EcmException(String.format("余额不足扣：%s<%s", balance, stockQuantities));
		}
		balance = balance.subtract(stockQuantities);
		Bson filter = Document.parse(String.format("{}"));
		Bson update = Document.parse(String.format("{'$set':{'tuple.stockQuantitiesBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_MarketBalance, filter, update, op);

	}

	@Override
	public BigDecimal getTTM(String market, String bondbank) {
		String cjql = String.format("select {'tuple.ttm':1} from tuple %s %s where {'tuple.bondbank':'%s'}",
				TABLE_BondbankBalance, HashMap.class.getName(), bondbank);
		IQuery<HashMap<String, Object>> q = marketStore.market(market).createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return new BigDecimal(BigDecimalConstants.default_ttm);
		}
		Object v = doc.tuple().get("ttm");
		if (v == null) {
			v = BigDecimalConstants.default_ttm;
		}
		return new BigDecimal(v + "");
	}

	@Override
	public void updateTTM(String market, String bondbank, BigDecimal ttm) {
		Bson filter = Document.parse(String.format("{'tuple.bondbank':'%s'}", bondbank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.ttm':%s}}", ttm));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_BondbankBalance, filter, update, op);
	}

	@Override
	public BigDecimal getBondQuantitiesBalance(String market, String bondbank) {
		String cjql = String.format(
				"select {'tuple.bondQuantitiesBalance':1} from tuple %s %s where {'tuple.bondbank':'%s'}",
				TABLE_BondbankBalance, HashMap.class.getName(), bondbank);
		IQuery<HashMap<String, Object>> q = marketStore.market(market).createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return new BigDecimal("0");
		}
		Object v = doc.tuple().get("bondQuantitiesBalance");
		if (v == null) {
			v = "0";
		}
		return new BigDecimal(v + "");
	}

	@Override
	public BigDecimal getBondAmountBalance(String market, String bondbank) {
		String cjql = String.format(
				"select {'tuple.bondAmountBalance':1} from tuple %s %s where {'tuple.bondbank':'%s'}",
				TABLE_BondbankBalance, HashMap.class.getName(), bondbank);
		IQuery<HashMap<String, Object>> q = marketStore.market(market).createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return new BigDecimal("0");
		}
		Object v = doc.tuple().get("bondAmountBalance");
		if (v == null) {
			v = "0";
		}
		return new BigDecimal(v + "");
	}

	@Override
	public BigDecimal getStockQuantitiesBalance(String market, String bondbank) {
		String cjql = String.format(
				"select {'tuple.stockQuantitiesBalance':1} from tuple %s %s where {'tuple.bondbank':'%s'}",
				TABLE_BondbankBalance, HashMap.class.getName(), bondbank);
		IQuery<HashMap<String, Object>> q = marketStore.market(market).createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return new BigDecimal("0");
		}
		Object v = doc.tuple().get("stockQuantitiesBalance");
		if (v == null) {
			v = "0";
		}
		return new BigDecimal(v + "");
	}

	@Override
	public BigDecimal addBondQuantitiesBalance(String market, String bondbank, BigDecimal bondQuantities) {
		BigDecimal balance = getBondQuantitiesBalance(market, bondbank);
		balance = balance.add(bondQuantities);
		Bson filter = Document.parse(String.format("{'tuple.bondbank':'%s'}", bondbank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondQuantitiesBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_BondbankBalance, filter, update, op);
		return balance;
	}

	@Override
	public BigDecimal addBondAmountBalance(String market, String bondbank, BigDecimal bondAmount) {
		BigDecimal balance = getBondAmountBalance(market, bondbank);
		balance = balance.add(bondAmount);
		Bson filter = Document.parse(String.format("{'tuple.bondbank':'%s'}", bondbank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondAmountBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_BondbankBalance, filter, update, op);
		return balance;
	}

	@Override
	public BigDecimal addStockQuantitiesBalance(String market, String bondbank, BigDecimal stockQuantities) {
		BigDecimal balance = getStockQuantitiesBalance(market, bondbank);
		balance = balance.add(stockQuantities);
		Bson filter = Document.parse(String.format("{'tuple.bondbank':'%s'}", bondbank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.stockQuantitiesBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_BondbankBalance, filter, update, op);
		return balance;
	}

	@Override
	public void decBondQuantitiesBalance(String market, String bondbank, BigDecimal bondQuantities) {
		BigDecimal balance = getStockQuantitiesBalance(market, bondbank);
		if (balance.compareTo(bondQuantities) < 0) {
			throw new EcmException(String.format("余额不足扣：%s<%s", balance, bondQuantities));
		}
		balance = balance.subtract(bondQuantities);
		Bson filter = Document.parse(String.format("{'tuple.bondbank':'%s'}", bondbank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondQuantitiesBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_BondbankBalance, filter, update, op);
	}

	@Override
	public void decBondAmountBalance(String market, String bondbank, BigDecimal bondAmount) {
		BigDecimal balance = getStockQuantitiesBalance(market, bondbank);
		if (balance.compareTo(bondAmount) < 0) {
			throw new EcmException(String.format("余额不足扣：%s<%s", balance, bondAmount));
		}
		balance = balance.subtract(bondAmount);
		Bson filter = Document.parse(String.format("{'tuple.bondbank':'%s'}", bondbank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.bondAmountBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_BondbankBalance, filter, update, op);
	}

	@Override
	public void decStockQuantitiesBalance(String market, String bondbank, BigDecimal stockQuantities) {
		BigDecimal balance = getStockQuantitiesBalance(market, bondbank);
		if (balance.compareTo(stockQuantities) < 0) {
			throw new EcmException(String.format("余额不足扣：%s<%s", balance, stockQuantities));
		}
		balance = balance.subtract(stockQuantities);
		Bson filter = Document.parse(String.format("{'tuple.bondbank':'%s'}", bondbank));
		Bson update = Document.parse(String.format("{'$set':{'tuple.stockQuantitiesBalance':%s}}", balance));
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		marketStore.market(market).updateDocOne(TABLE_BondbankBalance, filter, update, op);

	}
}
