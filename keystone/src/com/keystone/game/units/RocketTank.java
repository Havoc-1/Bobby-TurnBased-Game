package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.keystone.game.*;

public class RocketTank extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {
		try {
			blue = ImageIO.read(new File("sprites/environment/units/vehicles/rocket_tank_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/vehicles/rocket_tank_RED.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RocketTank(int team) { super("Rocket Tank", 10, 9, 5, 8, 1800, false, team, team > 0 ? red : blue, swingGame2D.root); }
}
