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
* File name: UIReceiver.java
*
* Description:
* This file contains the receiver interface functionality and displays the
* interaction between the drone. The this behavior
* is simulated as the interface provides various buttons the represent
* the operations of the receiver. The messages will be visible
* in a scrolling text box showing the user the current message execution
* and provide any feed back as necessary.
*
* Requirements (Additional details can be found in the file below):
* - UI
*
*=================================================================================
* */

package Receiver;

import java.awt.*;

import java.awt.event.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import Messages.Message;

import java.net.*;

public class UIReceiver {
	private JFrame mainFrame;
	private JTextArea statusLabel;
	private JPanel controlPanel;
	private StringBuffer log = new StringBuffer();
	private int LogStringCount = 0;
	private JScrollPane sp;
	public JPasswordField passwordField;
	private JTextField hostnameField;
	public String VERSION = "1.0";
	public String RandomNum = getRandomNumber();

	public String PORTNUMBER = "8080";
//	public String HOSTNAME = "127.0.0.1";
	public String HOSTNAME;
	public static UIReceiver swingControlDemo;
	public Socket echoSocket;
	private boolean powerOn = false;
	
	static public Message responseDroneMessage;

	private ReceiverClient receiverClient;
	public ReadACK ReadACK;
	public BlockingQueue<String> commandQueue = new LinkedBlockingQueue<String>();
	public BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();
	public BlockingQueue<Integer> timeoutQueue = new LinkedBlockingQueue<Integer>();

	public UIReceiver() {
		prepareGUI();
	}

	public static void main(String[] args) {
		swingControlDemo = new UIReceiver();
		swingControlDemo.showEventDemo();
	}

	/**
	 * The graphic user interface section that initiates the various
	 * swing components and objects
	 */
	private void prepareGUI() {
		mainFrame = new JFrame("RCDP receiver");
		mainFrame.setSize(800, 600);

		mainFrame.setLayout(new GridLayout(2, 1));

		statusLabel = new JTextArea();
		// statusLabel.setSize(350,100);
		sp = new JScrollPane(statusLabel);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		controlPanel = new JPanel();
		// controlPanel.setLayout(new FlowLayout());
		controlPanel.setLayout(null);

		mainFrame.add(controlPanel);
		mainFrame.getContentPane().add(sp);
		mainFrame.setVisible(true);

	}

