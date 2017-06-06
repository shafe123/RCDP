package DFA;
import messages.Message;
/**
 * 
 * @author Ajinkya
 * This is an abstract class which declares the methods that ReceiverDFA and DroneDFA should define.
 * This class provides extensibility and it will be used to add extended or new features in future.
 */
abstract class DFA {
	
	/**
	 * 
	 * @param message
	 * @return {@link DFAResponse}
	 * This method should perform the initial authentication for Client-Server Handshake
	 */
	abstract DFAResponse authenticate(Message message);
}
