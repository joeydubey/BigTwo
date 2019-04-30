/**
 * Subclass of Hand class, defines a hand of cards that form a Flush. 
 * 
 * @author Aryaman Dubey
 *
 */
public class Flush extends Hand implements HandInterface
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor which creates and returns an object of type Flush. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public Flush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * Returns the type of a flush. 
	 */
	public String getType()
	{
		return "Flush";
	}
	
	/**
	 * Checks whether the flush is valid/legal.
	 */
	public boolean isValid()
	{
		if (this.size() != 5)
		{
			return false;
		}
		
		for (int i = 0; i < 4; i++)
		{
			if (this.getCard(i + 1).suit != this.getCard(i).suit)
			{
				return false;
			}
		}
		
		return true;
	}
}
