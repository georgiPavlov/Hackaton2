package processes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;

public class ProcessHandler {
	static final String KILL_COMMNAD = "sudo kill";
	static public int getPid(Process process) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
    	Field f = process.getClass().getDeclaredField("pid");
    	f.setAccessible(true);
    	return  f.getInt(process);
    }
	
	public static void killProcess(RunningProcess process){
		startProcess(KILL_COMMNAD + " " + process.getPID());
	}
	
	public static void killProcesse(List<RunningProcess> processes){
		StringBuilder sb = new StringBuilder(KILL_COMMNAD + " ") ;
		for(RunningProcess process : processes){
			sb.append(process.getPID()).append(" ");
		}
		startProcess(sb.toString());
	}
	
	public static Process startProcess(String command) {
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
	}
	
	public static Scanner getProcessInputStream(String command){
		Process p = startProcess(command);
		return new Scanner(new InputStreamReader(p.getInputStream()));
	}
	
	public static Process startProcess(String command,long delayMilliseconds) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(delayMilliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	public static Scanner getProcessInputStream(String command,long delayMilliseconds) {
		Process p = startProcess(command,delayMilliseconds);
		try{
		return new Scanner(new InputStreamReader(p.getInputStream()));
		}finally{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			try {
				writer.write("^C");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//p.destroy();	
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static StringBuilder getProcessStringBuilder(String command,long delayMilliseconds) {
		Process p = startProcess(command,delayMilliseconds);
		try{
			Scanner sc = new Scanner(new InputStreamReader(p.getInputStream()));
			StringBuilder sb = new StringBuilder();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		    try {
				writer.write("^C");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    long start = System.currentTimeMillis();
		    //System.currentTimeMillis() - start < delayMilliseconds &&
		    while( sc.hasNext()){
		    	sb.append(sc.nextLine()).append(System.lineSeparator());
		    }
		    sc.close();
		    
		    return sb;
		}finally{
			
		}
	}
	
	public static void sedTerminationComandToProcess(Process process){
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
	    try {
			writer.write("^C");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //process.destroy();
	}
}
