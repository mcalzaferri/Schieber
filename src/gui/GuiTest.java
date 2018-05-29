package gui;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import client.AbstractClientView;
import client.ViewEnumeration;
import client.ViewObserver;
import gui.Dialog.MessageType;
import shared.Card;
import shared.Seat;
import shared.Trump;

public class GuiTest extends JFrame implements ViewObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5781101487160760073L;
	private AbstractClientView gui;
	private ClientModelTest data;

	public static void main(String[] args) {
		new GuiTest();
	}
	
	public GuiTest() {
		super();
		data = new ClientModelTest();
		gui = new Gui(data);
		gui.addObserver(this);
		this.initFrame();
	}

	private void initFrame() {
		this.setLayout(new FlowLayout());
		
		JComboBox<ViewEnumeration> box = new JComboBox<>(ViewEnumeration.values());

		box.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				gui.changeView(ViewEnumeration.valueOf(box.getSelectedItem().toString()));
				gui.showDialog("View changed to: " + ViewEnumeration.valueOf(box.getSelectedItem().toString()), MessageType.ERROR);
				gui.updateView(ViewEnumeration.PLAYVIEW);
			}
			
		});
		
		JButton btn = new JButton("Close Dialog");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gui.closeDialog();
				
			}
			
		});
		
		this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE); //Prevents test frame to be excluded from dialogs
		this.setLayout(new FlowLayout());
		this.add(btn);
		this.add(box);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void btnConnectClick(InetSocketAddress serverAddress, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnRestartClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnDisconnectClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnTrumpClick(Trump trump) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnCardClick(Card card) {
		System.out.println(card.getColor().toString() + card.getValue());
//		Card[] cards = data.getHand().toArray();
//		cards[0] = ClientModelTest.createRandomCard();
//		data.updateHand(cards);
		data.getHand().remove(card);
		data.getHand().add(ClientModelTest.createRandomCard());
		gui.updateView(ViewEnumeration.PLAYVIEW);	
	}

	@Override
	public void btnCloseWindowClick(ViewEnumeration view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnWeisActionChosen(boolean allowBroadcast) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnJoinTableClick(Seat preferedSeat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnChangeStateClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playViewClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnFillWithBots() {
		// TODO Auto-generated method stub
		
	}
}
