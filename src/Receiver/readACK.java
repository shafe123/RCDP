package Receiver;

import java.io.DataInputStream;
import java.io.IOException;

import messages.Message;

public class readACK implements Runnable {

	public DataInputStream dIn;
	public UIReceiver UI;

	public readACK(DataInputStream din, UIReceiver ui) {
		dIn = din;
		UI = ui;

	}

	public void run() {

		int length;
		try {
			while ((length = dIn.readInt()) != 0) {
				if (length > 0) {
					byte[] messagebyte = new byte[length];
					dIn.readFully(messagebyte, 0, messagebyte.length);
					// TODO Auto-generated catch block
					Message msg;

					msg = Message.fromByteArray(messagebyte);
					testDisplay(msg);
					break;

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			UI.display(e.getMessage());
		}
	}

	public void testDisplay(Message msg) {
		for (byte theByte : Message.toByteArray(msg)) {
			UI.display(Integer.toHexString(theByte));
		}
	}

}
