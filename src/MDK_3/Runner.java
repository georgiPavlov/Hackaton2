package MDK_3;

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
            long waitTime = (long) (1000);
            int howMany =10;
            long howManyMinutes = 60000*1;
            ExecutorService pool = Executors.newFixedThreadPool(howMany);
            pool.execute(new ExecuteShellCommandDDOS(command1,System.currentTimeMillis(),waitTime,howManyMinutes));
            pool.execute(new ExecuteShellCommandEAPOL(
                    command2,System.currentTimeMillis(),waitTime,howManyMinutes));
            pool.shutdown();
            try {
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


