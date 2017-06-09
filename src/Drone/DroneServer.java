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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import DFA.DFAResponse;
import DFA.DFAState;
import DFA.DroneDFA;
import Messages.AckMessage;
import Messages.ControlMessage;
import Messages.Message;
import Messages.Message.MessageType;
import Messages.ControlMessage.ControlType;

/**
 * DroneServer class implements runnable in order to execute
 * instanceas as a thread which meets concurrency
 */
public class DroneServer implements Runnable {
	public String PORT_NUMBER;
	public String PASSWORD;
	public String DRONE_ID;
	public UIDrone UI;
	public String VERSION;
	public String RandomNum;
	public DroneDFA droneDFA;
	public DFAResponse droneResponse;
	public DFAResponse nextState;
	public ControlType currentState = ControlType.GROUNDED;
	
	public int ackmessageId;
	public Message returnmsg;
	public boolean isAuthenticate = false;
	public int messageID = 100;
	public String command;


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
		PASSWORD = passward;
		DRONE_ID = drone_id;
		UI = ui;
		VERSION = "1.2";
		RandomNum = "234";
		droneDFA = new DroneDFA(PASSWORD,VERSION,RandomNum,DRONE_ID);

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
	 * Initial section to setup a socket with an associate port number and preparing
	 * input and output streams. Additionally, it includes read command, call to DFA states status , and returns ACKs.
	 */
	public void run() {

		try {
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PORT_NUMBER));
			while(true){
				Socket clientSocket = serverSocket.accept();
				new Thread( new DroneListener(PORT_NUMBER,PASSWORD,DRONE_ID,UI,clientSocket)).start();
				
			}
				
		} catch (IOException e) {
			UI.display(
					"Exception caught when trying to listen on port " + PORT_NUMBER + " or listening for a connection");
			UI.display(e.getMessage());
		}
	}
}
