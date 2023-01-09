package com.keystone.game;

import java.awt.image.BufferedImage;

public class Utils {

    public static int dRow[] = { -1, 0, 1, 0 };
    public static int dCol[] = { 0, 1, 0, -1 };

    public static BufferedImage flip(BufferedImage image) {
        for (int i = 0; i < image.getWidth() / 2; i++)
            for (int j = 0; j < image.getHeight(); j++) {
                int tmp = image.getRGB(i, j);
                image.setRGB(i, j, image.getRGB(image.getWidth() - i - 1, j));
                image.setRGB(image.getWidth() - i - 1, j, tmp);
            }
        return image;
    }

    public static class pair {
        public int first;
        public int second;

        public pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public String toString() {
            return "Pair of [" + first + ", " + second + "]";
        }
    }

    public static boolean isValid(boolean vis[][], int row, int col) {
        if (row < 0 || col < 0 || row >= swingGame2D.MAP_HEIGHT || col >= swingGame2D.MAP_WIDTH)
            return false;
        if (vis[row][col])
            return false;
        return true;
    }

    public float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

}

