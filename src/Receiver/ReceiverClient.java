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

	public ReceiverClient(Socket socket, UIReceiver ui, String password) {
		echoSocket = socket;
		UI = ui;
		PASSWORD = password;
		VERSION = "1.0";
		RandomNum = "123";
	}

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

	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}

	/**
	 * method send command
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
}
