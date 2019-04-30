/**
 * Subclass of Hand class, defines a hand of cards that form a Straight Flush. 
 * 
 * @author Aryaman Dubey
 *
 */
public class StraightFlush extends Hand
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor which creates and returns an object of type StraightFlush. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public StraightFlush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * Checks whether the straight flush is valid/legal.
	 */
	public boolean isValid()
	{
		if (this.size() != 5)
		{
			return false;
		}
		
		int suit = this.getCard(0).suit;
		
		for (int i = 1; i < 5; i++)
		{
			if (this.getCard(i).suit != suit)
			{
				return false;
			}
		}
		
		for (int i = 0; i < 4; i++)
		{
			int rank_next = this.getCard(i+1).rank;
			int rank = this.getCard(i).rank;
			
			if (rank < 2)
			{
				rank += 13;
			}
			
			if (rank_next < 2)
			{
				rank_next += 13;
			}
			
			if (rank_next != (rank + 1))
			{
				return false;
			}
		}
		
		return true;
		
	}
	
	/**
	 * Returns the type of a straight flush. 
	 */
	public String getType()
	{
		return "StraightFlush";
	}
}
