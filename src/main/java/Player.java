import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.layout.HBox;

public class Player {
	List<Card> hand = new ArrayList<Card>();
	HBox handGUI = new HBox(5);
	
	Player() {
		handGUI.setStyle("-fx-alignment: center;");
	}
	
	void addCard(Card card) {
		hand.add(card);
		Collections.sort(hand);
		
		// update hand GUI
		handGUI.getChildren().clear();
		for(int i=0; i<hand.size(); i++) {
			handGUI.getChildren().add(hand.get(i).imageView);
		}
	}
	
	void playCard(Card card) {
		card.action();
		hand.remove(card);
		
		// update hand GUI
		handGUI.getChildren().remove(card.imageView);
	}
	
	boolean isPlayable(Card pileTop) {
		for(Card card : hand) {
			if (pileTop.match(card)) {
				return true;
			}
		}
		return false;
	}
	
	int getTotalPoints() {
		int total=0;
		for(Card card : hand) {
			total+=card.getScore();
		}
		return total;
	}
}
