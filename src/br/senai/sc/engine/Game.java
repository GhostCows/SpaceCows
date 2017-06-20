package br.senai.sc.engine;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Game extends Canvas {
	private static final long serialVersionUID = 6058040659371962305L;
	protected JFrame container;
	private BufferStrategy strategy;
	private boolean gameRunning = true;
	private boolean sairAoTermino = false;
	private Graphics2D graphics2D;
	private Fps fps;
	private Map<String, Mp3> musicas;
	private Map<String, CustomFont> customFonts;

	public Game() {
		Dimension fullscreen = Toolkit.getDefaultToolkit().getScreenSize();
		Utils.getInstance().setHeight(fullscreen.height);
		Utils.getInstance().setWidth(fullscreen.width);
		this.container = new JFrame(Utils.getInstance().getNomeJogo());
		this.container.setUndecorated(true);
		JPanel panel = (JPanel)this.container.getContentPane();
		panel.setPreferredSize(new Dimension(Utils.getInstance().getWidth(), Utils.getInstance().getHeight()));
		panel.setLayout((LayoutManager)null);
		this.setBounds(0, 0, Utils.getInstance().getWidth(), Utils.getInstance().getHeight());
		panel.add(this);
		this.setIgnoreRepaint(true);
		this.container.pack();
		this.container.setResizable(false);
		this.container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.fps = new Fps();
		this.musicas = new HashMap();
		this.customFonts = new HashMap();
		this.init();
		this.container.setVisible(true);
		this.requestFocus();
		this.createBufferStrategy(2);
		this.strategy = this.getBufferStrategy();
	}

	public Game(String nomeJogo, int width, int height) {
		Utils.getInstance().setNomeJogo(nomeJogo);
		Utils.getInstance().setHeight(height);
		Utils.getInstance().setWidth(width);
		this.container = new JFrame(Utils.getInstance().getNomeJogo());
		this.container.setUndecorated(true);
		JPanel panel = (JPanel) this.container.getContentPane();
		panel.setPreferredSize(new Dimension(Utils.getInstance().getWidth() - 10, Utils.getInstance().getHeight() - 10));
		panel.setLayout((LayoutManager)null);
		this.setBounds(0, 0, Utils.getInstance().getWidth(), Utils.getInstance().getHeight());
		panel.add(this);
		this.setIgnoreRepaint(true);
		this.container.pack();
		this.container.setResizable(false);
		this.container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.fps = new Fps();
		this.musicas = new HashMap();
		this.customFonts = new HashMap();
		this.init();
		this.container.setLocationRelativeTo((Component)null);
		this.container.setVisible(true);
		this.requestFocus();
		this.createBufferStrategy(2);
		this.strategy = this.getBufferStrategy();
	}

	public abstract void init();

	public void startGame() {
		while(this.gameRunning) {
			this.graphics2D = (Graphics2D)this.strategy.getDrawGraphics();
			this.fps.updateFPS();
			this.graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
			this.graphics2D.setColor(Color.white);
			this.graphics2D.fillRect(0, 0, Utils.getInstance().getWidth(), Utils.getInstance().getHeight());
			this.gameLoop();
			this.graphics2D.dispose();
			this.strategy.show();
			this.fps.synchronize();
		}

		this.aposTermino();
		if(this.sairAoTermino) {
			System.exit(0);
		}

	}

	public void desenharGif(Image image, int x, int y) {
		this.graphics2D.drawImage(image, x, y, this.container);
	}

	public void desenharImagem(Image image, int x, int y) {
		this.graphics2D.drawImage(image, x, y, (ImageObserver)null);
	}

	public void desenharString(String mensagem, int x, int y) {
		FontMetrics fm = graphics2D.getFontMetrics();
		this.graphics2D.drawString(mensagem, x, y + fm.getAscent() - fm.getDescent() - fm.getLeading());
	}

	public void desenharString(String mensagem, int x, int y, Color color) {
		this.graphics2D.setColor(color);
		FontMetrics fm = graphics2D.getFontMetrics();
		this.graphics2D.drawString(mensagem, x, y + fm.getAscent() - fm.getDescent() - fm.getLeading());
	}

	public void desenharString(String mensagem, int x, int y, Color color, int fontSize) {
		this.graphics2D.setColor(color);
		this.graphics2D.setFont(new Font("Arial", 1, fontSize));
		FontMetrics fm = graphics2D.getFontMetrics();
		this.graphics2D.drawString(mensagem, x, y + fm.getAscent() - fm.getDescent() - fm.getLeading());
	}

	public void desenharString(String mensagem, int x, int y, Color color, Font font) {
		this.graphics2D.setColor(color);
		this.graphics2D.setFont(font);
		FontMetrics fm = graphics2D.getFontMetrics();
		this.graphics2D.drawString(mensagem, x, y + fm.getAscent() - fm.getDescent() - fm.getLeading());
	}

	public void desenharString(String mensagem, int x, int y, Color color, int fontSize, Font font, int fontStyle) {
		this.graphics2D.setColor(color);
		this.graphics2D.setFont(font);
		FontMetrics fm = graphics2D.getFontMetrics();
		this.graphics2D.drawString(mensagem, x, y + fm.getAscent() - fm.getDescent() - fm.getLeading());
	}

	public Image carregarImagem(String path) {
		return Utils.getInstance().loadImage(path);
	}

	public void finalizarJogo() {
		this.gameRunning = false;
	}

	public abstract void aposTermino();

	public abstract void gameLoop();

	public void sairAoTerminar() {
		this.sairAoTermino = true;
	}

	public void alterarFramesPorSegundos(int fps) {
		this.fps = new Fps(fps);
	}

	public void adicionarAudio(String nome, String path) {
		Mp3 mp3 = new Mp3();
		mp3.carregar(path);
		if(this.musicas.get(nome) != null) {
			this.musicas.remove(nome);
		}

		this.musicas.put(nome, mp3);
	}

	public void tocarAudio(String nome) {
		String audioName = this.musicas.get(nome).getAudioName();
		this.musicas.remove(nome);
		Mp3 mp3 = new Mp3();
		mp3.carregar(audioName);
		this.musicas.put(nome, mp3);
		this.musicas.get(nome).iniciar();
	}

	public void pararAudio(String nome) {
		((Mp3)this.musicas.get(nome)).finalizar();
	}

	public boolean audioIsCompleted(String nome) {
		return this.musicas.get(nome) != null?((Mp3)this.musicas.get(nome)).isCompleted():true;
	}

	public void removerAudio(String nome) {
		this.musicas.remove(nome);
	}

	public void desenharRetangulo(int x, int y, int width, int height, Color color) {
		this.graphics2D.setColor(color);
		this.graphics2D.fillRect(x, y, width, height);
	}

	public void desenharCirculo(int x, int y, int width, int height, Color color) {
		this.graphics2D.setColor(color);
		this.graphics2D.fillOval(x, y, width, height);
	}

	public void addNewFont(String name, String path, float size, int style) {
		CustomFont cf = new CustomFont(path, size, style);
		this.customFonts.put(name, cf);
	}

	public void setFont(String name) {
		this.graphics2D.setFont(((CustomFont)this.customFonts.get(name)).getCustomFont());
	}

	public Graphics2D getGraphics2D() {
		return this.graphics2D;
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
}
