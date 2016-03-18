package comunication;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ComunicationStream<T> {
	 private BlockingQueue<T> StateManagerToOtherQueue = new LinkedBlockingQueue<>(10);
	 private BlockingQueue<T> OtherToStateManagerQueue = new LinkedBlockingQueue<>(10);
	 
	 public T getFromOther() throws InterruptedException{
			return OtherToStateManagerQueue.take();	
	 }
	 
	 public T getFromStateManager() throws InterruptedException{
			return StateManagerToOtherQueue.take();	
	 }
	 
	 public void sendToOther(T data) throws InterruptedException{
		 StateManagerToOtherQueue.put(data);
	 }
	 
	 public void sendToStateManager(T data) throws InterruptedException{
		 OtherToStateManagerQueue.put(data);
	 }
}
