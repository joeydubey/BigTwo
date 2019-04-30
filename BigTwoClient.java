import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Class that models a BigTwo card game and controls the flow of the game.
 *
 * @author Aryaman Dubey
 *
 */
public class BigTwoClient implements NetworkGame, CardGame
{
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int currentIdx;
	private BigTwoTable table;

	/**
	 * Constructor which creates and returns an object of type BigTwo.
	 *
	 */
	public BigTwoClient()
	{
		playerList = new ArrayList<CardGamePlayer> ();

		for (int i = 0; i < 4; i++)
		{
			playerList.add(new CardGamePlayer());
		}

		handsOnTable = new ArrayList<Hand>();
		this.playerName = "";
		this.setPlayerID(-1);
		table = new BigTwoTable(this);
		this.makeConnection();
	}

	/**
	 * Public getter function to get the number of players playing the game.
	 * @see CardGame#getNumOfPlayers()
	 */
	public int getNumOfPlayers()
	{
		return this.playerList.size();
	}

	/**
	 * Retrieves the deck of cards being played in a BigTwo game.
	 *
	 * @return deck
	 * 						A deck of cards
	 */
	public Deck getDeck()
	{
		return this.deck;
	}

	/**
	 * Retrieves the list of players in a BigTwo game.
	 *
	 * @return playerList
	 * 						List of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList()
	{
		return this.playerList;
	}

	/**
	 * Retrieves the hands played on the table in a BigTwo game.
	 *
	 * @return handsOnTable
	 * 						List of hands on the table
	 */
	public ArrayList<Hand> getHandsOnTable()
	{
		return this.handsOnTable;
	}

	/**
	 * Retrieves the index of the current player.
	 *
	 * @return currentIdx
	 * 						an integer specifying the index of the current player
	 */
	public int getCurrentIdx()
	{
		return this.currentIdx;
	}

	/**
	 * Starts a BigTwo game with a (shuffled) deck of
     * cards supplied as the argument.
	 * @param deck
	 * 				a deck of cards
	 */
	public void start(Deck deck)
	{
		int x = 0;
		currentIdx = -1;

		handsOnTable.clear();

		for (int i = 0; i < 4; i++)   // adding cards to the hand
		{
			playerList.get(i).removeAllCards();

			for(int j = 0; j < 13; j++)
			{
				playerList.get(i).addCard(deck.getCard(x+i+j));
			}

			x += 12;
		}

		for (int i = 0; i < 4; i++)
		{
			playerList.get(i).getCardsInHand().sort();
		}


		Card card = new Card(0, 2); // Three of Diamonds

		for (int i = 0; i < 4; i++)
		{
			if (playerList.get(i).getCardsInHand().contains(card))
			{
				currentIdx = i;
			}
		}

		table.printMsg(this.getPlayerList().get(this.currentIdx).getName() + "'s turn.\n");
		table.repaint();
	}

	/**
	 * Public function to make the selected move.
	 * @see CardGame#makeMove(int, int[])
	 */
	public void makeMove(int playerID, int[] cardIdx)
	{
		CardGameMessage cardGameMessage = new CardGameMessage(6, -1, cardIdx);
		this.sendMessage(cardGameMessage);
	}

