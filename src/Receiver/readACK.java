package Receiver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import org.hamcrest.core.IsInstanceOf;

import DFA.DFAResponse;
import DFA.ReceiverDFA;
import messages.ControlMessage;
import messages.Message;

public class readACK implements Runnable {

	public DataInputStream dIn;
	public UIReceiver UI;
	public Socket echoSocket;
	public boolean isAuthenticate = false;
	public String VERSION;
	public String RandomNum;
	public String PASSWORD;
	public ReceiverDFA receiverDFA;
	
	public readACK(Socket socket,UIReceiver ui,String password,String version, String randomNum) {
		UI = ui;
		echoSocket = socket;
		PASSWORD = password;
		VERSION = version;
		RandomNum = randomNum;
		receiverDFA = new ReceiverDFA(PASSWORD, VERSION, RandomNum);

	}
/**
 * use for read ACK
 */
	public void run() {

		int length;
		try {
			dIn = new DataInputStream(echoSocket.getInputStream());
			while(true){
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
					UI.timeoutQueue.poll();
					UI.display("Received Message Detail: \n" + msg.toString());
//					testDisplay(msg);
					// if isAuthenticate, the message is ack or err
					if (isAuthenticate){
						
					} else{
						// if not isAuthenticate, the msg is drone hello
						

						DFAResponse receiverResponse = receiverDFA.authenticate(msg);
						if (receiverResponse.isErrorFlag()){
							// if error
							UI.display(receiverResponse.getErrorMessage());
						} else{
							// if not error return reponse drone hello
//							UI.display("Drone Hello Received");
							UI.display("responseRDroneHello sent");
							Message responseRDroneHello = receiverResponse.getMessage();
							UI.commandQueue.offer("RDH");

							UI.messageQueue.offer(responseRDroneHello);
							isAuthenticate = true;
						}
					}
					


				}

			}
			}
		} catch (Exception e) {
			UI.display(e.getMessage());
		}
	}

	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}

}
