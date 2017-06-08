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
* File name: MessageTests.java
*
* Description:
* Used for testing purposes to ensure proper functionality of protocol messages
* and sanity checking of related message types. This class uses junit to run the various
* tests.
*
* Requirements (Additional details can be found in the file below):
* -NONE
*
*=================================================================================
* */

package Messages;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Messages.ControlMessage.ControlType;
import Messages.DataMessage.DataType;
import Messages.ErrorMessage.ErrorType;
import Messages.Message.MessageType;

public class MessageTests {
	Message errMsg;
	Message dataMsg;
	Message ctrlMsg;
	Message ackMsg;
	byte[] imageData;

	@Before 
	public void onlyOnce() throws Exception {

		/**
		 * Template error message
		 */
		this.errMsg = new Message(MessageType.ERROR, 1, new ErrorMessage(ErrorType.CONNECTION_ERROR));
		
		/**
		 * Template data message with example image
		 */
		byte m2flag = 0x01;
		Path imagePath = Paths.get("assets/drone-image.png");
		byte[] image = Files.readAllBytes(imagePath);
		byte[] imageShortened = Arrays.copyOfRange(image, 0, 125);
		this.imageData = imageShortened;
		DataMessage m2bdy = new DataMessage(DataType.IMAGE, m2flag, imageShortened);
		this.dataMsg = new Message(MessageType.DATA, 2, m2bdy);
		
		/**
		 * Template control message
		 * not currently implemented is using enums for the CommandType, we will have to use bytes until
		 * that is implemented for quick reference on which command is in which position, use the CommandTypes class
 		 */
		byte m3Command = 0x00;
		JSONObject json = new JSONObject();
		json.put("unit_type", "receiver");
		json.put("unit_id", "abcdef");
		json.put("unit_ipaddress", "123.123.123.123");
		json.put("version", 1.0);
		ControlMessage m3bdy = new ControlMessage(ControlType.GROUNDED, m3Command, json);
		this.ctrlMsg = new Message(MessageType.CONTROL, 3, m3bdy);
		
		/**
		 * Template ack message
		 */
		AckMessage m5bdy = new AckMessage(3);
		this.ackMsg = new Message(MessageType.ACK, 5, m5bdy);
	}

	/**
	 * Test the error message id and length
	 * @throws Exception throws an error if assertion not met
	 */
	@Test
	public void ErrorMessageTest() throws Exception {
		assertEquals(errMsg.header.messageID, 1);
		assertEquals(errMsg.header.length, 1);
		assertTrue(errMsg.body instanceof ErrorMessage);
	}

	/**
	 * Test the error message length, id, and data message adhere to
	 * equality of various types
	 * @throws Exception throws an error if assertion not met
	 */
	@Test
	public void DataMessageTest() throws Exception {
		assertEquals(dataMsg.header.length, 127);
		assertEquals(dataMsg.header.messageID, 2);
		assertEquals(((DataMessage)dataMsg.body).data, this.imageData);
		assertEquals(((DataMessage)dataMsg.body).flag, 0x01);
	}

	/**
	 * Test the control message based on the message and state
	 * @throws Exception throws an error if assertion not met
	 */
	@Test
	public void ControlMessageTest() throws Exception {	
		assertEquals(((ControlMessage)ctrlMsg.body).type, ControlType.GROUNDED);
	}

	/**
	 * Test the ACK message based on the message type
	 * @throws Exception throws an error if assertion not met
	 */
	@Test
	public void AckMessageTest() throws Exception {
		assertEquals(ackMsg.header.type, MessageType.ACK);
	}

	/**
	 * Test the message conversion of various types (i.e. Message, byte array, and string)
	 * @throws Exception throws an error if assertion not met
	 */
	@Test
	public void MessageConversionTest() throws Exception {
		byte[] errbyte = Message.toByteArray(this.errMsg);
		Message errCheck = Message.fromByteArray(errbyte);
		assertTrue(this.errMsg.equals(errCheck));
		
		byte[] databyte = Message.toByteArray(this.dataMsg);
		Message dataCheck = Message.fromByteArray(databyte);
		assertTrue(this.dataMsg.equals(dataCheck));
		
		byte[] ctrlbyte = Message.toByteArray(this.ctrlMsg);
		Message ctrlCheck = Message.fromByteArray(ctrlbyte);
		assertEquals(this.ctrlMsg, ctrlCheck);
		
		byte[] ackbyte = Message.toByteArray(this.ackMsg);
		Message ackCheck = Message.fromByteArray(ackbyte);
		assertEquals(this.ackMsg, ackCheck);
	}

	/**
     * Test proper functionality of the to string method of the various message types
	 * @throws Exception throws an error if assertion not met
	 */
	@Test
	public void toStringTest() throws Exception {
		System.out.println(ctrlMsg.toString());
	}

}
