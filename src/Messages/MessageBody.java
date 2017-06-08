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
* File name: MessageBody.java
*
* Description:
* An abstract class that defines the common functionality of each message
*
* Requirements (Additional details can be found in the file below):
* -STATEFUL, SERVICE
*
* Refer to classes that extend this class for further details
*
*=================================================================================
* */

package Messages;

public abstract class MessageBody {

	/**
	 * @return The number of bytes used to represent the body
	 */
	public abstract int length();

	/**
	 * @return The actual message body as an array of bytes
	 */
	public abstract byte[] toByteArray();

	/**
	 * @param obj a type object to compare
	 * @return boolean, value if object equals depending on implementation
	 */
	public abstract boolean equals(Object obj);

	/**
	 * @return String value of the message
	 */
	public abstract String toString();
}
