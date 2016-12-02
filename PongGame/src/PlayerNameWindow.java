import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * 
 */

/**
 * @author Binh Tran
 *	T151266
 * 
 */
public class PlayerNameWindow extends JDialog {

	/**
	 * @param args
	 */
	JTextField txtPlayer1Name = new JTextField();
	JTextField txtPlayer2Name = new JTextField();
	JLabel lblPlayer1 = new JLabel("Player 1 Name:");
	JLabel lblPlayer2 = new JLabel("Player 2 Name:");
	JButton btnOk = new JButton("OK");
	
	public PlayerNameWindow(){
		setSize(390, 160);
		setTitle("PlayerName");
		setLayout(null);
		setLocationRelativeTo(null);
		setModal(true);
		add(txtPlayer1Name);
		add(txtPlayer2Name);
		add(lblPlayer1);
		add(lblPlayer2);
		add(btnOk);
		
		lblPlayer1.setBounds(10, 10, 100, 30);
		txtPlayer1Name.setBounds(120, 10, 250, 30);
		lblPlayer2.setBounds(10, 50, 100, 30);
		txtPlayer2Name.setBounds(120, 50, 250, 30);
		btnOk.setBounds(150, 90, 100, 30);
		
		txtPlayer1Name.setText(PongPanel.PlayerName1);
		txtPlayer2Name.setText(PongPanel.PlayerName2);
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				PongPanel.PlayerName1 = txtPlayer1Name.getText();
				PongPanel.PlayerName2 = txtPlayer2Name.getText();
				dispose();
			}
		});
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PlayerNameWindow mainWidow = new PlayerNameWindow();
		mainWidow.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainWidow.setVisible(true);
		
		
	}

}
