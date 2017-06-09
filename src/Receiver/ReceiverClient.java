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
* -SERVICE, CLIENT, CONCURRENT
*
*=================================================================================
* */

package Receiver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

import DFA.DFAResponse;
import DFA.DFAState;
import DFA.ReceiverDFA;
import Messages.ControlMessage;
import Messages.ControlMessage.ControlType;
import Messages.Message.MessageType;
import Messages.Message;

public class ReceiverClient implements Runnable {

	private String HOSTNAME;

	private String PORTNUMBER;
	public String PASSWORD;
	private UIReceiver UI;
	public String command;
	public Message MSG;
	public Integer msgID = 0;
	private Integer droneID = 0;
	public ControlType currentState = ControlType.GROUNDED;

	public boolean automode = false;
	public boolean propeller = false;
	public boolean beacon = false;

	public boolean takeoff = false;
	public boolean turnon = false;
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
// errorcase3 Authentication Random Number error use this line
//		RandomNum = "";
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

	public Integer getMsgID() {
		return msgID;
	}

	public void setMsgID(Integer msgID) {
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

				/**
				 * Determines what command was sent based on the message type
				 */
				switch (command) {
				case "TurnOn":
					if (turnon){
						UI.display("Already connected");
					}else{
					MSGType = MessageType.CONTROL;
					commandbyte = 0x00;
					json = new JSONObject();
					json.put("version", VERSION);
					json.put("random number A", RandomNum);
					json.put("password", PASSWORD);
					
					try {
						Message msgg = getReturnMsg(MSGType, commandbyte, json);
						
						sendMSG(msgg, dOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						UI.display(e1.getMessage());
					}
					}
					
					break;
				case "RDH":
					try {
						Message RDH = UI.messageQueue.take();
						sendMSG(RDH, dOut);
						currentState = ControlType.PREFLIGHT;
						turnon = true;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case "Up":
					if (takeoff) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x04;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("throttle", "0");
						json.put("attitude", "pitch");

						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
					} else {
						MSGType = MessageType.CONTROL;
						// error case invalid state uncomment next line
//						MSGType = MessageType.TEST;
						commandbyte = 0x02;
						json = new JSONObject();
						json.put("drone_id", droneID);
						takeoff = true;
						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
					}
					break;
				case "Down":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x04;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "pitch");

					try {
						Message msgg = getReturnMsg(MSGType, commandbyte, json);
						changeState(msgg);
						sendMSG(msgg, dOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						UI.display(e1.getMessage());
					}

					break;
				case "RollLeft":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x04;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "rollleft");
					
					try {
						Message msgg = getReturnMsg(MSGType, commandbyte, json);
						changeState(msgg);
						sendMSG(msgg, dOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						UI.display(e1.getMessage());
					}
					break;
				case "RollRight":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x04;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "rollright");

					try {
						Message msgg = getReturnMsg(MSGType, commandbyte, json);
						changeState(msgg);
						sendMSG(msgg, dOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						UI.display(e1.getMessage());
					}

					break;
				case "Left":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x04;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "yaw");

					try {
						Message msgg = getReturnMsg(MSGType, commandbyte, json);
						changeState(msgg);
						sendMSG(msgg, dOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						UI.display(e1.getMessage());
					}
					break;
				case "Right":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x04;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "yaw");

					try {
						Message msgg = getReturnMsg(MSGType, commandbyte, json);
						
						sendMSG(msgg, dOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						UI.display(e1.getMessage());
					}
					break;
				case "Forward":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x04;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "forward");

					try {
						Message msgg = getReturnMsg(MSGType, commandbyte, json);
						changeState(msgg);
						sendMSG(msgg, dOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						UI.display(e1.getMessage());
					}
					break;
				case "Backward":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x04;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "backward");

					try {
						Message msgg = getReturnMsg(MSGType, commandbyte, json);
						changeState(msgg);
						sendMSG(msgg, dOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						UI.display(e1.getMessage());
					}
					break;
				case "Land":
					if (currentState == ControlType.FLYING) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x06;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("landing_location", "123,123");

						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
					} else {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x09;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("landing_location", "123,123");
						
						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
						automode = false;
					}
					takeoff = false;
					break;
				case "Auto":
					if (automode) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x07;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("automode", "off");
						UI.display("auto off");
						automode = false;

						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
					} else {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x05;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("automode", "on");
						UI.display("auto on");
						automode = true;

						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
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

						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
					} else {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x01;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("propeller", "on");
						UI.display("propeller on");
						propeller = true;

						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
					}
					break;
				case "Beacon":
					if (beacon) {
						MSGType = MessageType.CONTROL;
						commandbyte = 0x0A;
						json = new JSONObject();
						json.put("drone_id", droneID);
						json.put("beacon", "off");
						json.put("to_mode", ControlType.PREFLIGHT);
						UI.display("beacon Off");
						beacon = false;

						try {
							Message msgg = getReturnMsg(MSGType, commandbyte, json);
							changeState(msgg);
							sendMSG(msgg, dOut);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							UI.display(e1.getMessage());
						}
					} 
//					else {
//						MSGType = MessageType.CONTROL;
//						commandbyte = 0x02;
//						json = new JSONObject();
//						json.put("drone_id", droneID);
//						json.put("beacon", "on");
//						UI.display("beacon On");
//						beacon = true;
//						try {
//							Message msgg = getReturnMsg(MSGType, commandbyte, json);
//							changeState(msgg);
//							sendMSG(msgg, dOut);
//						} catch (Exception e1) {
//							// TODO Auto-generated catch block
//							UI.display(e1.getMessage());
//						}
//					}
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
	 * Changes the current state depending on the message bing passed in
	 * @param message the message to receive the next state, of type Message
	 */
	public void changeState(Message message){
		try {
			DFAResponse nextState = DFAState.getNextState(message, currentState);
			if(nextState.getMessage().body instanceof ControlMessage){
				ControlMessage controlMessage2 = (ControlMessage) nextState.getMessage().body;
				currentState = controlMessage2.type;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method used to a retrieve message commands
	 * @param mSGType of type MessageType, the type of message being sent
	 * @param commandbyte of type byte, the command to be executed by the drone
	 * @param json of type JSONObject, which encodes the message parameters
	 */
	public Message getReturnMsg(MessageType mSGType, byte commandbyte, JSONObject json) throws Exception{
		ControlMessage controlMessage = new ControlMessage(currentState, commandbyte, json);
		Message mup = new Message(mSGType, 3, controlMessage);
		return mup;
	}

	/**
	 * Method used to send a message commands
	 * @param msg of type Message, takes in a message object to be sent to the drone
	 * @param dOut of type DataOutputStream, a stream to establish output to
	 * @throws IOException
	 */
	private void sendMSG(Message msg, DataOutputStream dOut) throws IOException {
		// TODO Auto-generated method stub
//		DFAResponse nextState = DFAState.getNextState(msg, currentState);
		UI.timeoutQueue.offer(msg.header.messageID);
		dOut.writeInt(Message.toByteArray(msg).length);
		dOut.write(Message.toByteArray(msg));
		
	}
}
