package br.senai.sc.engine;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Utils {
	private static Utils instance;
	private int width;
	private int height;
	private String nomeJogo;

	public Utils() {
	}

	public static Utils getInstance() {
		if(instance == null) {
			instance = new Utils();
		}

		return instance;
	}

	public Image loadImage(String fileName) {
		URL imgUrl = this.getClass().getClassLoader().getResource(fileName);
		if(imgUrl == null) {
			try {
				throw new Exception("Erro ao carregar a imagem!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				String[] split = fileName.split("\\.");
				if(split[split.length - 1].equalsIgnoreCase("gif")) {
					return (new ImageIcon(imgUrl)).getImage();
				}

				return ImageIO.read(imgUrl);
			} catch (IOException var4) {
				var4.printStackTrace();
			}
		}

		return null;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getNomeJogo() {
		return this.nomeJogo;
	}

	public void setNomeJogo(String nomeJogo) {
		this.nomeJogo = nomeJogo;
	}
}
