import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.util.*;
public class BigTwoTable implements CardGameTable
{
	private BigTwoClient game; //Creates BigTwo game
	private boolean[] selected; //Array to check for selected cards.
	private int activePlayer; //The index of the current player.
	private JFrame frame; //The main frame for the GUI
	private JPanel bigTwoPanel; //The panel representing the green area of the Table.
	private JButton playButton; //Play Button
	private JButton passButton; // Button
	private JTextArea msgArea; //The message area where text is displayed.
	private JTextArea chatArea; //The chat area.
	private Image[][] cardImages; //Array to store all images of the game cards.
	private Image cardBackImage; //Stores back image of cards.
	private Image[] avatars; //Array to store the images of all avatars.
	private BigTwoPanel p0, p1, p2, p3, prev_hand; //Five panels to display the five parts of the bigTwoPanel
	private Border blackline; //Border of the frame.
	private JTextField tf; //Text field
	private JScrollPane textScroll; //The scroll pane for the message area.

	/**
	 * Public randomizer function that chooses a given number of random Strings from the input array
	 * and returns the selected random array.
	 *
	 * @param array
	 * 				The input String array to be randomized.
	 * @param n
	 * 				Number of elements to be chosen at random from the input array.
	 */
	public static String[] randomize(String[] array, int n)
	{
		ArrayList<String> list = new ArrayList<String>(array.length);
		for (String s: array)
		{
			list.add(s);
		}
		Collections.shuffle(list);
		String[] random = new String[n];

		for (int i = 0; i < n; i++)
		{
			random[i] = list.get(i);
		}

		Arrays.sort(random);

		return random;
	}

