package com.keystone.game.units;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.keystone.game.*;

public class Recon extends Unit {

	static BufferedImage blue;
	static BufferedImage red;
	static {
		try {
			blue = ImageIO.read(new File("sprites/environment/units/vehicles/recon_blue.png"));
			red = ImageIO.read(new File("sprites/environment/units/vehicles/recon_red.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Recon(int team) { super("Recon", 5, 8, 8, 1, 1300, false, team, team > 0 ? red : blue, swingGame2D.root); }
}

