package Drone;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import DFA.DFAResponse;
import DFA.DFAState;
import DFA.DroneDFA;
import Messages.AckMessage;
import Messages.ControlMessage;
import Messages.Message;
import Messages.ControlMessage.ControlType;
import Messages.Message.MessageType;

public class DroneListener implements Runnable{
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
	public Socket clientSocket;
	
	public DroneListener(String p_number, String passward, String drone_id, UIDrone ui, Socket clientsocket){
		PORT_NUMBER = p_number;
		PASSWORD = passward;
		DRONE_ID = drone_id;
		UI = ui;
		VERSION = "1.2";
		RandomNum = "234";
		clientSocket = clientsocket;
		droneDFA = new DroneDFA(PASSWORD,VERSION,RandomNum,DRONE_ID);
		
		
	}
	
	public void run() {
		try{
		DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());
		DataInputStream dIn = new DataInputStream(clientSocket.getInputStream()); 
	int length;

	while ((length = dIn.readInt()) != 0) {
		if (length > 0) {
			byte[] messagebyte = new byte[length];
			dIn.readFully(messagebyte, 0, messagebyte.length); // read the message 
			Message msg;
			
			try {
				msg = Message.fromByteArray(messagebyte);
				UI.display("Received Message Detail: \n" + msg.toString());
				if (isAuthenticate){
					
					ackmessageId = msg.header.messageID;
					nextState = DFAState.getNextState(msg, currentState);
					if (nextState.isErrorFlag()){
						returnmsg = nextState.getMessage();
						UI.display(nextState.getErrorMessage());
						UI.display("error sent");

					} else{
						AckMessage ackMessage = new AckMessage(ackmessageId);
						
						ControlMessage controlMessage = (ControlMessage) nextState.getMessage().body;
						currentState = controlMessage.type;
						returnmsg = new Message(MessageType.ACK,messageID,ackMessage);
						messageID ++;
					}
					
				}else{

					// if not authenticated, the first message must be receiver hello
					droneResponse = droneDFA.authenticate(msg);

					// if error
					if (droneResponse.isErrorFlag()){

						returnmsg = droneResponse.getMessage();
						UI.display(droneResponse.getErrorMessage());
						UI.display("error sent");
					}else{

						// if not error
						
						ControlMessage controlMessage = (ControlMessage) msg.body;
					
						// if receievd message is receiver hello
						if (controlMessage.command == 0x01){
							UI.display("send back Drone Hello");
							returnmsg = droneResponse.getMessage();
							returnmsg.header.messageID = messageID;
							messageID ++;
						}else{
							// if received message is response drone hello
							UI.display("send ack for response drone hello");
							AckMessage ackMessage = new AckMessage(ackmessageId);
							returnmsg = new Message(MessageType.ACK,messageID,ackMessage);
							messageID ++;

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
		}catch (Exception e) {
			// TODO: handle exception
			UI.display(e.getMessage());
		}
		
	}
}
