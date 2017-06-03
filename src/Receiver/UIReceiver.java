package Receiver;

import java.awt.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.ssl.HostnameVerifier;
import javax.swing.*;

import Drone.DroneServer;

import java.net.*;

public class UIReceiver {
	private JFrame mainFrame;
	private JTextArea statusLabel;
	private JPanel controlPanel;
	private StringBuffer log = new StringBuffer();
	private int LogStringCount = 0;
	private JScrollPane sp;
	private JPasswordField passwordField;

	public String PORTNUMBER = "8080";
	public String HOSTNAME = "127.0.0.1";
	public static UIReceiver swingControlDemo;

	private ReceiverClient receiverClient;
	public BlockingQueue<String> commandQueue = new LinkedBlockingQueue<String>();

	public UIReceiver() {
		prepareGUI();
	}

	public static void main(String[] args) {
		swingControlDemo = new UIReceiver();
		swingControlDemo.showEventDemo();
	}

	private void prepareGUI() {
		mainFrame = new JFrame("RCDP receiver");
		mainFrame.setSize(600, 600);

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

	private void showEventDemo() {

		JLabel headerLable = new JLabel("Remote control Drone Protocol: Receiver", JLabel.CENTER);
		JButton turnOnButton = new JButton("TurnOn");
		JButton turnOffButton = new JButton("TurnOff");
		JButton upButton = new JButton("Up");
		JButton downButton = new JButton("Down");
		JButton rollLeftButton = new JButton("RollLeft");
		JButton rollRightButton = new JButton("RollRight");
		JButton leftButton = new JButton("<");
		JButton rightButton = new JButton(">");
		JButton forwardButton = new JButton("^");
		JButton backwardButton = new JButton("v");
		JButton landButton = new JButton("Land");
		JButton autoButton = new JButton("Auto");
		JButton propellerButton = new JButton("Propeller");
		JButton beaconButton = new JButton("Beacon");
		
		JLabel pw = new JLabel("Password");
		passwordField = new JPasswordField(10);

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
		beaconButton.setActionCommand("Beacon");

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
		beaconButton.addActionListener(new ButtonClickListener());

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbs = new GridBagConstraints();
		controlPanel.setLayout(gbl);

		controlPanel.add(pw);
		controlPanel.add(passwordField);
		
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
		controlPanel.add(beaconButton);

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
		gbs.ipadx = 30;
		gbs.ipady = 20;
		gbl.setConstraints(passwordField, gbs);

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
		gbl.setConstraints(forwardButton, gbs);

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

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 4;
		gbs.gridy = 2;
		gbs.ipadx = 50;
		gbs.ipady = 20;
		gbl.setConstraints(beaconButton, gbs);

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
		gbl.setConstraints(upButton, gbs);

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
		gbl.setConstraints(leftButton, gbs);

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
		gbl.setConstraints(rightButton, gbs);

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
		gbl.setConstraints(rollLeftButton, gbs);

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
		gbl.setConstraints(rollRightButton, gbs);

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
		gbl.setConstraints(backwardButton, gbs);

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
		gbl.setConstraints(downButton, gbs);

		mainFrame.setVisible(true);
	}

	// Anything above this line are for UI

	// display on UI
	public void display(String logstring) {
		LogStringCount++;
		log.append(LogStringCount + ":" + logstring + "\n");
		statusLabel.setText(log.toString());
	}
	
	public void inputCommand(){
		
	}

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			String TurnOn = "TurnOn, port number: " + PORTNUMBER;
			String TurnOff = "TurnOff";
			String Up = "Up commend sent";
			String Down = "Down commend sent";
			String Left = "< commend sent";
			String Right = "> commend sent";
			String Forward = "^ commend sent";
			String Backward = "v commend sent";
			String Land = "Land commend sent";
			String Auto = "Auto commend sent";
			String Propeller = "Propeller commend sent";
			String Beacon = "Beacon commend sent";

			switch (command) {
			case "TurnOn":
				display(TurnOn);
				char[] cs = passwordField.getPassword();
				String PASSWORD = new String(cs);
				receiverClient = new ReceiverClient(HOSTNAME, PORTNUMBER, PASSWORD,swingControlDemo);
				 
//				while(commandQueue.poll() != null){};
			
				Thread t = new Thread(receiverClient);
				t.start();
				commandQueue.offer("TurnOn");				
				break;
			case "TurnOff":
				System.exit(0);
			default:
				
				commandQueue.offer(command);	
				break;
			}
		}
	}
}