	/**
	 * Public function to check if the move is valid and then play it.
	 * @see CardGame#checkMove(int, int[])
	 */
	public void checkMove(int playerID, int[] cardIdx)
	{
		boolean valid_move = true;
		Card card = new Card(0, 2); // Three of Diamonds

		if (cardIdx != null)
		{
			CardList played_list = this.getPlayerList().get(playerID).play(cardIdx); //Cardlist converting the array to cards.
			Hand hand = composeHand(playerList.get(playerID), played_list); //Converting the hand to its corresponding type.
			if (handsOnTable.isEmpty())
			{
				if (hand != null && hand.contains(card))
				{
					valid_move = true;
				}
				else
				{
					valid_move = false;
				}
			}
			else
			{
				if (handsOnTable.get(handsOnTable.size() - 1).getPlayer() != playerList.get(playerID))
				{
					if (hand != null)
					{
						valid_move = hand.beats(handsOnTable.get(handsOnTable.size() - 1));
					}
					else
					{
						valid_move = false;
					}
				}
				else
				{
					if (hand != null)
					{
						valid_move = true;
					}
					else
					{
						valid_move = false;
					}
				}
			}

			if (valid_move)
			{
				for (int i = 0; i < played_list.size(); i++)
				{
					playerList.get(playerID).getCardsInHand().removeCard(played_list.getCard(i));
				}

				table.printMsg("{" + hand.getType() + "} " + hand + "\n");
				handsOnTable.add(hand);
				currentIdx = (currentIdx + 1) % 4;
				table.setActivePlayer(currentIdx);
			}

			else
			{
				table.printMsg(played_list +" <= Not a legal move!!!\n");
			}
		}

		else
		{
			if (!handsOnTable.isEmpty() && handsOnTable.get(handsOnTable.size()-1).getPlayer() != playerList.get(playerID)) //Player passes
			{
				table.printMsg("{Pass}\n");
				currentIdx = (currentIdx + 1) % 4;
				table.setActivePlayer(currentIdx);
				valid_move = true;
			}

			else
			{
				table.printMsg("{Pass} <= Not a legal move!!!\n");
				valid_move = false;
			}
		}

		table.repaint();

		if (this.endOfGame())
		{
			table.setActivePlayer(-1);
			table.repaint();
			table.disable();
			table.printMsg("\nGame ends.\n");
			JTextArea end_txt = new JTextArea();
			end_txt.setEditable(false);
			end_txt.setFont(new Font("Arial", Font.PLAIN, 12));

			for (int i = 0; i < 4; i++)
			{
				if (playerList.get(i).getCardsInHand().size() == 0)
				{
					end_txt.append("Player " + i + " wins the game.\n");
				}
				else
				{
					end_txt.append("Player " + i +" has " + playerList.get(i).getCardsInHand().size() + " cards in hand.\n");
				}
			}

			JOptionPane.showMessageDialog(null, end_txt);
			this.sendMessage(new CardGameMessage(4, -1, null));
		}
	}

