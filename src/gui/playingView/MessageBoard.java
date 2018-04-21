package gui.playingView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

public class MessageBoard extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4304403924411783670L;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	public static final Dimension minSize = new Dimension(350, 160);
	
	public MessageBoard() {
		initializeComponents();
	}
	
	private void initializeComponents() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		scrollPane = new JScrollPane(textArea);
		
		setOpaque(false);
		setMinimumSize(minSize);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	private Font font;
	public void publisch(int textSize, String text) {
		//Sets up font for text area with the given size
		font = textArea.getFont();
		textArea.setFont(new Font(font.getFontName(), font.getStyle(), textSize));
		
		//Appends text to text area. Does nothing if text is null
		textArea.append(text + "\n");
		scrollToBottom();
	}
	public void scrollToBottom() {
		//Sets viewport position to the bottom of text area
		JViewport vp = scrollPane.getViewport();
		vp.setViewPosition(new Point(0, vp.getViewSize().height)); 
	}
}
