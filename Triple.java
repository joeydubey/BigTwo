/**
 * Subclass of Hand class, defines a hand of cards that form a Triple. 
 * 
 * @author Aryaman Dubey
 *
 */
public class Triple extends Hand implements HandInterface
{	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor which creates and returns an object of type Triple. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public Triple(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	
	/**
	 * Finds and returns the top card of a triple. 
	 */
	public Card getTopCard()
	{
		int max_idx = 0;
		int max = this.getCard(0).suit;
		
		for (int i = 1; i < this.size(); i++)
		{
			if (this.getCard(i).suit > max)
			{
				max = this.getCard(i).suit;
				max_idx = i;
			}
		}
		
		return this.getCard(max_idx);
	}
	
	/**
	 * Checks whether the triple is valid/legal.
	 */
	public boolean isValid()
	{
		if (this.size() != 3)
		{
			return false;
		}
		
		if ( (this.getCard(0).rank != this.getCard(1).rank) || (this.getCard(0).rank != this.getCard(2).rank) || (this.getCard(1).rank != this.getCard(2).rank))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the type of a triple. 
	 */
	public String getType()
	{
		return "Triple";
	}
}