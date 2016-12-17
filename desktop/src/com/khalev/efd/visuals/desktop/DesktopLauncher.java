package com.khalev.efd.visuals.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.khalev.efd.visuals.BitmapProcessor;
import com.khalev.efd.visuals.EnvironmentMap;
import com.khalev.efd.visuals.Visualizer;

import java.io.*;

//TODO: comment all the public elements in the project
public class DesktopLauncher {

	public static int zoom = 3;
	public static void main (String[] arg) {
		if (arg.length < 1) {
			System.out.println("Please provide a name of simulation logs file");
		} else {
			try {
				File logfile = new File(arg[0]);
				LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
				BufferedReader reader = new BufferedReader(new FileReader(logfile));
				File bitmap = new File(reader.readLine());
				EnvironmentMap map = (new BitmapProcessor()).readBitmap(bitmap);
				config.width = map.getWidth() * zoom;
				config.height = map.getHeight() * zoom;
				reader.close();
				new LwjglApplication(new Visualizer(logfile, zoom, map), config);

			} catch (FileNotFoundException e) {
				System.out.println("Provided file doesn't exist");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
