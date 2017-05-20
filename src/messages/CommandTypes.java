package messages;

public class CommandTypes {
	public enum UniversalCommand { ALARM; }
	public enum GroundedCommand { RECEIVERHELLO, DRONEHELLO, SECONDHELLO; }
	public enum PreflightCommand { PROPELLERON, PROPELLEROFF, TAKEOFF; }
	public enum FlyingCommand { MAINTAINLOCATION, CHANGEDIRECTION, AUTONOMOUSON, LANDED; }
	public enum BeaconCommand { STOPBEACON, ALARM; }
}
