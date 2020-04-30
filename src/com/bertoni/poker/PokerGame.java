package com.bertoni.poker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author fascarza
 * Mini proyecto como parte de la parte técnica del proceso de selección Bertoni (Senior Java Developer)
 */
public class PokerGame {
	public static final long ROYAL_FLUSH_MASK = cardCode("TH") | cardCode("JH") | cardCode("QH") | cardCode("KH")
			| cardCode("AH");
	public static final long FLUSH_MASK = cardCode("2H") | cardCode("3H") | cardCode("4H") | cardCode("5H")
			| cardCode("6H");
	public static final long FOUR_OF_A_KIND = cardCode("2H") | cardCode("2C") | cardCode("2S") | cardCode("2D");

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// File input = new File("pokerdata.txt");
		File input = new File(PokerGame.class.getClass().getResource("/com/bertoni/poker/pokerdata.txt").getFile());

		assert (input.canRead());
		int winsPlayer1 = 0;
		int winsPlayer2 = 0;
		int winsNeitherPlayers = 0;
		BufferedReader in = new BufferedReader(new FileReader(input));
		String line;

		while (null != (line = in.readLine())) {
			long hands[] = parseHands(line);

			switch (winner(hands)) {
			case 1:
				winsPlayer1++;
				break;
			case 2:
				winsPlayer2++;
				break;
			case 0:
				winsNeitherPlayers++;
				break;
			}

		}

		in.close();

