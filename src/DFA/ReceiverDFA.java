package DFA;

import org.json.simple.JSONObject;

import Receiver.ReceiverClient;
import messages.ControlMessage;
import messages.Message;
import messages.MessageBody;

/**
 * 
 * @author Ajinkya
 * This class provides functionality related to Receiver
 *
 */
public class ReceiverDFA extends DFA {
	private String password;
	private String version;
	private String randomNumber;
	
	public ReceiverDFA(String password, String version, String randomNumber) {
		this.password = password;
		this.version = version;
		this.randomNumber = randomNumber;
	}
	
	/**
	 * This method is used by Receiver initially to authenticate the Hello Handshake Message from Drone.
	 */
	@Override
	DFAResponse authenticate(Message message) {
		DFAResponse response = null;
		MessageBody messageBody = message.body;
		
		if(messageBody instanceof ControlMessage){
			ControlMessage controlMessage = (ControlMessage) messageBody;
			JSONObject params = controlMessage.params;
			byte command = controlMessage.command;
			switch (command) {
			case 0x01:
						if(!params.containsKey("version")){
							response = new DFAResponse(message, true, "version field not present in message params");
							return response;
						}
						
						//set minimum of receiverVersion and droneVersion as final version.
						double receiverVersion = Double.parseDouble(String.valueOf(params.get("version")));
						double droneVersion = Double.parseDouble(version);
						String version = String.valueOf(receiverVersion < droneVersion ? receiverVersion : droneVersion);
						params.put("version", version);
						
						if(!params.containsKey("random number A")){//Check if random number from receiver is present
							response = new DFAResponse(message, true, "random number A field not present in message params");
							return response;
						}
						
						//Verify random number A sent by drone
						String randomNumberA = String.valueOf(params.get("random number A"));
						if(Utility.isEmpty(randomNumberA)){
							response = new DFAResponse(message, true, "random number A is null or empty");
							return response;
						}
						
						if(Utility.isEmpty(randomNumber)){
							response = new DFAResponse(message, true, "random number for receiver is null or empty");
							return response;
						}
						
						if(!randomNumberA.equals(randomNumber)){
							response = new DFAResponse(message, true, "random number A sent by drone is invalid");
							return response;
						}
						
						//Verify random number B sent by drone 
						if(!params.containsKey("random number B")){//Check if random number from receiver is present
							response = new DFAResponse(message, true, "random number B field not present in message params");
							return response;
						}
						String randomNumberB = String.valueOf(params.get("random number B"));
						if(Utility.isEmpty(randomNumberB)){
							response = new DFAResponse(message, true, "random number B is null or empty");
							return response;
						}
						controlMessage.command = 0x02;
						response = new DFAResponse(message, false, null);
						return response;
			default:
						return new DFAResponse(message, true, "Invalid Command Byte");
			}
		}
		return new DFAResponse(message, true, "Message is not of type Control Message");
	}
}