	/**
	 * Creations of button components and labels
	 */
	private void showEventDemo() {

		JLabel headerLable = new JLabel("Remote Control Drone Protocol: Receiver", JLabel.CENTER);
		JButton turnOnButton = new JButton("On");
		JButton turnOffButton = new JButton("Off");
		JButton upButton = new JButton("Up");
		JButton downButton = new JButton("Down");
		JButton rollLeftButton = new JButton("Turn Left");
		JButton rollRightButton = new JButton("Turn Right");
		JButton leftButton = new JButton("Move Left");
		JButton rightButton = new JButton("Move Right");
		JButton forwardButton = new JButton("Forward");
		JButton backwardButton = new JButton("Backward");
		JButton landButton = new JButton("Land");
		JButton autoButton = new JButton("Auto");
		JButton propellerButton = new JButton("Propeller");
//		JButton beaconButton = new JButton("Beacon");
		
		JLabel pw = new JLabel("Password");
		passwordField = new JPasswordField(10);
		
		JLabel hostname = new JLabel("Host IP");
		hostnameField = new JTextField("127.0.0.1");

		turnOnButton.setActionCommand("TurnOn");
		turnOffButton.setActionCommand("TurnOff");
		upButton.setActionCommand("Up");
		downButton.setActionCommand("Down");
		rollLeftButton.setActionCommand("RollLeft");
		rollRightButton.setActionCommand("RollRight");
		leftButton.setActionCommand("Left");
		rightButton.setActionCommand("Right");
		forwardButton.setActionCommand("Forward");
		backwardButton.setActionCommand("Backward");
		landButton.setActionCommand("Land");
		autoButton.setActionCommand("Auto");
		propellerButton.setActionCommand("Propeller");
//		beaconButton.setActionCommand("Beacon");

		turnOnButton.addActionListener(new ButtonClickListener());
		turnOffButton.addActionListener(new ButtonClickListener());
		upButton.addActionListener(new ButtonClickListener());
		downButton.addActionListener(new ButtonClickListener());
		rollLeftButton.addActionListener(new ButtonClickListener());
		rollRightButton.addActionListener(new ButtonClickListener());
		leftButton.addActionListener(new ButtonClickListener());
		rightButton.addActionListener(new ButtonClickListener());
		forwardButton.addActionListener(new ButtonClickListener());
		backwardButton.addActionListener(new ButtonClickListener());
		landButton.addActionListener(new ButtonClickListener());
		autoButton.addActionListener(new ButtonClickListener());
		propellerButton.addActionListener(new ButtonClickListener());
//		beaconButton.addActionListener(new ButtonClickListener());

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbs = new GridBagConstraints();
		controlPanel.setLayout(gbl);

		controlPanel.add(pw);
		controlPanel.add(passwordField);
		
		controlPanel.add(hostname);
		controlPanel.add(hostnameField);
		
		controlPanel.add(headerLable);
		controlPanel.add(turnOnButton);
		controlPanel.add(turnOffButton);
		controlPanel.add(upButton);
		controlPanel.add(downButton);
		controlPanel.add(rollLeftButton);
		controlPanel.add(rollRightButton);
		controlPanel.add(leftButton);
		controlPanel.add(rightButton);
		controlPanel.add(forwardButton);
		controlPanel.add(backwardButton);
		controlPanel.add(landButton);
		controlPanel.add(autoButton);
		controlPanel.add(propellerButton);
//		controlPanel.add(beaconButton);

		gbs.fill = GridBagConstraints.HORIZONTAL;
		gbs.gridwidth = 7;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 0;
		gbs.gridy = 0;
		gbl.setConstraints(headerLable, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 0;
		gbs.gridy = 1;
		gbs.ipadx = 30;
		gbs.ipady = 20;
		gbl.setConstraints(turnOnButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 1;
		gbs.gridy = 1;
		gbs.ipadx = 30;
		gbs.ipady = 20;
		gbl.setConstraints(turnOffButton, gbs);
		
		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 3;
		gbs.gridy = 1;
		gbs.ipadx = 30;
		gbs.ipady = 20;
		gbl.setConstraints(pw, gbs);
		
		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 4;
		gbs.gridy = 1;
		gbs.ipadx = 100;
		gbs.ipady = 10;
		gbl.setConstraints(passwordField, gbs);
		
		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 6;
		gbs.gridy = 1;
		gbs.ipadx = 100;
		gbs.ipady = 10;
		gbl.setConstraints(hostname, gbs);
		
		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 7;
		gbs.gridy = 1;
		gbs.ipadx = 100;
		gbs.ipady = 10;
		gbl.setConstraints(hostnameField, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 1;
		gbs.gridy = 2;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(upButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 3;
		gbs.gridy = 2;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(propellerButton, gbs);

//		gbs.fill = GridBagConstraints.NONE;
//		gbs.gridwidth = 1;
//		gbs.gridheight = 1;
//		gbs.insets = new Insets(1, 0, 1, 0);
//		gbs.weightx = 1;
//		gbs.weighty = 1;
//		gbs.gridx = 4;
//		gbs.gridy = 2;
//		gbs.ipadx = 50;
//		gbs.ipady = 20;
//		gbl.setConstraints(beaconButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 6;
		gbs.gridy = 2;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(forwardButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 0;
		gbs.gridy = 3;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(rollLeftButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 2;
		gbs.gridy = 3;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(rollRightButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 3;
		gbs.gridy = 3;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(landButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 4;
		gbs.gridy = 3;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(autoButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 5;
		gbs.gridy = 3;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(leftButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 7;
		gbs.gridy = 3;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(rightButton, gbs);	

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 1;
		gbs.gridy = 4;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(downButton, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 6;
		gbs.gridy = 4;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(backwardButton, gbs);
		
		mainFrame.setVisible(true);
	}
	// end of GUI section

	/**
	 * Displaies the receiver GUI
	 * @param logstring a log the represents the various interactions between the receiver
	 *                  that are displayed to the GUI. Is a type of String
	 */
	public void display(String logstring) {
		LogStringCount++;
		log.append(LogStringCount + ":" + logstring + "\n");
		statusLabel.setText(log.toString());
		statusLabel.setCaretPosition(statusLabel.getDocument().getLength()); 

	}
	
	public void inputCommand(){}

	/**
	 * The button clicker listener operates in a way that, if a button is selected, add
	 * a command to the Queue, and wait for the receiver (i.e. client) to read and
	 * send its Messages
	 */
	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			String TurnOn = "TurnOn, port number: " + PORTNUMBER;

			switch (command) {
			case "TurnOn":
				display(TurnOn);
//				if (powerOn){
//					display("Already turned on");
//				}else{
						while (commandQueue.poll() != null){}
		
					char[] cs = passwordField.getPassword();
					String PASSWORD = new String(cs);
					
					HOSTNAME = hostnameField.getText();
					
					try {
						echoSocket = new Socket(HOSTNAME, Integer.parseInt(PORTNUMBER));
						receiverClient = new ReceiverClient(echoSocket, swingControlDemo,PASSWORD,VERSION,RandomNum);
						ReadACK = new ReadACK(echoSocket, swingControlDemo,PASSWORD,VERSION,RandomNum);
						 				
						Thread t = new Thread(receiverClient);
						t.start();
						Thread t1 = new Thread(ReadACK);
						t1.start();
						display("connected");
						powerOn = true;
						
						commandQueue.offer("TurnOn");
					} catch (NumberFormatException | IOException e1) {
						display(e1.getMessage());
					}
//				}

				break;
			case "TurnOff":
				try {
					TimeUnit.SECONDS.sleep(1);
					echoSocket.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
				break;
			default:
				commandQueue.offer(command);
				break;
			}
		}
	}
	/**
	 * Method used to generate a random number
	 */
	public static String getRandomNumber()
	{
		Random rand = new Random();
		int  n = rand.nextInt(1000) + 1;
		return ""+n;	
	}
}