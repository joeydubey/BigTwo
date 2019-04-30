/**
 * Subclass of Hand class, defines a hand of cards that form a Full House. 
 * 
 * @author Aryaman Dubey
 *
 */
public class FullHouse extends Hand implements HandInterface
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor which creates and returns an object of type FullHouse. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	
	/**
	 * Finds and returns the top card of a full house. 
	 */
	public Card getTopCard()
	{
		if (this.getCard(0).rank == this.getCard(2).rank)
		{
			return this.getCard(2);
		}
		else
		{
			return this.getCard(4);
		}
	}
	
	/**
	 * Checks whether the full house is valid/legal.
	 */
	public boolean isValid()
	{
		if (this.size() == 5)
		{
			if ( (this.getCard(0).rank == this.getCard(2).rank) && (this.getCard(0).rank == this.getCard(1).rank) )
			{
				if (this.getCard(3).rank == this.getCard(4).rank)
				{
					return true;
				}
			}
			else if ( (this.getCard(2).rank == this.getCard(4).rank) && (this.getCard(2).rank == this.getCard(3).rank) )
			{
				if (this.getCard(0).rank == this.getCard(1).rank)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the type of a full house. 
	 */
	public String getType()
	{
		return "FullHouse";
	}
}
