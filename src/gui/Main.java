package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main {

	public static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main(String[] args) {
		int k = 1;
		JGamePanel gamePanel = new JGamePanel();
		JFrame gameFrame = new JFrame();
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setMinimumSize(new Dimension(screen.width/k, screen.height/k));
//		gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gameFrame.setContentPane(gamePanel);
//		gameFrame.setUndecorated(true);
		gameFrame.setVisible(true);
		
		
		gamePanel.start();
	}
	
}
