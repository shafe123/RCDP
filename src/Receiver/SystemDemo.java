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
* File name: SystemDemo.java
*
* Description:
* Demonstrates the initial interactions between the receiver and the drone.
* First interaction is the handshake which consist of the password and
* version negotiation. After a successful connection is established the
* file demonstrates some basic commands sent by the receiver to the drone
* to act on.
*
* Requirements (Additional details can be found in the file below):
* - SERVICE - The demonstration generates and represents some of the core
* functionality of the protocol required by the protocol implementation.
*
*=================================================================================
* */

package Receiver;

import java.io.*;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;

import messages.AckMessage;
import messages.ControlMessage;
import messages.ErrorMessage;
import messages.Message;
import messages.ControlMessage.ControlType;
import messages.ErrorMessage.ErrorType;
import messages.Message.MessageType;

public class SystemDemo {

   public static void main(String[] args) throws Exception {

	   /**
		* Initiating json object to simulate the creation of a receiver
		* and it's associated parameters before a connection is established
		* with the drone
 		*/
	   byte m3Command = 0x01;
		JSONObject json = new JSONObject();
		json.put("unit_type", "receiver");
		json.put("unit_id", "abcdef");
		json.put("unit_ipaddress", "123.123.123.123");
		json.put("version", 1.0);
		ControlMessage m3bdy = new ControlMessage(ControlType.GROUNDED, m3Command, json);

	   /**
		* Testing of message creation based on message control
		*/
		Message m5 = new Message(MessageType.CONTROL, 5, m3bdy);
		byte [] temp = Message.toByteArray(m5);
		Message m55 = Message.fromByteArray(temp);

	   /**
		* Displaying message as byte array and string
		*/
	   System.out.println(Message.toByteArray(m5));
		System.out.println(Message.fromByteArray(Message.toByteArray(m5)));
		
   }
} 