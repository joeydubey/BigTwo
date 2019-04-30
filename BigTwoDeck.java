/**
 * Subclass of Deck class, defines decks which are designed to be used in a BigTwo game. 
 * 
 * @author Aryaman Dubey
 *
 */
public class BigTwoDeck extends Deck
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes a BigTwoDeck.
	 * 
	 * @see Deck#initialize()
	 * 
	 */
	public void initialize()
	{
		removeAllCards();
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 13; j++)
			{
				BigTwoCard bigtwo_card = new BigTwoCard(i, j);
				this.addCard(bigtwo_card);
			}
		}
	}
}
