package com.bertoni.poker;

import org.junit.Test;
import static org.junit.Assert.*;

public class PokerGameTest {
	
	public void cardCodeTest() {
		String cardCode = "8C";
		long expectedResult = 33554432l;
		long testResult = PokerGame.cardCode(cardCode);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void parseHandsTest() {
		String line = "8C TS KC 9H 4S 7D 2S 5D 3S AC";
		long[] testResult = PokerGame.parseHands(line);
		long[] expectedResults = new long[] {35201853948928l, 562949961842756l};
		assertArrayEquals(expectedResults, testResult);
	}
	
	@Test
	public void royalFlushTest() {
		long hand = 562949961842756l;
		boolean expectedResult = false;
		boolean testResult = PokerGame.royalFlush(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void straightFlushTest() {
		long hand = 562949961842756l;
		int expectedResult = 0;
		int testResult = PokerGame.straightFlush(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void fourOfAKindTest() {
		long hand = 562949961842756l;
		int expectedResult = 0;
		int testResult = PokerGame.fourOfAKind(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void fullHouseTest() {
		long hand = 562949961842756l;
		int expectedResult = 0;
		int testResult = PokerGame.fullHouse(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void flushTest() {
		long hand = 562949961842756l;
		int expectedResult = 0;
		int testResult = PokerGame.flush(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void straightTest() {
		long hand = 562949961842756l;
		int expectedResult = 0;
		int testResult = PokerGame.flush(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void threeOfAKindTest() {
		long hand = 562949961842756l;
		int expectedResult = 0;
		int testResult = PokerGame.threeOfAKind(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void twoPairsTest() {
		long hand = 562949961842756l;
		int expectedResult = 0;
		int testResult = PokerGame.twoPairs(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void onePairTest() {
		long hand = 562949961842756l;
		int expectedResult = 0;
		int testResult = PokerGame.onePair(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void highCardTest() {
		long hand = 562949961842756l;
		int expectedResult = 947506;
		int testResult = PokerGame.highCard(hand);
		assertEquals(expectedResult, testResult);
	}
	
	@Test
	public void playerWithHigherCardsTest() {
		long[] hands = new long[]{35201853948928l, 562949961842756l};
		int testResult = PokerGame.playerWithHigherCards(hands);
		int expectedResults = 2;
		assertEquals(expectedResults, testResult);
	}
	
	@Test
	public void whoWinsTest() {
		int v1 = 1405;
		int v2 = 0;
		long[] hands = new long[]{2814750304018432l, 70403240235008l};
		int testResult = PokerGame.whoWins(v1, v2, hands);
		int expectedResults = 1;
		assertEquals(expectedResults, testResult);
	}
	
	
	
	

}
