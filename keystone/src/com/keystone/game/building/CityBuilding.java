package com.keystone.game.building;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.keystone.game.swingGame2D;

public class CityBuilding extends Building {

    static BufferedImage blue;
    static BufferedImage red;
    static BufferedImage neutral;
    static {
        try {
            blue = ImageIO.read(new File("sprites/environment/buildings/city_blue.png"));
            red = ImageIO.read(new File("sprites/environment/buildings/city_red.png"));
            neutral = ImageIO.read(new File("sprites/environment/buildings/city_neutral.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CityBuilding(int x, int y, int team) {
        super("City", x, y, 250, team, blue, red, neutral, swingGame2D.root);
    }

}
