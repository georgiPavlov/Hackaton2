package comunication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import MDK_3.Mdk3_manager;
import accessPoint.AccessPoint;
import adapter.Adapter;
import airmon.AirMonNG;
import airodump.AiroDumpNG;

public class StateManager implements Runnable {
	//private ComunicationStream<StatePakage> reaver = new ComunicationStream<>();
	private ComunicationStream<StatePakage> gui = new ComunicationStream<>();
	//private ComunicationStream<Boolean> mkd3 = new ComunicationStream<>();
	private StateProducer reaverState;
	private Mdk3_manager mdk3Manager;
     
	public StateManager(ComunicationStream<StatePakage> gui){
		this.gui = gui;
	}
	@Override
	public void run() {
		while (true) {
			end: while (true) {
				try {
					StatePakage pakage = gui.getFromOther();
					switch (pakage.getState()) {
					case INIT: {
						gui.sendToOther(getAccessPoints());
						break;
					}
					case CRACK: {
						//System.out.println(crack);
						initReaver(pakage.getAps(), pakage.getMonitor());
						break end;
					}
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			try {
				gui.sendToOther(new StatePakage(StatePakage.State.RUNING,null,null,null,null));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			end1: while (true) {
				try {

					StatePakage p = reaverState.startReaver();

					switch (p.getState()) {
					case BLOKED: {
						gui.sendToOther(p);
                        long start = System.currentTimeMillis();
						// call mkd3
						mdk3Manager.executeRunner();
						System.err.println("time ----------------------------------------------------------------------------> : " + (System.currentTimeMillis()-start));
		/*				try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
						gui.sendToOther(new StatePakage(StatePakage.State.RUNING, null, null, null, null));

						break;
					}
					case FINISHED: {
						gui.sendToOther(p);
						break end1;
					}
					}
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	
	private static StatePakage getAccessPoints() {
		List<Adapter> adapters = AirMonNG.getAdapters();

		Adapter monitor = AirMonNG.doubleStart(adapters.get(0));
		Set<AccessPoint> set = AiroDumpNG.getAccessPoints(monitor, 10_000);
		//set.forEach(kur -> System.out.println(kur));
		List<AccessPoint> accessPoints = new ArrayList<AccessPoint>(set);
		System.out.println("Sended Access Points");
		accessPoints.forEach(ap -> System.out.println(ap));
		return new StatePakage(StatePakage.State.INIT, null, null, monitor, accessPoints);
	}

	private void initReaver(List<AccessPoint> accessPoints, Adapter monitor) {
		this.reaverState = new StateProducer(accessPoints, monitor);
		this.mdk3Manager = new Mdk3_manager(accessPoints, monitor);

	}

	public static void main(String[] args) {
		System.out.println(File.separatorChar);

		getAccessPoints().getAps().forEach(hui-> System.out.println(hui));

	}
}
