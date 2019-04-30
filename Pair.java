/**
 * Subclass of Hand class, defines a hand of cards that form a Pair. 
 * 
 * @author Aryaman Dubey
 *
 */
public class Pair extends Hand implements HandInterface
{	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor which creates and returns an object of type Pair. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public Pair(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	
	/**
	 * Finds and returns the top card of a pair. 
	 */
	public Card getTopCard()
	{
		if (this.getCard(0).suit > this.getCard(1).suit)
		{
			return this.getCard(0);
		}
		
		return this.getCard(1);
	}
	
	/**
	 * Checks whether the pair is valid/legal.
	 */
	public boolean isValid()
	{
		if (this.size() != 2)
		{
			return false;
		}
		
		if (this.getCard(0).rank != this.getCard(1).rank)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the type of a pair. 
	 */
	public String getType()
	{
		return "Pair";
	}
}
