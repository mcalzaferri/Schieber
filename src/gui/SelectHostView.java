package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import javax.swing.*;

import client.ViewEnumeration;
import client.ViewObserver;
import gui.PictureFactory.Pictures;

public class SelectHostView extends ObservableView implements Viewable{
	
	//Components
	JPanel serverPanel;
	private JPanel innerPanel;
	private JPanel selectHostViewPanel;
	
	private JTextField serverIPText; 
	private JTextField serverPortText;
	private JTextField usernameText;
	private JButton startButton;
	private JLabel serverHeadingLabel;
	private JLabel serverIPLabel;
	private JLabel serverPortLabel;
	private JLabel usernameLabel;
	private JLabel userHeadingLabel;
	
	//Window size and coordinates 
    private static final int width = 400;
    private static final int height = 450;
    private static final int top = 
    Toolkit.getDefaultToolkit().getScreenSize().height/2-height/2;
    private static final int left = 
    Toolkit.getDefaultToolkit().getScreenSize().width/2-width/2;
    private int scale;
    private int imgwidth;
    
    //local variables
    private BufferedImage imgBackground;


	public SelectHostView(ArrayList<ViewObserver> observers) {
		super(null, observers);
		
		//layoutSelectHostView();
		layoutSelectHostPanel();
		
		this.imgBackground = null;
		try {
			this.imgBackground = Gui.pictureFactory.getPicture(Pictures.SelectHostViewBackground);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					InetSocketAddress serverAddress = new InetSocketAddress(serverIPText.getText(), Integer.parseInt(serverPortText.getText())); //TODO change port 
					observer.btnConnectClick(serverAddress,usernameText.getText());
				}
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {	
		//Draw content
		double scale;
		scale = this.getSize().getWidth()/imgBackground.getWidth();
		g.drawImage(Gui.pictureFactory.getScaledPicture(imgBackground, new Dimension((int)this.getSize().getWidth(),(int)(imgBackground.getHeight()*scale))), 0, 0, null);
	}
	
	private void layoutSelectHostPanel() {
		this.setPreferredSize(new Dimension(width,height));
		this.setLocation(left,top);
		
		selectHostViewPanel = new JPanel();
		selectHostViewPanel.setOpaque(false);
		layoutInnerPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWeights = new double[] {0.4,0.2,0.4};
		gbl.rowWeights = new double[] {0.95,0.05};

		selectHostViewPanel.setLayout(gbl);
		
		GridBagConstraints c = new GridBagConstraints();
		//c.insets = new Insets(0,0,10,0);
		
		c.fill = GridBagConstraints.BOTH;
	    c.gridx = 0; 
	    c.gridy = 0; 
	    c.gridwidth = 3;
	    selectHostViewPanel.add(Box.createGlue(),c);
	    
	    //c.fill = GridBagConstraints.SOUTH;
	    c.gridx = 1; 
	    c.gridy = 1; 
	    c.gridwidth = 1;
	    selectHostViewPanel.add(innerPanel,c);
	    
	    this.setLayout(new BorderLayout());
	    this.add(selectHostViewPanel, BorderLayout.CENTER);
	}
	
	private void layoutInnerPanel()
	{
		innerPanel = new JPanel();
		innerPanel.setOpaque(false);
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWeights = new double[] {0.5,0.5};
		gbl.rowWeights = new double[] {1.0/7,1.0/7,1.0/7,1.0/7,1.0/7,1.0/7,1.0/7};
		innerPanel.setLayout(gbl);
		
		//SERVER-HEADER
		serverHeadingLabel = new JLabel("Bitte Server-Socket angeben");
		serverHeadingLabel.setFont(new Font(serverHeadingLabel.getFont().getFontName(), Font.BOLD, 18));
		serverHeadingLabel.setForeground(Color.BLACK);
		serverHeadingLabel.setHorizontalAlignment(JLabel.CENTER);
		
		
		//Server-IP 
		serverIPLabel = new JLabel("IP-Adresse:");
		serverIPLabel.setFont(new Font(serverIPLabel.getFont().getFontName(), Font.PLAIN, 14));
		serverIPLabel.setForeground(Color.BLACK);
		serverIPText = new JTextField("localhost");
		
		//Server-Port
		serverPortLabel = new JLabel("Port:");
		serverPortLabel.setFont(new Font(serverPortLabel.getFont().getFontName(), Font.PLAIN, 14));
		serverPortLabel.setForeground(Color.BLACK);
		serverPortText = new JTextField("65000");
		
		
		//USERNAME-HEADER
		userHeadingLabel = new JLabel("Bitte Username angeben");
		userHeadingLabel.setFont(new Font(serverHeadingLabel.getFont().getFontName(), Font.BOLD, 18));
		userHeadingLabel.setForeground(Color.BLACK);
		userHeadingLabel.setHorizontalAlignment(JLabel.CENTER);
		
		
		//Username
		usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font(serverPortLabel.getFont().getFontName(), Font.PLAIN, 14));
		usernameLabel.setForeground(Color.BLACK);
		usernameText = new JTextField("test");
		
		//Start-Button
		startButton = new JButton("starte Schieber");
		startButton.setFont(new Font(startButton.getFont().getFontName(), Font.PLAIN, 14));
		
		//inner-Panel add components
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill=GridBagConstraints.BOTH;
		c.insets = new Insets(0,0,10,0);
		
	    c.gridx = 0; 
	    c.gridy = 0; 
	    c.gridwidth = 2;
	    innerPanel.add(serverHeadingLabel,c);
	    
	    c.gridx = 0; 
	    c.gridy = 1; 
	    c.gridwidth = 1;
	    innerPanel.add(serverIPLabel,c);
	    
	    c.gridx = 1; 
	    c.gridy = 1; 
	    innerPanel.add(serverIPText,c);
	    
	    c.gridx = 0; 
	    c.gridy = 2; 
	    innerPanel.add(serverPortLabel,c);
	    
	    c.gridx = 1; 
	    c.gridy = 2; 
	    innerPanel.add(serverPortText,c);
	    
	    c.gridx = 0; 
	    c.gridy = 3; 
	    c.gridwidth = 2;
	    innerPanel.add(userHeadingLabel,c);
	    
	    c.gridx = 0; 
	    c.gridy = 4; 
	    innerPanel.add(usernameLabel,c);
	    
	    c.gridx = 1; 
	    c.gridy = 4; 
	    innerPanel.add(usernameText,c);
	    
	    c.fill=GridBagConstraints.BOTH;
	    c.gridx = 0;
	    c.gridy = 5;
	    c.gridwidth = 2;
	    innerPanel.add(Box.createGlue(),c);
	    
	    c.gridx = 0; 
	    c.gridy = 6; 
	    innerPanel.add(startButton,c);
		
		/*innerPanel.add(serverHeadingLabel);
		innerPanel.add(serverIPPanel);
		innerPanel.add(serverPortPanel);
		innerPanel.add(userHeadingLabel);
		innerPanel.add(usernamePanel);
		innerPanel.add(startButton);*/
		
	}

