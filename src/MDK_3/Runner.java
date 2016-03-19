package MDK_3;

import util.Connection_WPA;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by georgipavlov on 18.03.16.
 */

    public class Runner {
         private String command1;
         private String command2;


         public Runner(String command1,String command2){
             this.command1 = command1;
             this.command2 = command2;
         }

        public void startMDK3() {

            long start = System.currentTimeMillis();
            long waitTime =  (1000L);
            int howMany =2;
            long howManyMinutes = 60000*1;
            ExecutorService pool = Executors.newFixedThreadPool(howMany);
            for(int i = 0 ; i < howMany; i++) {
                pool.execute(new ExecuteShellCommandDDOS(command1, System.currentTimeMillis(), waitTime, howManyMinutes));
                pool.execute(new ExecuteShellCommandEAPOL(
                        command2, System.currentTimeMillis(), waitTime, howManyMinutes));
            }
            pool.shutdown();
            try {
                pool.awaitTermination(waitTime, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                pool.shutdownNow();
            }

        }
    }


