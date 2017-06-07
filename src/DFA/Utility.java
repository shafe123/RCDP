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
* File name: Utility.java
*
* Description:
* This class defines the static utility methods that other classes of DFA package will use
* to perform some basic tasks.
*
* Requirements (Additional details can be found in the file below):
* - NONE
*
*=================================================================================
* */

package DFA;

public class Utility {

	/**
	 * Returns true if field String is null or empty. Otherwise, returns false
	 * @param field
	 * @return {@link Boolean}
	 */
	static boolean isEmpty(String field){
		return (field == null || field.length() == 0);
	}
}
