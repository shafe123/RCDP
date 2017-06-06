package messages;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.junit.Test;

import messages.ControlMessage.ControlType;
import messages.DataMessage.DataType;
import messages.ErrorMessage.ErrorType;
import messages.Message.MessageType;

public class MessageTests {

	@Test
	public void ErrorMessageTest() throws Exception {
		//templated error message
		Message m1 = new Message(MessageType.ERROR, 1, new ErrorMessage(ErrorType.CONNECTION_ERROR));
		assertEquals(m1.header.messageID, 1);
		assertEquals(m1.header.length, 1);
		assertTrue(m1.body instanceof ErrorMessage);
	}
	
	@Test
	public void DataMessageTest() throws Exception {
		
		//templated data message with example image
		byte m2flag = 0x01;
		Path imagePath = Paths.get("assets/drone-image.png");
		byte[] image = Files.readAllBytes(imagePath);
		byte[] imageShortened = Arrays.copyOfRange(image, 0, 127);
		DataMessage m2bdy = new DataMessage(DataType.IMAGE, m2flag, imageShortened);
		Message m2 = new Message(MessageType.DATA, 2, m2bdy);
		assertEquals(m2.header.length, 129);
		assertEquals(m2.header.messageID, 2);
		assertEquals(((DataMessage)m2.body).data, imageShortened);
		assertEquals(((DataMessage)m2.body).flag, 0x01);
		
	}
	
	
	@Test
	public void ControlMessageTest() throws Exception {	
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
		assertEquals(((ControlMessage)m3.body).type, ControlType.GROUNDED);
	}
	
	@Test
	public void MessageConversionTest() throws Exception {	
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
		
		assertEquals(m3, m4);
		//boolean test = m3.equals(m4);
		//System.out.println(test);
	}
		
	public void AckMessageTest() throws Exception {
		//templated ack message
		AckMessage m5bdy = new AckMessage(3);
		Message m5 = new Message(MessageType.ACK, 5, m5bdy);
		
		assertEquals(m5.header.type, MessageType.ACK);
	}

}
