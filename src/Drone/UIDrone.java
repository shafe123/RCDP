package Drone;
import java.awt.*;
import java.awt.event.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.*;

import javax.swing.*;

import DFA.DroneDFA;
import messages.Message;

public class UIDrone {
	private JFrame mainFrame;
	private JTextArea statusLabel;
	private JPanel controlPanel;
	private StringBuffer log = new StringBuffer();
	private int LogStringCount = 0;
	private JScrollPane sp;
	private static UIDrone swingControlDemo;
	private JPasswordField Jpassword;
	private JTextField JdroneId; 
	public JLabel currentState;
	public BlockingQueue<String> commandQueue = new LinkedBlockingQueue<String>();


	private String portNumber = "8080";
	private String password;
	private String DroneId;
	private Integer messageID = 0;
	
	

	public UIDrone() {
		prepareGUI();
	}

	public static void main(String[] args) {
		swingControlDemo = new UIDrone();
		swingControlDemo.showEventDemo();
	}

	private void prepareGUI() {
		mainFrame = new JFrame("RCDP drone");
		mainFrame.setSize(1000, 600);

		mainFrame.setLayout(new GridLayout(1, 2));

		statusLabel = new JTextArea();
		sp = new JScrollPane(statusLabel);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		controlPanel = new JPanel();
		controlPanel.setLayout(null);

		mainFrame.add(controlPanel);
		mainFrame.getContentPane().add(sp);
		mainFrame.setVisible(true);

	}

