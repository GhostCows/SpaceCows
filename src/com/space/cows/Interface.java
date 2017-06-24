package com.space.cows;

import br.senai.sc.engine.Game;
import br.senai.sc.engine.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Timer;

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
		super("TSIFOC", 1156, 650);
		addMouseListener(new MouseInputHandler());
		addMouseMotionListener(new MouseInputHandler());
		addKeyListener(new KeyInputHandler());
	}
	//</editor-fold>

	//<editor-fold desc="Variable Declarations">
	private Dimension size;
	private int width; // 1156
	private int height; // 650
	private int maxWidth; // 1920
	private int maxHeight; // 1080
	private int length; // 7
	private int marginTop;
	//	private int marginBottom;
	private int marginLeft;
	private int slotSize;
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

	private String gamemode;

	private double ratio;

	private Random random = new Random();

	private int[][] board;
	private int[][] prevs;
	private int farmerTurn;

	private int indexEE = 0;
	private int eeNum = 5;

	private Point initialClick;
	private boolean dragging = false;
	private boolean fullscreen;
	private Dimension fullscrn;

	private int isPopup = 0;

	private String[] popups;
	private Runnable[] popupsAction;

	private Font popupFont;
	private int popupFontSize;

	private int delayMin = 500; //default: 500ms  // must be greater than 0
	private int delayMax = 2500; //default: 2500ms // must be greater than 0

	private boolean ratioNull;
	private boolean ratioBig;
	private boolean ratioSmall;

	private Image logoBig;
	private Image logoBigR;
	private Image logoBigO;
	private Image backGround;
	private Image backGroundR;
	private Image backGroundO;
	private Image popup;
	private Image popupR;
	private Image popupO;

	//<editor-fold desc="Screen 0">
	private Image btnSingle;
	private Image btnSingleR;
	private Image btnSingleO;
	private Image btnPersonalizar;
	private Image btnPersonalizarR;
	private Image btnPersonalizarO;
	private Image btnConfig;
	private Image btnConfigR;
	private Image btnConfigO;
	private Image btnDual;
	private Image btnDualR;
	private Image btnDualO;
	private Image btnSair;
	private Image btnSairR;
	private Image btnSairO;
	//</editor-fold>

	//<editor-fold desc="Screen 1">
	private Image btnFacil;
	private Image btnFacilR;
	private Image btnFacilO;
	private Image btnMedio;
	private Image btnMedioR;
	private Image btnMedioO;
	private Image btnDificil;
	private Image btnDificilR;
	private Image btnDificilO;
	private Image btnVoltar;
	private Image btnVoltarR;
	private Image btnVoltarO;
	//</editor-fold>

	//<editor-fold desc="Screen 2">
	private Image[] vacas;
	private Image[] vacasR;
	private Image[] vacasO;
	private Image[] slots;
	private Image[] slotsR;
	private Image[] slotsO;
	private Image logoSmall;
	private Image logoSmallR;
	private Image logoSmallO;
	private Image btnConfigSmall;
	private Image btnConfigSmallR;
	private Image btnConfigSmallO;
	private Image[] placares;
	private Image[] placaresR;
	private Image[] placaresO;
	private Image btnDesistir;
	private Image btnDesistirR;
	private Image btnDesistirO;
	//</editor-fold>
	//</editor-fold>

	@Override
	public void init() {

		vacasO = new Image[2];
		vacasO[0] = carregarImagem("images/vaca-terra.png");
		vacasO[1] = carregarImagem("images/vaca-alien-no_bright.png");

		placaresO = new Image[2];
		placaresO[0] = carregarImagem("images/placar-terra.png");
		placaresO[1] = carregarImagem("images/placar-alien.png");

		slotsO = new Image[2];
		slotsO[0] = carregarImagem("images/casa-clara.png");
		slotsO[1] = carregarImagem("images/casa-escura.png");

		popupO = carregarImagem("images/popup.png");
		logoBigO = carregarImagem("images/logo-big.png");
		logoSmallO = carregarImagem("images/logo-small.png");
		backGroundO = carregarImagem("images/bg.png");
		btnSingleO = carregarImagem("images/btn-single.png");
		btnPersonalizarO = carregarImagem("images/btn-perso.png");
		btnConfigO = carregarImagem("images/btn-config.png");
		btnDualO = carregarImagem("images/btn-dual.png");
		btnSairO = carregarImagem("images/btn-sair.png");
		btnFacilO = carregarImagem("images/btn-facil.png");
		btnMedioO = carregarImagem("images/btn-medio.png");
		btnDificilO = carregarImagem("images/btn-dificil.png");
		btnVoltarO = carregarImagem("images/btn-voltar.png");
		btnConfigSmallO = carregarImagem("images/btn-config-small.png");
		btnDesistirO = carregarImagem("images/btn-desistir.png");

//		container.setIconImage(carregarImagem("images/icon.jpg"));

		prevs = new int[2][2];

		prevs[0] = new int[]{-1, -1};
		prevs[1] = new int[]{-1, -1};

		adicionarAudio("track", "audios/track.mp3");

		popups = new String[2];
		popupsAction = new Runnable[2];

		for (int i = 0; i < 2; i++) {

			popups[i] = "";

		}

		width = 1156; // 1156
		height = 650; // 650

		maxWidth = 1920; // 1920
		maxHeight = 1080; // 1080

		size = new Dimension(width, height);

		fullscrn = Toolkit.getDefaultToolkit().getScreenSize();

		ratio = ((double) height) / maxHeight;
		ratioNull = fullscrn.getWidth() == maxWidth && fullscrn.getHeight() == maxHeight;
		ratioBig = fullscrn.getWidth() > maxWidth || fullscrn.getHeight() > maxHeight;
		ratioSmall = fullscrn.getWidth() <= width || fullscrn.getHeight() <= height;

		length = 7;

		board = new int[length][length];

		farmerTurn = 1;

		marginTop = 155;
		marginLeft = 505;

		slotSize = 130;

		popupFontSize = 26;
		addNewFont("popup", "fonts/popup.ttf", popupFontSize, 0);

		popupFont = getFont("popup");

		if (ratioSmall) {
			fullscreen();
		}

		fullscrn.setSize(Math.min(maxWidth, fullscrn.getWidth()), Math.min(maxHeight, fullscrn.getHeight()));

		fullscreen = ratioSmall;

		vacasR = new Image[2];
		vacasR[0] = vacasO[0].getScaledInstance(r(201), r(195), Image.SCALE_SMOOTH);
		vacasR[1] = vacasO[1].getScaledInstance(r(201), r(195), Image.SCALE_SMOOTH);
		placaresR = new Image[2];
		placaresR[0] = placaresO[0].getScaledInstance(r(260), r(173), Image.SCALE_SMOOTH);
		placaresR[1] = placaresO[1].getScaledInstance(r(260), r(173), Image.SCALE_SMOOTH);
		slotsR = new Image[2];
		slotsR[0] = slotsO[0].getScaledInstance(r(130), r(130), Image.SCALE_SMOOTH);
		slotsR[1] = slotsO[1].getScaledInstance(r(130), r(130), Image.SCALE_SMOOTH);
		popupR = popupO.getScaledInstance(r(595), r(333), Image.SCALE_SMOOTH);
		logoBigR = logoBigO.getScaledInstance(r(489), r(282), Image.SCALE_SMOOTH);
		logoSmallR = logoSmallO.getScaledInstance(r(228), r(132), Image.SCALE_SMOOTH);
		backGroundR = backGroundO.getScaledInstance(r(1920) + 1, r(1080) + 1, Image.SCALE_SMOOTH);
		btnSingleR = btnSingleO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnPersonalizarR = btnPersonalizarO.getScaledInstance(r(460), r(225), Image.SCALE_SMOOTH);
		btnConfigR = btnConfigO.getScaledInstance(r(460), r(225), Image.SCALE_SMOOTH);
		btnDualR = btnDualO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnSairR = btnSairO.getScaledInstance(r(230), r(113), Image.SCALE_SMOOTH);
		btnFacilR = btnFacilO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnMedioR = btnMedioO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnDificilR = btnDificilO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnVoltarR = btnVoltarO.getScaledInstance(r(230), r(113), Image.SCALE_SMOOTH);
		btnConfigSmallR = btnConfigSmallO.getScaledInstance(r(57), r(58), Image.SCALE_SMOOTH);
		btnDesistirR = btnDesistirO.getScaledInstance(r(161), r(34), Image.SCALE_SMOOTH);

		double prevRatio = ratio;
		ratio = ((double) fullscrn.height) / maxHeight;

		vacasO[0] = vacasO[0].getScaledInstance(r(201), r(195), Image.SCALE_SMOOTH);
		vacasO[1] = vacasO[1].getScaledInstance(r(201), r(195), Image.SCALE_SMOOTH);
		placaresO[0] = placaresO[0].getScaledInstance(r(260), r(173), Image.SCALE_SMOOTH);
		placaresO[1] = placaresO[1].getScaledInstance(r(260), r(173), Image.SCALE_SMOOTH);
		slotsO[0] = slotsO[0].getScaledInstance(r(130), r(130), Image.SCALE_SMOOTH);
		slotsO[1] = slotsO[1].getScaledInstance(r(130), r(130), Image.SCALE_SMOOTH);
		popupO = popupO.getScaledInstance(r(595), r(333), Image.SCALE_SMOOTH);
		logoBigO = logoBigO.getScaledInstance(r(489), r(282), Image.SCALE_SMOOTH);
		logoSmallO = logoSmallO.getScaledInstance(r(228), r(132), Image.SCALE_SMOOTH);
		backGroundO = backGroundO.getScaledInstance(r(1920) + 1, r(1080) + 1, Image.SCALE_SMOOTH);
		btnSingleO = btnSingleO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnPersonalizarO = btnPersonalizarO.getScaledInstance(r(460), r(225), Image.SCALE_SMOOTH);
		btnConfigO = btnConfigO.getScaledInstance(r(460), r(225), Image.SCALE_SMOOTH);
		btnDualO = btnDualO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnSairO = btnSairO.getScaledInstance(r(230), r(113), Image.SCALE_SMOOTH);
		btnFacilO = btnFacilO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnMedioO = btnMedioO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnDificilO = btnDificilO.getScaledInstance(r(460), r(460), Image.SCALE_SMOOTH);
		btnVoltarO = btnVoltarO.getScaledInstance(r(230), r(113), Image.SCALE_SMOOTH);
		btnConfigSmallO = btnConfigSmallO.getScaledInstance(r(57), r(58), Image.SCALE_SMOOTH);
		btnDesistirO = btnDesistirO.getScaledInstance(r(161), r(34), Image.SCALE_SMOOTH);

		ratio = prevRatio;

		setImagesSmall();
	}

	@Override
	public void gameLoop() {

//		System.out.println("a");

		drawImageVertex(backGround, 0, 0);

		switch (screen) {
			//<editor-fold desc="Screen -1">
			case -1:
				break;
			//</editor-fold>
			//<editor-fold desc="Screen 0">
			case 0: // tela inicial

				drawImageVertex(logoBig, r(715), r(25));

				drawImageVertex(btnSingle, r(260), r(390));

				drawImageVertex(btnPersonalizar, r(730), r(390));

				drawImageVertex(btnConfig, r(730), r(625));

				drawImageVertex(btnDual, r(1200), r(390));

				drawImageVertex(btnSair, r(1430), r(875));

				break;
			//</editor-fold>
			//<editor-fold desc="Screen 1">
			case 1: // configurações

				drawImageVertex(logoBig, r(715), r(25));

				drawImageVertex(btnFacil, r(260), r(390));

				drawImageVertex(btnMedio, r(730), r(390));

				drawImageVertex(btnDificil, r(1200), r(390));

				drawImageVertex(btnVoltar, r(260), r(875));

				break;
			//</editor-fold>
			//<editor-fold desc="Screen 2">
			case 2:

				drawImageVertex(logoSmall, r(845), r(14));

				drawImageVertex(btnConfigSmall, r(764), r(80));

				drawImageVertex(placares[0], r(220), r(435));
				drawImageVertex(placares[1], r(1440), r(435));

				drawImageVertex(btnDesistir, r(285), r(610));
				drawImageVertex(btnDesistir, r(1475), r(610));

				int[] points = points();

				desenharString(String.valueOf(points[0]), 0, height / 2, WHITE, 24);
				desenharString(String.valueOf(points[1]), 0, height / 2 + 30, WHITE, 24);

				String msg = "O " + (farmerTurn == 1 ? "fazendeiro" : "alien") + " " + (gameEnd() ? "ganhou" : "deve jogar") + "!";

				desenharString(msg, 0, 0);

				for (int i = 0; i < length; i++) {
					for (int j = 0; j < length; j++) {

						Image slot = slots[(i + j) % 2];

						int size = r(slotSize);

						int x = r(marginLeft) + size * i;
						int y = r(marginTop) + size * j;

						drawImageVertex(slot, x, y);

						int num = board[i][j];

						if (num != 0) {

							Image cow = vacas[num - 1];

							drawImageCenter(cow, x + size / 2, y + size / 2);

						}

					}
				}

				break;
			//</editor-fold>
		}

		if (isPopup != 0) {

			String pops = popups[isPopup - 1];

			switch (isPopup) {

				case 1:
					popup(pops);
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
				// Alert player to play only on free vacas
				popup("Você só pode jogar em vacas vazios", () -> {
				});
			}
		} else {
			// Alert player not to play twice on the same place
			popup("Você não pode jogar duas vezes\nno mesmo lugar", () -> {
			});
		}
	}

	private void makeMove() {

		if (gamemode.charAt(0) == 'C') {

			aiPlay();

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

	//<editor-fold desc="Input">
	private class MouseInputHandler extends MouseAdapter {

		@Override
		public void mouseMoved(MouseEvent e) {

			int x = e.getX();
			int y = e.getY();

			switch (screen) {
				case 0:

					break;
				case 1:
					break;
				case 2:
					break;
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {

			int x = e.getX();
			int y = e.getY();

			switch (screen) {
				case 0:
				case 1:
					dragging = in(x, y, 715, 25, 489, 282);
					break;
				case 2:
					dragging = in(x, y, 845, 14, 228, 132);
					break;
			}

			if (dragging && (!fullscreen || ratioBig)) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}

		}

		@Override
		public void mouseDragged(MouseEvent e) {

			if (dragging && !fullscreen) {

				Point loc = container.getLocation();

				int x = e.getX() - initialClick.x + loc.x;
				int y = e.getY() - initialClick.y + loc.y;

				container.setLocation(x, y);

			}

		}

		@Override
		public void mouseClicked(MouseEvent e) {

			int x = e.getX();
			int y = e.getY();

			System.out.println();
			System.out.println("X " + x);
			System.out.println("Y " + y);

			if (isPopup != 0) {

				popupsAction[isPopup - 1].run();
				isPopup = 0;

			} else {

				switch (screen) {
					//<editor-fold desc="Screen 0">
					case 0:

						if (in(x, y, 260, 390, 460)) {

							screen = 1;
							gamemode = "PC";

							indexEE = 0;

						} else if (in(x, y, 730, 390, 460, 255)) {

							//perso

						} else if (in(x, y, 730, 625, 460, 255)) {

							//config

						} else if (in(x, y, 1200, 390, 460)) {

							screen = 2;
							gamemode = "PP";

						} else if (in(x, y, 1430, 875, 230, 113)) {

							System.exit(0);

						} else if (in(x, y, 715, 25, 489, 282)) {

							if (indexEE++ == eeNum - 1) {

								popup("Cuidado, Vacas Mordem", () -> indexEE = 0);

							}

						}

						break;
					//</editor-fold>
					//<editor-fold desc="Screen 1">
					case 1:

						if (in(x, y, 260, 390, 460)) {

							difficulty = 0;
							screen = 2;

							makeMove();

						} else if (in(x, y, 730, 390, 460)) {

							difficulty = 1;
							screen = 2;

							makeMove();

						} else if (in(x, y, 1200, 390, 460)) {

							difficulty = 2;
							screen = 2;

							makeMove();

						} else if (in(x, y, 715, 25, 489, 282)) {

							indexEE++;

							if (indexEE == eeNum) {
								gamemode = "CC";
							}

						} else if (in(x, y, 260, 875, 230, 113)) {

							screen = 0;

							indexEE = 0;

						}

						break;
					//</editor-fold>
					//<editor-fold desc="Screen 2">
					case 2:

						int boardSize = 7 * slotSize;

						if (!gameEnd() && in(x, y, marginLeft, marginTop, boardSize)) {

							int posX = x - r(marginLeft);
							int posY = y - r(marginTop);

							int sX = (int) Math.floor(posX / r(slotSize));
							int sY = (int) Math.floor(posY / r(slotSize));

							makeMove(sX, sY);

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

			if (isPopup != 0) {

				popupsAction[isPopup - 1].run();
				isPopup = 0;


			} else {

				switch (code) {
					case KeyEvent.VK_ESCAPE:
						gamemode = "PC";
						indexEE = 0;

						board = new int[length][length];

						if (screen <= 0) {
							System.exit(0);
						} else {
							screen = 0;
							board = new int[length][length];


						}
						break;
					case KeyEvent.VK_F11:
						if (!ratioSmall) {
							fullscreen();
						}
						break;
				}

			}
		}
	}
	//</editor-fold>

	private void fullscreen() {

		fps.setPause(true);
		fps.synchronize(false);

		fullscreen = !fullscreen;

		Dimension fullscreen;

		if (this.fullscreen) {

			fullscreen = fullscrn;
			setImagesBig();

		} else {

			fullscreen = size;
			setImagesSmall();

		}

		width = fullscreen.width;
		height = fullscreen.height;

		ratio = ((double) height) / maxHeight;

		container.dispose();

		Utils.getInstance().setWidth(width);
		Utils.getInstance().setHeight(height);

		container = new JFrame(Utils.getInstance().getNomeJogo());
		container.setUndecorated(true);
		JPanel panel = (JPanel) this.container.getContentPane();
		panel.setPreferredSize(fullscreen);
		panel.setLayout(null);
		setBounds(0, 0, width, height);
		panel.add(this);
		setIgnoreRepaint(true);
		container.pack();
		container.setResizable(false);
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		if (!this.fullscreen) {
			container.setLocationRelativeTo(null);
		}
		container.setVisible(true);
		requestFocus();
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		fps.setPause(false);

	}

	//<editor-fold desc="Essentials">
	private int r(int i) {
		return (int) Math.floor(((double) i) * ratio);
	}

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

	private void popup(String msg, Runnable confirm) {

		isPopup = 1;
		popups[0] = msg;

		popupsAction[0] = confirm;

	}

	private void popup(String msg) {

		popupBox();

		int x = (width / 2) - r(596 / 2) + r(64);

		int y = (height / 2) - r(333 / 2) + r(107);

		desenharString(msg, x, y, BLACK, popupFont);

	}

	private void popupBox() {

		drawRectVertex(0, 0, width, height * 2, new Color(0, 0, 0, 150));

		drawImageCenter(popup, width / 2, height / 2);

	}

	private void setImagesSmall() {

		vacas = vacasR;
		placares = placaresR;
		slots = slotsR;
		popup = popupR;
		logoBig = logoBigR;
		logoSmall = logoSmallR;
		backGround = backGroundR;
		btnSingle = btnSingleR;
		btnPersonalizar = btnPersonalizarR;
		btnConfig = btnConfigR;
		btnDual = btnDualR;
		btnSair = btnSairR;
		btnFacil = btnFacilR;
		btnMedio = btnMedioR;
		btnDificil = btnDificilR;
		btnVoltar = btnVoltarR;
		btnConfigSmall = btnConfigSmallR;
		btnDesistir = btnDesistirR;

	}

	private void setImagesBig() {

		vacas = vacasO;
		placares = placaresO;
		slots = slotsO;
		popup = popupO;
		logoBig = logoBigO;
		logoSmall = logoSmallO;
		backGround = backGroundO;
		btnSingle = btnSingleO;
		btnPersonalizar = btnPersonalizarO;
		btnConfig = btnConfigO;
		btnDual = btnDualO;
		btnSair = btnSairO;
		btnFacil = btnFacilO;
		btnMedio = btnMedioO;
		btnDificil = btnDificilO;
		btnVoltar = btnVoltarO;
		btnConfigSmall = btnConfigSmallO;
		btnDesistir = btnDesistirO;

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

	private void drawImageCenter(Image img, int x, int y) {

		BufferedImage bImg = toBufferedImage(img);

		desenharImagem(img, x - bImg.getWidth() / 2, y - bImg.getHeight() / 2);

	}

	private void drawImageVertex(Image img, int x, int y) {

		desenharImagem(img, x, y);

	}

	private void drawRectVertex(int x, int y, int width, int height, Color c) {

		desenharRetangulo(x, y, height, width, c);

	}

	private void drawCenteredString(String text, int x, int y, int width, int height, Font font) {
		Graphics2D g = getGraphics2D();
		FontMetrics metrics = g.getFontMetrics(font);
		int x1 = x + (width - metrics.stringWidth(text)) / 2;
		int y1 = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
		g.setFont(font);
		g.drawString(text, x1, y1);
	}

	private boolean between(int min, int num, int max) {
		return r(min) < num && num < r(max);
	}

	private boolean in(int x, int y, int xmin, int ymin, int width, int height) {
		return between(xmin, x, xmin + width) && between(ymin, y, ymin + height);
	}

	private boolean in(int x, int y, int xmin, int ymin, int size) {
		return between(xmin, x, xmin + size) && between(ymin, y, ymin + size);
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}
	//</editor-fold>

}
