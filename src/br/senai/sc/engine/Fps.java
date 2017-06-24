package br.senai.sc.engine;

public class Fps {
	private int targetFps = 30;
	private long optimalTime;
	private long lastFrameTime;
	private int fps;
	private long lastFPSMs;
	private boolean pause;

	public Fps() {
		this.optimalTime = (long)(1000 / this.targetFps);
		this.getDelta();
		this.lastFPSMs = this.getTime();
	}

	public Fps(int fps) {
		this.optimalTime = (long)(1000 / this.targetFps);
		this.targetFps = fps;
		this.optimalTime = (long)(1000 / this.targetFps);
		this.getDelta();
		this.lastFPSMs = this.getTime();
	}

	public int getTargetFps() {
		return this.targetFps;
	}

	public void setTargetFps(int targetFps) {
		this.targetFps = targetFps;
	}

	public long getOptimalTime() {
		return this.optimalTime;
	}

	public void setOptimalTime(long optimalTime) {
		this.optimalTime = optimalTime;
	}

	public long getLastFrameTime() {
		return this.lastFrameTime;
	}

	public void setLastFrameTime(long lastFrameTime) {
		this.lastFrameTime = lastFrameTime;
	}

	public int getFps() {
		return this.fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public long getLastFPSMs() {
		return this.lastFPSMs;
	}

	public void setLastFPSMs(long lastFPSMs) {
		this.lastFPSMs = lastFPSMs;
	}

	private long getTime() {
		return System.nanoTime() / 1000000L;
	}

	public int getDelta() {
		long time = this.getTime();
		int delta = (int)(time - this.lastFrameTime);
		this.lastFrameTime = time;
		return delta;
	}

	public void updateFPS() {
		if(this.lastFPSMs > 1000L) {
			this.fps = 0;
			this.lastFPSMs = 0L;
		}

		this.lastFPSMs += (long)this.getDelta();
		++this.fps;
	}

	public void synchronize(boolean p) {
		if(p) {
			while (pause) {
				System.out.print("");
			}
		}
		long ms = this.lastFrameTime - this.getTime() + this.optimalTime;

		try {
			if(ms > 0L) {
				Thread.sleep(ms);
			}
		} catch (InterruptedException var4) {
			System.err.println(var4.getMessage());
			System.exit(-1);
		}

	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
}
