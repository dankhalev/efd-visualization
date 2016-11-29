package com.khalev.efd.visuals;

/**
 * Created by Daniel on 20/11/2016.
 */
public class EnvironmentMap {

    Boolean[][] map;

    public EnvironmentMap(int sizeX, int sizeY) {
        map = new Boolean[sizeX][sizeY];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = false;
            }
        }
    }

    public void addVisibleObstacle(int x, int y) {
        map[x][y] = true;
    }

    public boolean isOccupied(int x, int y) {
        return map[x][y];
    }

    public int getHeight() {
        return map[0].length;
    }
    public int getWidth() {
        return map.length;
    }
}
