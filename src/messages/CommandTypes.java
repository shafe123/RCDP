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
* File name: CommandTypes.java
*
* Description:
* Defines the protocol command types as enumerated values based on the protocol definition
*
* Requirements (Additional details can be found in the file below):
* -STATEFUL, SERVICE
*
*=================================================================================
* */

package messages;

/**
 * These are five main command types along with their corresponding message which are
 * used to create the various messages and enforce shamefulness.
 */
public class CommandTypes {
	public enum UniversalCommand { ALARM; }
	public enum GroundedCommand { RECEIVERHELLO, DRONEHELLO, SECONDHELLO; }
	public enum PreflightCommand { PROPELLERON, PROPELLEROFF, TAKEOFF; }
	public enum FlyingCommand { MAINTAINLOCATION, CHANGEDIRECTION, AUTONOMOUSON, LANDED; }
	public enum BeaconCommand { STOPBEACON, ALARM; }
}
