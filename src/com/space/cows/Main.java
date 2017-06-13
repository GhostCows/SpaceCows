package com.space.cows;

import java.util.*;

/**
 * Created by Lhaion on 07/06/2017.
 */
public class Main {

	private static final int SIZE = 7;

	private static final Random random = new Random();

	public static void main(String[] str) {

		int[][] prevs = new int[2][2];

		prevs[0] = new int[]{-1, -1};
		prevs[1] = new int[]{-1, -1};

		Scanner scanner = new Scanner(System.in);

		int[][] board = new int[SIZE][SIZE];

		boolean yourTurn = true;

		System.out.println("Qual vai ser a ordem de jogo? (2 dígitos)\n[p][j] jogador\n[c][a] computador");

		String gamemode = scanner.next().toUpperCase().substring(0, 2);

		int difficulty = 0;

		if (gamemode.matches(".*[CA].*")) {

			System.out.println("What difficulty would you want? (0-2)");

			difficulty = scanner.nextInt() % 3;

		}

		do {

			printBoard(board);

			println("Placar:");
			int[] points = points(board);
			println("# - " + points[0]);
			println("@ - " + points[1]);

			if (yourTurn) {
				println("É a sua vez!");
			} else {
				println("É a vez do adversário!");
			}

			int turn = yourTurn ? 0 : 1;

			int[] move = new int[2];

			Character gameTurn = gamemode.charAt(turn);

			if (gameTurn.equals('C') || gameTurn.equals('A')) {

				println("Onde você vai jogar?");

				sleep(random.nextInt(2000) + 500);

				move = aiTurn(board, yourTurn, difficulty, prevs);

				System.out.println(String.valueOf(move[0] + 1) + ((char) (65 + move[1])));

			} else {

				println("Onde você vai jogar?");

				String pos = scanner.next().toUpperCase();

				char c1 = pos.charAt(0);
				char c2 = pos.charAt(1);

				if (Character.isDigit(c1)) {

					move[0] = Character.getNumericValue(c1) - 1;
					move[1] = ((int) c2) - 65;

				} else {

					move[0] = Character.getNumericValue(c2) - 1;
					move[1] = ((int) c1) - 65;

				}

			}

			if (play(move, board, yourTurn, prevs)) {

				yourTurn = !yourTurn;

			}

		} while (!gameEnd(board));

		if (!yourTurn) {
			println("Você Ganhou! Parabéns");
		} else {
			println("Que pena, você perdeu...");
		}

	}

