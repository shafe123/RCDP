package Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ReceiverClient implements Runnable{
	
	private String HOSTNAME;
	private String PORTNUMBER;
	private String PASSWORD;
	private UIReceiver UI;
	public String Commend = "";
	
	public ReceiverClient(String hostname, String portnumber, String password, UIReceiver ui){
		HOSTNAME = hostname;
		PORTNUMBER = portnumber;
		PASSWORD = password;
		UI = ui;
		
	}
	
	

	public void run() {



		try (Socket echoSocket = new Socket(HOSTNAME, Integer.parseInt(PORTNUMBER));
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				
				) {

			while (true) {
				
				getCommend();
				switch (Commend) {
				case "UP":
					out.println("UP");
					Commend = "";
					break;

				default:
					break;
				}
				
				
			}
        } catch (UnknownHostException e) {
            UI.display("Don't know about host " + HOSTNAME);
        } catch (IOException e) {
            UI.display("Couldn't get I/O for the connection to " +
                HOSTNAME);
        } 
	}

	public void getCommend(){
		while (Commend.equals("")){
			
		}
	}
}
