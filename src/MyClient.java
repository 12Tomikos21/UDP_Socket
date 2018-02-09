import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class MyClient implements ActionListener{
	
	public GUI window = null;
	MyClient(GUI w){
		window = w;
	}
	private boolean isInt(String numbers) { 
		if(numbers != null && !numbers.isEmpty())
			return numbers.chars().allMatch(c -> (c == '.' || Character.isDigit(c)));
		else return false;}
	public int Number(String numbers) throws Exception{ 
		if (!(isInt(numbers))){
				throw new Exception();
			}
		return Integer.parseInt(numbers);}

	public void actionPerformed(ActionEvent event) {
		
		JButton src = (JButton) event.getSource();
		
		if (src.equals(window.Client)){
			window.Send.setEnabled(true);
			window.Send.setSelected(true);
			window.FRAGMENT.setEnabled(true);
			window.status.setText("Client");
			window.textField.setEnabled(true);
			window.File.setEnabled(true);
			window.Filebutton.setEnabled(true);
			window.Messagebutton.setEnabled(true);
			window.Messagebutton.setSelected(true);
			window.wrong_send.setEnabled(true);
			window.FRAGMENT.setText("20");
			window.textField.setText("Ahoj, ako sa máš ???");
		}
	}

}