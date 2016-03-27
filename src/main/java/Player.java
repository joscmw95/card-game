import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/**
 * The Class Player.
 */
public class Player {
	
	/** The hand. */
	private List<Card> hand = new ArrayList<Card>();
	
	/** The hand GUI. */
	private HBox handGUI = new HBox(5);
	
	/**
	 * Instantiates a new player.
	 */
	Player() {
		handGUI.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Adds the card to the player hand.
	 *
	 * @param card the card
	 */
	void addCard(Card card) {
		hand.add(card);
		Collections.sort(hand);
		
		// update hand GUI
		handGUI.getChildren().clear();
		for(int i=0; i<hand.size(); i++) {
			handGUI.getChildren().add(hand.get(i).imageView);
		}
	}
	
	/**
	 * Play the selected card.
	 *
	 * @param card the card
	 */
	void playCard(Card card) {
		// remove card from hand
		hand.remove(card);
		// update hand GUI
		handGUI.getChildren().remove(card.imageView);
		// do card action if hand not empty
		if (hand.size() > 0) card.action();
	}
	
	/**
	 * Checks if player has playable cards.
	 *
	 * @param pileTop the pile top
	 * @return true, if is playable
	 */
	boolean isPlayable(Card pileTop) {
		for(Card card : hand) {
			if (card instanceof Ace || pileTop.match(card)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the total points.
	 *
	 * @return the total points
	 */
	int getTotalPoints() {
		int total=0;
		for(Card card : hand) {
			total+=card.getScore();
		}
		return total;
	}

	/**
	 * Gets the hand GUI.
	 *
	 * @return the hand GUI
	 */
	public HBox getHandGUI() {
		return handGUI;
	}

	/**
	 * Sets the hand GUI.
	 *
	 * @param handGUI the new hand GUI
	 */
	public void setHandGUI(HBox handGUI) {
		this.handGUI = handGUI;
	}

	/**
	 * Gets the hand.
	 *
	 * @return the hand
	 */
	public List<Card> getHand() {
		return hand;
	}

	/**
	 * Sets the hand.
	 *
	 * @param hand the new hand
	 */
	public void setHand(List<Card> hand) {
		this.hand = hand;
	}
}
