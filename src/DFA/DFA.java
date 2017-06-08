/*================================================================================
* CS544 - Computer Networks
* Drexel University, Spring 2017
* Protocol Implementation: Remote Control Drone Protocol
* Team 4:
* - Ajinkya Dhage
* - Ethan Shafer
* - Brent Varga
* - Xiaxin Xin
* --------------------------------------------------------------------------------
* File name: DFA.java
*
* Description:
* This is an abstract class which declares the methods that ReceiverDFA and DroneDFA should define.
* This class provides extensibility and it will be used to add extended or new features in future.
* It also ensures that the protocol adheres to the DFA guidelines during an active session.
*
* Requirements (Additional details can be found in the file below):
* - STATEFUL
*
*=================================================================================
* */

package DFA;
import Messages.Message;

abstract class DFA {
	
	/**
	 * This method should perform the initial authentication for Client-Server Handshake
	 * @param message
	 * @return {@link DFAResponse}
	 * @throws Exception
	 */
	abstract DFAResponse authenticate(Message message) throws Exception;
}
