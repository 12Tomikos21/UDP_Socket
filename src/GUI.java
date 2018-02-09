import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
	public JTextField textField,PORT,IP,FRAGMENT;
	public JLabel port_Label,status,IP_Label,fragment_Label;
	public JTextArea textArea;
	public JButton Send,Client,Server,File;
	private JPanel panel;
	public JScrollPane scroll;
	public JCheckBox wrong_send;
	public JFileChooser chooser;
	public JRadioButton Filebutton,Messagebutton;
	public int pocet = 0;
	
	public GUI() {
		super("Application");
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		Client = new JButton("Client");
		Client.setBounds(380, 51, 87, 25);
		Server = new JButton("Server");
		Server.setBounds(473, 51, 87, 25);
		Send = new JButton("Send");
		Send.setBounds(457, 337, 71, 25);
		File = new JButton("File");
		File.setBounds(380, 337, 71, 25);
		//textArea.setBounds(12, 5, 356, 276);
		textField = new JTextField();
		textField.setBounds(12, 338, 365, 22);
		PORT = new JTextField();
		PORT.setText("9846");
		PORT.setBounds(380, 103, 116, 22);
		IP = new JTextField();
		IP.setText("localhost");
		IP.setBounds(380, 167, 116, 22);
		FRAGMENT = new JTextField();
		FRAGMENT.setBounds(380, 231, 116, 22);
		port_Label = new JLabel("PORT");
		port_Label.setBounds(380,78,116,16);
		status = new JLabel("STATUS");
		status.setFont(new Font("Tahoma", Font.BOLD, 22));
		status.setBounds(423, 5, 87, 36);
		IP_Label = new JLabel("IP address");
		IP_Label.setBounds(380, 138, 116, 16);
		fragment_Label = new JLabel("Fragment size");
		fragment_Label.setBounds(380, 202, 116, 16);
		wrong_send = new JCheckBox("Send wrong");
		wrong_send.setBounds(380, 259, 113, 25);
		//scroll = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		MyServer server = new MyServer(this);
		MyClient client = new MyClient(this);
		SEND poslat = new SEND(this);
		FileChoose vyber = new FileChoose(this);
				
		Server.addActionListener(server);
		Client.addActionListener(client);
		Send.addActionListener(poslat);
		File.addActionListener(vyber);
		Send.setEnabled(false);
		Send.setSelected(false);
		FRAGMENT.setEnabled(false);
		textField.setEnabled(false);
		File.setEnabled(false);
		wrong_send.setEnabled(false);
		
		panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);
		
		scroll = new JScrollPane(textArea);
		scroll.setBounds(12, 12, 365, 310);
		
		panel.add(Server);
		panel.add(Client);
		panel.add(Send);
		panel.add(File);
		panel.add(port_Label);
		panel.add(IP_Label);
		panel.add(fragment_Label);
		panel.add(status);
		panel.add(wrong_send);
		panel.add(scroll);
		
		panel.add(textField);
		textField.setColumns(10);
		panel.add(PORT);
		PORT.setColumns(10);
		panel.add(IP);
		IP.setColumns(10);
		panel.add(FRAGMENT);
		FRAGMENT.setColumns(10);
		
		Filebutton = new JRadioButton("FILE");
		Filebutton.setBounds(380, 289, 53, 25);
		panel.add(Filebutton);
		Filebutton.setEnabled(false);
		
		Messagebutton = new JRadioButton("MESSAGE");
		Messagebutton.setBounds(454, 289, 87, 25);
		panel.add(Messagebutton);
		Messagebutton.setEnabled(false);
		
		
		setSize(584, 435);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setVisible(true);
    	setResizable(true);	
	}
	public void Error_Number(){
		JFrame frame1 = new JFrame();
		frame1.pack();
		JOptionPane.showMessageDialog(frame1,"Worng value of fragment\n"+ "HINT: 0 - 65485");}
	public void Error_File(){
		JFrame frame1 = new JFrame();
		frame1.pack();
		JOptionPane.showMessageDialog(frame1,"Choose between FILE or MESSAGE");}
}