	private static boolean play(int[] move, int[][] board, boolean yourTurn, int[][] prevs) {

		boolean valid = true;

		int x = move[0];
		int y = move[1];

		int index = yourTurn ? 0 : 1;

		if (board[x][y] != 0) {

			println("Você só pode jogar em slots vazios!");

			valid = false;

		} else if (Arrays.equals(prevs[index], move)) {

			println("Você não pode jogar duas vezes no mesmo lugar");

			valid = false;

		} else {

			prevs[index] = move;

			for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, SIZE - 1); i++) {
				for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, SIZE - 1); j++) {

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


		return valid;

	}

	private static int[] aiTurn(int[][] board, boolean yourTurn, int difficulty, int[][] prevs) {

		int[] move;

		int[][][] analizedBoard = analizeBoard(board, yourTurn);

		Integer maxProfitValue = Integer.MIN_VALUE;
		ArrayList<Integer[]> maxProfitIds = new ArrayList<>();

		int maxAdvantageValue = Integer.MIN_VALUE;
		ArrayList<Integer[]> maxAdvantageIds = new ArrayList<>();

		int maxBestValue = Integer.MIN_VALUE;
		ArrayList<Integer[]> maxBestIds = new ArrayList<>();

		for (int i = 0; i < SIZE; i++) {
			int[][] line = analizedBoard[i];

			for (int j = 0; j < SIZE; j++) {
				int[] column = line[j];

				Integer[] pos = new Integer[2];
				pos[0] = i;
				pos[1] = j;

				if (!contains(toPrimitive(pos), prevs)) {

					for (int k = 0; k < 2; k++) {

						int value = column[k];

						ArrayList<Integer[]> maxIds;
						int maxValue;

						if (k == 0) {
							maxIds = maxProfitIds;
							maxValue = maxProfitValue;
						} else {
							maxIds = maxAdvantageIds;
							maxValue = maxAdvantageValue;
						}

						if (value > maxValue) {

							if (k == 0) {
								maxProfitValue = value;
							} else {
								maxAdvantageValue = value;
							}

							maxIds.clear();
							maxIds.add(pos);

						} else if (value == maxValue) {

							maxIds.add(pos);

						}
					}

					int value = column[0] + column[1] / (3 - difficulty);

					if (value > maxBestValue) {

						maxBestValue = value;

						maxBestIds.clear();
						maxBestIds.add(pos);

					} else if (value == maxBestValue) {

						maxBestIds.add(pos);

					}
				}
			}
		}

		int[] points = points(board);
		int turn = yourTurn ? 1 : 0;
		int win = (int) Math.ceil(SIZE * SIZE / 2);

		if (difficulty > 0 &&
				points[turn] + maxProfitValue >= win) {

			move = randomID(maxProfitIds);

		} else if (difficulty > 1 &&
				points[1 - turn] + Math.ceil(SIZE / 3) - 1 >= win) {

			maxProfitIds.clear();
			maxProfitValue = Integer.MIN_VALUE;

			for (Integer[] advantage : maxAdvantageIds) {

				int x = advantage[0];
				int y = advantage[1];

				int profit = analizedBoard[x][y][0];

				if(profit > maxProfitValue) {

					maxProfitValue = profit;

					maxProfitIds.clear();
					maxProfitIds.add(advantage);

				} else if(profit == maxProfitValue) {

					maxProfitIds.add(advantage);

				}

			}

			move = randomID(maxProfitIds);

		} else {

			move = randomID(maxBestIds);

		}

		return move;

	}

	private static int[][][] analizeBoard(int[][] board, boolean yourTurn) {

		int[][][] moves = new int[SIZE][SIZE][2];

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {

				if (board[i][j] == 0) {

					moves[i][j] = analizeMove(i, j, board, yourTurn);

				}
			}
		}

		return moves;

	}

	/**
	 * @return moves
	 * 0 - profit
	 * 1 - advantage
	 */

	private static int[] analizeMove(int x, int y, int[][] board, boolean yourTurn) {

		int[] move = new int[2];

		for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, SIZE - 1); i++) {
			for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, SIZE - 1); j++) {

				int value = board[i][j];

				int turn = yourTurn ? 1 : 0;

				int index = (int) (Math.signum(value) * turn * Math.abs(value + turn - 2));

				move[index] += turn + value == 2 ? -1 : 1;

			}

		}

		return move;

	}

	private static void sleep(int ms) {

		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void println(String msg) {

		char[] chars = msg.toCharArray();

		for (char aChar : chars) {
			System.out.print(aChar);
			sleep(10);
		}

		System.out.print('\n');

	}

	private static void printBoard(int[][] board) {

		StringBuilder top = new StringBuilder("  ");
		StringBuilder line = new StringBuilder(" +");

		for (int i = 1; i <= SIZE; i++) {

			top.append(" ").append(i);
			line.append("--");

		}

		System.out.println(top);
		System.out.println(line);

		for (int i = 0; i < SIZE; i++) {

			System.out.print(((char) (i + 65)) + "| ");

			for (int j = 0; j < SIZE; j++) {

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

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int value = board[i][j];
				if (value == 1) {
					p1++;
				} else if (value == 2) {
					p2++;
				}
			}
		}

		int win = (int) Math.ceil(SIZE * SIZE / 2);

		return p1 >= win || p2 >= win;

	}

	private static int[] points(int[][] board) {
		int[] points = new int[2];

		for (int[] line : board) {
			for (int column : line) {

				if (column != 0) {
					points[column - 1]++;
				}

			}
		}

		return points;

	}

	private static int[] randomID(ArrayList<Integer[]> arrayList) {

		return toPrimitive(arrayList.get(random.nextInt(arrayList.size())));

	}

	private static int[] toPrimitive(Integer[] array) {
		return Arrays.stream(array).mapToInt(Integer::intValue).toArray();
	}

	private static boolean contains(int[] array, int[][] matrix) {
		boolean contains = false;
		int i = 0;
		while (!contains && i < 2) {
			contains = Arrays.equals(matrix[i], array);
			i++;
		}
		return contains;
	}

}
