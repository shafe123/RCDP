package Drone;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import messages.Message;

public class DroneServer implements Runnable {
	private String PORT_NUMBER;
	private String PASSWARD;
	private String DRONE_ID;
	private UIDrone UI;

	public DroneServer(String p_number, String passward, String drone_id, UIDrone ui) {
		PORT_NUMBER = p_number;
		PASSWARD = passward;
		DRONE_ID = drone_id;
		UI = ui;

	}

	public void run() {

		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PORT_NUMBER));
				Socket clientSocket = serverSocket.accept();
				// PrintWriter out = new
				// PrintWriter(clientSocket.getOutputStream(), true);
				// BufferedReader in = new BufferedReader(new
				// InputStreamReader(clientSocket.getInputStream()));
				DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());
				DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());) {
			int length;
			while ((length = dIn.readInt()) != 0) {
				if (length > 0) {
					byte[] messagebyte = new byte[length];
					dIn.readFully(messagebyte, 0, messagebyte.length); // read the
																	// message
					Message msg;
					try {
						msg = Message.fromByteArray(messagebyte);
						testDisplay(msg);
						dOut.writeInt(Message.toByteArray(msg).length);
						dOut.write(Message.toByteArray(msg));
						UI.display("ack sent");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						UI.display(e.getMessage());
					}

				}

				// UI.display("Received Message from client: " + inputLine);

				// Send back ACK base on received message
				// switch (inputLine){
				// case "ReceiverHello":
				// UI.display("DroneHello sent");
				// break;
				// case "RollLeft":
				// break;
				// case "RollRight":
				// break;
				// case "UP":
				// UI.display("ACK Up sent");
				// break;
				// case "Down":
				// UI.display("ACK Down sent");
				// break;
				// case "Left":
				// break;
				// case "Right":
				// break;
				// case "Forward":
				// break;
				// case "Backward":
				// break;
				// case "Land":
				// break;
				// case "Auto":
				// break;
				// case "Propeller":
				// break;
				// case "Beacon":
				// break;
				// default:
				// break;
				// }

			}
		} catch (IOException e) {
			UI.display(
					"Exception caught when trying to listen on port " + PORT_NUMBER + " or listening for a connection");
			UI.display(e.getMessage());
		}

	}

	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}
}
