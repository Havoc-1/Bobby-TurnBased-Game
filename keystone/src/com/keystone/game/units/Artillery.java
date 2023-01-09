package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.keystone.game.*;

public class Artillery extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {
		try {
			blue = ImageIO.read(new File("sprites/environment/units/vehicles/arty_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/vehicles/arty_red.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Artillery(int team) { super("Artillery", 8, 9, 5, 5, 1200, false, team, team > 0 ? red : blue, swingGame2D.root); }
}
