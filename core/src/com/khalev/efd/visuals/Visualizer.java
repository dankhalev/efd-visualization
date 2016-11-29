package com.khalev.efd.visuals;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.io.*;
import java.util.ArrayList;

public class Visualizer extends ApplicationAdapter {
	ShapeRenderer sr;
	SpriteBatch spriteBatch;
	BitmapFont font;
	BufferedReader logs;
	int sizeX;
	int sizeY;
	public final int ZOOM;
	public final int FPS = 10;
	public final int SLEEP_TIME = 1000 / FPS;
	public static final double RADIUS = 3.0;
	public final int OFFSET;
	ArrayList<Coordinates> coords = new ArrayList<Coordinates>();
	EnvironmentMap map;

	public Visualizer(File f, int zoom, EnvironmentMap map) {
		this.ZOOM = zoom;
		OFFSET = ZOOM * (int) RADIUS / 2;
		this.map = map;
		try {
			this.logs = new BufferedReader(new FileReader(f));
			this.sizeX = Integer.parseInt(logs.readLine());
			this.sizeY = Integer.parseInt(logs.readLine());
			if (sizeX != map.getWidth() || sizeY != map.getHeight()) {
				throw new RuntimeException();
			}

			Thread t = new Thread (new Runnable() {
				@Override
				public synchronized void run() {
					try {
						String s;
						while ((s = logs.readLine()) != null) {
							Thread.sleep(SLEEP_TIME);

							String[] ss = s.split(";");
							coords.clear();
							for (int i = 0; i < ss.length; i++) {
								String[] sss = ss[i].split(",");
								Coordinates c = new Coordinates(Double.parseDouble(sss[0]), Double.parseDouble(sss[1]));
								coords.add(c);
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();

		} catch (FileNotFoundException e) {
			System.out.println("Provided file doesn't exist");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void create () {
		sr = new ShapeRenderer();
		spriteBatch  = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("hfont.fnt"),Gdx.files.internal("hfont.png"),false);
	}

	@Override
	public synchronized void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sr.begin(ShapeRenderer.ShapeType.Filled);
		renderField(sizeX, sizeY);
		Object[] robots = coords.toArray();
		for (Object o : robots) {
			if (o != null) {
				Coordinates c = (Coordinates) o;
				renderRobot(Math.round((float) (c.x * ZOOM)), Math.round((float) (c.y * ZOOM)) + 0);

			}
		}
		for (int k = 0; k < map.getWidth(); k++) {
			for (int j = 0; j < map.getHeight(); j++) {
				if (map.isOccupied(k, j)) {
					renderObstacle(k, j);
				}
			}
		}
		sr.end();

		spriteBatch.begin();
		font.getData().setScale(ZOOM/2);
		font.setColor(Color.GREEN);
		int i = 1;
		for (Object o : robots) {
			if (o != null) {
				Coordinates c = (Coordinates) o;
				renderRobotNumber(Math.round((float) (c.x * ZOOM)), Math.round((float) (c.y * ZOOM)) + 0, i);
				i++;
			}
		}
		spriteBatch.end();
	}

	public void renderRobot(int x, int y) {
		sr.setColor(Color.RED);
		sr.circle(x, y, (float)RADIUS*ZOOM);
	}

	public void renderRobotNumber(int x, int y, int i) {
		if ( i < 10 ) {
			font.draw(spriteBatch, String.valueOf(i), x - OFFSET, y + OFFSET);
		} else {
			font.draw(spriteBatch, String.valueOf(i), x - 2*OFFSET, y + OFFSET);
		}
	}

	public void renderField(int sizeX, int sizeY) {
		sr.setColor(Color.WHITE);
		sr.rect(0,0,sizeX*ZOOM,sizeY*ZOOM);
	}

	public void renderObstacle(int x, int y) {
		sr.setColor(Color.BLACK);
		sr.rect(x*ZOOM, y*ZOOM, ZOOM, ZOOM);
	}

	@Override
	public void dispose () {
		sr.dispose();
	}

}
