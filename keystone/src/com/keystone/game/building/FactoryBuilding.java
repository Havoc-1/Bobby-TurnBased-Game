package com.keystone.game.building;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.keystone.game.swingGame2D;
import com.keystone.game.units.*;

public class FactoryBuilding extends Building {

    static List<Unit> redUnits;
    static List<Unit> blueUnits;

    Random r = new Random();

    static {
        blueUnits = List.of(new Rifleman(0), new MissileSpecialist(0), new Commando(0), new Recon(0), new LightTank(0), new MediumTank(0), new APC(0), new Artillery(0), new RocketTank(0)); // units that blue can build
        redUnits = List.of(new Rifleman(1), new MissileSpecialist(1), new Commando(1), new Recon(1), new LightTank(1), new MediumTank(1), new APC(1), new Artillery(1), new RocketTank(1)); // units that red can build
    }

    static String[] buildableRedUnits;
    static String[] buildableBlueUnits;

    static BufferedImage blue;
    static BufferedImage red;
    static BufferedImage neutral;
    protected JComboBox<?> dropdown;
    static {
        try {
            blue = ImageIO.read(new File("sprites/environment/buildings/factory_blue.png"));
            red = ImageIO.read(new File("sprites/environment/buildings/factory_red.png"));
            neutral = ImageIO.read(new File("sprites/environment/buildings/factory_neutral.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public FactoryBuilding(int x, int y, int team) throws IOException {
        super("Factory", x, y, 100, team, blue, red, neutral, swingGame2D.root);
        buildableRedUnits = new String[redUnits.size()];
        buildableBlueUnits = new String[blueUnits.size()];
        for (int i = 0; i < redUnits.size(); i++) {
            Unit u = redUnits.get(i);
            buildableRedUnits[i] = u.getName().substring(0, u.getName().length() > 9 ? 10 : u.getName().length())
                    + ((u.getName().length() > 8) ? "... $" : " $") + redUnits.get(i).getCost();
        }
        for (int i = 0; i < blueUnits.size(); i++) {
            Unit u = blueUnits.get(i);
            buildableBlueUnits[i] = u.getName().substring(0, u.getName().length() > 9 ? 10 : u.getName().length())
                    + ((u.getName().length() > 8) ? "... $" : " $") + blueUnits.get(i).getCost();
        }
        dropdown = new JComboBox(team > 0 ? buildableRedUnits : buildableBlueUnits);
        dropdown.setToolTipText("Click to buy");
        dropdown.setBounds(0, 0, 150, 20);
        dropdown.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (swingGame2D.BALANCE[swingGame2D.currentTeam] >= (team < 1
                        ? redUnits.get(dropdown.getSelectedIndex()).getCost()
                        : blueUnits.get(dropdown.getSelectedIndex()).getCost())) {
                    swingGame2D.BALANCE[swingGame2D.currentTeam] -= (team < 1
                            ? redUnits.get(dropdown.getSelectedIndex()).getCost()
                            : blueUnits.get(dropdown.getSelectedIndex()).getCost());
                    Unit u = new Unit(team < 1 ? redUnits.get(dropdown.getSelectedIndex())
                            : blueUnits.get(dropdown.getSelectedIndex()));
                    u.preventMove();
                    swingGame2D.getMap().addUnit(u, FactoryBuilding.this.x, FactoryBuilding.this.y,
                            FactoryBuilding.this.team);
                } else {
                    JOptionPane.showMessageDialog(parent, "Not enough money!");
                }
                parent.remove(dropdown);
            }
        });
    }

    private void buildRandomUnit() {
        int index = r.nextInt(dropdown.getItemCount());
        if (swingGame2D.BALANCE[swingGame2D.currentTeam] >= redUnits.get(index).getCost()) {
            swingGame2D.BALANCE[swingGame2D.currentTeam] -= redUnits.get(index).getCost();
            Unit u = new Unit(redUnits.get(index));
            u.enableAI();
            u.preventMove();
            swingGame2D.getMap().addUnit(u, FactoryBuilding.this.x, FactoryBuilding.this.y, FactoryBuilding.this.team);
        }
    }

    @Override
    public void onTurn() {
        if (swingGame2D.isAIEnabled() && swingGame2D.currentTeam > 0 && this.team == swingGame2D.currentTeam) {
            if (swingGame2D.getMap().getUnitAt(x, y) == null) {
                buildRandomUnit();
            }
        }
        super.onTurn();
    }

    @Override
    public void onClick() {
        if (swingGame2D.isAIEnabled() && swingGame2D.currentTeam > 0 && this.team == swingGame2D.currentTeam)
            return;

        if (swingGame2D.getMap().getUnitAt(x, y) != null) {
            JOptionPane.showMessageDialog(parent, "Can't place onether unit here!");
        }

        if (swingGame2D.currentTeam == this.team) {
            int xpos = x * swingGame2D.TILE_SIZE_X;
            int ypos = y * swingGame2D.TILE_SIZE_Y - 20;
            if (xpos + 150 > swingGame2D.WIDTH || ypos <= 0)
                dropdown.setLocation(swingGame2D.WIDTH - 165, (y + 1) * swingGame2D.TILE_SIZE_Y);
            else
                dropdown.setLocation(xpos, ypos);
            parent.add(dropdown, 0);
        }
    }

    @Override
    public void onLostFocus() {
        parent.remove(dropdown);
    }
}