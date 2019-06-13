package cj.netos.silvermarket.program.reactor.transaction;

import java.math.BigDecimal;
import java.util.Map;

import cj.netos.inform.Informer;
import cj.netos.silvermarket.bs.IMarketIssueBondBS;
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

@CjService(name = "transaction.issueOrder")
public class IssueOrderValve implements IValve {
	@CjServiceRef
	IMarketCacher marketCacher;
	@CjServiceRef(refByName = "MarketEngine.transaction#issueOrderBS")
	IMarketIssueBondBS marketIssueOrderBS;
	@CjServiceRef(refByName = "$.netos.informer")
	Informer informer;
	
	@Override
	public void flow(Event e, IPipeline pipeline) throws CircuitException {
		String issuer = (String) e.getParameters().get("issuer");
		String informAddress = (String) e.getParameters().get("informAddress");
		String bondbank = (String) e.getParameters().get("bondbank");
		BigDecimal bondQuantities = (BigDecimal) e.getParameters().get("bondQuantities");
		BigDecimal bondFaceValue = (BigDecimal) e.getParameters().get("bondFaceValue");
		Map<String, Object> map = marketIssueOrderBS.issueOrder(e.getKey(), issuer, bondbank, bondQuantities,
				bondFaceValue);
		if (!StringUtil.isEmpty(informAddress)) {
			Frame f = informer.createFrame(informAddress, map);
			MemoryOutputChannel oc = new MemoryOutputChannel();
			Circuit c = new Circuit(oc, "http/1.1 200 ok");
			informer.inform(f, c);
		}
	}

}
