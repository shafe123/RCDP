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
	private String PASSWORD;
	private UIReceiver UI;
	public String command;
	private String STATE;
	public Message MSG;
	public String msgID = "0";
	private Integer droneID = 0;
	public ControlType status = ControlType.GROUNDED;
	public boolean automode = false;
	public boolean propeller = false;
	public boolean beacon = false;
	public ControlMessage controlMessage;
	public Message msg;

	public ReceiverClient(String hostname, String portnumber, String password, UIReceiver ui) {
		HOSTNAME = hostname;
		PORTNUMBER = portnumber;
		PASSWORD = password;
		UI = ui;
	}

	public void run() {
		MessageType MSGType;
		byte commandbyte;
		JSONObject json;

		try (Socket echoSocket = new Socket(HOSTNAME, Integer.parseInt(PORTNUMBER));
				// PrintWriter out = new
				// PrintWriter(echoSocket.getOutputStream(),
				// true);
				// BufferedReader in = new BufferedReader(new
				// InputStreamReader(echoSocket.getInputStream()));
				DataOutputStream dOut = new DataOutputStream(echoSocket.getOutputStream());
				DataInputStream dIn = new DataInputStream(echoSocket.getInputStream());

		) {

			while (true) {

				try {
					while((command = UI.commandQueue.take()) == null){
						
					};
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					UI.display(e.getMessage());
				}

				switch (command) {
				case "TurnOn":

					break;
				// case "Up":
				// MSGType = MessageType.CONTROL;
				// commandbyte = 0x00;
				// json = new JSONObject();
				// json.put("throttle", "0");
				//
				// sendMSG(MSGType, commandbyte, json, dOut);

				// break;
				case "Up":
					MSGType = MessageType.CONTROL;
					commandbyte = 0x01;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("throttle", "0");
					json.put("attitude", "pitch");

					sendMSG(MSGType, commandbyte, json, dOut);

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
					MSGType = MessageType.CONTROL;
					commandbyte = 0x03;
					json = new JSONObject();
					json.put("drone_id", droneID);
					json.put("landing_location", "123,123");

					sendMSG(MSGType, commandbyte, json, dOut);
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
						commandbyte = 0x02;
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
				
				int length = dIn.readInt();
				UI.display(Integer.toString(length));
//				byte[] messagebyte = new byte[length];
//				dIn.readFully(messagebyte, 0, messagebyte.length);
//				try {
//					msg = Message.fromByteArray(messagebyte);
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				testDisplay(msg);
//
//				try {
//					// readACK(dIn);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					UI.display(e.getMessage());
//					
//				}
//
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

	private void readACK(DataInputStream dIn) throws Exception {
		// TODO Auto-generated method stub
		int length;
		while ((length = dIn.readInt()) != 0) {
			byte[] messagebyte = new byte[length];
			dIn.readFully(messagebyte, 0, messagebyte.length); // read the
																// message
			Message ACK;
			ACK = Message.fromByteArray(messagebyte);

			testDisplay(ACK);

		}
	}

	private void sendMSG(MessageType mSGType, byte commandbyte, JSONObject json, DataOutputStream dOut) {
		// TODO Auto-generated method stub
		try {
			ControlMessage controlMessage = new ControlMessage(status, commandbyte, json);
			Message mup = new Message(mSGType, 3, controlMessage);
			// UI.display(mup.toString());
			dOut.writeInt(Message.toByteArray(mup).length);
			dOut.write(Message.toByteArray(mup));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			UI.display(e.getMessage());
		}
		UI.display(command + " command sent");
	}
}
