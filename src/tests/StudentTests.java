package tests;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import blackjack.Blackjack;
import blackjack.Card;


/**
 * Please put your own test cases into this file, so they can be tested
 * on the server.
*/

public class StudentTests {
	@Test
	public void pubTest1DeckCreation() {
		Blackjack blackjack = setBlackJack();
		blackjack.createAndShuffleGameDeck();
		Card[] deck = blackjack.getGameDeck();
		
	}
	
	@Test
	public void studentTest1() {
		Random randomGenerator = new Random(1234567L);
		Blackjack blackjack = new Blackjack(randomGenerator, 4);
		assertEquals(blackjack.getNumberOfDecks(),4);

		
	}
	
	/**** Private methods ****/
	private static Blackjack setBlackJack() {
		Random randomGenerator = new Random(1234567L);
		int numberOfDecks = 1;
		Blackjack blackjack = new Blackjack(randomGenerator, numberOfDecks);
	    return blackjack;
	}
	
	private static String getCardsString(Card[] array) {
		String result = "";
		for (int i=0; i<array.length; i++) {
			result += array[i] + "\n";
		}
		return result;
	}
	
}