package comunication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
						initReaver(pakage.getAps(), pakage.getMonitor());
						break end;
					}
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			end1: while (true) {
				try {
					StatePakage p = reaverState.startReaver();
					switch (p.getState()) {
					case BLOKED: {
						gui.sendToOther(p);
						// call mkd3
						mdk3Manager.executeRunner();
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

	
	private StatePakage getAccessPoints() {
		List<Adapter> adapters = AirMonNG.getAdapters();
		Adapter monitor = AirMonNG.doubleStart(adapters.get(0));
		List<AccessPoint> accessPoints = new ArrayList<AccessPoint>(AiroDumpNG.getAccessPoints(monitor, 10_000));
		return new StatePakage(StatePakage.State.INIT, null, null, monitor, accessPoints);
	}

	private void initReaver(List<AccessPoint> accessPoints, Adapter monitor) {
		this.reaverState = new StateProducer(accessPoints, monitor);
		this.mdk3Manager = new Mdk3_manager(accessPoints, monitor);

	}
}
