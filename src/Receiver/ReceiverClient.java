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

import DFA.DFAResponse;
import DFA.DFAState;
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

	public ReceiverClient(Socket socket, UIReceiver ui, String password,String version, String randomNum) {
		echoSocket = socket;
		UI = ui;
		PASSWORD = password;
		VERSION = version;
		RandomNum = randomNum;
		receiverDFA = new ReceiverDFA(PASSWORD, VERSION, RandomNum);
	}

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
	 * client socket use for send command
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
						commandbyte = 0x01;
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
					commandbyte = 0x01;
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
					commandbyte = 0x01;
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
					commandbyte = 0x01;
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
					commandbyte = 0x01;
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
					commandbyte = 0x01;
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
					commandbyte = 0x01;
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
					commandbyte = 0x01;
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
						commandbyte = 0x03;
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
						commandbyte = 0x02;
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
					}
					takeoff = false;
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
						commandbyte = 0x02;
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
						commandbyte = 0x00;
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



	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}

	/**
	 * method send command
	 */

	public void changeState(Message message){
		try {
			DFAResponse nextState = DFAState.getNextState(message, currentState);
			ControlMessage controlMessage2 = (ControlMessage) nextState.getMessage().body;
			currentState = controlMessage2.type;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Message getReturnMsg(MessageType mSGType, byte commandbyte, JSONObject json) throws Exception{
		ControlMessage controlMessage = new ControlMessage(currentState, commandbyte, json);
		Message mup = new Message(mSGType, 3, controlMessage);
		return mup;
	}
	

	private void sendMSG(Message msg, DataOutputStream dOut) throws IOException {
		// TODO Auto-generated method stub
//		DFAResponse nextState = DFAState.getNextState(msg, currentState);
		dOut.writeInt(Message.toByteArray(msg).length);
		dOut.write(Message.toByteArray(msg));
		
	}
}
