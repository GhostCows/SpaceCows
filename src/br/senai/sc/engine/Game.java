package br.senai.sc.engine;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Game extends Canvas {
	private static final long serialVersionUID = 6058040659371962305L;
	protected JFrame container;
	private JPanel panel;
	protected BufferStrategy strategy;
	protected boolean gameRunning = true;
	private boolean sairAoTermino = false;
	protected Graphics2D graphics2D;
	protected Fps fps;
	private Map<String, Mp3> musicas;
	private Map<String, CustomFont> customFonts;
	private Map<String, Cursor> cursors;

	public Game() {
		Dimension fullscreen = Toolkit.getDefaultToolkit().getScreenSize();
		Utils.getInstance().setHeight(fullscreen.height);
		Utils.getInstance().setWidth(fullscreen.width);
		container = new JFrame(Utils.getInstance().getNomeJogo());
		container.setUndecorated(true);
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(Utils.getInstance().getWidth(), Utils.getInstance().getHeight()));
		panel.setLayout(null);
		setBounds(0, 0, Utils.getInstance().getWidth(), Utils.getInstance().getHeight());
		panel.add(this);
		setIgnoreRepaint(true);
		container.pack();
		container.setResizable(false);
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		fps = new Fps();
		musicas = new HashMap();
		customFonts = new HashMap();
		init();
		container.setVisible(true);
		requestFocus();
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}

	public Game(String nomeJogo, int width, int height) {
		Utils.getInstance().setNomeJogo(nomeJogo);
		Utils.getInstance().setHeight(height);
		Utils.getInstance().setWidth(width);
		container = new JFrame(Utils.getInstance().getNomeJogo());
		container.setUndecorated(true);
		panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(Utils.getInstance().getWidth(), Utils.getInstance().getHeight()));
		panel.setLayout(null);
		setBounds(0, 0, Utils.getInstance().getWidth(), Utils.getInstance().getHeight());
		panel.add(this);
		setIgnoreRepaint(true);
		container.pack();
		container.setResizable(false);
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		fps = new Fps();
		musicas = new HashMap<>();
		customFonts = new HashMap<>();
		cursors = new HashMap<>();
		init();
		container.setLocationRelativeTo(null);
		container.setVisible(true);
		requestFocus();
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}

	public abstract void init();

	public void startGame() {
		while (gameRunning) {
			graphics2D = (Graphics2D) strategy.getDrawGraphics();
			fps.updateFPS();
			graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
			graphics2D.setColor(Color.white);
			graphics2D.fillRect(0, 0, Utils.getInstance().getWidth(), Utils.getInstance().getHeight());
			gameLoop();
			graphics2D.dispose();
			strategy.show();
			fps.synchronize(true);
		}

		aposTermino();
		if (sairAoTermino) {
			System.exit(0);
		}

	}

	public void desenharGif(Image image, int x, int y) {
		graphics2D.drawImage(image, x, y, container);
	}

	public void desenharImagem(Image image, int x, int y) {
		graphics2D.drawImage(image, x, y, (ImageObserver) null);
	}

	public void desenharString(String mensagem, int x, int y) {
		FontMetrics fm = graphics2D.getFontMetrics();
		graphics2D.drawString(mensagem, x, y + fm.getAscent());
	}

	public void desenharString(String mensagem, int x, int y, Color color) {
		graphics2D.setColor(color);
		FontMetrics fm = graphics2D.getFontMetrics();
		graphics2D.drawString(mensagem, x, y + fm.getAscent());
	}

	public void desenharString(String mensagem, int x, int y, Color color, int fontSize) {
		graphics2D.setColor(color);
		graphics2D.setFont(new Font("Arial", 0, fontSize));
		FontMetrics fm = graphics2D.getFontMetrics();
		graphics2D.drawString(mensagem, x, y + fm.getAscent());
	}

	public void desenharString(String mensagem, int x, int y, Color color, Font font) {
		graphics2D.setColor(color);
		graphics2D.setFont(font);
		FontMetrics fm = graphics2D.getFontMetrics();
		graphics2D.drawString(mensagem, x, y + fm.getAscent());
	}

	public void desenharString(String mensagem, int x, int y, Color color, int fontSize, Font font) {
		graphics2D.setColor(color);
		graphics2D.setFont(font.deriveFont((float) fontSize));
		FontMetrics fm = graphics2D.getFontMetrics();
		graphics2D.drawString(mensagem, x, y + fm.getAscent());
	}

	public Image carregarImagem(String path) {
		return Utils.getInstance().loadImage(path);
	}

	public void finalizarJogo() {
		gameRunning = false;
	}

	public abstract void aposTermino();

	public abstract void gameLoop();

	public void sairAoTerminar() {
		sairAoTermino = true;
	}

	public void alterarFramesPorSegundos(int fps) {
		this.fps = new Fps(fps);
	}

	/*public void adicionarAudio(String nome, String path) {
		Mp3 mp3 = new Mp3();
		mp3.carregar(path);
		if (musicas.get(nome) != null) {
			musicas.remove(nome);
		}

		musicas.put(nome, mp3);
	}

	public void tocarAudio(String nome) {
		String audioName = musicas.get(nome).getAudioName();
		musicas.remove(nome);
		Mp3 mp3 = new Mp3();
		mp3.carregar(audioName);
		musicas.put(nome, mp3);
		musicas.get(nome).iniciar();
	}

	public void pararAudio(String nome) {
		((Mp3) musicas.get(nome)).finalizar();
	}

	public boolean audioIsCompleted(String nome) {
		return musicas.get(nome) != null ? ((Mp3) musicas.get(nome)).isCompleted() : true;
	}*

	public void removerAudio(String nome) {
		musicas.remove(nome);
	}*/

	public void desenharRetangulo(int x, int y, int width, int height, Color color) {
		graphics2D.setColor(color);
		graphics2D.fillRect(x, y, width, height);
	}

	public void desenharCirculo(int x, int y, int width, int height, Color color) {
		graphics2D.setColor(color);
		graphics2D.fillOval(x, y, width, height);
	}

	public void addNewFont(String name, String path, float size, int style) {
		CustomFont cf = new CustomFont(path, size, style);
		customFonts.put(name, cf);
	}

	public void setFont(String name) {
		graphics2D.setFont(((CustomFont) customFonts.get(name)).getCustomFont());
	}

	public Graphics2D getGraphics2D() {
		return graphics2D;
	}

	public int getWidth() {
		return Utils.getInstance().getWidth();
	}

	public int getHeight() {
		return Utils.getInstance().getHeight();
	}

	public Font getFont(String fontName) {

		return customFonts.get(fontName).getCustomFont();

	}

	public void addCursor(String name, Image img, Point hotSpot) {

		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(img, hotSpot, name);

		cursors.put(name, cursor);

	}

	public void setCursor(String cursorName) {

		if(cursors.containsKey(cursorName)) {
			panel.setCursor(cursors.get(cursorName));
		}

	}
}
