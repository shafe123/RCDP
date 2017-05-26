package Drone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DroneServer implements Runnable{
		private String PORT_NUMBER;
		private String PASSWARD;
		private String DRONE_ID;
		private UIDrone UI;
		
		public DroneServer(String p_number, String passward, String drone_id,UIDrone ui){
			PORT_NUMBER = p_number;
			PASSWARD = passward;
			DRONE_ID = drone_id;
			UI = ui;
			
		}
	

		public void run() {

			try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PORT_NUMBER));
					Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					UI.display("Received Message from client: " + inputLine);
					
					// Send back ACK base on received message
					switch (inputLine){
					case "ReceiverHello":
						out.println("DroneHello");
						UI.display("DroneHello sent");
						break;
					case "RollLeft":
						break;
					case "RollRight":
						break;
					case "UP":
						UI.display("ACK Up sent");
						out.println("ACK Up");
						break;
					case "Down":
						UI.display("ACK Down sent");
						out.println("ACK Down");
						break;
					case "Left":
						 break;
					case "Right":
						 break;
					case "Forward":
						 break;
					case "Backward":
						 break;
					case "Land":
						 break;
					case "Auto":
						 break;
					case "Propeller":
						 break;
					case "Beacon":
						 break;
					default:
						break;
					}
						

				}
			} catch (IOException e) {
				UI.display("Exception caught when trying to listen on port " + PORT_NUMBER
						+ " or listening for a connection");
				UI.display(e.getMessage());
			}

		
			}
}