	/**
	 * Public function to check if a game has ended.
	 * @see CardGame#endOfGame()
	 */
	public boolean endOfGame()
	{
		for (int i = 0; i < this.getNumOfPlayers(); i++)
		{
			if (this.playerList.get(i).getNumOfCards() == 0)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Public function to retrieve the value of the private variable playerID.
	 * @see NetworkGame#getPlayerID()
	 */
	public int getPlayerID()
	{
		return this.playerID;
	}

	/**
	 * Public function to set the value of the private variable playerID.
	 * @see NetworkGame#setPlayerID(int)
	 */
	public void setPlayerID(int playerID)
	{
		this.playerID = playerID;
	}

	/**
	 * Public function to retrieve the value of the private variable playerName.
	 * @see NetworkGame#getPlayerName()
	 */
	public String getPlayerName()
	{
		return this.playerName;
	}

	/**
	 * Public function to set the value of the private variable playerName.
	 * @see NetworkGame#setPlayerName(java.lang.String)
	 */
	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	/**
	 * Public function to retrieve the value of the private variable serverIP.
	 * @see NetworkGame#getServerIP()
	 */
	public String getServerIP()
	{
		return this.serverIP;
	}

	/**
	 * Public function to set the value of the private variable serverIP.
	 * @see NetworkGame#setServerIP(java.lang.String)
	 */
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}

	/**
	 * Public function to get the value of the private variable serverPort.
	 * @see NetworkGame#getServerPort()
	 */
	public int getServerPort()
	{
		return this.serverPort;
	}

	/**
	 * Public function to set the value of the private variable serverPort.
	 * @see NetworkGame#setServerPort(int)
	 */
	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	/**
	 * Public function that creates a connection between the client and the server.
	 * @see NetworkGame#makeConnection()
	 */
	public void makeConnection()
	{
        this.setPlayerName(JOptionPane.showInputDialog("Name:", "Player name"));
		this.setServerIP(JOptionPane.showInputDialog("IP address:", "127.0.0.1"));
		this.setServerPort(Integer.valueOf(JOptionPane.showInputDialog("TCP port:", "2396")));
		try {
			sock = new Socket(getServerIP(), getServerPort());
			System.out.println("Connection established.");
			ois = new ObjectInputStream(sock.getInputStream());
			oos = new ObjectOutputStream(sock.getOutputStream());
			Thread thread = new Thread(new ServerHandler()); //For multi-threading.
			thread.start();
			this.sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
			this.sendMessage(new CardGameMessage(4, -1, null));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Public function that parses the messages received by client.
	 * @see NetworkGame#parseMessage(GameMessage)
	 */
	public synchronized void parseMessage(GameMessage message)
	{
		if (message.getType() == 0)
		{
			this.setPlayerID(message.getPlayerID());
			if (message.getData() != null)
			{
				String[] playerNames = (String[]) message.getData();
				for(int i = 0; i < 4; i++)
				{
					this.getPlayerList().get(i).setName(playerNames[i]);
				}
			}
			table.setActivePlayer(playerID);
		}
		else if (message.getType() == 1)
		{
			if ((String) message.getData() != null)
			{
				this.getPlayerList().get(message.getPlayerID()).setName((String) message.getData());
			}
		}
		else if (message.getType() == 2)
		{
			table.printMsg("Game is full.\n");
		}
		else if (message.getType() == 3)
		{
			this.getPlayerList().get(message.getPlayerID()).setName("");
			table.disable();
			if (!this.endOfGame())
			{
				table.printMsg((String) message.getData() + " has left the game. \n");
				this.sendMessage(new CardGameMessage(4, -1, null));
			}
		}
		else if (message.getType() == 4)
		{
			table.printMsg(this.getPlayerList().get(message.getPlayerID()).getName() + " is ready.\n");
		}
		else if (message.getType() == 5)
		{
			this.start((BigTwoDeck) message.getData());
		}
		else if (message.getType() == 6)
		{
			this.checkMove(message.getPlayerID(), (int[]) message.getData());
			table.disable();
			if (this.getPlayerID() == this.getCurrentIdx())
			{
				table.enable();
				table.printMsg("Your turn\n");
			}
			else
			{
				table.printMsg(this.getPlayerList().get(this.currentIdx).getName() + "'s turn.\n");
			}
		}
		else if (message.getType() == 7)
		{
			table.chatMsg((String) message.getData());
		}
		else
		{
			System.out.println("Invalid message.");
		}
	}

	/**
	 * Public function that sends message from the client to the server.
	 * @see NetworkGame#sendMessage(GameMessage)
	 */
	public void sendMessage(GameMessage message)
	{
		try
		{
			oos.writeObject(message);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Inner class that implements the Runnable interface.
	 * @see Runnable
	 *
	 * @author Aryaman Dubey
	 *
	 */
	class ServerHandler implements Runnable
	{
		public void run()
		{
			try
			{
				CardGameMessage inMessage; //Object for CardGameMessage.
				while((inMessage = (CardGameMessage)ois.readObject()) != null)
				{
					parseMessage(inMessage);
				}

			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}

	/**
	 * Creates the BigTwo game and calls the start function.
	 *
	 * @param args
	 *
	 */
	public static void main(String[] args)
	{
		BigTwoClient game = new BigTwoClient();
	}

	/**
	 * Function that returns a legal hand from the list of cards played by the player.
	 *
	 * @param player
	 * 					The player playing the hand
	 * @param cards
	 * 					The cards being played in the hand
	 * @return
	 * 					Returns valid hand
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards)
	{
		StraightFlush straightflush = new StraightFlush(player, cards);
		if (straightflush.isValid())
		{
			return straightflush;
		}

		Quad quad = new Quad(player, cards);
		if (quad.isValid())
		{
			return quad;
		}

		FullHouse fullhouse = new FullHouse(player, cards);
		if (fullhouse.isValid())
		{
			return fullhouse;
		}

		Flush flush = new Flush(player, cards);
		if (flush.isValid())
		{
			return flush;
		}

		Straight straight = new Straight(player, cards);
		if (straight.isValid())
		{
			return straight;
		}

		Triple triple = new Triple(player, cards);
		if (triple.isValid())
		{
			return triple;
		}

		Pair pair = new Pair(player, cards);
		if (pair.isValid())
		{
			return pair;
		}

		Single single = new Single(player, cards);
		if (single.isValid())
		{
			return single;
		}

		return null;
	}
}
