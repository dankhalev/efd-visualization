package com.khalev.efd.visuals.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.khalev.efd.visuals.Visualizer;

import java.io.*;

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
				config.width = Integer.parseInt(reader.readLine()) * zoom;
				config.height = Integer.parseInt(reader.readLine()) * zoom;
				reader.close();
				new LwjglApplication(new Visualizer(logfile, zoom), config);

			} catch (FileNotFoundException e) {
				System.out.println("Provided file doesn't exist");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
