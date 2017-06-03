package messages;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import messages.ControlMessage.ControlType;
import messages.DataMessage.DataType;
import messages.ErrorMessage.ErrorType;
import messages.Message.MessageType;

public class TestMessages {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//templated error message
		Message m1 = new Message(MessageType.ERROR, 1, new ErrorMessage(ErrorType.CONNECTION_ERROR));
		
		//templated data message with example image
		byte m2flag = 0x01;
		Path imagePath = Paths.get("assets/drone-image.png");
		byte[] image = Files.readAllBytes(imagePath);
		byte[] imageShortened = Arrays.copyOfRange(image, 0, 127);
		DataMessage m2bdy = new DataMessage(DataType.IMAGE, m2flag, imageShortened);
		Message m2 = new Message(MessageType.DATA, 2, m2bdy);
		
		//templated control message
		//not currently implemented is using enums for the CommandType, we will have to use bytes until that is implemented
		//for quick reference on which command is in which position, use the CommandTypes class
		byte m3Command = 0x00;
		JSONObject json = new JSONObject();
		json.put("unit_type", "receiver");
		json.put("unit_id", "abcdef");
		json.put("unit_ipaddress", "123.123.123.123");
		json.put("version", 1.0);
		ControlMessage m3bdy = new ControlMessage(ControlType.GROUNDED, m3Command, json);
		Message m3 = new Message(MessageType.CONTROL, 3, m3bdy);
		
		byte[] m3byte = Message.toByteArray(m3);
		Message m4 = Message.fromByteArray(m3byte);
		
		boolean test = m3.equals(m4);
		System.out.println(test);
	}

}
