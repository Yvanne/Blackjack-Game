package blackjack;

import java.util.*;

public class Blackjack implements BlackjackEngine {

	/**
	 * Constructor you must provide. Initializes the player's account to 200 and the
	 * initial bet to 5. Feel free to initialize any other fields. Keep in mind that
	 * the constructor does not define the deck(s) of cards.
	 * 
	 * @param randomGenerator
	 * @param numberOfDecks
	 */

	private Random randomGenerator;
	private int numberOfDecks, playerAccount, initialBet, playerResult, dealerResult, gameUpdate;
	private Card card, c;
	private List<Card> deck;
	private List<Card> playerCards;
	private List<Card> dealerCards;
	private Card[] currentDeck, dealerDeck, playerDeck;
	private int[] dealerTotal, dealerTotal1, dealerTotal2, newDealerTotal, playerTotal, playerTotal1, playerTotal2,
			newPlayerTotal;
	private boolean lessSixteen;

	// constructor for Blackjack class
	public Blackjack(Random randomGenerator, int numberOfDecks) {
		this.randomGenerator = randomGenerator;
		this.numberOfDecks = numberOfDecks;
		this.playerAccount = 200;
		this.initialBet = 5;
	}

	// returns the dumber of decks
	public int getNumberOfDecks() {
		return numberOfDecks;
	}

	public void createAndShuffleGameDeck() {
		deck = new ArrayList<>();

		// goes through each suit
		for (CardSuit s : CardSuit.values()) {
			// goes through each enum value
			for (CardValue v : CardValue.values()) {
				deck.add(new Card(v, s));
			}
		}
		// shuffles deck of cards randomly
		Collections.shuffle(deck, randomGenerator);
	}

	public Card[] getGameDeck() {
		currentDeck = new Card[deck.size()];

		for (int i = 0; i < deck.size(); i++) {
			currentDeck[i] = deck.get(i);
		}
		return currentDeck;
	}

	public void deal() {
		playerCards = new ArrayList<>();
		dealerCards = new ArrayList<>();

		createAndShuffleGameDeck();

		// deals first deck to player
		playerCards.add(deck.get(0));
		deck.remove(0);

		// deals second deck to dealer face down
		c = deck.get(0);
		c.setFaceDown();
		dealerCards.add(c);
		deck.remove(0);

		// deals third deck to player
		playerCards.add(deck.get(0));
		deck.remove(0);

		// deals fourth deck to dealer
		dealerCards.add(deck.get(0));
		deck.remove(0);

		// delete bet amount
		playerAccount -= initialBet;

		// game status
		gameUpdate = BlackjackEngine.GAME_IN_PROGRESS;
	}

	public Card[] getDealerCards() {
		// puts dealer cards into an array
		dealerDeck = new Card[dealerCards.size()];

		for (int i = 0; i < dealerCards.size(); i++) {
			dealerDeck[i] = dealerCards.get(i);
		}
		return dealerDeck;
	}

	// dealer's card total
	public int[] getDealerCardsTotal() {
		int sum = 0;
		int aceCount = 0;
		lessSixteen = false;
		dealerTotal = new int[dealerCards.size()];
		dealerTotal1 = new int[playerCards.size()];

		for (int i = 0; i < dealerCards.size(); i++) {
			if ((dealerCards.get(i).getValue().getIntValue()) == 1) {
				aceCount += (dealerCards.get(i).getValue().getIntValue());
			} else {
				sum += (dealerCards.get(i).getValue().getIntValue());
			}
		}
		// if there are no aces
		if (aceCount == 0) {
			dealerTotal = new int[1];

			dealerTotal[0] = sum;

			if (dealerTotal[0] < 16) {
				lessSixteen = true;
			}
			if (dealerTotal[0] >= 16 && dealerTotal[0] < 21) {
				lessSixteen = false;
			}
			if (dealerTotal[0] == 21) {
				lessSixteen = false;
			}
			if (dealerTotal[0] > 21) {
				return null;
			} else
				return dealerTotal;
		}
		// if there is an ace or more
		else {
			dealerTotal1[0] = sum + aceCount;
			dealerTotal1[1] = sum + aceCount * 11;

			if (dealerTotal1[1] < 16) {
				lessSixteen = true;
			}
			if (dealerTotal1[1] >= 16 && dealerTotal1[1] < 21) {
				lessSixteen = false;
			}
			if (dealerTotal1[1] == 21) {
				lessSixteen = false;
			}

			// if both dealer cards are over 21
			if (dealerTotal1[0] > 21 && dealerTotal1[1] > 21) {
				return null;
			}

			// if one dealer card is over 21
			if (dealerTotal1[1] > 21 && dealerTotal1[0] <= 21) {
				dealerTotal2 = new int[1];
				dealerTotal2[0] = sum + aceCount;
				return dealerTotal2;
			}
			return dealerTotal1;

		}
	}