	/**
	 * Constructor that initializes and sets up the game.
	 * @param game
	 * 				The BigTwo game.
	 */
	public BigTwoTable(BigTwoClient game)
	{
		this.game = game;

		String avatar_src[] = new String[5]; //Array to store avatar locations
		avatar_src[0] = "src/avatars/superman_128.png";
		avatar_src[1] = "src/avatars/batman_128.png";
		avatar_src[2] = "src/avatars/flash_128.png";
		avatar_src[3] = "src/avatars/green_lantern_128.png";
		avatar_src[4] = "src/avatars/wonder_woman_128.png";

		String avatar_selected[] = randomize(avatar_src, 4); //Randomly picks any 4 of the 5 avatars to allot to players.

		avatars = new Image[4];
		for (int i = 0; i < 4; i++)
		{
			avatars[i] = new ImageIcon(avatar_selected[i]).getImage();
		}

		char[] suits = { 'd', 'c', 'h', 's'}; //Array to store card suits
		char[] ranks = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' }; //Array to store card ranks

		cardImages = new Image[4][13];
		String loc = new String();
		cardBackImage = new ImageIcon("src/cards/b.gif").getImage();

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 13; j++)
			{
				loc = "src/cards/"+ ranks[j] + suits[i] + ".gif";
				cardImages[i][j] = new ImageIcon(loc).getImage();
			}
		}

		//Creates main frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		//Menubar
		JMenuBar menubar = new JMenuBar(); //The menubar item.
		JMenu menu = new JMenu("Game"); //The menu item.
		JMenuItem c = new JMenuItem("Connect"); //The menu option Restart.
		JMenuItem q = new JMenuItem("Quit"); //The menu option Quit.
		c.addActionListener(new ConnectMenuItemListener());
		q.addActionListener(new QuitMenuItemListener());
		menu.add(c);
		menu.add(q);
		JMenu msgMenu = new JMenu("Message");
		JMenuItem clr = new JMenuItem("Clear");
		clr.addActionListener(new ClearMenuItemListener());
		msgMenu.add(clr);
		menubar.add(menu);
		menubar.add(msgMenu);
		frame.setJMenuBar(menubar);

		//Buttons at the bottom
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new ButtonListener());

		JPanel buttons = new JPanel(); //Bottom Panel for the two buttons.
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttons.add(playButton);
		buttons.add(new JSeparator());
		buttons.add(passButton);
		buttons.setBackground(Color.gray.brighter());

		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());

		JLabel msgLabel = new JLabel("Message: ");
		msgLabel.setFont(new Font(null, Font.PLAIN, 16));
		tf = new JTextField(40);
		tf.addActionListener(new TextFieldListener());
		tf.setPreferredSize(new Dimension(30, 26));
		tf.setFont(new Font(null, Font.PLAIN, 16));
		tf.setFocusable(true);

		JPanel chatMsgArea = new JPanel();
		chatMsgArea.setLayout(new FlowLayout(FlowLayout.CENTER));
		chatMsgArea.add(msgLabel);
		chatMsgArea.add(tf);
		chatMsgArea.setBackground(Color.gray.brighter());

		chatPanel.setBackground(Color.gray.brighter());
		chatPanel.add(buttons, BorderLayout.CENTER);
		chatPanel.add(chatMsgArea, BorderLayout.EAST);

		//Green Play Area
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));

		p0 = new BigTwoPanel();
		p1 = new BigTwoPanel();
		p2 = new BigTwoPanel();
		p3 = new BigTwoPanel();
		prev_hand = new BigTwoPanel();

		bigTwoPanel.add(p0);
		bigTwoPanel.add(p1);
		bigTwoPanel.add(p2);
		bigTwoPanel.add(p3);
		bigTwoPanel.add(prev_hand);

		frame.add(BorderLayout.SOUTH, chatPanel);
		frame.add(bigTwoPanel);

		//Text area displaying gameplay dialogue.
		JPanel gameTxtArea = new JPanel();
		gameTxtArea.setLayout(new BoxLayout(gameTxtArea, BoxLayout.Y_AXIS));
		msgArea = new JTextArea();
		msgArea.setFont(new Font("Courier", Font.PLAIN, 20));
		msgArea.setEditable(false);

		DefaultCaret caret = (DefaultCaret) msgArea.getCaret(); //Sets scrollbar to down configuration permanently.
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textScroll = new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textScroll.setPreferredSize(new Dimension(600, 400));
		chatArea = new JTextArea();
		chatArea.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		chatArea.setEditable(false);

		DefaultCaret chatCaret = (DefaultCaret)chatArea.getCaret(); //Setting the scrollbar to always remain down.
		chatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane chatScroll = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		chatScroll.setPreferredSize(new Dimension(600, 400));

		gameTxtArea.add(textScroll);
		gameTxtArea.add(chatScroll);
		frame.getContentPane().add(BorderLayout.EAST, gameTxtArea);

		//Displays frame at end.
		frame.setTitle("BigTwo");
		frame.setSize(1500, 950);
		frame.setVisible(true);

		blackline = BorderFactory.createLineBorder(Color.black);
		selected = new boolean[13];
		this.reset();
		this.setActivePlayer(game.getCurrentIdx());

	}

	/**
	 * Setter function that sets the value of variable activePlayer.
	 * @see CardGameTable#setActivePlayer(int)
	 */
	public void setActivePlayer(int activePlayer)
	{
		this.activePlayer = activePlayer;
	}

	/**
	 * Getter function that returns a boolean array to determine which cards are selected.
	 * @see CardGameTable#getSelected()
	 */
	public int[] getSelected()
	{
		int num = 0;
		for (int i = 0; i < 13; i++)
		{
			if (this.selected[i])
			{
				num++;
			}
		}
		int[] selected_cards = new int[num];

		int c = 0;

		for (int i = 0; i < 13; i++)
		{
			if (this.selected[i])
			{
				selected_cards[c] = i;
				c++;
			}
		}

		if (num > 0)
		{
			return selected_cards;
		}

		return null;
	}

	/**
	 * Reseter function for the list of selected cards. Deselects all cards.
	 * @see CardGameTable#resetSelected()
	 */
	public void resetSelected()
	{
		for (int i = 0; i < 13; i++)
		{
			this.selected[i] = false;
		}
	}

	/**
	 * Public repainter function for the GUI.
	 * @see CardGameTable#repaint()
	 */
	public void repaint()
	{
		if (game.getPlayerID() != game.getCurrentIdx())
		{
			this.disable();
		}
		else
		{
			this.enable();
		}

		this.resetSelected();
		bigTwoPanel.repaint();
	}

	/**
	 * Public printer function to display a message in the text area.
	 * @see CardGameTable#printMsg(java.lang.String)
	 */
	public void printMsg(String msg)
	{
		msgArea.append(msg);
	}

	/**
	 * Public function that prints a message in the chat area.
	 * @see CardGameTable#printMsg(java.lang.String)
	 */
	public void chatMsg(String msg)
	{
		chatArea.append(msg + "\n");
	}

	/**
	 * Public function to clear text from the text area.
	 * @see CardGameTable#clearMsgArea()
	 */
	public void clearMsgArea()
	{
		msgArea.setEditable(true);
		msgArea.setText("");
		msgArea.setEditable(false);
	}

	/**
	 * Public reseter function for the GUI.
	 * @see CardGameTable#reset()
	 */
	public void reset()
	{
		this.resetSelected();
		this.clearMsgArea();
	}

	/**
	 * Enables the play and pass buttons, and the panel for interaction by users.
	 * @see CardGameTable#enable()
	 */
	public void enable()
	{
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}

	/**
	 * Disables the interaction of the play and pass buttons, and the panel.
	 * @see CardGameTable#disable()
	 */
	public void disable()
	{
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}

	/**
	 * This class models a BigTwoPanel and implements all the components of the panel.
	 *
	 * @author Aryaman Dubey
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		/**
		 * Public constructor for the panel to add a MouseListener.
		 */
		public BigTwoPanel()
		{
			this.addMouseListener(this);
		}
		/**
		 * Determines whether the mouse is clicked, and whether it is used to select or deselect a card.
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e)
		{
			int x = e.getX(); //X-coordinate of mouse click.
			int y = e.getY(); //Y-coordinate of mouse click.
			if (e.getComponent().getY() == 1 + (p2.getY()-p1.getY())*activePlayer)
			{
				if (x >= 200 && x <= 200 + 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-1)+cardBackImage.getWidth(this))
				{
					if (x >= 200 + 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-1) && x < 200 + 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-1) + cardBackImage.getWidth(this))
					{
						if (game.getPlayerList().get(activePlayer).getCardsInHand().size() >= 2)
						{
							if (selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == true && selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-2] == false && x < 200 + 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-2)+cardBackImage.getWidth(this) && y >= this.getHeight() - avatars[0].getHeight(this)+cardBackImage.getHeight(this)+10 && y < this.getHeight() - avatars[0].getHeight(this)+20 +cardBackImage.getHeight(this))
							{
								selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-2] = true;//Lower condition
							}
							else if (selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == false && selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-2] == true && x < 200 + 40*(game.getPlayerList().get(activePlayer).getCardsInHand().size()-2)+cardBackImage.getWidth(this) && y >= this.getHeight() - avatars[0].getHeight(this) + 10 && y < this.getHeight() - avatars[0].getHeight(this)+20)
							{
								selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-2] = false;//Upper condition
							}
							else
							{
								if (selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == true && y >= this.getHeight() - avatars[0].getHeight(this) + 10 && y < this.getHeight() + 10 - avatars[0].getHeight(this)+cardBackImage.getHeight(this))
								{
									selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] = false;
								}
								else if (selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == false && y >= this.getHeight() - avatars[0].getHeight(this) + 20 && y < this.getHeight() - avatars[0].getHeight(this) + 20+cardBackImage.getHeight(this))
								{
									selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] = true;
								}
							}
						}
						else if (game.getPlayerList().get(activePlayer).getCardsInHand().size() == 1)
						{
							if (selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == true && y >= this.getHeight() - avatars[0].getHeight(this) + 10 && y < this.getHeight() + 10 - avatars[0].getHeight(this)+cardBackImage.getHeight(this))
							{
								selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] = false;
							}
							else if (selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] == false && y >= this.getHeight() - avatars[0].getHeight(this) + 20 && y < this.getHeight() - avatars[0].getHeight(this) + 20+cardBackImage.getHeight(this))
							{
								selected[game.getPlayerList().get(activePlayer).getCardsInHand().size()-1] = true;
							}
						}
					}
					else
					{
						for (int i = 0; i < 12; i++)
						{
							if (x >= 200+(40*i) && x < 200+(40*(i+1)))
							{
								if (selected[i] == true && y >= this.getHeight() - avatars[0].getHeight(this) + 10 && y < this.getHeight() +10 - avatars[0].getHeight(this)+cardBackImage.getHeight(this))
								{
									selected[i] = false;
								}
								else if (selected[i] == false && y >= this.getHeight() - avatars[0].getHeight(this) + 20 && y < this.getHeight() - avatars[0].getHeight(this) + 20+cardBackImage.getHeight(this))
								{
									selected[i] = true;
								}
	 						}
							else if (x >= 200+(40*i) && x < 200+(40*i)+cardBackImage.getWidth(this) && selected[i] == true && selected[i+1] == false && y >= this.getHeight() - avatars[0].getHeight(this) + 10 && y < this.getHeight() - avatars[0].getHeight(this)+20)
							{
								selected[i] = false;
							}
							else if (x >= 200+(40*i) && x < 200+(40*i)+cardBackImage.getWidth(this) && selected[i] == false && selected[i+1] == true && y >= this.getHeight() - avatars[0].getHeight(this)+cardBackImage.getHeight(this)+10 && y < this.getHeight() - avatars[0].getHeight(this)+20 +cardBackImage.getHeight(this))
							{
								selected[i] = true;
							}
						}
					}
				}
			}
			p0.repaint();
			p1.repaint();
			p2.repaint();
			p3.repaint();
			prev_hand.repaint();
		}

		/**
		 * Public function that executes when the mouse is pressed down.
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e)
		{
			return;
		}

		/**
		 * Public function that executes when the mouse is released.
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e)
		{
			return;
		}

		/**
		 * Public function that executes when the mouse enters a region.
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e)
		{
			return;
		}

		/**
		 * Public function that executes when a mouse exits a region.
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e)
		{
			return;
		}

		/**
		 * Public function that paints the panel and adds the player avatars and cards to the panel.
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			this.setBackground(Color.green.darker());
			this.setBorder(blackline);
			Font f = new Font("Arial", Font.BOLD, 20); //Creating the font.

			if (this == p0)
			{
				int x = this.getHeight() - avatars[0].getHeight(this);

				if (activePlayer == 0)
				{
					g.setColor(Color.blue);
				}

				if (game.getPlayerID() == 0)
				{
					g.setColor(Color.red);
				}

				g.setFont(f);
				g.drawString(game.getPlayerList().get(0).getName(), 60, (x-5));
				g.drawImage(avatars[0], 30, x, this);

				if (game.getPlayerID() == 0)
				{
					for (int i = 0; i < game.getPlayerList().get(0).getNumOfCards(); i++)
					{
						if (selected[i] == true)
						{
							g.drawImage(cardImages[game.getPlayerList().get(0).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(0).getCardsInHand().getCard(i).getRank()], 200 + (40*i), (x+10), this);
						}
						else
						{
							g.drawImage(cardImages[game.getPlayerList().get(0).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(0).getCardsInHand().getCard(i).getRank()], 200 + (40*i), (x+20), this);
						}
					}
				}

				else
				{
					for (int i = 0; i < game.getPlayerList().get(0).getNumOfCards(); i++)
					{
						g.drawImage(cardBackImage, 200 + (40*i), (x+20), this);
					}
				}

			}

			else if (this == p1)
			{
				int x = this.getHeight() - avatars[1].getHeight(this);

				if (activePlayer == 1)
				{
					g.setColor(Color.blue);
				}

				if (game.getPlayerID() == 1)
				{
					g.setColor(Color.red);
				}

				g.setFont(f);
				g.drawString(game.getPlayerList().get(1).getName(), 60, (x-5));
				g.drawImage(avatars[1], 30, x, this);

				if (game.getPlayerID() == 1)
				{
					for (int i = 0; i < game.getPlayerList().get(1).getNumOfCards(); i++)
					{
						if (selected[i] == true)
						{
							g.drawImage(cardImages[game.getPlayerList().get(1).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(1).getCardsInHand().getCard(i).getRank()], 200 + (40*i), (x+10), this);
						}

						else
						{
							g.drawImage(cardImages[game.getPlayerList().get(1).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(1).getCardsInHand().getCard(i).getRank()], 200 + (40*i), (x+20), this);
						}
					}
				}

				else
				{
					for (int i = 0; i < game.getPlayerList().get(1).getNumOfCards(); i++)
					{
						g.drawImage(cardBackImage, 200 + (40*i), (x+20), this);
					}
				}

			}

			else if (this == p2)
			{
				int x = this.getHeight() - avatars[2].getHeight(this);

				if (activePlayer == 2)
				{
					g.setColor(Color.blue);
				}

				if (game.getPlayerID() == 2)
				{
					g.setColor(Color.red);
				}

				g.setFont(f);
				g.drawString(game.getPlayerList().get(2).getName(), 60, (x-5));
				g.drawImage(avatars[2], 30, x, this);

				if (game.getPlayerID() == 2)
				{
					for (int i = 0; i < game.getPlayerList().get(2).getNumOfCards(); i++)
					{
						if (selected[i] == true)
						{
							g.drawImage(cardImages[game.getPlayerList().get(2).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(2).getCardsInHand().getCard(i).getRank()], 200 + (40*i), (x+10), this);
						}

						else
						{
							g.drawImage(cardImages[game.getPlayerList().get(2).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(2).getCardsInHand().getCard(i).getRank()], 200 + (40*i), (x+20), this);
						}
					}
				}

				else
				{
					for (int i = 0; i < game.getPlayerList().get(2).getNumOfCards(); i++)
					{
						g.drawImage(cardBackImage, 200 + (40*i), (x+20), this);
					}
				}

			}

			else if (this == p3)
			{
				int x = this.getHeight() - avatars[3].getHeight(this);

				if (activePlayer == 3)
				{
					g.setColor(Color.blue);
				}

				if (game.getPlayerID() == 3)
				{
					g.setColor(Color.red);
				}

				g.setFont(f);
				g.drawString(game.getPlayerList().get(3).getName(), 60, (x-5));
				g.drawImage(avatars[3], 30, x, this);

				if (game.getPlayerID() == 3)
				{
					for (int i = 0; i < game.getPlayerList().get(3).getNumOfCards(); i++)
					{
						if (selected[i] == true)
						{
							g.drawImage(cardImages[game.getPlayerList().get(3).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(3).getCardsInHand().getCard(i).getRank()], 200 + (40*i), (x+10), this);
						}

						else
						{
							g.drawImage(cardImages[game.getPlayerList().get(3).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(3).getCardsInHand().getCard(i).getRank()], 200 + (40*i), (x+20), this);
						}
					}
				}

				else
				{
					for (int i = 0; i < game.getPlayerList().get(3).getNumOfCards(); i++)
					{
						g.drawImage(cardBackImage, 200 + (40*i), (x+20), this);
					}
				}

			}

			else if (this == prev_hand)
			{
				if (game.getHandsOnTable().size() != 0)
				{
					String prev_player = game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName();
					int prev_idx = -1;

					for (int i = 0; i < game.getPlayerList().size(); i++)
					{
						if (game.getPlayerList().get(i).getName() == prev_player)
						{
							prev_idx = i;
						}
					}

					int x = this.getHeight() - avatars[0].getHeight(this);
					g.setFont(f);
					for (int i = 0; i < game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).size(); i++)
					{
						g.drawString("Played by " + prev_player, 30, (x-5));
						g.drawImage(avatars[prev_idx], 30, x, this);
						g.drawImage(cardImages[game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(i).getSuit()][game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(i).getRank()], 200 + (40*i), (x+20), this);
					}
				}
			}

		}
	}

	/**
	 * Class that creates a listener for the Play Button.
	 *
	 * @author Aryaman Dubey
	 *
	 */
	class PlayButtonListener implements ActionListener
	{

		/**
		 * Public function that makes a move on clicking the Play button.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (getSelected() == null)
			{
				printMsg("Select cards to play.\n");
			}
			else
			{
				game.makeMove(activePlayer, getSelected());
			}
		}

	}

	/**
	 * Class that creates a listener for the Pass Button.
	 *
	 * @author Aryaman Dubey
	 *
	 */
	class ButtonListener implements ActionListener
	{

		/**
		 * Public function that passes a move.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			game.makeMove(activePlayer, null);
		}

	}

	/**
	 * Class that creates a listener for the text message input area.
	 *
	 * @author Aryaman Dubey
	 *
	 */
	class TextFieldListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			game.sendMessage(new CardGameMessage(7, -1, tf.getText()));
			tf.setText(null);
		}
	}
	/**
	 * Class that creates a listener for the Connect Button.
	 *
	 * @author Aryaman Dubey
	 *
	 */
	class ConnectMenuItemListener implements ActionListener
	{

		/**
		 * Public function that connects a client to the game server.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (game.getPlayerID() == -1)
			{
				clearMsgArea();
				game.makeConnection();
			}
			else
			{
				printMsg("You're already in the game.\n");
			}
			/*
			BigTwoDeck ndeck = new BigTwoDeck();
			ndeck.shuffle();
			game.start(ndeck);
			*/

		}

	}

	/**
	 * Class that creates a listener for the Quit Button.
	 * @author Aryaman Dubey
	 *
	 */
	class QuitMenuItemListener implements ActionListener
	{

		/**
		 * Public function that quits from the BigTwo game.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(activePlayer);
		}
	}

	/**
	 * Class that creates a listener for the Clear Button.
	 *
	 * @author Aryaman Dubey
	 *
	 */
	class ClearMenuItemListener implements ActionListener
	{

		/**
		 * Public function that clears the chat messages.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			chatArea.setText("");
		}

	}

}
