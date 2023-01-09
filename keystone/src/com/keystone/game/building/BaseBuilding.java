package com.keystone.game.building;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.keystone.game.swingGame2D;

public class BaseBuilding extends Building {

    static BufferedImage blue;
    static BufferedImage red;
    static {
        try {
            blue = ImageIO.read(new File("sprites/environment/buildings/HQ_blue.png"));
            red = ImageIO.read(new File("sprites/environment/buildings/HQ_red.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BaseBuilding(int x, int y, int team) throws IOException {
        super("Base", x, y, 100, team, blue, red, blue, swingGame2D.root);
    }

    @Override
    public void changeTeam(int team) {
        super.changeTeam(team);
        swingGame2D.win(team, swingGame2D.isAIEnabled());
    }

}
