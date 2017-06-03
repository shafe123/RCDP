package messages;

import java.util.Arrays;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ControlMessage extends MessageBody {
	public enum ControlType { UNIVERSAL, GROUNDED, PREFLIGHT, FLYING, AUTONOMOUS, BEACON; }
	
	public ControlType type;
	public byte command;
	public JSONObject params;
	
	
	public ControlMessage(ControlType type, byte cmd, JSONObject parameters) throws Exception {
		if(parameters.toString().getBytes().length > 128) {
			String err = "Size of parameters is too large to send message.";
			throw new Exception(err);
		}
		
		this.type = type;
		this.command = cmd;
		this.params = parameters;
	}
	
	public ControlMessage(byte[] bdy) throws ParseException {
		this.type = ControlType.values()[bdy[0]];
		switch (this.type) {
		/*case AUTONOMOUS:
			break;
		case BEACON:
			break;
		case FLYING:
			break;
		case GROUNDED:
			break;
		case PREFLIGHT:
			break;
		case UNIVERSAL:
			break;*/
		default:
			this.command = bdy[1];
			break;
		}
		
		//convert the last bytes into the JSON Object parameters
		byte[] byteParams = Arrays.copyOfRange(bdy, 2, bdy.length);
		JSONParser parser = new JSONParser();
		String stringParams = new String(byteParams);
		this.params = (JSONObject) parser.parse(stringParams);
	}
	
	
	@Override
	public int length() {
		return 2 + params.toString().getBytes().length;
	}

	@Override
	public byte[] toByteArray() {
		byte[] body = new byte[this.length()];
		body[0] = (byte) type.ordinal();
		body[1] = 0;
		
		byte[] json = params.toString().getBytes();
		for (int i = 0; i < json.length; i++)
		{
			body[i+2] = json[i];
		}
		
		return body;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ControlMessage) {
			ControlMessage other = (ControlMessage) obj;
			return (this.type == other.type && this.command == other.command && this.params.equals(other.params));
		}
		return false;
	}

	public static MessageBody fromByteArray(byte[] bdy) throws ParseException {
		return new ControlMessage(bdy);
	}
	
}
