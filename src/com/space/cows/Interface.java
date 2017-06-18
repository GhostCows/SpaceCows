package com.space.cows;

import br.senai.sc.engine.Game;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static java.awt.Color.*;

/**
 * Leonardo Antunes
 * Leonardo Vignatti
 * Lhaion Alexandre
 * Marcelo Vogt
 */
public class Interface extends Game {

	//<editor-fold desc="Contructors">
	public static void main(String[] args) {
		Interface interf = new Interface();
		interf.startGame();
	}

	public Interface() {
		super("TSIFOC", 1040, 650);
		addMouseListener(new MouseInputHandler());
		addMouseMotionListener(new MouseInputHandler());
		addKeyListener(new KeyInputHandler());
	}
	//</editor-fold>

	//<editor-fold desc="Variable Declarations">
	private int width = 1040;
	private int height = 650;
	private int length = 9;
	private int marginTopBottom = 31;
	private int boardSize = height - 2 * marginTopBottom;
	private int marginLeftRight = (width - boardSize) / 2;
	private int slotSize = boardSize / length;
	private int win = (int) Math.ceil(length * length / 2);

	/**
	 * Variable that defines in what screen you are in.
	 * -1 Splash Screen
	 * 0 Title Screen
	 * 1 Options Screen
	 * 2 Test Screen
	 * 3 Settings Screen
	 */
	private int screen = 0;

	private int difficulty = 0;

	private String gamemode = "PC";

	private Color selected = new Color(78, 102, 249);
	private Random random = new Random();

	private int[][] board = new int[length][length];
	private int[][] prevs;
	private int farmerTurn = 1;

	private Point initialClick;
	private boolean dragging = false;

	private int isPopup = 0;

	private String[][] popups;
	private Runnable[][] popupsAction;

	private int delayMin = 1; //default: 500ms  // must be greater than 0
	private int delayMax = 2; //default: 2500ms // must be greater than 0

	//<editor-fold desc="Screen 0">
	private Image matrisse;
	//</editor-fold>

	//<editor-fold desc="Screen 1">
	private Image computer;
	private Image person;
	//</editor-fold>

	//<editor-fold desc="Screen 2">
	private Image[] slots;
	//</editor-fold>
	//</editor-fold>

	@Override
	public void init() {

		matrisse = carregarImagem("images/Matrisse.png");

		computer = carregarImagem("images/computer.png");
		person = carregarImagem("images/person.png");

		slots = new Image[3];
		for (int i = 0; i < slots.length; i++) {
			slots[i] = carregarImagem("images/slot" + i + ".png");
		}

		container.setIconImage(carregarImagem("images/icon.jpg"));

		prevs = new int[2][2];

		prevs[0] = new int[]{-1, -1};
		prevs[1] = new int[]{-1, -1};

		adicionarAudio("track", "audios/track.mp3");

		popups = new String[2][];
		popupsAction = new Runnable[2][];

		for (int i = 0; i < 2; i++) {

			popups[i] = new String[i + 2];

			popupsAction[i] = new Runnable[i + 1];

		}

	}

	@Override
	public void gameLoop() {

		switch (screen) {
			//<editor-fold desc="Screen -1">
			case -1:
				drawImageCenter(matrisse, 1040, 650, 160, 100);
				break;
			//</editor-fold>
			//<editor-fold desc="Screen 0">
			case 0: // tela inicial

				desenharImagem(matrisse, 0, 0);

				desenharRetangulo(415, 490, 190, 40, WHITE);

				desenharString("COMEÇAR", 430, 520, BLACK, 30);

				break;
			//</editor-fold>
			//<editor-fold desc="Screen 1">
			case 1: // configurações

				drawBG();

				desenharString("Opções:", 463, 60, WHITE, 30);
				desenharRetangulo(450, 110, 70, 70, new Color(200, 200, 200));
				desenharRetangulo(520, 110, 70, 70, WHITE);

				if (gamemode.charAt(0) == 'P') {
					desenharImagem(person, 450, 110);
				} else {
					desenharImagem(computer, 450, 110);
				}
				if (gamemode.charAt(1) == 'P') {
					desenharImagem(person, 520, 110);
				} else {
					desenharImagem(computer, 520, 110);
				}

				desenharRetangulo(450, 200, 140, 40, WHITE);
				desenharString("FÁCIL", 480, 232, difficulty == 0 ? selected : Color.BLACK);
				desenharRetangulo(450, 240, 140, 40, new Color(200, 200, 200));
				desenharString("NORMAL", 455, 272, difficulty == 1 ? selected : Color.BLACK);
				desenharRetangulo(450, 280, 140, 40, WHITE);
				desenharString("DIFÍCIL", 470, 312, difficulty == 2 ? selected : Color.BLACK);

				break;
			//</editor-fold>
			//<editor-fold desc="Screen 2">
			case 2:

				drawBG();

				for (int i = 0; i < length; i++) {
					for (int j = 0; j < length; j++) {

						int num = board[i][j];

						Image slot = slots[num];

						int x1 = marginLeftRight + slotSize * i;
						int y1 = marginTopBottom + slotSize * j;

						drawImageVertex(slot, x1, y1, slotSize, slotSize);

					}
				}

				if (gameEnd()) {

					desenharString((farmerTurn == 1 ? "Farmer" : "Alien") + " wins!", 5, 30, WHITE, 30);

				}

				break;
			//</editor-fold>
		}

		if (isPopup != 0) {

			String[] pops = popups[isPopup - 1];

			switch (isPopup) {

				case 1:
					popup(pops[0], pops[1]);
					break;

			}

		}

	}

