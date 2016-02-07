public class Eight extends Card {
	Eight(String suit, String rank, IGame iGame) {
		super(suit, rank, iGame);
	}

	@Override 
	void action() {
		iGame.showMessage("An Eight is played! The direction of play is reversed.");
		iGame.reverse();
	}
}
