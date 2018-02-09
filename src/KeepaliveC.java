import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
 
public class KeepaliveC extends Thread {
 
	public GUI window = null;
	KeepaliveC(GUI w){
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
    Timer timer = new Timer(); 
    TimerTask task = new TimerTask() {     
       
        public void run() {
            DatagramSocket clientSocket;
            try {
            clientSocket = new DatagramSocket();
           
        	InetAddress ip = InetAddress.getByName(window.IP.getText());                                        
 
            byte[] receiveData = new byte[22];                                                        
            Hlavicka header = new Hlavicka(0, 22, 22, 'K',0);
            byte[] sendData = header.vlkadanie(header);                     
             
              DatagramPacket sendPacket = new DatagramPacket(sendData, 22, ip, Number(window.PORT.getText()));
              clientSocket.send(sendPacket);                 
              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
              clientSocket.receive(receivePacket);
              header.vyberanie(receiveData);
              if(header.getType()== 'L')
            	  window.textArea.append("Keepalive");
              clientSocket.close();
           
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
        }
    };
   
    public void start(KeepaliveC client){
        timer.scheduleAtFixedRate(task, 1000, 10000);
       
    }
}
