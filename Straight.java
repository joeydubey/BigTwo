/**
 * Subclass of Hand class, defines a hand of cards that form a Straight. 
 * 
 * @author Aryaman Dubey
 *
 */
import java.util.Arrays;

public class Straight extends Hand implements HandInterface
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor which creates and returns an object of type Straight. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public Straight(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * Returns the type of a straight. 
	 */
	public String getType()
	{
		return "Straight";
	}
	
	/**
	 * Checks whether the straight is valid/legal.
	 */
	public boolean isValid()
	{
		if (this.size() != 5)
		{
			return false;
		}
		
		int[] ranks = new int[5];
		
		for (int i = 0; i < 5; i++)
		{
			if (this.getCard(i).rank < 2)
			{
				ranks[i] = this.getCard(i).rank + 13;
			}
			else
			{
				ranks[i] = this.getCard(i).rank;
			}
		}
		
		Arrays.sort(ranks);
		
		for (int i = 0; i < 4; i++)
		{
			if ( ranks[i] + 1 != ranks[i + 1])
			{
				return false;
			}
		}
		
		return true;
	}
}
