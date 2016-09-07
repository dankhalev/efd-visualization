package com.khalev.efd.visuals;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.io.*;
import java.util.ArrayList;

public class Visualizer extends ApplicationAdapter {
	ShapeRenderer sr;
	BufferedReader logs;
	int sizeX;
	int sizeY;
	public final int ZOOM;
	public final int FPS = 20;
	public final int SLEEP_TIME = 1000 / FPS;
	public static final double RADIUS = 3.0;
	ArrayList<Coordinates> coords = new ArrayList<Coordinates>();

	public Visualizer(File f, int zoom) {
		this.ZOOM = zoom;
		try {
			this.logs = new BufferedReader(new FileReader(f));
			this.sizeX = Integer.parseInt(logs.readLine());
			this.sizeY = Integer.parseInt(logs.readLine());
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
		sr.end();
	}

	public void renderRobot(int x, int y) {
		sr.setColor(Color.RED);
		sr.circle(x, y, (float)RADIUS*ZOOM);
	}

	public void renderField(int sizeX, int sizeY) {
		sr.setColor(Color.WHITE);
		sr.rect(0,0,sizeX*ZOOM,sizeY*ZOOM);
	}


	@Override
	public void dispose () {
		sr.dispose();
	}
}
