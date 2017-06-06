package Receiver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import messages.Message;

public class readACK implements Runnable {

	public DataInputStream dIn;
	public UIReceiver UI;
	public Socket echoSocket;

	public readACK(Socket socket,UIReceiver ui) {
		UI = ui;
		echoSocket = socket;

	}
/**
 * use for read ACK
 */
	public void run() {

		int length;
		try {
			dIn = new DataInputStream(echoSocket.getInputStream());
			while(true){
			if ((length = dIn.readInt()) > 0) {
				if (length > 0) {
					byte[] messagebyte = new byte[length];
					dIn.readFully(messagebyte, 0, messagebyte.length);
					Message msg;

					msg = Message.fromByteArray(messagebyte);
					testDisplay(msg);

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
