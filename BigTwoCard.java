/**
 * Subclass of Card class, defines cards which are designed to be used in a BigTwo game. 
 * 
 * @author Aryaman Dubey
 *
 */

public class BigTwoCard extends Card
{	
	private static final long serialVersionUID = -1L;
	
	/**
	 * Constructor which creates and returns a Card for the BigTwo game by calling its superclass (Card) constructor.
	 * 
	 * @param suit
	 * 				The suit of the card.
	 * 
	 * @param rank
	 * 				The rank of the card.
	 */
	public BigTwoCard(int suit, int rank)
	{
		super(suit, rank);
	}
	
	/**
	 * Public function to compare whether a BigTwoCard beats another BigTwoCard by comparing the rank and the suit.
	 * 
	 * @see Card#compareTo(Card)
	 * 
	 */
	public int compareTo(Card card)
	{
		int obj_rank = this.rank;
		int card_rank = card.rank;
		
		if (obj_rank < 2)
		{
			obj_rank += 13;
		}
		
		if (card_rank < 2)
		{
			card_rank += 13;
		}
		
		if (obj_rank > card_rank)
		{
			return 1;
		}
		else if (obj_rank < card_rank)
		{
			return -1;
		}
		else if (this.suit > card.suit)
		{
			return 1;
		}
		else if (this.suit < card.suit)
		{
			return -1;
		}
		else
		{
			return 0;
		}
		
	}
}
