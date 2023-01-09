package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.keystone.game.*;
import com.keystone.game.building.*;

public class MediumTank extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {
		try {
			blue = ImageIO.read(new File("sprites/environment/units/vehicles/med_tank_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/vehicles/med_tank_red.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MediumTank(int team) { super("MediumTank", 8, 12, 5, 1, 1500, false, team, team > 0 ? red : blue, swingGame2D.root); }
}
