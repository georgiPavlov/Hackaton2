package GraphicalUserInteface;

import accessPoint.AccessPoint;
import adapter.Adapter;
import comunication.ComunicationStream;
import comunication.StateManager;
import comunication.StatePakage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import javax.swing.*;

public class GraphicUserInterface extends Thread{

	private JFrame frame;
	private JPanel panel;
	private JLabel statusLabel;
	private JLabel resultLabel;
	private JButton getNetworksButton;
	private JButton crackButton;
	private JButton exitButton;
	private JComboBox networksComboBox;
	private Graphics g;
	private ComunicationStream<StatePakage> communicationLine;
	private Adapter a;
	private List<AccessPoint> points;
	private StatePakage pakage;



	public GraphicUserInterface(ComunicationStream<StatePakage> cLine) {
		frame = new JFrame("Wi-Fi cracker");
		panel = new JPanel();
		statusLabel = new JLabel("");
		resultLabel = new JLabel("");
		getNetworksButton = new JButton("Generate Networks List");
		crackButton = new JButton("Crack Network");
		exitButton = new JButton("Quit");
		networksComboBox = new JComboBox();
		this.communicationLine = cLine;
	}

	public void paintRectangle(Graphics graphics) {
		while (true) {
			graphics.setColor( statusLabel.getForeground());
			graphics.fillRect(panel.getX() + 160, panel.getY() + 200, panel.getWidth() - 200, panel.getHeight() - 220);
		}
	}

	public void run() {
		//statusLabel.setText("Not Working");
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

		statusLabel.setForeground(red);
		frame.setContentPane(panel);
		g = panel.getGraphics();

		getNetworksButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					pakage =new StatePakage(StatePakage.State.INIT,null,null,null,null);
					communicationLine.sendToStateManager(pakage);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					pakage = communicationLine.getFromStateManager();
					if(pakage.getState()!=StatePakage.State.INIT){
						System.out.println("Error");
					}
					else {

						points = pakage.getAps();
						for(int i = 0;i<points.size();i++){
							networksComboBox.addItem(points.get(i));
						}

					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}


			}

		});

		crackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color green = new Color(0, 255, 0);
				int row = networksComboBox.getSelectedIndex();
				try {
					communicationLine.sendToStateManager(new StatePakage(StatePakage.State.CRACK,null,null,pakage.getMonitor(),Arrays.asList(pakage.getAps().get(row))));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while(true){
					try {
						pakage = communicationLine.getFromStateManager();
						switch(pakage.getState()){

							case RUNING:{
								System.out.println("run");
								statusLabel.setForeground(green);

								statusLabel.setText("Runing");
								statusLabel.repaint();
								frame.setContentPane(panel);

								break;
							}
							case BLOKED:{
								System.out.println("block");
								statusLabel.setForeground(red);
								statusLabel.setText("Bloked");
								frame.setContentPane(panel);
								break;
							}
							case FINISHED:{
								System.out.println("done");
								statusLabel.setForeground(green);
								statusLabel.setText("Done");
								resultLabel.setText(pakage.getPin());
								frame.setContentPane(panel);
								break;
							}default:{
								statusLabel.setForeground(new Color (0,0,255));
							}
                        }
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

				}


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
		ComunicationStream<StatePakage> communicationLine = new ComunicationStream<StatePakage>();
		GraphicUserInterface Interface = new GraphicUserInterface(communicationLine);
		Interface.start();
	}
}
