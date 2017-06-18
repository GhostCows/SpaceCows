package br.senai.sc.engine;

import java.awt.Font;
import java.io.File;
import java.net.URL;

public class CustomFont {
	private Font customFont;

	public CustomFont(String path, float size, int style) {
		try {
			URL uc = this.getClass().getClassLoader().getResource(path);
			this.customFont = Font.createFont(0, new File(uc.getPath())).deriveFont(style, size);
		} catch (Exception var5) {
			var5.printStackTrace();
		}

	}

	public Font getCustomFont() {
		return this.customFont;
	}

	public void setCustomFont(Font customFont) {
		this.customFont = customFont;
	}
}