		System.out.println("Player 1 won: " + winsPlayer1 + " hands");
		System.out.println("Player 2 won: " + winsPlayer2 + " hands");
		System.out.println(winsNeitherPlayers + " hands neither player won");
	}

	public static long[] parseHands(String line) {
		String[] cards = line.split(" ");
		long[] r = new long[2];

		for (int card = 0; card < 10; card++) {
			r[card / 5] |= cardCode(cards[card]);
		}

		return r;
	}

	public static int winner(long[] hands) {
		boolean b2 = royalFlush(hands[1]);
		boolean b1 = royalFlush(hands[0]);
		int v1, v2;

		if (b1 && !b2) {
			return 1;
		} else if (b2 && !b1) {
			return 2;
		} else if (b1 & b2) {
			return 0;
		}

		v1 = straightFlush(hands[0]);
		v2 = straightFlush(hands[1]);

		if (v1 + v2 != 0) {
			return whoWins(v1, v2, hands);
		}

		v1 = fourOfAKind(hands[0]);
		v2 = fourOfAKind(hands[1]);

		if (v1 + v2 != 0) {
			return whoWins(v1, v2, hands);
		}

		v1 = fullHouse(hands[0]);
		v2 = fullHouse(hands[1]);

		if (v1 + v2 != 0) {
			return whoWins(v1, v2, hands);
		}

		v1 = flush(hands[0]);
		v2 = flush(hands[1]);

		if (v1 + v2 != 0) {
			return whoWins(v1, v2, hands);
		}

		v1 = straight(hands[0]);
		v2 = straight(hands[1]);

		if (v1 + v2 != 0) {
			return whoWins(v1, v2, hands);
		}

		v1 = threeOfAKind(hands[0]);
		v2 = threeOfAKind(hands[1]);

		if (v1 + v2 != 0) {
			return whoWins(v1, v2, hands);
		}

		v1 = twoPairs(hands[0]);
		v2 = twoPairs(hands[1]);

		if (v1 + v2 != 0) {
			return whoWins(v1, v2, hands);
		}

		v1 = onePair(hands[0]);
		v2 = onePair(hands[1]);

		if (v1 + v2 != 0) {
			return whoWins(v1, v2, hands);
		}

		return playerWithHigherCards(hands);
	}

	public static long cardCode(String card) {
		String suitTexts = "HCSD";
		int suitCode = suitTexts.indexOf(card.charAt(1));
		final char C = card.charAt(0);
		int valueCode = "TJQKA".indexOf(C);
		int value = (valueCode == -1) ? (C - '0') - 2 : 8 + valueCode;

		return 1L << (4 * value + suitCode);
	}

	public static boolean royalFlush(long hand) {
		return hand == ROYAL_FLUSH_MASK || hand == (ROYAL_FLUSH_MASK << 1) || hand == (ROYAL_FLUSH_MASK << 2)
				|| hand == (ROYAL_FLUSH_MASK << 3);
	}

	public static int straightFlush(long hand) {
		int value = 2;

		while (0 == (hand & 15)) {
			hand >>= 4;
			value++;
		}

		while (0 == (hand & 1)) {
			hand >>= 1;
		}

		return hand == FLUSH_MASK ? value : 0;
	}

	public static int fourOfAKind(long hand) {
		long mask = FOUR_OF_A_KIND;

		for (int value = 2; value <= 14; value++) {
			if (mask == (hand & mask)) {
				return value;
			}

			mask <<= 4;
		}

		return 0;
	}

	public static int fullHouse(long hand) {
		int value3 = 0;
		int value2 = 0;
		int currentValue = 2;

		for (int i = 0; i < 2; i++) {
			while ((hand & 15) == 0) {
				currentValue++;
				hand >>= 4;
			}

			switch (Long.bitCount(hand & 15)) {
			case 2:
				value2 = currentValue;
				break;
			case 3:
				value3 = currentValue;
				break;
			default:
				return 0;
			}

			currentValue++;
			hand >>= 4;
		}

		if (value3 == 0 || value2 == 0) {
			return 0;
		}

		return 100 * value3 + value2;
	}

	public static int threeOfAKind(long hand) {
		for (int value = 2; value <= 14; value++) {
			long slot = hand >> 4 * (value - 2) & 15;

			if (slot == 0b0111 || slot == 0b1011 || slot == 0b1101 || slot == 0b1110) {
				return value;
			}
		}

		return 0;
	}

	public static int twoPairs(long hand) {
		int pairs = 0;
		int resultValue = 0;

		for (int value = 14; value >= 2; value--) {
			long slot = hand >> 4 * (value - 2) & 15;

			if (Long.bitCount(slot) == 2) {
				resultValue = resultValue * 100 + value;
				pairs++;
			}
		}

		return pairs == 2 ? resultValue : 0;
	}

	public static int flush(long hand) {
		int result = 0;
		int pot = 1;
		long check = 0;

		for (int value = 2; value <= 14; value++) {
			long slot = hand >> 4 * (value - 2) & 15;

			if (slot != 0) {
				result += value * pot;
				check = (check << 4) | slot;
				pot *= 16;
			}
		}

		while ((check & 1) == 0) {
			check >>= 1;
		}

		return (check == 0b00010001000100010001) ? result : 0;
	}

	public static int straight(long hand) {
		int value = 2;
		long slot;

		while (0 == (hand >> 4 * (value - 2) & 15)) {
			value++;
		}

		for (int i = 2; i <= 5; i++) {
			value++;
			slot = hand >> 4 * (value - 2) & 15;

			if (slot == 0) {
				return 0;
			}
		}

		return value;
	}

	public static int onePair(long hand) {
		if (threeOfAKind(hand) != 0 || twoPairs(hand) != 0) {
			return 0;
		}

		for (int value = 14; value >= 2; value--) {
			long slot = hand >> 4 * (value - 2) & 15;

			if (Long.bitCount(slot) == 2) {
				return value;
			}
		}

		return 0;
	}

	public static int highCard(long hand) {
		int result = 0;

		for (int value = 14; value >= 2; value--) {
			long slot = (hand >> 4 * (value - 2)) & 15;

			if (slot > 0) {
				result = result * 16 + value;
			}
		}

		return result;
	}

	public static int playerWithHigherCards(long[] hands) {
		switch (Integer.compare(highCard(hands[0]), highCard(hands[1]))) {
		case -1:
			return 2;
		case 1:
			return 1;
		default:
			return 0;
		}
	}

	public static int whoWins(int v1, int v2, long[] hands) {
		if (v1 > v2) {
			return 1;
		} else if (v1 < v2) {
			return 2;
		} else {
			return playerWithHigherCards(hands);
		}
	}
}