import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
 
public class KeepaliveS extends Thread {
 
	public GUI window = null;
	KeepaliveS(GUI w){
		window = w;
	}
    Timer timer = new Timer(); 
    TimerTask task = new TimerTask() {     
       
        public void run() {
            DatagramSocket clientSocket;
            try {
            Hlavicka hlavicka = new Hlavicka(0, 22, 22, 'X',0);
            clientSocket = new DatagramSocket();
            byte[] receiveData = new byte[22];
            byte[] sendData = new byte[22]; 
            DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);                             
            clientSocket.receive(receivePacket); 
            hlavicka.vyberanie(receiveData);
            if (hlavicka.getType()=='K'){
            	window.textArea.append("Keepalive");
            	InetAddress IPAddress = receivePacket.getAddress();
            	DatagramPacket sendPacket = new DatagramPacket(sendData, 22, IPAddress, receivePacket.getPort());
            	clientSocket.send(sendPacket);
            }
            else
            	window.textArea.append("Error 404\n");
              clientSocket.close();
             
            } catch (Exception e) {
                e.printStackTrace();
            }
           
        }
    };
   
    public void start(KeepaliveS client){
        timer.scheduleAtFixedRate(task, 1000, 10000);
       
    }
}
