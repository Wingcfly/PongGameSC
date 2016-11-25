
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 * 
 */

/**
 * @author Binh Tran - T151266 Editor Nguyen Nguyen - T144249
 * 
 */
public class PaddlesColor extends JDialog {
	JRadioButton optWhite = new JRadioButton("White");
	JRadioButton optRed = new JRadioButton("Red");
	JRadioButton optBlue = new JRadioButton("Blue");
	JLabel lblWhite = new JLabel();
	JLabel lblRed = new JLabel();
	JLabel lblBlue = new JLabel();
	ImageIcon imWhite = new ImageIcon("./PaddlesColor/white.png");
	ImageIcon imRed = new ImageIcon("./PaddlesColor/red.png");
	ImageIcon imBlue = new ImageIcon("./PaddlesColor/blue.png");
	ButtonGroup btnGroup = new ButtonGroup();
	JButton btnChoose = new JButton("Choose");

	public PaddlesColor() {
		setSize(500, 280);
		setTitle("PaddlesColor");
		setLayout(null);
		setLocationRelativeTo(null);
		setModal(true);
		add(optWhite);
		add(optRed);
		add(optBlue);
		add(lblWhite);
		add(lblRed);
		add(lblBlue);
		add(btnChoose);
		lblWhite.setIcon(imWhite);
		lblBlue.setIcon(imRed);
		lblRed.setIcon(imBlue);
		btnGroup.add(optWhite);
		btnGroup.add(optRed);
		btnGroup.add(optBlue);
		lblWhite.setBounds(10, 10, 50, 50);
		optWhite.setBounds(60, 20, 100, 30);
		lblRed.setBounds(10, 70, 50, 50);
		optBlue.setBounds(60, 80, 100, 30);
		lblBlue.setBounds(10, 130, 50, 50);
		optRed.setBounds(60, 140, 100, 30);
		btnChoose.setBounds(350, 190, 100, 30);
		// default option
		optWhite.setSelected(true);
		btnChoose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (optWhite.isSelected() == true) {
					PongPanel.NumPaddlesColor = 0;
				} else if (optBlue.isSelected() == true) {
					PongPanel.NumPaddlesColor = 1;
				} else if (optRed.isSelected() == true) {
					PongPanel.NumPaddlesColor = 2;
				}
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PaddlesColor mainWidow = new PaddlesColor();
		mainWidow.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainWidow.setVisible(true);
	}

}