	// evaluate dealer's cards.
	public int getDealerCardsEvaluation() {
		newDealerTotal = getDealerCardsTotal();
		dealerResult = BlackjackEngine.LESS_THAN_21;

		if (newDealerTotal == null)
			return dealerResult = BlackjackEngine.BUST;

		// if the array has no Ace
		if (newDealerTotal.length == 1) {
			if (newDealerTotal[0] < 21) {
				return dealerResult = BlackjackEngine.LESS_THAN_21;
			}

			if (newDealerTotal[0] > 21) {
				return dealerResult = BlackjackEngine.BUST;
			}
			if (newDealerTotal[0] == 21) {
				return dealerResult = BlackjackEngine.HAS_21;
			}

		}

		// if the array has an Ace
		if (newDealerTotal.length == 2) {

			if (newDealerTotal[1] < 21) {
				return dealerResult = BlackjackEngine.LESS_THAN_21;
			}
			if (newDealerTotal[1] > 21) {
				return dealerResult = BlackjackEngine.BUST;
			}
			if (newDealerTotal[1] == 21) {
				return dealerResult = BlackjackEngine.BLACKJACK;
			}
		}
		return dealerResult;
	}

	public Card[] getPlayerCards() {
		// puts player cards into an array
		playerDeck = new Card[playerCards.size()];
		for (int i = 0; i < playerCards.size(); i++) {
			playerDeck[i] = playerCards.get(i);
		}
		return playerDeck;
	}

	public int[] getPlayerCardsTotal() {
		int sum = 0;
		int aceCount = 0;
		playerTotal = new int[playerCards.size()];
		playerTotal1 = new int[playerCards.size()];

		for (int i = 0; i < playerCards.size(); i++) {
			if ((playerCards.get(i).getValue().getIntValue()) == 1) {
				aceCount += (playerCards.get(i).getValue().getIntValue());
			} else {
				sum += (playerCards.get(i).getValue().getIntValue());
			}
		}

		// if player does not have an ace
		if (aceCount == 0) {
			playerTotal = new int[1];
			if (sum <= 21) {
				playerTotal[0] = sum;
				return playerTotal;
			} else
				return null;
		}

		// if player has an ace
		else {
			playerTotal1[0] = sum + aceCount;
			playerTotal1[1] = sum + aceCount * 11;

			// if player has more than 21
			if (playerTotal1[0] > 21 && playerTotal1[1] > 21) {
				return null;
			}
			// if one hand is over 21
			if (playerTotal1[1] > 21 && playerTotal1[0] <= 21) {
				playerTotal2 = new int[1];
				playerTotal2[0] = sum + aceCount;
				return playerTotal2;
			}
			return playerTotal1;

		}
	}

	public int getPlayerCardsEvaluation() {
		newPlayerTotal = getPlayerCardsTotal();
		playerResult = BlackjackEngine.LESS_THAN_21;

		if (getPlayerCardsTotal() == null)
			return playerResult = BlackjackEngine.BUST;

		// if the array has no Ace
		if (newPlayerTotal.length == 1) {
			if (newPlayerTotal[0] < 21) {
				return playerResult = BlackjackEngine.LESS_THAN_21;
			}
			if (newPlayerTotal[0] > 21) {
				return playerResult = BlackjackEngine.BUST;
			}
			if (newPlayerTotal[0] == 21) {
				return playerResult = BlackjackEngine.HAS_21;
			}
		}
		// if the array has an Ace
		if (newPlayerTotal.length == 2) {

			if (newPlayerTotal[1] < 21) {
				return playerResult = BlackjackEngine.LESS_THAN_21;
			}
			if (newPlayerTotal[1] > 21) {
				return playerResult = BlackjackEngine.BUST;
			}
			if (newPlayerTotal[1] == 21) {
				return playerResult = BlackjackEngine.BLACKJACK;
			}
		}
		return playerResult;
	}

	public void playerHit() {
		// assign a new card to the player
		playerCards.add(deck.get(0));
		deck.remove(0);
		getPlayerCardsEvaluation();

		if (playerResult == BlackjackEngine.BUST) {
			gameUpdate = BlackjackEngine.DEALER_WON;
		} else {
			gameUpdate = BlackjackEngine.GAME_IN_PROGRESS;
		}

	}

	public void playerStand() {
		// sets dealer card faced up
		card = dealerCards.get(0);
		card.setFaceUp();
		getDealerCardsEvaluation();
		getPlayerCardsEvaluation();

		// when dealer has cards totaling less than sixteen
		while (lessSixteen == true) {
			dealerCards.add(deck.get(0));
			deck.remove(0);
			getDealerCardsEvaluation();
			if (dealerResult == BlackjackEngine.BUST) {
				gameUpdate = BlackjackEngine.PLAYER_WON;
			}
		}

		// if both the dealer and player total do not return null, check which has the
		// highest value
		if (this.newDealerTotal != null && this.newPlayerTotal != null) {

			if (this.newDealerTotal[newDealerTotal.length - 1] > this.newPlayerTotal[newPlayerTotal.length - 1]) {
				gameUpdate = BlackjackEngine.DEALER_WON;
			} else if (this.newDealerTotal[newDealerTotal.length - 1] < this.newPlayerTotal[newPlayerTotal.length
					- 1]) {
				gameUpdate = BlackjackEngine.PLAYER_WON;
				playerAccount += (initialBet * 2);
			} else if (this.newDealerTotal[newDealerTotal.length - 1] == this.newPlayerTotal[newPlayerTotal.length
					- 1]) {
				gameUpdate = BlackjackEngine.DRAW;
				playerAccount += initialBet;
			}

		}

	}

	public int getGameStatus() {
		return gameUpdate;
	}

	public void setBetAmount(int amount) {
		initialBet = amount;
	}

	public int getBetAmount() {
		return initialBet;
	}

	public void setAccountAmount(int amount) {
		playerAccount = amount;
	}

	public int getAccountAmount() {
		return playerAccount;
	}

	/* Feel Free to add any private methods you might need */
}