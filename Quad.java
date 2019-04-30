/**
 * Subclass of Hand class, defines a hand of cards that form a Quad. 
 * 
 * @author Aryaman Dubey
 *
 */
public class Quad extends Hand implements HandInterface
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor which creates and returns an object of type Quad. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public Quad(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * Finds and returns the top card of a Quad. 
	 */
	public Card getTopCard()
	{
		if (this.getCard(0).rank == this.getCard(3).rank)
		{
			return this.getCard(3);
		}
		else
		{
			return this.getCard(4);
		}
	}
	
	/**
	 * Checks whether the quad is valid/legal.
	 */
	public boolean isValid()
	{
		if (this.size() == 5)
		{
			if ( (this.getCard(0).rank == this.getCard(3).rank) && (this.getCard(0).rank == this.getCard(2).rank) && (this.getCard(0).rank == this.getCard(1).rank) )
			{
				return true;
			}
			else if ( (this.getCard(4).rank == this.getCard(3).rank) && (this.getCard(4).rank == this.getCard(2).rank) && (this.getCard(4).rank == this.getCard(1).rank) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the type of a quad. 
	 */
	public String getType()
	{
		return "Quad";
	}
}
