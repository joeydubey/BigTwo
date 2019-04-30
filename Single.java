/**
 * Subclass of Hand class, defines a hand of cards that form a Single. 
 * 
 * @author Aryaman Dubey
 *
 */
public class Single extends Hand implements HandInterface
{	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor which creates and returns an object of type Single. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public Single(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	
	/**
	 * Finds and returns the top card of a single. 
	 */
	public Card getTopCard()
	{
		return this.getCard(0);
	}
	
	/**
	 * Checks whether the single is valid/legal.
	 */
	public boolean isValid()
	{
		if (this.size() != 1)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the type of a single. 
	 */
	public String getType()
	{
		return "Single";
	}
}