	private void layoutSelectHostView() {
		
		//dimensions and location of window

		this.setPreferredSize(new Dimension(width,height));
		setLocation(left,top);
		
		serverHeadingLabel = new JLabel("Server");
		serverHeadingLabel.setFont(new Font(serverHeadingLabel.getFont().getFontName(), Font.BOLD, 24));
		serverHeadingLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//Server-IP and Server-Socket
		serverIPLabel = new JLabel("IP-Adresse:");
		serverIPLabel.setFont(new Font(serverIPLabel.getFont().getFontName(), Font.PLAIN, 12));
		serverIPText = new JTextField("localhost");
		serverPortLabel = new JLabel("Port:");
		serverPortLabel.setFont(new Font(serverPortLabel.getFont().getFontName(), Font.PLAIN, 12));
		serverPortText = new JTextField("65000");
		usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font(serverPortLabel.getFont().getFontName(), Font.PLAIN, 12));
		usernameText = new JTextField("test");
		
		JPanel serverIPSocketPanel = new JPanel();
		serverIPSocketPanel.setLayout(new GridLayout(3,2,0,5));
		serverIPSocketPanel.add(serverIPLabel);
		serverIPSocketPanel.add(serverIPText);
		serverIPSocketPanel.add(serverPortLabel);
		serverIPSocketPanel.add(serverPortText);
		serverIPSocketPanel.add(usernameLabel);
		serverIPSocketPanel.add(usernameText);
		
		
		//Server IP and Socket
		serverPanel = new JPanel();
		serverPanel.setLayout(new BorderLayout(0,50));
		serverPanel.add(serverHeadingLabel,BorderLayout.NORTH);
		serverPanel.add(serverIPSocketPanel,BorderLayout.CENTER);
		//serverPanel.add(serverSocketPanel);

		
		//Button
		JPanel startButtonPanel = new JPanel();
		startButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		startButton = new JButton("starte Schieber");
		startButton.setFont(new Font(startButton.getFont().getFontName(), Font.PLAIN, 12));
		startButtonPanel.add(startButton);
		
		//add Components 
		this.setLayout(new BorderLayout());
		this.add(serverPanel,BorderLayout.NORTH);
		this.add(startButtonPanel,BorderLayout.SOUTH);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getContent() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		// TODO Auto-generated method stub
		return ViewEnumeration.SELECTHOSTVIEW;
	}
}
