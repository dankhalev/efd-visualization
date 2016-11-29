package com.khalev.efd.visuals.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.khalev.efd.visuals.BitmapProcessor;
import com.khalev.efd.visuals.Visualizer;

import java.io.*;

//TODO: comment all the public elements in the project
public class DesktopLauncher {

	public static int zoom = 3;
	public static void main (String[] arg) {
		if (arg.length < 2) {
			System.out.println("Please provide a name of simulation logs file and a bitmap");
		} else {
			try {
				File logfile = new File(arg[0]);
				File bitmap = new File(arg[1]);
				LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
				BufferedReader reader = new BufferedReader(new FileReader(logfile));
				config.width = Integer.parseInt(reader.readLine()) * zoom;
				config.height = Integer.parseInt(reader.readLine()) * zoom;
				reader.close();
				new LwjglApplication(new Visualizer(logfile, zoom, (new BitmapProcessor()).readBitmap(bitmap)), config);

			} catch (FileNotFoundException e) {
				System.out.println("Provided file doesn't exist");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
