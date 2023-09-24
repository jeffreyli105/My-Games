import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final JPanel buttonPane = new JPanel();
	private final JButton okButton = new JButton("Start");
	private final JButton cancelButton = new JButton("Exit");
	private JButton[] buttons = new JButton[9];
	private final JButton btnNewButton = new JButton("Solve!");
	
	public static void main(String[] args) {
		try {
			Main dialog = new Main();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Main() {
		setBounds(600, 600, 600, 600); //100,100,450,300
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(3, 3, 0, 0));
		
		//create and add buttons into dialog
		for(int i = 0; i < 9; i++) {
			buttons[i] = new JButton("O");
			contentPanel.add(buttons[i]);
			buttons[i].setActionCommand(String.valueOf(i));
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String param = e.getActionCommand();
					Model.updateState(param);
					Model.updateView(buttons);
					if(Model.checking()) {
						JOptionPane.showMessageDialog(Main.this, "You win!");
				 }
				}
			});
		}
		

		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Model.init();
				Model.updateView(buttons);
			}
		});
		
		
		buttonPane.add(btnNewButton);
		okButton.setActionCommand("Start");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.this.dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		Model.init();
		Model.updateView(buttons);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Model.solve(buttons);
			}
		});
	}

}
