import javafx.scene.Cursor;
import javafx.scene.Node;

public class MouseGestures {

    class DragContext {
        double x;
        double y;
    }
    
	final DragContext dragContext = new DragContext();
	private Game gameContext;
    
    // Constructor
    MouseGestures(Game game) {
    	this.gameContext = game;
    	
    }
    
    public void makeDraggable(final Card card) {
    	Node node = card.imageView;
    	
    	node.setOnMouseEntered(e -> node.setCursor(Cursor.OPEN_HAND));
    	
    	node.setOnMousePressed(e -> {
    		node.setCursor(Cursor.CLOSED_HAND);
            dragContext.x = e.getSceneX();
            dragContext.y = e.getSceneY();
    	});
    	
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
    	    	if (gameContext.pileTop.match(card)) {
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
    				if (gameContext.currentPlayer.getHand().isEmpty()) gameContext.end();
    				else {
    					gameContext.nextPlayer();
    					gameContext.getUserSelection();
    				}
    			} else {
    				gameContext.errorDialog.setTitle("Message");
    				gameContext.errorDialog.setHeaderText("Cannot play this card.");
    				gameContext.errorDialog.setContentText("Your card does not match the rank and suit.");
    				gameContext.errorDialog.showAndWait();
    			}
    	    }
        });
    }
}
