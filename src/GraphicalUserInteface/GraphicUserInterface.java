package GraphicalUserInteface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GraphicUserInterface {

	private JFrame frame;
	private JPanel panel;
	private JLabel statusLabel;
	private JLabel resultLabel;
	private JButton getNetworksButton;
	private JButton crackButton;
	private JButton exitButton;
	private JComboBox networksComboBox;
	private Graphics g;


	public GraphicUserInterface() {
		frame = new JFrame("Wi-Fi cracker");
		panel = new JPanel();
		statusLabel = new JLabel("Not Working");
		resultLabel = new JLabel();
		getNetworksButton = new JButton("Generate Networks List");
		crackButton = new JButton("Crack Network");
		exitButton = new JButton("Quit");
		networksComboBox = new JComboBox();

	}

	public void paintRectangle(Graphics graphics) {
		while (true) {
			graphics.setColor( statusLabel.getForeground());
			graphics.fillRect(panel.getX() + 80, panel.getY() + 100, panel.getWidth() - 100, panel.getHeight() - 120);
		}
	}

	public void start() {

		frame.setVisible(true);
		frame.setSize(820, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setSize(800, 300);
		panel.add(getNetworksButton);
		panel.add(networksComboBox);
		panel.add(crackButton);
		panel.add(statusLabel);
		panel.add(resultLabel);
		panel.add(exitButton);
		networksComboBox.setToolTipText("lqlqlq");
		networksComboBox.setEnabled(true);
		Color red = new Color(255, 0, 0);
		Color green = new Color(0, 255, 0);
		statusLabel.setForeground(red);
		frame.setContentPane(panel);
		g = panel.getGraphics();
		getNetworksButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				networksComboBox.addItem("mqh");
				networksComboBox.addItem("vah");
				networksComboBox.addItem("ltah");
			}

		});

		crackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusLabel.setForeground(green);
				statusLabel.setText("Done");
				resultLabel.setText("Network Password:hahaha");

			}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		paintRectangle(g);
		

	}

	public static void main(String[] args) {
		GraphicUserInterface Interface = new GraphicUserInterface();
		Interface.start();
	}
}
