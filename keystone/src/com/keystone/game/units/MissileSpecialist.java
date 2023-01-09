package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.keystone.game.*;

public class MissileSpecialist extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {
		try {
			blue = ImageIO.read(new File("sprites/environment/units/infantry/misslespec_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/infantry/misslespec_red.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MissileSpecialist(int team) { super("Missile Specialist", 8, 7, 3, 1, 1200, false, team, team > 0 ? red : blue, swingGame2D.root); }
}
