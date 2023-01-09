package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.keystone.game.*;
import com.keystone.game.building.*;

public class LightTank extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {
		try {
			blue = ImageIO.read(new File("sprites/environment/units/vehicles/light_tank_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/vehicles/light_tank_red.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LightTank(int team) { super("Light Tank", 7, 10, 7, 1, 1400, false, team, team > 0 ? red : blue, swingGame2D.root); }
}
