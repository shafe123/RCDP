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
* File name: DroneListener.java
*
* Description:
* Used for establishing multiple connections on the drone.
* When multiple receivers try to connect the first valid authenticate connection
* will gain control of the drone while the subsequent connections will not have
* control but have limited control.
*
* Requirements (Additional details can be found in the file below):
* - UI
* - CONCURRENT
*
*=================================================================================
* */

package Drone;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import DFA.DFAResponse;
import DFA.DFAState;
import DFA.DroneDFA;
import Messages.AckMessage;
import Messages.ControlMessage;
import Messages.ErrorMessage;
import Messages.Message;
import Messages.ControlMessage.ControlType;
import Messages.ErrorMessage.ErrorType;
import Messages.Message.MessageType;

public class DroneListener implements Runnable {
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
	public static boolean isAuthenticate = false;
	public int messageID = 100;
	public String command;
	public Socket clientSocket;

	/**
	 * Constructs the drone listener in order to find any incoming connections that
	 * match the following parameters.
	 * @param p_number is the port number to establish connection to. This is of type String
	 * @param passward a unique passphrase used to establish authentication and is of type String
	 * @param drone_id uniquely identifies the drone with any incoming connections
	 * @param ui is the drone graphical user interface of type UIDrone
	 * @param clientsocket of type Socket
	 */
	public DroneListener(String p_number, String passward, String drone_id, UIDrone ui, Socket clientsocket,String randomNum) {
		PORT_NUMBER = p_number;
		PASSWORD = passward;
		DRONE_ID = drone_id;
		UI = ui;
		VERSION = "1.1";
		RandomNum = randomNum;
		clientSocket = clientsocket;
	}

	/**
	 * Implements runnable which assists in the threading process fulfilling hte
	 * concurrent requirement
	 */
	public void run() {
		try {
			DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());
			int length;

			while ((length = dIn.readInt()) != 0) {
				if (length > 0) {
					byte[] messagebyte = new byte[length];

					/**
					 * Message is read in
					 */
					dIn.readFully(messagebyte, 0, messagebyte.length);
					Message msg;

					try {
						msg = Message.fromByteArray(messagebyte);
						UI.display("Received Message from:" +clientSocket.getRemoteSocketAddress().toString()+ " Detail: \n" + msg.toString() + "\n");
						if (isAuthenticate) {
							// if isAuthenticate and a new receiver want to
							// connect, send connect deny error 
							ControlMessage controlMessage1 = (ControlMessage) msg.body;
							if (controlMessage1.type == ControlType.GROUNDED && controlMessage1.command == 0x00) {
								returnmsg = new Message(MessageType.ERROR, 0, new ErrorMessage(ErrorType.Connect_Deny));
							} else {

								ackmessageId = msg.header.messageID;
								nextState = DFAState.getNextState(msg, currentState);
								if (nextState.isErrorFlag()) {
									returnmsg = nextState.getMessage();
									UI.display(nextState.getErrorMessage());
									UI.display("error sent");

								} else {
									AckMessage ackMessage = new AckMessage(ackmessageId);

									ControlMessage controlMessage = (ControlMessage) nextState.getMessage().body;
									currentState = controlMessage.type;
									returnmsg = new Message(MessageType.ACK, messageID, ackMessage);
									messageID++;
								}
							}
						} else {

							/**
							 * If not authenticated, the first message must be
							 * receiver hello
							 */
							droneDFA = new DroneDFA(PASSWORD, VERSION, RandomNum, DRONE_ID);
							droneResponse = droneDFA.authenticate(msg);

							/**
							 * If an error occurs
							 */
							if (droneResponse.isErrorFlag()) {

								returnmsg = droneResponse.getMessage();
								UI.display(droneResponse.getErrorMessage());
								UI.display("error sent");
							} else {

								/**
								 * If no error occurs
								 */
								ControlMessage controlMessage = (ControlMessage) msg.body;

								/**
								 * If the received message is receiver hello
								 */
								if (controlMessage.command == 0x01) {
									UI.display("send back Drone Hello");
									returnmsg = droneResponse.getMessage();
									returnmsg.header.messageID = messageID;
									messageID++;
								} else {

									/**
									 * If received message is response drone hello
									 */
									UI.display("send ack for response drone hello");
									AckMessage ackMessage = new AckMessage(ackmessageId);
									returnmsg = new Message(MessageType.ACK, messageID, ackMessage);
									messageID++;

									currentState = ControlType.PREFLIGHT;

									isAuthenticate = true;
									UI.display("Authenticate Done");
								}
							}
						}
						UI.currentState.setText("" + currentState);
						dOut.writeInt(Message.toByteArray(returnmsg).length);
						dOut.write(Message.toByteArray(returnmsg));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						UI.display(e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			UI.display(e.getMessage());
		}

	}
	

}
