package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Window extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar pBar;
	
	public Window() {
		initComponent();
	}
	
	private void initComponent() {
		pBar = new JProgressBar(0, 10000);
		
		JPanel pnl = new JPanel();
		pnl.add(pBar);
		
		add(pnl);
		setTitle("Download Progress..");
		setSize(500, 80);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void update(int progress) {
		pBar.setValue(progress);
	}
	
}
