/**
 * Subclass of CardList class, defines a hand (list) of cards played by a player. 
 * 
 * @author Aryaman Dubey
 *
 */

public class Hand extends CardList implements HandInterface
{
	private static final long serialVersionUID = 0;
	private CardGamePlayer player;
	
	/**
	 * Constructor which creates and returns an object of type Hand. 
	 * 
	 * @param player
	 * 					The player playing the hand
	 * 
	 * @param cards
	 * 					The cards being played in the hand
	 */
	public Hand(CardGamePlayer player, CardList cards)
	{
		this.player = player;
		for (int i = 0; i < cards.size(); i++)
		{
			this.addCard(cards.getCard(i));
		}
		this.sort();
	}
	
	/**
	 * 
	 * Function that returns the player of the hand.
	 * 
	 * @return
	 * 			Return the private variable player.
	 */
	public CardGamePlayer getPlayer()
	{
		return this.player;
	}
	
	/**
	 * Finds and returns the top card of a hand. Overridden in some subclasses.
	 * 
	 * @return
	 *
	 */
	public Card getTopCard()
	{
		BigTwoCard top_card = new BigTwoCard(this.getCard(0).suit, this.getCard(0).rank);
		
		for (int i = 1; i < this.size(); i++)
		{
			if (this.getCard(i).compareTo(top_card) == 1)
			{
				top_card = (BigTwoCard) this.getCard(i);
			}
		}
		
		return (Card) top_card;
	}
	
	/**
	 * Determines if current hand beats another hand.
	 * 
	 * @param hand
	 * 				The hand representing the last hand on table.
	 * 
	 * @return
	 * 				Returns true if the last hand is beaten, else false.
	 */
	public boolean beats(Hand hand)
	{
		if (hand.size() < 4 && hand.size() > 0)
		{
			if (this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard()) == 1)
			{
				return true;
			}
		}
		
		if (hand.size() == 5)
		{
			if (this instanceof StraightFlush)
			{
				if (this.size() == hand.size())
				{
					if (this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1)
					{
						return false;
					}
					
					return true;
				}
			}
			
			if (this instanceof Quad)
			{
				if (this.size() == hand.size())
				{
					if (this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1)
					{
						return false;
					}
					
					if (this.getType() != hand.getType())
					{
						if (hand.getType() == "StraightFlush")
						{
							return false;
						}
					}
					
					return true;
				}
			}
			
			if (this instanceof FullHouse)
			{
				if (this.size() == hand.size())
				{
					if (this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1)
					{
						return false;
					}
					
					if (this.getType() != hand.getType())
					{
						if (hand.getType() == "StraightFlush" || hand.getType() == "Quad")
						{
							return false;
						}
					}
					
					return true;
				}
			}
			
			if (this instanceof Flush)
			{
				if (this.size() == hand.size())
				{
					if (this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1)
					{
						return false;
					}
					
					if (this.getType() != hand.getType())
					{
						if (hand.getType() == "StraightFlush" || hand.getType() == "Quad" || hand.getType() == "FullHouse")
						{
							return false;
						}
					}
					
					return true;
				}
			}
			
			if (this instanceof Straight)
			{
				if (this.size() == hand.size())
				{
					if (this.getType() == hand.getType() && this.getTopCard().compareTo(hand.getTopCard()) != 1)
					{
						return false;
					}
					
					if (this.getType() != hand.getType())
					{
						return false;
					}
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Abstract method that checks whether a hand is valid/legal.
	 * @see HandInterface#isValid()
	 */
	
	public boolean isValid()
	{
		return false;
	}
	
	/**
	 * Abstract method that returns the type of the hand.
	 * @see HandInterface#getType()
	 */
	public String getType()
	{
		return null;
	}
}