	@Override
	public void aposTermino() {

	}

	private void makeMove(int x, int y) {

		int[] pos = new int[]{x, y};

		if (!Arrays.equals(prevs[farmerTurn], pos)) {

			if (board[x][y] == 0) {

				for (int i = max(x - 1, 0); i <= min(x + 1, length - 1); i++) {
					for (int j = max(y - 1, 0); j <= min(y + 1, length - 1); j++) {

						int slot = board[i][j];

						board[i][j] = (slot == 0 ? 1 : 0) * (2 - farmerTurn);

					}
				}

				prevs[farmerTurn] = pos;

				if (!gameEnd()) {

					farmerTurn = 1 - farmerTurn;

					if (gamemode.charAt(1 - farmerTurn) == 'C') {

						aiPlay();

					}

				}

			} else {
				// Alert player to play only on free slots
				popup("Você só pode jogar em slots vazios", "Entendido", () -> isPopup = 0);
			}
		} else {
			// Alert player not to play twice on the same place
			popup("Você não pode jogar duas vezes\nno mesmo lugar", "Entendido", () -> isPopup = 0);
		}
	}

	//<editor-fold desc="Artificial Intelligence">
	private void aiPlay() {

		if (gamemode.charAt(1 - farmerTurn) == 'C') {

			int delay = random.nextInt(delayMax - delayMin) + delayMin;

			new Timer().schedule(
					new TimerTask() {
						@Override
						public void run() {

							int[] move = aiTurn();

							makeMove(move[0], move[1]);

						}
					}, delay
			);

		}

	}

