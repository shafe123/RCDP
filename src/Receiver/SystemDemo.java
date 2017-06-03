package Receiver;

import java.io.*;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SystemDemo {

   public static void main(String[] args) throws Exception {


String str = "This is a String ~ GoGoGo";

		final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		Thread running = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String data = queue.take();
						// handle the data
					} catch (InterruptedException e) {
						System.err.println("Error occurred:" + e);
					}
				}
			}
		});

		running.start();
		// Send data to the running thread
		for (int i = 0; i < 10; i++) {
			queue.offer("data " + i);
		}       
   }
} 