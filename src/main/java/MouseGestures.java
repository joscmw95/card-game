import javafx.scene.Cursor;
import javafx.scene.Node;

/**
 * The Class MouseGestures.
 */
public class MouseGestures {

    /**
     * The Class DragContext. 
     * Stores the X and Y translations of the card.
     */
    class DragContext {
        
        /** The x. */
        double x;
        
        /** The y. */
        double y;
    }
    
	/** The drag context. */
	final DragContext dragContext = new DragContext();
	
	/** The game context. */
	private Game gameContext;
    
    /**
     * Instantiates a new mouse gestures.
     *
     * @param game the game
     */
    // Constructor
    MouseGestures(Game game) {
    	this.gameContext = game;
    	
    }

    /**
     * Make the card ImageView draggable.
     *
     * @param card the card
     */
    public void makeDraggable(final Card card) {
    	Node node = card.imageView;
    	
    	// Set cursor to Open Hand when mouse enters card
    	node.setOnMouseEntered(e -> node.setCursor(Cursor.OPEN_HAND));
    	
    	// Set cursor to Closed Hand when mouse enters card
    	node.setOnMousePressed(e -> {
    		node.setCursor(Cursor.CLOSED_HAND);
            dragContext.x = e.getSceneX();
            dragContext.y = e.getSceneY();
    	});
    	
    	// Move the card as mouse is dragged
        node.setOnMouseDragged(e -> {
            node.setTranslateX(e.getSceneX() - dragContext.x);
            node.setTranslateY(e.getSceneY() - dragContext.y);
        });
        
        node.setOnMouseReleased(e -> {
        	node.setCursor(Cursor.DEFAULT);
        	
    	    // snap back card in place after mouse release
    	    node.setTranslateX(0);
    	    node.setTranslateY(0);
    	    
    	    // tedious calculation to determine cursor is on pileTop
    	    double minXOfPlayTable = gameContext.playTable.getBoundsInParent().getMinX();
    	    double minYOfPlayTable = gameContext.playTable.getBoundsInParent().getMinY();
    	    double minXOfCenter = gameContext.playTable.getCenter().getBoundsInParent().getMinX();
    	    double minYOfCenter = gameContext.playTable.getCenter().getBoundsInParent().getMinY();
    	    double minXOfPilePane = gameContext.pilePane.getBoundsInParent().getMinX();
    	    double minYOfPilePane = gameContext.pilePane.getBoundsInParent().getMinY();
    	    double minXOfNode = gameContext.pileTop.imageView.getBoundsInParent().getMinX();
    	    double minYOfNode = gameContext.pileTop.imageView.getBoundsInParent().getMinY();
    	    
    	    double X = e.getSceneX() - minXOfPlayTable - minXOfCenter - minXOfPilePane - minXOfNode;
    	    double Y = e.getSceneY() - minYOfPlayTable - minYOfCenter - minYOfPilePane - minYOfNode;
    	    
    	    // try to play card if released in pileTop
    	    if (gameContext.pileTop.imageView.contains(X, Y)) {
    	    	if (card instanceof Ace || gameContext.pileTop.match(card)) {
    	    		// detach all event handlers
    	    		node.setOnMouseEntered(null);
    	        	node.setOnMousePressed(null);
    	            node.setOnMouseDragged(null);
    	            node.setOnMouseReleased(null);
    	            
    	    		gameContext.showMessage("Player " + (gameContext.playerIndex+1) + " discarded " + card);
    				gameContext.currentPlayer.playCard(card);
    				gameContext.pile.add(card);
    				gameContext.pileTop = gameContext.pile.get(gameContext.pile.size()-1);
    				gameContext.pilePane.getChildren().add(gameContext.pileTop.imageView);
    				
    				// end game if player's hand is empty
    				if (gameContext.currentPlayer.getHand().isEmpty()) gameContext.endGame();
    				else {
    					// continue game
    					gameContext.nextPlayer();
    					gameContext.getUserSelection();
    				}
    			} else {
    				// card is not playable
    				gameContext.errorDialog.setTitle("Message");
    				gameContext.errorDialog.setHeaderText("Cannot play this card.");
    				gameContext.errorDialog.setContentText("Your card does not match the rank and suit.");
    				gameContext.errorDialog.showAndWait();
    			}
    	    }
        });
    }
}
