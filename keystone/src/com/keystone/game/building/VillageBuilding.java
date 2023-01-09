package com.keystone.game.building;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.keystone.game.swingGame2D;

public class VillageBuilding extends Building {

    static BufferedImage blue;
    static BufferedImage red;
    static BufferedImage neutral;
    static {
        try {
            blue = ImageIO.read(new File("sprites/environment/buildings/village_blue.png"));
            red = ImageIO.read(new File("sprites/environment/buildings/village_red.png"));
            neutral = ImageIO.read(new File("sprites/environment/buildings/village_neutral.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VillageBuilding(int x, int y, int team) {
        super("Village", x, y, 250, team, blue, red, neutral, swingGame2D.root);
    }

}
