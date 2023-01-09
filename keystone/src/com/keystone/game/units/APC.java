package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.keystone.game.*;

public class APC extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {

		try {
			blue = ImageIO.read(new File("sprites/environment/units/vehicles/APC_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/vehicles/APC_red.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public APC(int team) { super("APC", 0, 15, 8, 0, 600, true, team, team > 0 ? red : blue, swingGame2D.root); }
}