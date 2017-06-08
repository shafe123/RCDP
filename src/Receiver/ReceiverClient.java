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
* File name: ReceiverClient.java
*
* Description:
* Facilitates the establishment of a connection from the receiver end
* to the drone. The class implements run() a method in Runnable()
* for concurrency.
*
* Requirements (Additional details can be found in the file below):
* -SERVICE, CLIENT, CURRENT
*
*=================================================================================
* */

package Receiver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ResourceBundle.Control;

import org.json.simple.JSONObject;
import org.omg.CORBA.PUBLIC_MEMBER;

import DFA.ReceiverDFA;
import messages.ControlMessage;
import messages.ControlMessage.ControlType;
import messages.Message.MessageType;
import messages.Message;

public class ReceiverClient implements Runnable {

	private String HOSTNAME;

	private String PORTNUMBER;
	public String PASSWORD;
	private UIReceiver UI;
	public String command;
	public Message MSG;
	public String msgID = "0";
	private Integer droneID = 0;
	public ControlType status = ControlType.GROUNDED;
	public boolean automode = false;
	public boolean propeller = false;
	public boolean beacon = false;
	public ControlMessage controlMessage;
	public Message msg;
	public Socket echoSocket;
	public String VERSION;
	public String RandomNum;
	public ReceiverDFA receiverDFA;

	/**
	 * Constructs a receiver provided a socket, ui, password, version, and a unique number to
	 * simulate the connection with a drone entity.
	 * @param socket of type Socket, that the receiver wishes to connect to. Default is port 8080
	 * @param ui of type UIReceiver, the graphical interface of the receiver
	 * @param password of type String, that represents the secure passphrase to connect to the drone
	 * @param version of type String, the current version the receiver supports
	 * @param randomNum of type String, which designates a unique identifier for the receiver
	 */
	public ReceiverClient(Socket socket, UIReceiver ui, String password,String version, String randomNum) {
		echoSocket = socket;
		UI = ui;
		PASSWORD = password;
		VERSION = version;
		RandomNum = randomNum;
		receiverDFA = new ReceiverDFA(PASSWORD, VERSION, RandomNum);
	}

	/**
	 * Various getter and setter methods by the receiver
	 */
	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public Integer getDroneID() {
		return droneID;
	}

	public void setDroneID(Integer droneID) {
		this.droneID = droneID;
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
	 * Client socket use for sending commands to the drone
	 */
	public void run() {
		MessageType MSGType;
		byte commandbyte;
		JSONObject json;

		try (DataOutputStream dOut = new DataOutputStream(echoSocket.getOutputStream());

		) {

			while (true) {

				try {
					while ((command = UI.commandQueue.take()) == null) {

					}
					;
				} catch (InterruptedException e) {
					UI.display(e.getMessage());
				}

				switch (command) {
				case "TurnOn":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x00;
					json = new JSONObject();
					json.put("version", VERSION);
					json.put("random number A", RandomNum);
					json.put("password", PASSWORD);

					sendMSG(MSGType, commandbyte, json, dOut);
					
					break;
				case "RDH":
					try {
						Message RDH = UI.messageQueue.take();
						sendMSG(RDH, dOut);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "Up":
					if (status == ControlType.FLYING) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x01;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("throttle", "0");
						json.put("attitude", "pitch");

						sendMSG(MSGType, commandbyte, json, dOut);
					} else {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x02;
						json = new JSONObject();
						json.put("drone_id", droneID);

						sendMSG(MSGType, commandbyte, json, dOut);
					}
					break;
				case "Down":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x01;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "pitch");

					sendMSG(MSGType, commandbyte, json, dOut);

					break;
				case "RollLeft":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x01;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "roll");

					sendMSG(MSGType, commandbyte, json, dOut);
					break;
				case "RollRight":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x01;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "roll");

					sendMSG(MSGType, commandbyte, json, dOut);

					break;
				case "Left":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x01;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "yaw");

					sendMSG(MSGType, commandbyte, json, dOut);
					break;
				case "Right":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x01;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "yaw");

					sendMSG(MSGType, commandbyte, json, dOut);
					break;
				case "Forward":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x01;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "forward");

					sendMSG(MSGType, commandbyte, json, dOut);
					break;
				case "Backward":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x01;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "backward");

					sendMSG(MSGType, commandbyte, json, dOut);
					break;
				case "Land":
					if (status == ControlType.FLYING) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x03;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("landing_location", "123,123");

						sendMSG(MSGType, commandbyte, json, dOut);
					} else {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x02;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("landing_location", "123,123");

						sendMSG(MSGType, commandbyte, json, dOut);
					}
					break;
				case "Auto":
					if (automode) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x00;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("automode", "off");
						UI.display("auto off");
						automode = false;

						sendMSG(MSGType, commandbyte, json, dOut);
					} else {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x02;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("automode", "on");
						UI.display("auto on");
						automode = true;

						sendMSG(MSGType, commandbyte, json, dOut);
					}
					break;
				case "Propeller":
					if (propeller) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x00;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("propeller", "off");
						UI.display("propeller off");
						propeller = false;

						sendMSG(MSGType, commandbyte, json, dOut);
					} else {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x01;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("propeller", "on");
						UI.display("propeller on");
						propeller = true;

						sendMSG(MSGType, commandbyte, json, dOut);
					}
					break;
				case "Beacon":
					if (beacon) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x00;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("beacon", "off");
						UI.display("beacon Off");
						beacon = false;

						sendMSG(MSGType, commandbyte, json, dOut);
					} else {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x02;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("beacon", "on");
						UI.display("beacon On");
						beacon = true;
						sendMSG(MSGType, commandbyte, json, dOut);
					}
					break;
				default:
					break;
				}

			}
		} catch (UnknownHostException e) {
			UI.display("Don't know about host " + HOSTNAME);
		} catch (IOException e) {
			UI.display("Couldn't get I/O for the connection to " + HOSTNAME);
		}
	}

	/**
	 * Used to test the display of the message onto he drone
	 * graphical user interface.
	 * @param msg the message to be displayed on the interface of type Message
	 */
	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}

	/**
	 * Method used to send a message commands
	 * @param mSGType of type MessageType, the type of message being sent
	 * @param commandbyte of type byte, the command to be executed by the drone
	 * @param json of type JSONObject, which encodes the message parameters
	 * @param dOut of type DataOutputStream, a stream to establish output to
	 */
	private void sendMSG(MessageType mSGType, byte commandbyte, JSONObject json, DataOutputStream dOut) {
		try {
			ControlMessage controlMessage = new ControlMessage(status, commandbyte, json);
			Message mup = new Message(mSGType, 3, controlMessage);
			dOut.writeInt(Message.toByteArray(mup).length);
			dOut.write(Message.toByteArray(mup));
		} catch (Exception e) {
			UI.display(e.getMessage());
		}
		UI.display(command + " command sent");
	}

	/**
	 * Method used to send a message commands
	 * @param take of type Message, takes in a message object to be sent to the drone
	 * @param dOut of type DataOutputStream, a stream to establish output to
	 * @throws IOException
	 */
	private void sendMSG(Message take, DataOutputStream dOut) throws IOException {
		// TODO Auto-generated method stub
		dOut.writeInt(Message.toByteArray(take).length);
		dOut.write(Message.toByteArray(take));
		
	}
}
