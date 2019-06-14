package cj.netos.silvermarket.plugin.MarketEngine.dealmaking;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cj.netos.silvermarket.args.DealmakingContract;
import cj.netos.silvermarket.dealmaking.IDealmakingEngine;
import cj.netos.silvermarket.plugin.MarketEngine.db.IMarketStore;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.IServiceProvider;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.ultimate.util.StringUtil;

@CjService(name = "dealmakingEngine")
public class DealmakingEngine implements IDealmakingEngine {
	IMarketStore marketStore;
	ExecutorService workexe;
	ExecutorService bossexe;
	boolean isStoped;

	@Override
	public void start(IServiceProvider site) {
		marketStore = (IMarketStore) site.getService("marketStore");
		int nThreads = (int)site.getService("$.dealmakingEngine.nThreads");

		CJSystem.logging().info(getClass(), String.format("撮合引擎工作线程数：%s", nThreads));
		workexe = Executors.newFixedThreadPool(nThreads);
		bossexe = Executors.newSingleThreadExecutor();
		bossexe.execute(new Runnable() {

			@Override
			public void run() {
				while (!isStoped || !Thread.interrupted()) {
					IDealmakingTask task = new DealmakingTask(site);
					Future<DealmakingContract> f = workexe.submit(task);
					try {
						DealmakingContract dc = f.get();
						System.out.println(".....完成撮合，将根据properties中的配置向外部发出事件通知。撮合结果：" + dc);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void stop() {
		isStoped = true;
		bossexe.shutdownNow();
		workexe.shutdown();
		marketStore = null;
		workexe = null;
	}

}
