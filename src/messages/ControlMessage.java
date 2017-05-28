package messages;

import java.util.Arrays;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ControlMessage extends MessageBody {
	public enum ControlType { UNIVERSAL, GROUNDED, PREFLIGHT, FLYING, AUTONOMOUS, BEACON; }
	
	public ControlType type;
	public CommandTypes command;
	public JSONObject params;
	
	
	public ControlMessage(ControlType type/*, CommandType cmd*/, JSONObject parameters) throws Exception {
		if(parameters.toString().getBytes().length > 128) {
			String err = "Size of parameters is too large to send message.";
			throw new Exception(err);
		}
		
		this.type = type;
		this.params = parameters;
	}
	
	public ControlMessage(byte[] bdy) throws ParseException {
		this.type = ControlType.values()[bdy[0]];
		switch (this.type) {
		case AUTONOMOUS:
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
			break;
		default:
			break;
		}
		
		//convert the last bytes into the JSON Object parameters
		byte[] byteParams = Arrays.copyOfRange(bdy, 2, bdy.length - 2);
		JSONParser parser = new JSONParser();
		String stringParams = new String(byteParams);
		this.params = (JSONObject) parser.parse(stringParams);
	}
	
	
	@Override
	public int length() {
		return params.toString().getBytes().length;
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

	public static MessageBody fromByteArray(byte[] bdy) throws ParseException {
		return new ControlMessage(bdy);
	}
	
}
