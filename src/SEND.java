
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.zip.CRC32;
import javax.swing.JButton;


public class SEND implements ActionListener {

	public GUI window = null;
	SEND(GUI w){
		window = w;
	}
	public boolean isInt(String numbers) { 
		if(numbers != null && !numbers.isEmpty())
			return numbers.chars().allMatch(c -> (c == '.' || Character.isDigit(c)));
		else return false;}
	public int Number(String numbers) throws Exception{ 
		if ((isInt(numbers)))
			return Integer.parseInt(numbers);
		else{
			window.Error_Number();
			return 0;}}
	
	public void SEND(DatagramSocket clientSoket,InetAddress ip,int port,int sizeof_fragment,byte [] Data,char data_type) throws Exception{
		
		int number = 0,pos = 0,size = 0;
		boolean wrong = true;
		byte [] sendData_h = new byte[sizeof_fragment + 22];
		byte [] receiveData = new byte[22];
		int pocet;
		
		if(Data.length <= sizeof_fragment){
			size = Data.length;
			pocet = 1;}
		else{
			size = sizeof_fragment;
			pocet = Data.length / sizeof_fragment + 1;}
		
		while(number<pocet){
			number++;
			
			byte [] sendData = new byte[size];
			if(Data.length<sizeof_fragment)
				System.arraycopy(Data, pos, sendData, 0,Data.length);
			else
				if (pocet == number && pocet != 1)
				System.arraycopy(Data, pos, sendData, 0,Data.length%sizeof_fragment);
				else
					System.arraycopy(Data, pos, sendData, 0,sizeof_fragment);	
			pos += sizeof_fragment;
			
			CRC32 crc = new CRC32();
			crc.update(sendData,0,size);
			
			Hlavicka header = new Hlavicka(number,size,Data.length,data_type,crc.getValue());
				
			if(window.wrong_send.isSelected()&& wrong){
				sendData[0]++;
				wrong = false;}
			
				
			byte [] header_value = header.vlkadanie(header);
			System.arraycopy(header_value, 0, sendData_h, 0, header_value.length);
			System.arraycopy(sendData, 0, sendData_h, header_value.length, sendData.length);
		    
				
			DatagramPacket datagrampaket = new DatagramPacket(sendData_h,sendData_h.length,ip,port);
			clientSoket.send(datagrampaket);
			window.textArea.append("Sended ");
			
			
		DatagramPacket recivePaket = new DatagramPacket(receiveData,receiveData.length);
		clientSoket.receive(recivePaket);
		header.vyberanie(receiveData);
		if(header.getType() == 'O')
			window.textArea.append("OK\n");
		
		else{
			pos -= sizeof_fragment;
			if(Data.length<sizeof_fragment)
				System.arraycopy(Data, pos, sendData, 0,Data.length);
			else
				if (pocet == number && pocet != 1)
				System.arraycopy(Data, pos, sendData, 0,Data.length%sizeof_fragment);
				else
					System.arraycopy(Data, pos, sendData, 0,sizeof_fragment);
			pos += sizeof_fragment;
			
			CRC32 Crc = new CRC32();
			crc.update(sendData,0,size);
			
			header.insert(header, number, size, Data.length, data_type, Crc.getValue());
			header_value = header.vlkadanie(header);
			System.arraycopy(header_value, 0, sendData_h, 0, header_value.length);
			System.arraycopy(sendData, 0, sendData_h, header_value.length, sendData.length);
			
			datagrampaket = new DatagramPacket(sendData_h,sendData_h.length,ip,port);
			clientSoket.send(datagrampaket);
			window.textArea.append("Poslal opravený\n");
			}
		}
		
	}
	public boolean COMM(DatagramSocket clientSoket,InetAddress ip,int port)throws Exception{
		
		char data_type = 'C';									//Zaèatie komunikácie
		long CRC = 0;
		Hlavicka header = new Hlavicka(0,22,22,data_type,CRC);
		byte [] header_value = header.vlkadanie(header); 
			
		DatagramPacket datagramPaket = new DatagramPacket(header_value,header_value.length,ip,port);
			
		clientSoket.send(datagramPaket);
		
		byte [] confirm = new byte[header.velkost_h()];
		DatagramPacket receivePaket = new DatagramPacket(confirm,confirm.length);
		
		clientSoket.receive(receivePaket);
			
		header.vyberanie(confirm);
		
		if (header.getType() == 'A'){
			window.textArea.append("Establish communication\n");
			return true;}
		else
			return false;
			
	}

	private void startThread() {
	    new Thread() {
	        public void run() {
	        	try {
					boolean a;
					byte [] Data = null;
					char data_type = 'X';
					FileInputStream fis = null;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					File f = new File(window.textField.getText());
					Data = new byte[(int) f.length()];
					int port = Number(window.PORT.getText());
					int sizeof_fragment = Number(window.FRAGMENT.getText());
					if (sizeof_fragment>65507-22 || sizeof_fragment< 0){
						window.Error_Number();
						return;}
					if (window.Messagebutton.isSelected()&&window.Filebutton.isSelected()){
						window.Error_File();
						return;}
					InetAddress ip = InetAddress.getByName(window.IP.getText());
					DatagramSocket clientSoket = new DatagramSocket();
					if(window.Messagebutton.isSelected()){
						data_type = 'I';
						Data = window.textField.getText().getBytes();
					}
					else{
						
					switch (window.textField.getText().substring(window.textField.getText().length()-3, window.textField.getText().length() )){
					
					case "txt":{	data_type = 'T';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "pdf":{	data_type = 'P';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "jpg":{	data_type = 'J';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "mp3":{	data_type = '3';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "zip":{	data_type = 'Z';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "mp4":{	data_type = '4';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "gif":{	data_type = 'g';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "avi":{	data_type = 'V';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "png":{	data_type = 'p';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					case "exe":{	data_type = 'E';
						fis = new FileInputStream(window.textField.getText());
						fis.read(Data);baos.write(Data);break;}
					}
					}
					a = COMM(clientSoket,ip,port);
					if (a){
						
						SEND(clientSoket,ip,port,sizeof_fragment,Data,data_type);
					}
					if(window.Filebutton.isSelected())
						fis.close();
					KeepaliveC live = new KeepaliveC(window);
					live.start(live);
					window.textArea.append("Sending ended\n");
					clientSoket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    }.start();

	}
	public void actionPerformed(ActionEvent event) {
		
		JButton src = (JButton) event.getSource();
		
		if (src.equals(window.Send)){
			 startThread();
			
		}
	}
}
