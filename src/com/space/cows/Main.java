package com.space.cows;

import java.util.Scanner;

/**
 * Created by Lhaion on 07/06/2017.
 */
public class Main {

	private static String[] prevs = new String[2];

	public static void main(String[] str) {

		prevs[0] = "";
		prevs[1] = "";

		Scanner scanner = new Scanner(System.in);

		int[][] board = new int[7][7];

		boolean yourTurn = true;

		do {

			if (yourTurn) {
				println("É a sua vez!");
			} else {
				println("É a vez do adversário!");
			}

			printBoard(board);

			println("Onde você vai jogar?");

			String pos = scanner.nextLine();

			if (play(pos, board, yourTurn)) {

				yourTurn = !yourTurn;

			}

		} while (!gameEnd(board));

		if (!yourTurn) {
			println("Você Ganhou! Parabéns");
		} else {
			println("Que pena, você perdeu...");
		}

	}

	private static boolean play(String num, int[][] board, boolean yourTurn) {

		num = num.toUpperCase();

		boolean valid = true;

		if (num.length() != 2) {
			println("O número deve conter dois dígitos!");
		} else {

			//‒
			//—

			int x, y;

			char c1 = num.charAt(0);
			char c2 = num.charAt(1);

			if(Character.isDigit(c1)) {

				y = Character.getNumericValue(c1) - 1;
				x = ((int) c2) - 65;

			} else {

				x = Character.getNumericValue(c2) - 1;
				y = ((int) c1) - 65;

			}

			System.out.println(x);
			System.out.println(y);

			int index = yourTurn ? 0 : 1;

			if (board[x][y] != 0) {

				println("Você só pode jogar em slots vazios!");

				valid = false;

			} else if (prevs[index].equals(num)) {

				println("Você não pode jogar duas vezes no mesmo lugar");

				valid = false;

			} else {

				prevs[index] = num;

				for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, 6); i++) {
					for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, 6); j++) {

						if (board[i][j] == 0) {
							if (yourTurn) {
								board[i][j] = 1;
							} else {
								board[i][j] = 2;
							}
						} else {
							board[i][j] = 0;
						}
					}
				}

			}
		}

		return valid;

	}

    /*private static String aiTurn(int[][] board) {



    }

    private static int[] analizePlay(int x, int y) {



    }*/

	private static void sleep(int ms) {

		long time = System.currentTimeMillis();

		while (time + ms > System.currentTimeMillis()) ;

	}

	private static void println(String msg) {

		char[] chars = msg.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			System.out.print(chars[i]);
			sleep(50);
		}

		System.out.print('\n');

	}

	private static void printBoard(int[][] board) {

		System.out.println("   1 2 3 4 5 6 7");
		System.out.println(" +--------------");

		for (int i = 0; i < board.length; i++) {

			System.out.print(((char) (i + 65)) + "| ");

			for (int j = 0; j < board[i].length; j++) {

				switch (board[j][i]) {
					case 0:
						System.out.print('º');
						break;
					case 1:
						System.out.print('#');
						break;
					case 2:
						System.out.print('@');
				}
				System.out.print(' ');
				sleep(10);

			}
			System.out.print("\n");
		}
	}

	private static boolean gameEnd(int[][] board) {

		int p1 = 0;
		int p2 = 0;

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				int value = board[i][j];
				if (value == 1) {
					p1++;
				} else if (value == 2) {
					p2++;
				}
			}
		}

		return p1 >= 25 || p2 >= 25;

	}

}
