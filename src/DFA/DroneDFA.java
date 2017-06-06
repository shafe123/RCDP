package DFA;

import org.json.simple.JSONObject;

import Drone.DroneServer;
import messages.ControlMessage;
import messages.Message;
import messages.MessageBody;

/**
 * 
 * @author Ajinkya
 * This class provides functionality related to Drone
 */
public class DroneDFA extends DFA {
	private DroneServer drone;
	
	public DroneServer getDrone() {
		return drone;
	}
	public void setDrone(DroneServer drone) {
		this.drone = drone;
	}
	
	public DroneDFA(DroneServer drone) {
		this.drone = drone;
	}
	
	@Override
	DFAResponse authenticate(Message message) {
		DFAResponse response = null;
		MessageBody messageBody = message.body;
		
		if(messageBody instanceof ControlMessage){
			ControlMessage controlMessage = (ControlMessage) messageBody;
			JSONObject params = controlMessage.params;
			byte command = controlMessage.command;
			switch (command) {
			case 0x00:
						if(!params.containsKey("password")){
							response = new DFAResponse(message, true, "password field not present in message params");
							return response;
						}
						//Authenticate Password
						String receiverPassword = (String) params.get("password");
						String dronePassword = drone.PASSWORD;
						if(Utility.isEmpty(receiverPassword)){
							response = new DFAResponse(message, true, "Receiver Password is null or empty");
							return response;
						}
						
						if(Utility.isEmpty(dronePassword)){
							response = new DFAResponse(message, true, "Drone Password is null or empty");
							return response;
						}
						
						if(!receiverPassword.equals(dronePassword)){
							response = new DFAResponse(message, true, "Incorrect Password");
							return response;
						}
						
						if(!params.containsKey("version")){
							response = new DFAResponse(message, true, "version field not present in message params");
							return response;
						}
						
						//set minimum of receiverVersion and droneVersion as final version.
						double receiverVersion = Double.parseDouble(String.valueOf(params.get("version")));
						double droneVersion = Double.parseDouble(drone.VERSION);
						String version = String.valueOf(receiverVersion < droneVersion ? receiverVersion : droneVersion);
						params.put("version", version);
						
						if(!params.containsKey("random number A")){//Check if random number from receiver is present
							response = new DFAResponse(message, true, "random number A field not present in message params");
							return response;
						}
						
						if(Utility.isEmpty(String.valueOf(params.get("random number A")))){
							response = new DFAResponse(message, true, "random number A is null or empty");
							return response;
						}
						
						//Add random number from drone
						params.put("random number B", String.valueOf(Math.random()));
						controlMessage.command = 0x01;
						response = new DFAResponse(message, false, null);
						return response;
			case 0x02:
						//Verify Version
						if(!params.containsKey("version")){
							response = new DFAResponse(message, true, "version field not present in message params");
							return response;
						}
						
						version = String.valueOf(params.get("version"));
						if(Utility.isEmpty(version)){
							response = new DFAResponse(message, true, "Version is null or empty");
							return response;
						}
						
					    if(!version.equals(drone.VERSION)){
					    	response = new DFAResponse(message, true, "Version is invalid");
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
						
						String randomNumber = drone.getRandomNum();
						if(Utility.isEmpty(randomNumber)){
							response = new DFAResponse(message, true, "random number for drone is null or empty");
							return response;
						}
						
						if(!randomNumberB.equals(randomNumber)){
							response = new DFAResponse(message, true, "random number B sent by drone is invalid");
							return response;
						}
						response = new DFAResponse(message, false, null);
						return response;
			
			default:
						return new DFAResponse(message, true, "Invalid Command Byte");
			}
		}
		return new DFAResponse(message, true, "Message is not of type Control Message");
	}
}
