import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.zip.CRC32;
import javax.swing.JButton;

public class MyServer implements ActionListener{
	
	public GUI window = null;
	MyServer(GUI w){
		window = w;}
	
	public boolean isInt(String numbers) { 
		if(numbers != null && !numbers.isEmpty())
			return numbers.chars().allMatch(c -> (c == '.' || Character.isDigit(c)));
		else return false;}
	public int Number(String numbers) throws Exception{ 
		if ((isInt(numbers)))
			return Integer.parseInt(numbers);
		else
			window.Error_Number();
			return 0;}
	
	
	public Data RECIVE(DatagramSocket ServerSoket) throws Exception {
		
		Hlavicka header = new Hlavicka(0,0,0,'X',0);			//Prázdna hlavicka
		int pocet = 1 ,x = 0,count = 0, kolko = 0, size = 0;
		int pos = 0;char data_type = 'N';
		byte [] receiveData = new byte[65507];
		byte [] Data = null;
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
		
		while(count<pocet){
			
			ServerSoket.receive(receivePacket);
			header.vyberanie(receiveData);
			kolko++;
			
			count++;
			if(x == 0){
				Data = new byte [header.getAll_size()];
				if(header.getSize() == header.getAll_size())
					pocet = 1;
				else
					pocet = header.getAll_size()/header.getSize() + 1;
				x = 1;}
		
			byte [] Datafragment = new byte[header.getSize()];
			
			byte [] sendData = new byte[header.velkost_h()];
		
			System.arraycopy(receiveData, 22, Datafragment, 0, Datafragment.length);
			CRC32 crc = new CRC32();
			crc.update(receiveData,22,header.getSize());
			
			if (crc.getValue() == header.getCRC()&& header.getNumber() == count){
				window.textArea.append(count+". CRC = OK \n");		
				data_type = header.getType();
				size = header.getSize();		//upravené
				if(header.getAll_size()== header.getSize())
					System.arraycopy(Datafragment, 0, Data, pos, Data.length);
				else
					if (count == pocet && count != 1)
						System.arraycopy(Datafragment, 0, Data, pos, Data.length % header.getSize());	
					else
					System.arraycopy(Datafragment, 0, Data, pos, header.getSize());
				
				pos += Datafragment.length;
				header.insert(header, 0, 22, 22,'O', 0);
				sendData = header.vlkadanie(header);
			}
			else{
				window.textArea.append(count+". CRC = BAD\n");
				header.insert(header, count, 22, 22, 'X', 0);
				sendData = header.vlkadanie(header);
			}
			InetAddress IPAddress = receivePacket.getAddress();
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,IPAddress,receivePacket.getPort());
			ServerSoket.send(sendPacket);
		
			if(header.getType() == 'X'){								//ÈAKÁM NA SPRÁVNY PAKET
				ServerSoket.receive(receivePacket);
				header.vyberanie(receiveData);
				kolko++;
				
				System.arraycopy(receiveData, 22, Datafragment, 0, Datafragment.length);
				if(header.getAll_size()== header.getSize())
					System.arraycopy(Datafragment, 0, Data, pos, Data.length);
				else
					if (count == pocet && count != 1)
						System.arraycopy(Datafragment, 0, Data, pos, Data.length % header.getSize());	
					else
						System.arraycopy(Datafragment, 0, Data, pos, header.getSize());
			
				pos += Datafragment.length;
			}
		}
		window.textArea.append("Size of fragment "+ size +"\n");		//upravené
		window.pocet += kolko;
		window.textArea.append("Prijatých " + window.pocet + " fragmentov\n");
		Data d = new Data(Data,Data.length,data_type);
		return d;
		
	}
	public boolean COMM(DatagramSocket ServerSoket) throws Exception {
		
		Hlavicka header = new Hlavicka(0,0,22,'X',0);
		byte [] reciveData = new byte[header.getAll_size()];
		byte [] sendData = new byte[header.velkost_h()];
		
		
		DatagramPacket recivePacket = new DatagramPacket(reciveData,reciveData.length);
		ServerSoket.receive(recivePacket);
		
		header.vyberanie(reciveData);
		
		if(header.getType() == 'C'){
			window.textArea.append("Request for communication\n");
			header.setType('A');
			
			InetAddress IPAddress = recivePacket.getAddress();
			sendData = header.vlkadanie(header);
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,IPAddress,recivePacket.getPort());
			ServerSoket.send(sendPacket);
			return true;
		}
		else
			return false;
	}
	
	private void startThread() {
	    new Thread() {
	        public void run() {
	        	Data d = null;
				
				boolean a = false;
				byte [] Data = null;
				window.status.setText("Server");
				window.Send.setEnabled(false);
				window.Send.setSelected(false);
				window.FRAGMENT.setEnabled(false);
				window.textField.setEnabled(false);
				window.File.setEnabled(false);
				window.Filebutton.setEnabled(false);
				window.Messagebutton.setEnabled(false);
				window.wrong_send.setEnabled(false);
				try {
					int port = Number(window.PORT.getText());
					
					DatagramSocket ServerSoket = new DatagramSocket(port);
					//while (true){
					a = COMM(ServerSoket);
					if (a)
						d = RECIVE(ServerSoket);
					else
						return;
					window.textArea.append("Data recived\n");			//upravené
					Data = new byte[d.getSize()];
					System.arraycopy(d.getB(), 0, Data, 0, d.getSize());
					switch(d.getT()){
					case 'I':{ 	window.textArea.append(new String(d.getB())+"\n");
								break;
					}
					case 'T':{
						FileOutputStream fos = new FileOutputStream("Send_Data.txt");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case 'P':{
						FileOutputStream fos = new FileOutputStream("Send_Data.pdf");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case 'J':{
						FileOutputStream fos = new FileOutputStream("Send_Data.jpg");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case '3':{
						FileOutputStream fos = new FileOutputStream("Send_Data.mp3");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case '4':{
						FileOutputStream fos = new FileOutputStream("Send_Data.mp4");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case 'G':{
						FileOutputStream fos = new FileOutputStream("Send_Data.gif");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case 'V':{
						FileOutputStream fos = new FileOutputStream("Send_Data.avi");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case 'p':{
						FileOutputStream fos = new FileOutputStream("Send_Data.png");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case 'E':{
						FileOutputStream fos = new FileOutputStream("Send_Data.exe");
						fos.write(Data);fos.flush();fos.close(); break;
					}
					case 'Z':{
						FileOutputStream fos = new FileOutputStream("Send_Data.zip");
						fos.write(Data);fos.flush();fos.close(); break;
					}}
					
					ServerSoket.close();					//upravené
					KeepaliveS live = new KeepaliveS(window);
					live.start(live);
					//}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    }.start();
}
	public void actionPerformed(ActionEvent event){
		
		JButton src = (JButton) event.getSource();

		if (src.equals(window.Server)){
			 startThread();	 
		}
	}
}
