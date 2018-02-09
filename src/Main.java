import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	
	public static void Welcome(){
		JFrame frame = new JFrame();
		frame.pack();
		JOptionPane.showMessageDialog(frame,"Choose between Client or Server");}
	public static void main(String[] args){
		
		Welcome();
		new GUI();
	}
}
