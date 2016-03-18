package MDK_3;
import java.io.*;
import java.lang.reflect.Field;

/**
 * Created by georgipavlov on 17.03.16.
 */
public class ExecuteShellCommandEAPOL implements Runnable {

    private String command;
    private long startTime;
    private long waitTime;
    private long howManyMinutes;

    public String executeCommand(String command) {
        StringBuffer output=null;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (System.currentTimeMillis() - startTime < howManyMinutes){
            output = new StringBuffer();
            try {

                //p.waitFor();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;

                while (true) {
                    line = reader.readLine();
                    System.out.println("---------------------");
                    System.out.println("T2");
                    System.out.println(line);
                    System.out.println("----------------------");
                    output.append(line + "\n");
                    if(line.contains("connected!")){
                        System.out.println("stop...");
                        break;
                    }
                }/*
           // p.destroy();
            BufferedWriter in =
                    new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            try {
                in.write("^C");
            } catch (IOException e) {
                e.printStackTrace();
            }*/
                int pid = retrievePID(p);
                Process p2 = Runtime.getRuntime().exec("sudo -TSTP  kill " + pid);
                p2.waitFor();
                synchronized(this){
                    System.out.println("i am waiting");
                    this.wait(waitTime);}
                p2 = Runtime.getRuntime().exec("sudo  -CONT kill " + pid);
                p2.destroy();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return output.toString();

    }

    public ExecuteShellCommandEAPOL(String command,long startTime,long waitTime,long howManyMinutes){
        this.command = command;
        this.startTime = startTime;
        this.waitTime = waitTime;
        this.howManyMinutes = howManyMinutes;
    }

    @Override
    public void run() {
        executeCommand(command);
    }



    Integer retrievePID(final Process process) {
        if (process == null) {
            return null;
        }

        //--------------------------------------------------------------------
        // Jim Tough - 2014-11-04
        // NON PORTABLE CODE WARNING!
        // The code in this block works on the company UNIX servers, but may
        // not work on *any* UNIX server. Definitely will not work on any
        // Windows Server instances.
        final String EXPECTED_IMPL_CLASS_NAME = "java.lang.UNIXProcess";
        final String EXPECTED_PID_FIELD_NAME = "pid";
        final Class<? extends Process> processImplClass = process.getClass();
        if (processImplClass.getName().equals(EXPECTED_IMPL_CLASS_NAME)) {
            try {
                Field f = processImplClass.getDeclaredField(
                        EXPECTED_PID_FIELD_NAME);
                f.setAccessible(true); // allows access to non-public fields
                int pid = f.getInt(process);
                return pid;
            } catch (Exception e) {}
        } else {
            System.out.println("error");
        }
        //--------------------------------------------------------------------

        return null; // If PID was not retrievable, just return null
    }
}

