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
* File name: readACK.java
*
* Description:
* This file is used to establish the initial acknowledgement between the receiver
* and the drone and facilitate communications between the two by adhering to the
* receiver DFA.
*
* Requirements (Additional details can be found in the file below):
* -SERVICE, UI, CLIENT, CONCURRENT
*
*=================================================================================
* */

package Receiver;

import java.io.DataInputStream;
import java.net.Socket;

import org.hamcrest.core.IsInstanceOf;

import DFA.DFAResponse;
import DFA.ReceiverDFA;
import Messages.ControlMessage;
import Messages.Message;

public class ReadACK implements Runnable {

	public DataInputStream dIn;
	public UIReceiver UI;
	public Socket echoSocket;
	public boolean isAuthenticate = false;
	public String VERSION;
	public String RandomNum;
	public String PASSWORD;
	public ReceiverDFA receiverDFA;
	public boolean running = true;

	/**
	 * Constructs the initial read acknowledgment of the receiver provided
	 * socket, ui, password, version, and rnadom number.
 	 * @param socket the socket used to connect of type socket
	 * @param ui the receiver graphical user interface of type UIReceiver
	 * @param password the passphrase to connect to the drone, of type String
	 * @param version the version of the protocol the receiver currently has of type String
	 * @param randomNum a randomly generated number to establish unique session
	 */
	public ReadACK(Socket socket, UIReceiver ui, String password, String version, String randomNum) {
		UI = ui;
		echoSocket = socket;
		PASSWORD = password;
		VERSION = version;
		RandomNum = randomNum;
		receiverDFA = new ReceiverDFA(PASSWORD, VERSION, RandomNum);
	}

/**
 * Use for read ACK
 */
	public void run() {

		int length;
		try {
			dIn = new DataInputStream(echoSocket.getInputStream());
			while(running){
			if (UI.timeoutQueue.size() > 6){
				UI.display("LOST SIGNAL!!!!!!");
			} else if (UI.timeoutQueue.size() > 3){
				UI.display("LOW SIGNAL!!!!!!");
			}
			if ((length = dIn.readInt()) > 0) {
				if (length > 0) {
					byte[] messagebyte = new byte[length];
					dIn.readFully(messagebyte, 0, messagebyte.length);
					Message msg;
					msg = Message.fromByteArray(messagebyte);
			// error case lost signal error, comment next line and send any 8 commands
					UI.timeoutQueue.poll();
					UI.display("Received Message Detail: \n" + msg.toString() + "\n");
//					testDisplay(msg);

					/**
					 * If isAuthenticate, the message is ack or err
					 */
					if (isAuthenticate){
						
					} else{

						/**
						 * If not isAuthenticate, the msg is drone hello
						 */
						
						if(msg.body instanceof ControlMessage){
							UI.display("Drone Hello Received");
							DFAResponse receiverResponse = receiverDFA.authenticate(msg);
						
						// if error
						if (receiverResponse.isErrorFlag()){
							UI.display(receiverResponse.getErrorMessage());

						} else{ // if not error return response drone hello
//							UI.display("Drone Hello Received");
							UI.display("responseRDroneHello sent");
							Message responseRDroneHello = receiverResponse.getMessage();
							UI.commandQueue.offer("RDH");

							UI.messageQueue.offer(responseRDroneHello);
							isAuthenticate = true;
						}}else{
							UI.commandQueue.offer("Close");
							running = false;
						}
						
					}
				}
			}
			}
		} catch (Exception e) {
			UI.display(e.getMessage());
		}
	}

	/**
	 * @param msg the message to display to the receiver GUI
	 */
	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}

}
