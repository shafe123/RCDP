/*================================================================================
* CS544 - Computer Networks
* Drexel University, Spring 2017
* Protocol Implementation: Remote Control Drone Protocol
* Team 4:
* - Ajinkya Dhage
* - Ethan Shafer
* - Brent Varga
* - Xiaxin Xin
* --------------------------------------------------------------------------------
* File name: DroneServer.java
*
* Description:
* Implements the main functionality of the drone. Recall that the drone is
* implemented as a server so it's operations is quite similar with some
* restrictions on how many clients (i.e. receivers) can connect.
*
* Requirements (Additional details can be found in the file below):
* - SERVICE, CONCURRENT
*
*=================================================================================
* */

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

/**
 * DroneServer class implements runnable in order to execute
 * instanceas as a thread which meets concurrency
 */
public class DroneServer implements Runnable {
	private String PORT_NUMBER;
	private String PASSWARD;
	private String DRONE_ID;
	private UIDrone UI;

	/**
	 * DroneServer constructor that take a port number, password, drone id, and
	 * a drone user interface
	 * @param p_number string that represents a port number
	 * @param passward string that takes a passphrase
	 * @param drone_id string a unique drone identifier
	 * @param ui and a UIDrone object
	 */
	public DroneServer(String p_number, String passward, String drone_id, UIDrone ui) {
		PORT_NUMBER = p_number;
		PASSWARD = passward;
		DRONE_ID = drone_id;
		UI = ui;

	}

	/**
	 * Initial section to setup a socket with an associate port number and preparing
	 * input and output streams
	 */
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

				/**
				 * UI section to capture the message received
				 */
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

	/**
	 * TestDisplay initial testing to get things up and running
	 * @param msg
	 */
	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}
}
