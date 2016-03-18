package airmon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import adapter.Adapter;
import adapter.WifiAdapter;
import processes.ProcessHandler;
import processes.RunningProcess;

public final class AirMonNG {
	static final int NUMBER_OF_TOKENS = 3;
	static final String INIT_COMMAND = "sudo airmon-ng";
	static final String START_COMMAND = "sudo airmon-ng start";
	private static List<Adapter> entries = new ArrayList<>();

	static {
		try {
			execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	public static Adapter doubleStart(Adapter adapter){
		start(adapter);
		return start(adapter);
	}
	
	public static Adapter start(Adapter adapter) {
		Scanner reader = ProcessHandler.getProcessInputStream(START_COMMAND + " " + adapter.getInterface());
		
		//killing the processes which can make trouble
		List<RunningProcess>  processesToKill = getTroubleRunningProcess(reader);
		ProcessHandler.killProcesse(processesToKill);
		//
		try{
			while (reader.hasNext()) {
				String line = reader.nextLine().trim();
				if (line == null || line.isEmpty() || !line.contains("monitor mode enabled on")) {
					continue;
				}
			    
				int startIndex = line.indexOf("monitor mode enabled on") + "monitor mode enabled on".length();
				int endIndex = line.indexOf(" ",startIndex+1);
				if(endIndex == -1){
					endIndex = line.length();
				}
				String monitor = line.substring(startIndex, endIndex).replaceAll("[^a-zA-Z0-9]", "");
				return new WifiAdapter(monitor,null,null);
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	
	}
	
	public static List<Adapter> getAdapters() {
		return entries;
	}
    
	private static List<RunningProcess> getTroubleRunningProcess(Scanner reader){
    	while (reader.hasNext()) {
    		String line = reader.nextLine();
    		if(line.startsWith("PID") || line.startsWith("Pid") || line.startsWith("pid")){
    			break;
    		}
    	}
    	List<RunningProcess> resultSet = new ArrayList<>();
    	while (reader.hasNext()) {
    		String[] tokens = reader.nextLine().replaceAll("[\t]+", "\t").split("\t");
    		boolean isEmpty = tokens[0].replaceAll("[0-9]", "").equals("");
    		if(tokens.length > 0 && tokens[0].length() > 0&& isEmpty){
    			resultSet.add(new RunningProcess(tokens[0],tokens[1]));
    		}else{
    			break;
    		}
    	}
    	
    	return resultSet;
    }
	
	/*private static Process getProcess(String command) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return p;
	}*/
	
	private static void execute() throws Exception {
	
		Scanner reader = ProcessHandler.getProcessInputStream(INIT_COMMAND);
		
		try {
			while (reader.hasNext()) {
				String line = reader.nextLine().trim();
				if (line == null || line.isEmpty() || line.startsWith("Interface")) {
					continue;
				}

				String[] tokens = line.replaceAll("[\t]+", "\t").split("\t");
				List<String> list = new ArrayList<>(NUMBER_OF_TOKENS);

				for (String token : tokens) {
					token = token.trim();
					if (token != null && !token.isEmpty()) {
						list.add(token);
					}
				}

				if (list.size() > 3) {
					throw new Exception("Two many tokens");
				}

				if (list.size() < 1) {
					throw new Exception("Two few tokens");
				}

				entries.add(new WifiAdapter(list.get(0), list.size() > 1 ? list.get(1) : null,
						list.size() > 2 ? list.get(2) : null));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

	}
	
	public static void main(String[] args) {
		List<Adapter> adapters = getAdapters();
		for (Adapter adapter:adapters) {
			System.out.println(adapter);
		}
		System.out.println(AirMonNG.start(adapters.get(0)));
		//System.out.println(AirMonNG.start(adapters.get(0)));

	}
	
}
