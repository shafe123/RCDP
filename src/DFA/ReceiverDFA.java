package DFA;

import org.json.simple.JSONObject;

import messages.ControlMessage;
import messages.ErrorMessage;
import messages.Message;
import messages.MessageBody;
import messages.ControlMessage.ControlType;
import messages.ErrorMessage.ErrorType;
import messages.Message.MessageType;

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
	 * @throws Exception 
	 */
	@Override
	public DFAResponse authenticate(Message message) throws Exception {
		DFAResponse response = null;
		MessageBody messageBody = message.body;
		Message errorMessage = null;
		int messageID = message.header.messageID;
		
		if(messageBody instanceof ControlMessage){
			ControlMessage controlMessage = (ControlMessage) messageBody;
			JSONObject params = controlMessage.params;
			byte command = controlMessage.command;
			switch (command) {
			case 0x01:
						if(!params.containsKey("version")){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.VERSION_ERROR));
							response = new DFAResponse(errorMessage, true, "version field not present in message params");
							return response;
						}
						
						//set minimum of receiverVersion and droneVersion as final version.
						double receiverVersion = Double.parseDouble(String.valueOf(params.get("version")));
						double droneVersion = Double.parseDouble(version);
						String version = String.valueOf(receiverVersion < droneVersion ? receiverVersion : droneVersion);
						params.put("version", version);
						
						if(!params.containsKey("random number A")){//Check if random number from receiver is present
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number A field not present in message params");
							return response;
						}
						
						//Verify random number A sent by drone
						String randomNumberA = String.valueOf(params.get("random number A"));
						if(Utility.isEmpty(randomNumberA)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number A is null or empty");
							return response;
						}
						
						if(Utility.isEmpty(randomNumber)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number for receiver is null or empty");
							return response;
						}
						
						if(!randomNumberA.equals(randomNumber)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number A sent by drone is invalid");
							return response;
						}
						
						//Verify random number B sent by drone 
						if(!params.containsKey("random number B")){//Check if random number from receiver is present
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number B field not present in message params");
							return response;
						}
						String randomNumberB = String.valueOf(params.get("random number B"));
						if(Utility.isEmpty(randomNumberB)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number B is null or empty");
							return response;
						}
						controlMessage.command = 0x02;
						
						ControlMessage responseControlMessage = new ControlMessage(controlMessage.type, controlMessage.command, params);
						Message responseMessage = new Message(MessageType.CONTROL, 3, responseControlMessage);
						response = new DFAResponse(responseMessage, false, null);
						return response;
			default:
						errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_COMMAND));
						return new DFAResponse(errorMessage, true, "Invalid Command Byte");
			}
		}
		errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_MESSAGE));
		return new DFAResponse(errorMessage, true, "Message is not of type Control Message");
	}
}
