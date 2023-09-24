import java.util.ArrayList;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Model {
	private static boolean[] buttonStates = new boolean[9];

	public static void updateState(String param) {
		System.out.println("=====> " + param);
		int btnPressed = Integer.parseInt(param);

		switch (btnPressed) {
		case 0:
			buttonStates[0] = !buttonStates[0];
			buttonStates[1] = !buttonStates[1];
			buttonStates[3] = !buttonStates[3];
			buttonStates[4] = !buttonStates[4];
			break;
		case 2:
			buttonStates[2] = !buttonStates[2];
			buttonStates[1] = !buttonStates[1];
			buttonStates[5] = !buttonStates[5];
			buttonStates[4] = !buttonStates[4];
			break;
		case 6:
			buttonStates[6] = !buttonStates[6];
			buttonStates[7] = !buttonStates[7];
			buttonStates[3] = !buttonStates[3];
			buttonStates[4] = !buttonStates[4];
			break;
		case 8:
			buttonStates[8] = !buttonStates[8];
			buttonStates[7] = !buttonStates[7];
			buttonStates[4] = !buttonStates[4];
			buttonStates[5] = !buttonStates[5];
			break;
		case 1:
			buttonStates[0] = !buttonStates[0];
			buttonStates[1] = !buttonStates[1];
			buttonStates[2] = !buttonStates[2];
			break;
		case 3:
			buttonStates[0] = !buttonStates[0];
			buttonStates[3] = !buttonStates[3];
			buttonStates[6] = !buttonStates[6];
			break;
		case 5:
			buttonStates[2] = !buttonStates[2];
			buttonStates[5] = !buttonStates[5];
			buttonStates[8] = !buttonStates[8];
			break;
		case 7:
			buttonStates[6] = !buttonStates[6];
			buttonStates[7] = !buttonStates[7];
			buttonStates[8] = !buttonStates[8];
			break;
		case 4:
			buttonStates[4] = !buttonStates[4];
			buttonStates[1] = !buttonStates[1];
			buttonStates[3] = !buttonStates[3];
			buttonStates[5] = !buttonStates[5];
			buttonStates[7] = !buttonStates[7];
			break;
		default:
			break;
		}
	}

	public static void updateView(JButton[] buttons) {
		Icon up = new ImageIcon("Naruto.jpg");
		Icon down = new ImageIcon("Sasuke.jpg");
		for (int i = 0; i < 9; i++) {
			if (buttonStates[i] == true) {
//				buttons[i].setText("O");
				buttons[i].setIcon(up);
			} else {
//				buttons[i].setText("X");
				buttons[i].setIcon(down);
			}
		}

	}

	public static void init() {
		Random rand = new Random();

		for (int i = 0; i < 9; i++) {
			int int_random = rand.nextInt();
			buttonStates[i] = (int_random % 2 == 1);
		}

	}

	public static boolean checking() {
		for (int i = 0; i < 9; i++) {
			if (buttonStates[i] == false) {
				return false;
			}
		}
		return true;
	}

	public static void solve(JButton[] buttons) {
	/*	//=== Kenneth's method ===
		while(true) {
			Model.output();
			if(Model.checking()) {
				break;
			}
			
		//Beginning corner
			if(buttonStates[0] == false) {
				Model.updateState("0");
				continue;
			}
		//UpperRight and Lower Left
			if(buttonStates[2] == false) {
				Model.updateState("2");
				continue;
			}
			if(buttonStates[6] == false) {
				Model.updateState("6");
				continue;
			}
		//Bottom Right
			if(buttonStates[8] == false) {
				Model.updateState("8");
				continue;
			}
		//4 shoulders
			if(buttonStates[1] == false) {
				Model.updateState("6");
				Model.updateState("7");
				Model.updateState("8");
				Model.updateState("4");
				continue;
			}
			if(buttonStates[3] == false) {
				Model.updateState("2");
				Model.updateState("5");
				Model.updateState("8");
				Model.updateState("4");
				continue;
			}
			if(buttonStates[5] == false) {
				Model.updateState("0");
				Model.updateState("3");
				Model.updateState("6");
				Model.updateState("4");
				continue;
			}
			if(buttonStates[7] == false) {
				Model.updateState("0");
				Model.updateState("1");
				Model.updateState("2");
				Model.updateState("4");
				continue;
			}
		//Only center left maybe
			Model.updateState("1");
			Model.updateState("3");
			Model.updateState("5");
			Model.updateState("7");
			Model.updateState("4");
		}
	*/	
		//=== My method ===
		System.out.println("== New Game ==");
		ArrayList<Integer> falseButtons = new ArrayList<Integer>();
		Integer btnPressed = 0;
		while (true) {
			if (Model.checking()) {
				System.out.println("Solved!");
				JOptionPane.showMessageDialog(null, "You win!");
				return;
			}
			for (int i = 0; i < buttonStates.length; i++) {
				if (buttonStates[i] == false) {
					btnPressed = i;
					falseButtons.add(i);
				}
			}
			System.out.println("===============");
			System.out.println(falseButtons.size() + " false buttons");
			System.out.println("===============");
			for (int index = 0; index < falseButtons.size(); index++) {
				btnPressed = falseButtons.get(index);
				System.out.println("Button " + btnPressed + " pressed");
				updateState(btnPressed.toString());	
				Model.updateView(buttons);
			}
			falseButtons.clear();
		}
		
	}

	private static void output() {
		for(int i = 0; i < 9; i++) {
			if(buttonStates[i]) {
				System.out.print("O ");
			}else {
				System.out.print("X ");
			}
			if((i + 1) % 3 == 0) {
				System.out.println();
			}
		}
		System.out.println();
		
	}

}
