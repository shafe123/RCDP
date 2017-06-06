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
	public String PORT_NUMBER;
	public String PASSWORD;
	public String DRONE_ID;
	public UIDrone UI;
	public String VERSION;
	public String RandomNum;

	public DroneServer(String p_number, String passward, String drone_id, UIDrone ui) {
		PORT_NUMBER = p_number;
		PASSWORD = passward;
		DRONE_ID = drone_id;
		UI = ui;
		VERSION = "1.1";
		RandomNum = "234";

	}
	
	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWARD) {
		PASSWORD = pASSWARD;
	}

	public String getDRONE_ID() {
		return DRONE_ID;
	}

	public void setDRONE_ID(String dRONE_ID) {
		DRONE_ID = dRONE_ID;
	}

	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}

	public String getRandomNum() {
		return RandomNum;
	}

	public void setRandomNum(String randomNum) {
		RandomNum = randomNum;
	}

	/**
	 * read command and call DFA change status and return ACK
	 */

	public void run() {

		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PORT_NUMBER));
				Socket clientSocket = serverSocket.accept();

				DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());
				DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());) {
			int length;
			while ((length = dIn.readInt()) != 0) {
				if (length > 0) {
					byte[] messagebyte = new byte[length];
					dIn.readFully(messagebyte, 0, messagebyte.length); // read the message 
					Message msg;
					
					try {
						msg = Message.fromByteArray(messagebyte);
						displayMsg(msg);					
						testDisplay(msg);
						dOut.writeInt(Message.toByteArray(msg).length);
						dOut.write(Message.toByteArray(msg));
						
						UI.display("ack sent");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						UI.display(e.getMessage());
					}

				}



			}
		} catch (IOException e) {
			UI.display(
					"Exception caught when trying to listen on port " + PORT_NUMBER + " or listening for a connection");
			UI.display(e.getMessage());
		}

	}

	private void displayMsg(Message msg) {
		UI.display(msg.toString());
	}

	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}
}
