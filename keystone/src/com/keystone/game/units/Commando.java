package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.keystone.game.*;

public class Commando extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {
		try {
			blue = ImageIO.read(new File("sprites/environment/units/infantry/commando_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/infantry/commando_red.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Commando(int team) { super("Commando", 4, 6, 8, 1, 1250, false, team, team > 0 ? red : blue, swingGame2D.root); }
}