	private int[] aiTurn() {

		int[] move;

		int[][][] analizedBoard = analizeBoard(board);

		Integer maxProfitValue = Integer.MIN_VALUE;
		ArrayList<Integer[]> maxProfitIds = new ArrayList<>();

		int maxAdvantageValue = Integer.MIN_VALUE;
		ArrayList<Integer[]> maxAdvantageIds = new ArrayList<>();

		int maxBestValue = Integer.MIN_VALUE;
		ArrayList<Integer[]> maxBestIds = new ArrayList<>();

		for (int i = 0; i < length; i++) {
			int[][] line = analizedBoard[i];

			for (int j = 0; j < length; j++) {
				int[] column = line[j];

				Integer[] pos = new Integer[2];
				pos[0] = i;
				pos[1] = j;

				if (!contains(toPrimitive(pos))) {

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

		int[] points = points();
		int turn = farmerTurn;

		if (difficulty > 0 &&
				points[turn] + maxProfitValue >= win) {

			move = randomID(maxProfitIds);

		} else if (difficulty > 1 &&
				points[1 - turn] + Math.ceil(length / 3) - 1 >= win) {

			maxProfitIds.clear();
			maxProfitValue = Integer.MIN_VALUE;

			for (Integer[] advantage : maxAdvantageIds) {

				int x = advantage[0];
				int y = advantage[1];

				int profit = analizedBoard[x][y][0];

				if (profit > maxProfitValue) {

					maxProfitValue = profit;

					maxProfitIds.clear();
					maxProfitIds.add(advantage);

				} else if (profit == maxProfitValue) {

					maxProfitIds.add(advantage);

				}

			}

			move = randomID(maxProfitIds);

		} else {

			move = randomID(maxBestIds);

		}

		return move;

	}

	private int[][][] analizeBoard(int[][] board) {

		int[][][] moves = new int[length][length][2];

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {

				if (board[i][j] == 0) {

					moves[i][j] = analizeMove(i, j, board);

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

	private int[] analizeMove(int x, int y, int[][] board) {

		int[] move = new int[2];

		for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, length - 1); i++) {
			for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, length - 1); j++) {

				int value = board[i][j];

				int turn = farmerTurn;

				int index = (int) (Math.signum(value) * turn * Math.abs(value + turn - 2));

				move[index] += turn + value == 2 ? -1 : 1;

			}

		}

		return move;

	}

	private int[] randomID(ArrayList<Integer[]> arrayList) {

		return toPrimitive(arrayList.get(random.nextInt(arrayList.size())));

	}

	private int[] toPrimitive(Integer[] array) {
		return Arrays.stream(array).mapToInt(Integer::intValue).toArray();
	}

	private boolean contains(int[] array) {

		return Arrays.equals(array, prevs[farmerTurn]);

	}
	//</editor-fold>

	private class MouseInputHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {

			initialClick = e.getPoint();
			getComponentAt(initialClick);

		}

		@Override
		public void mouseDragged(MouseEvent e) {

			Point loc = container.getLocation();

			int x = e.getX() - initialClick.x + loc.x;
			int y = e.getY() - initialClick.y + loc.y;

			container.setLocation(x, y);

		}

		@Override
		public void mouseClicked(MouseEvent e) {

			int x = e.getX();
			int y = e.getY();

			System.out.println();
			System.out.println("X " + x);
			System.out.println("Y " + y);

			if (isPopup != 0) {

				popupsAction[isPopup - 1][0].run();

			} else {

				switch (screen) {
					//<editor-fold desc="Screen 0">
					case 0:

						if (between(415, x, 605) && between(490, y, 530)) {
							screen = 1;

							tocarAudio("track");
						} else {

							popup("Cuidado. Vacas Mordem", "Estou ciente", () -> isPopup = 0);

						}

						break;
					//</editor-fold>
					//<editor-fold desc="Screen 1">
					case 1:
						if (between(450, x, 590)) {

							if (between(110, y, 180)) { // change gamemode

								int i = x < 520 ? 0 : 1;
								int j = 1 - i;
								char[] gm = new char[2];
								gm[i] = ((char) (147 - gamemode.charAt(i)));
								gm[j] = gamemode.charAt(j);
								gamemode = new String(gm);

							} else if (between(200, y, 320)) { // change difficulty

								difficulty = (int) Math.floor(y / 40 - 5);

							} else {
								screen = 2;
								aiPlay();
							}

						} else {
							screen = 2;
							aiPlay();
						}
						break;
					//</editor-fold>
					//<editor-fold desc="Screen 2">
					case 2:
						if (!gameEnd() && gamemode.charAt(1 - farmerTurn) == 'P' && between(marginLeftRight, x, marginLeftRight + boardSize) && between(marginTopBottom, y, marginTopBottom + boardSize)) {

							int x1 = x - marginLeftRight;
							int y1 = y - marginTopBottom;

							int x2 = (int) Math.floor((double) x1 / slotSize);
							int y2 = (int) Math.floor(y1 / slotSize);

							makeMove(x2, y2);

						}
						break;
					//</editor-fold>
				}
			}

		}
	}

	private class KeyInputHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();

			System.out.println(code);

			if(isPopup != 0) {

				Runnable[] rs = popupsAction[isPopup - 1];

				rs[rs.length - 1].run();

			} else {

				switch (code) {
					case KeyEvent.VK_ESCAPE:
						if (screen <= 0) {
							System.exit(0);
						} else {
							screen = 0;
							pararAudio("track");
							board = new int[length][length];
						}
						break;
				}

			}
		}
	}

	//<editor-fold desc="Essentials">
	private int[] points() {
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

	private boolean gameEnd() {

		int[] points = points();

		return points[0] >= win || points[1] >= win;

	}

	private void popup(String msg, String ok) {

		popupBox();

		desenharString(msg, width / 2 - 120, height / 2 - 30, WHITE, 20);
		desenharString(ok, width / 2 - 120, height / 2 + 45, WHITE, 15);

	}

	private void popup(String msg, String ok, Runnable confirm) {

		isPopup = 1;
		popups[0][0] = msg;

		System.out.println(popups[0][0]);
		popups[0][1] = ok;
		popupsAction[0][0] = confirm;

	}

	private void popupBox() {

		drawRectVertex(0, 0, width, height * 2, new Color(0, 0, 0, 200));

		drawRectCenter(width / 2, height / 2, 250, 100, new Color(81, 86, 88));

	}
	//</editor-fold>

	//<editor-fold desc="Extra Functions">
	private int min(int n, int m) {
		return Math.min(n, m);
	}

	private int max(int n, int m) {
		return Math.max(n, m);
	}

	private void drawBG() {

		desenharRetangulo(0, 0, 1040, 650, new Color(18, 18, 18));

	}

	private void drawImageCenter(Image img, int x, int y, int width, int height) {

		Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		desenharImagem(scaled, x - width / 2, y - height / 2);

	}

	private void drawImageCenter(Image img, int x, int y) {

		BufferedImage bimg = (BufferedImage) img;

		desenharImagem(img, x - bimg.getWidth() / 2, y - bimg.getHeight() / 2);

	}

	private void drawImageVertex(Image img, int x, int y, int width, int height) {

		Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		desenharImagem(scaled, x, y);

	}

	private void drawImageVertex(Image img, int x, int y) {

		desenharImagem(img, x, y);

	}

	private void drawRectCenter(int x, int y, int width, int height, Color c) {

		desenharRetangulo(x - width / 2, y - height / 2, width, height, c);

	}

	private void drawRectVertex(int x, int y, int width, int height, Color c) {

		desenharRetangulo(x, y, height, width, c);

	}

	private boolean between(int min, int num, int max) {
		return min < num && num < max;
	}
	//</editor-fold>

}
