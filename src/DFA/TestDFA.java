package DFA;

import java.net.Socket;

import org.json.simple.JSONObject;

import Drone.DroneServer;
import Drone.UIDrone;
import Receiver.ReceiverClient;
import Receiver.UIReceiver;
import messages.ControlMessage;
import messages.Message;
import messages.ControlMessage.ControlType;
import messages.Message.MessageType;

public class TestDFA {
	public static void main(String[] args) throws Exception {
		//Steps to be followed for authenticating a receiver hello message and receiver response hello
		byte commandByte = 0x00;
		JSONObject json = new JSONObject();
		json.put("password", "pass");
		json.put("version", "1.2");
		json.put("random number A", "2.3456");
		ControlMessage controlMessage = new ControlMessage(ControlType.GROUNDED, commandByte, json);
		Message message = new Message(MessageType.CONTROL, 3, controlMessage);
		
		DroneServer droneServer = new DroneServer("8080", "pass", "1234", new UIDrone());
		DroneDFA droneDFA = new DroneDFA(droneServer);
		DFAResponse droneResponse = droneDFA.authenticate(message);
		
		//Steps to be followed for authenticating a drone hello message
		json.put("random number B", "2.3456");
		ReceiverClient receiverClient = new ReceiverClient(new Socket(), new UIReceiver());
		ReceiverDFA receiverDFA = new ReceiverDFA(receiverClient);
		DFAResponse receiverResponse = receiverDFA.authenticate(message);
		
		//Steps to be followed for getting next DFA state
		ControlType currentState = ControlType.PREFLIGHT;
		DFAResponse nextState = DFAState.getNextState(message, currentState);
	}

}
