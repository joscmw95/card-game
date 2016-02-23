public class King extends Card {
	King(String suit, String rank, IGame iGame) {
		super(suit, rank, iGame);
	}

	@Override 
	void action() {
		iGame.showMessage("A King is played! The next player is forced to draw a card.");
		iGame.drawCard(true);
	}
}
