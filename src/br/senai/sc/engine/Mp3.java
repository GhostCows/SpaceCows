package br.senai.sc.engine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URL;
import javazoom.jl.player.Player;

public class Mp3 extends Thread {
	private File mp3;
	private Player player;
	private String audioName;

	public Mp3(String caminho) {
		try {
			this.audioName = caminho;
			URL path = this.getClass().getClassLoader().getResource(caminho);
			this.mp3 = new File(path.toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAudioName() {
		return this.audioName;
	}

	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}

	public File getMp3() {
		return this.mp3;
	}

	public void setMp3(File mp3) {
		this.mp3 = mp3;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void run() {
		try {
			FileInputStream fis = new FileInputStream(this.mp3);
			BufferedInputStream bis = new BufferedInputStream(fis);
			this.player = new Player(bis);
			this.player.play();
			if(this.player.isComplete()) {
				this.player.close();
				this.interrupt();
			}
		} catch (Exception var3) {
			System.out.println("Problemas ao tocar a música");
			var3.printStackTrace();
		}

	}

	public void iniciar() {
		this.start();
	}

	public void finalizar() {
		this.player.close();
		this.interrupt();
	}

	public boolean isCompleted() {
		return this.player != null && (this.player.isComplete() || !this.isAlive());
	}
}