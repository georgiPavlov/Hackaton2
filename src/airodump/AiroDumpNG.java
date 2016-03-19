package airodump;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


import accessPoint.AccessPoint;
import adapter.Adapter;
import airmon.AirMonNG;
import processes.ProcessHandler;
import util.Validator;

public class AiroDumpNG {
	static final String COMMAND = "sudo airodump-ng";
	static final String DUMPFILE_NAME = "out_file";
	static final String DUMP_FILE = System.getProperty("user.dir") + File.separatorChar +DUMPFILE_NAME;
	static final String FILE_DUMP_COMMAND = " --write " + DUMP_FILE;
	static final String FILE_EXTENSION = "-01.csv";
	private static File dumFile;

	static{
		deleteAllFile();
	}
	
	private static String getCleanPath() {
		ClassLoader classLoader = File.class.getClassLoader();
		File classPathRoot = new File(classLoader.getResource("").getPath());
		return classPathRoot.getPath();
	}

	public static Set<AccessPoint> getAccessPoints(Adapter adapter, long delayMilliseconds) {
		System.out.println(adapter);
		Scanner sc;
		try {
			sc = getMonitorInput(COMMAND + " " + adapter.getInterface() + FILE_DUMP_COMMAND, delayMilliseconds);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		HashSet<AccessPoint> result = new HashSet<>();

		while (sc.hasNext()) {
			String line = null;
			line = sc.nextLine();
			if(line.startsWith("Station MAC")){
				break;
			}
			AccessPoint ap = getAccessPoint(line);
			if (ap != null) {
				result.add(getAccessPoint(line));
			}
		}
	    if(sc != null){
			sc.close();
		}
		//deleteAllFile();
		//if(dumFile != null){
		//	dumFile.delete();
		//}
		return result;
	}

	
	private static AccessPoint getAccessPoint(String line) {

		// line = line.replaceAll("( ) +", " ").trim();
		String[] tokens = line.split(",");

		// make some token cheks
		String bssid = tokens[0];
		if (!Validator.validateMac(bssid)) {
			return null;
		}
		String chanel = tokens[3];
		String enc = tokens[9];
		String essid = null;

		if(tokens.length >= 14){
			essid = tokens[13];
		}


		return new AccessPoint(bssid, chanel, enc, essid);
	}

	private static Scanner getMonitorInput(String command, long delayMilliseconds) throws FileNotFoundException {
		ProcessHandler.sedTerminationComandToProcess(ProcessHandler.startProcess(command, delayMilliseconds));

		dumFile = new File(DUMP_FILE + FILE_EXTENSION);
		Scanner sc = new Scanner(dumFile);

		return sc;
	}

	private static void deleteAllFile(){
		File dir = new File(System.getProperty("user.dir"));
		for(File file : dir.listFiles() ){
			if(file.getName().startsWith(DUMPFILE_NAME)){
				file.delete();
			}
		}
	}


	public static void main(String[] args) {
		//System.out.println(File.pathSeparator);
		List<Adapter> adapters = AirMonNG.getAdapters();
		for (Adapter adapter : adapters) {
			System.out.println(adapter);
		}
		System.out.println(AirMonNG.start(adapters.get(0)));
		Adapter monitor = AirMonNG.start(adapters.get(0));
		getAccessPoints(monitor, 20000).forEach(ap -> System.out.println(ap));
		// System.out.println(getMonitor(monitor,10000));
	}
}
