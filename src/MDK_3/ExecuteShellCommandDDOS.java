package MDK_3;

import java.io.*;

/**
 * Created by georgipavlov on 17.03.16.
 */
public class ExecuteShellCommandDDOS implements Runnable {

    private String command;
    private long startTime;
    private long waitTime;
    private long howManyMinutes;

    public String executeCommand(String command) {
        StringBuffer output=null;
        Process p=null;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  try {
        // p.waitFor();
        //} catch (InterruptedException e) {
        //  e.printStackTrace();
        //  }

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
        while (System.currentTimeMillis() - startTime < howManyMinutes){
            output = new StringBuffer();
            try {
                String line;
                line = reader.readLine();
                if(line == null){
                    continue;
                }
                /*if(!line.equals("got authentication frame: from wrong AP or failed authentication!") && line != null){
                System.out.println("---------------------");
                  System.out.println("T1");
                    while ((reader.readLine())!= null && !line.equals("got authentication frame: from wrong AP or failed authentication!")){
                        line = reader.readLine();
                    System.out.println(line);
                    }
                   System.out.println("----------------------");
                }
               */    //output.append(line + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BufferedWriter in =
                new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        try {
            in.write("^C");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(p.isAlive());
        p.destroy();


        return output.toString();

    }

    public ExecuteShellCommandDDOS(String command,long startTime,long waitTime,long howManyMinutes){
        this.command = command;
        this.startTime = startTime;
        this.waitTime = waitTime;
        this.howManyMinutes = howManyMinutes;
    }

    @Override
    public void run() {
        executeCommand(command);
    }
}
