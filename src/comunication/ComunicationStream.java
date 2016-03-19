package comunication;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ComunicationStream<T> {
	 private BlockingQueue<T> StateManagerToOtherQueue = new BoundedBuffer<>(100);
	 private BlockingQueue<T> OtherToStateManagerQueue = new BoundedBuffer<>(100);
	 
	 public T getFromOther() throws InterruptedException{
		 System.out.println("Msnigrt seeking request from GUI");
			return OtherToStateManagerQueue.take();	
	 }
	 
	 public T getFromStateManager() throws InterruptedException{
		 System.out.println("GUI seeking from Manager");
			return StateManagerToOtherQueue.take();	
	 }
	 
	 public void sendToOther(T data) throws InterruptedException {
		 System.out.println("Managet sending");
		 StateManagerToOtherQueue.put(data);
	 }
	 public void sendToStateManager(T data) throws InterruptedException{
		 System.out.println("GUI is sending");
		 OtherToStateManagerQueue.put(data);
	 }
}
