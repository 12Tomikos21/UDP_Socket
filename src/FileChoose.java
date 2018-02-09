import java.awt.event.*;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class FileChoose implements ActionListener{
	
	public GUI window = null;
	FileChoose(GUI w){
		window = w;}
	
	
	public void Filechoose(){
		
		JFileChooser chooser = chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setDialogTitle("Select folder");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
		chooser.setAcceptAllFileFilterUsed(false);
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			window.textField.setText(chooser.getSelectedFile().getPath());
			window.Filebutton.setSelected(true);
			window.Messagebutton.setSelected(false);
		}
	}
	public void actionPerformed(ActionEvent event) {
	
			JButton src = (JButton) event.getSource();
		
		if (src.equals(window.File)){
			Filechoose();
		}
	}
}
