package shared.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.*;

public class AdvancedConsoleGui {
	private Timer updateTimer;
	
	private JFrame mainFrame;
	
	private JPanel mainPanel;
	
	private Map<ContentCategory, Map<String, JCheckBox>> filters;
	private Map<ContentCategory, JPanel> filterPanels;
	private ActionListener filterChangedListener;
	
	private JPanel consolePanel;
	private JTextArea consoleOutput;
	private JScrollPane scrollConsolePanel;
	private boolean scrollToBottom;
	private LinkedList<ConsoleLine> newLines;
	
	public AdvancedConsoleGui() {
		initialComponents();
	}
	
	private void initialComponents() {
		mainFrame = new JFrame("Advanced Console");
		mainFrame.setVisible(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	hide();
		    }
		});
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setMinimumSize(new Dimension(2000, 1000));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		
		filterPanels = new HashMap<>();
		filters = new HashMap<>();
		BoxLayout boxLayout;
		JScrollPane scrollPane;
		JPanel panel;
		for(ContentCategory category : ContentCategory.values()) {
			panel = new JPanel();
			filterPanels.put(category, panel);
			filters.put(category, new HashMap<>());
			boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setLayout(boxLayout);
			scrollPane = new JScrollPane(panel);
			scrollPane.setBorder(BorderFactory.createTitledBorder(category.toString()));
			mainPanel.add(scrollPane, c);
			c.gridy += 1;
		}
		filterChangedListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				filterChanged();
			}
		};
		
		consolePanel = new JPanel();
		boxLayout = new BoxLayout(consolePanel, BoxLayout.Y_AXIS);
		consolePanel.setLayout(boxLayout);
		
		consoleOutput = new JTextArea();
		consoleOutput.setFont(new Font("Consolas", Font.PLAIN, 16));
		consoleOutput.setEditable(false);
		consolePanel.add(consoleOutput);
		scrollConsolePanel = new JScrollPane(consolePanel);
		scrollConsolePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JScrollBar vertical = scrollConsolePanel.getVerticalScrollBar();
		vertical.setUnitIncrement(7);
		scrollToBottom = true;
		vertical.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				scrollToBottom = false;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		scrollConsolePanel.addMouseWheelListener(new MouseWheelListener() {
			private long lastScrollDown = 0;
			private int sequentScrollDowns = 0;
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.getWheelRotation() <= 0) {
					scrollToBottom = false;
				}else {
					if(lastScrollDown + 1000 > e.getWhen()) {
						sequentScrollDowns += 1;
						if(sequentScrollDowns >= 3) {
							scrollToBottom = true;
						}
					}else {
						sequentScrollDowns = 0;
						lastScrollDown = e.getWhen();
					}
				}
				
			}
		});
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 3;
		c.weightx = 1.0;
		mainPanel.add(scrollConsolePanel, c);
		
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.pack();
		
		newLines = new LinkedList<>();
		updateTimer = new Timer(30, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				appendNewLines();
				if(scrollToBottom) {
					JScrollBar vertical = scrollConsolePanel.getVerticalScrollBar();
					vertical.setValue(vertical.getMaximum());
				}
			}
		});
		updateTimer.start();
	}
	
	private void filterChanged() {
		if(mainFrame.isVisible())
			refreshConsole();
	}
	
	private void refreshConsole() {
		RingList<ConsoleLine> data = AdvancedConsole.getData();
		StringBuilder sb = new StringBuilder();
		for(ConsoleLine cl : data) {
			if(filters.get(cl.category).get(cl.filter).isSelected()) {
				sb.append(cl.category + ": " + cl.content + "\r\n");
			}
		}
		consoleOutput.setText(sb.toString());
	}
	
	public synchronized void appendLine(ConsoleLine cl) {
		if(mainFrame.isVisible())
			newLines.add(cl);
	}
	
	private synchronized void appendNewLines() {
		while(!newLines.isEmpty()) {
			ConsoleLine cl = newLines.removeLast();
			if(filters.get(cl.category).get(cl.filter).isSelected()) {
				consoleOutput.append(cl.category + ": " + cl.content + "\r\n");
			}	
		}
	}

	public void addFilter(ContentCategory category, String filter) {
		JCheckBox checkBox = new JCheckBox(filter);
		filters.get(category).put(filter, checkBox);
		checkBox.setSelected(true);
		filterPanels.get(category).add(checkBox);
		checkBox.addActionListener(filterChangedListener);
		filterChanged();
	}

	public void show() {
		refreshConsole();
		mainFrame.setVisible(true);
	}
	
	public void hide() {
		mainFrame.setVisible(false);
	}
	
}
