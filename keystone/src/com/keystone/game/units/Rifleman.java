package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.keystone.game.*;
import com.keystone.game.building.*;

public class Rifleman extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {
		try {
			blue = ImageIO.read(new File("sprites/environment/units/infantry/rifleman_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/infantry/rifleman_red.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Rifleman(int team) { super("Rifleman", 5, 8, 5, 1, 1000, false, team, team > 0 ? red : blue, swingGame2D.root); }
}