	private void showEventDemo() {

		JLabel headerLable = new JLabel("Remote control Drone Protocol: Drone", JLabel.CENTER);
		JLabel errorLable = new JLabel("Error simulate", JLabel.CENTER);
		JButton turnOnButton = new JButton("TurnOn");
		JButton turnOffButton = new JButton("TurnOff");
		
		
		JLabel pw = new JLabel("set password for Drone");
		Jpassword= new JPasswordField(10);
//		Jpassword.setText("secret");
		
		JLabel d_id = new JLabel("set Drone ID");
		
		JdroneId = new JTextField(10); 
		
		JLabel currentStatelabel = new JLabel("Current State: ");
		
		currentState = new JLabel("");
		

		turnOnButton.setActionCommand("TurnOn");
		turnOffButton.setActionCommand("TurnOff");

		JButton E0000Button = new JButton("Can not find drone");
		E0000Button.setActionCommand("E0000");

		JButton E0001Button = new JButton("Connection error");
		E0001Button.setActionCommand("E0001");

		JButton E0010Button = new JButton("Authentication error");
		E0010Button.setActionCommand("E0010");

		JButton E0101Button = new JButton("Drone low battery");
		E0101Button.setActionCommand("E0101");

		JButton E0111Button = new JButton("Weak signal");
		E0111Button.setActionCommand("E0111");

		JButton E1000Button = new JButton("Lost signal");
		E1000Button.setActionCommand("E1000");

		turnOnButton.addActionListener(new ButtonClickListener());
		turnOffButton.addActionListener(new ButtonClickListener());
		E0000Button.addActionListener(new ButtonClickListener());
		E0001Button.addActionListener(new ButtonClickListener());
		E0010Button.addActionListener(new ButtonClickListener());
		E0101Button.addActionListener(new ButtonClickListener());
		E0111Button.addActionListener(new ButtonClickListener());
		E1000Button.addActionListener(new ButtonClickListener());

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbs = new GridBagConstraints();
		controlPanel.setLayout(gbl);

		controlPanel.add(headerLable);
		controlPanel.add(turnOnButton);
		controlPanel.add(turnOffButton);
		controlPanel.add(pw);
		controlPanel.add(Jpassword);
		controlPanel.add(d_id);
		controlPanel.add(JdroneId);
		controlPanel.add(currentState);
		controlPanel.add(currentStatelabel);
		
//		controlPanel.add(errorLable);
////		controlPanel.add(E0000Button);
//		controlPanel.add(E0001Button);
//		controlPanel.add(E0010Button);
//		controlPanel.add(E0101Button);
//		controlPanel.add(E0111Button);
//		controlPanel.add(E1000Button);

		gbs.fill = GridBagConstraints.HORIZONTAL;
		gbs.gridwidth = 2;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 0;
		gbs.gridy = 0;
		gbl.setConstraints(headerLable, gbs);
		
		gbs.fill = GridBagConstraints.HORIZONTAL;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 0;
		gbs.gridy = 1;
		gbl.setConstraints(pw, gbs);
		
		gbs.fill = GridBagConstraints.HORIZONTAL;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 1;
		gbs.gridy = 1;
		gbl.setConstraints(Jpassword, gbs);
		
		gbs.fill = GridBagConstraints.HORIZONTAL;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 0;
		gbs.gridy = 2;
		gbl.setConstraints(d_id, gbs);
		
		gbs.fill = GridBagConstraints.HORIZONTAL;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 1;
		gbs.gridy = 2;
		gbl.setConstraints(JdroneId, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 0;
		gbs.gridy = 3;
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
		gbs.gridy = 3;
		gbs.ipadx = 30;
		gbs.ipady = 20;
		gbl.setConstraints(turnOffButton, gbs);
		
		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 0;
		gbs.gridy = 4;
		gbs.ipadx = 30;
		gbs.ipady = 20;
		gbl.setConstraints(currentStatelabel, gbs);

		gbs.fill = GridBagConstraints.NONE;
		gbs.gridwidth = 1;
		gbs.gridheight = 1;
		gbs.insets = new Insets(1, 0, 1, 0);
		gbs.weightx = 1;
		gbs.weighty = 1;
		gbs.gridx = 1;
		gbs.gridy = 4;
		gbs.ipadx = 30;
		gbs.ipady = 20;
		gbl.setConstraints(currentState, gbs);
		
//		gbs.fill = GridBagConstraints.NONE;
//		gbs.gridwidth = 2;
//		gbs.gridheight = 1;
//		gbs.insets = new Insets(1, 0, 1, 0);
//		gbs.weightx = 1;
//		gbs.weighty = 1;
//		gbs.gridx = 0;
//		gbs.gridy = 5;
//		gbs.ipadx = 200;
//		gbs.ipady = 20;
//		gbl.setConstraints(errorLable, gbs);
//
//
//
//		gbs.fill = GridBagConstraints.NONE;
//		gbs.gridwidth = 2;
//		gbs.gridheight = 1;
//		gbs.insets = new Insets(1, 0, 1, 0);
//		gbs.weightx = 1;
//		gbs.weighty = 1;
//		gbs.gridx = 0;
//		gbs.gridy = 6;
//		gbs.ipadx = 400;
//		gbs.ipady = 20;
//		gbl.setConstraints(E0001Button, gbs);
//
//		gbs.fill = GridBagConstraints.NONE;
//		gbs.gridwidth = 2;
//		gbs.gridheight = 1;
//		gbs.insets = new Insets(1, 0, 1, 0);
//		gbs.weightx = 1;
//		gbs.weighty = 1;
//		gbs.gridx = 0;
//		gbs.gridy = 7;
//		gbs.ipadx = 400;
//		gbs.ipady = 20;
//		gbl.setConstraints(E0010Button, gbs);
//
//		gbs.fill = GridBagConstraints.NONE;
//		gbs.gridwidth = 2;
//		gbs.gridheight = 1;
//		gbs.insets = new Insets(1, 0, 1, 0);
//		gbs.weightx = 1;
//		gbs.weighty = 1;
//		gbs.gridx = 0;
//		gbs.gridy = 8;
//		gbs.ipadx = 400;
//		gbs.ipady = 20;
//		gbl.setConstraints(E0101Button, gbs);
//
//		gbs.fill = GridBagConstraints.NONE;
//		gbs.gridwidth = 2;
//		gbs.gridheight = 1;
//		gbs.insets = new Insets(1, 0, 1, 0);
//		gbs.weightx = 1;
//		gbs.weighty = 1;
//		gbs.gridx = 0;
//		gbs.gridy = 9;
//		gbs.ipadx = 400;
//		gbs.ipady = 20;
//		gbl.setConstraints(E0111Button, gbs);
//
//		gbs.fill = GridBagConstraints.NONE;
//		gbs.gridwidth = 2;
//		gbs.gridheight = 1;
//		gbs.insets = new Insets(1, 0, 1, 0);
//		gbs.weightx = 1;
//		gbs.weighty = 1;
//		gbs.gridx = 0;
//		gbs.gridy = 10;
//		gbs.ipadx = 400;
//		gbs.ipady = 20;
//		gbl.setConstraints(E1000Button, gbs);

		mainFrame.setVisible(true);
	}
	
	
	// Anything above this line are for UI
	
	// display on UI
	public void display(String s) {
		LogStringCount++;
		log.append(LogStringCount + ":" + s + "\n");
		statusLabel.setText(log.toString());
		statusLabel.setCaretPosition(statusLabel.getDocument().getLength()); 
	}

	/**
	 * 
	 * click turn on to turn on the server, turn off to turn off server
	 *
	 */

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			String TurnOn = "TurnOn, port number: " + portNumber;
			String TurnOff = "TurnOff";
//			String E0000 = "Simulate Error 0000: Can not find drone";
//			String E0001 = "Simulate Error 0001: Connection error";
//			String E0010 = "Simulate Error 0010: Authentication error";
//			String E0101 = "Simulate Error 0101: Drone low battery";
//			String E0111 = "Simulate Error 0111: Weak signal";
//			String E1000 = "Simulate Error 1000: Lost signal";

			if (command.equals("TurnOn")) {
				display(TurnOn);
				char[] cs = Jpassword.getPassword();
				password = new String(cs);
				DroneId = JdroneId.getText();
				display("DroneID: "+DroneId+ " password:" + password);
				currentState.setText("" + "GROUNDED");
				Thread t = new Thread( new DroneServer(portNumber,password,DroneId,swingControlDemo));
				t.start();

			} else if (command.equals("TurnOff")) {
				display(TurnOff);
//				commandQueue.offer("TurnOff");
//				try {
//					TimeUnit.SECONDS.sleep(1);
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				System.exit(0);
			} 
//			else if (command.equals("E0000")) {
//				display(E0000);
//			} else if (command.equals("E0001")) {
//				display(E0001);
//			} else if (command.equals("E0010")) {
//				display(E0010);
//			} else if (command.equals("E0101")) {
//				display(E0101);
//			} else if (command.equals("E0111")) {
//				display(E0111);
//			} else if (command.equals("E1000")) {
//				display(E1000);
//			}
		}
	}


}