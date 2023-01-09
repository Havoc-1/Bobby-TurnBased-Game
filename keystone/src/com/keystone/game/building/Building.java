package com.keystone.game.building;


import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import com.keystone.game.swingGame2D;
import com.keystone.game.units.Unit;

public class Building {

    String name;
    int income;
    Icon icon_blue;
    Icon icon_red;
    Icon icon_neutral;
    JComponent parent;
    protected int x, y;
    protected JButton button;
    protected int team;
    protected int id;

    protected int roundsToCapture = 3;

    public Building(String name, int x, int y, int income, int team, BufferedImage img_blue, BufferedImage img_red, BufferedImage img_neutral,
                    JComponent parent) {
        this.name = name;
        this.income = income;
        this.parent = parent;
        this.team = team;
        this.icon_blue = new ImageIcon(img_blue.getScaledInstance(swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y,
                java.awt.Image.SCALE_SMOOTH));
        this.icon_red = new ImageIcon(img_red.getScaledInstance(swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y,
                java.awt.Image.SCALE_SMOOTH));
        this.icon_neutral = new ImageIcon(img_neutral.getScaledInstance(swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y,
                java.awt.Image.SCALE_SMOOTH));
        if (team == 0 )
            button = new JButton(icon_blue);
        else if (team == 1)
            button = new JButton(icon_red);
        else
            button = new JButton(icon_neutral);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBounds(0, 0, swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y);
        button.setContentAreaFilled(false);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onClick();
            }
        });
        parent.add(button, 0);
        setPosition(x, y);
    }

    private void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        button.setLocation(x * swingGame2D.TILE_SIZE_X, y * swingGame2D.TILE_SIZE_Y);
    }

    public void tick(float deltaTime) {

    }

    public void changeTeam(int team) {
        this.team = team;
        this.button.setIcon(team > 0 ? icon_red : icon_blue);
    }

    public void onTurn() {
        Unit u = swingGame2D.getMap().getUnitAt(x, y);
        swingGame2D.BALANCE[team] += income;
        if (u != null) {
            if (u.getTeam() != team) {
                roundsToCapture--;
                if (roundsToCapture <= 0) {
                    changeTeam(u.getTeam());
                    System.out.println("Team now: " + u.getTeam());
                    System.out.println("Team now: " + getBuildingTeam());
                    roundsToCapture = 3;
                }
            }
        }
    }

    public int getBuildingTeam() {
        return this.team;
    }

    public int getID() {
        return this.id;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void onClick() {

    }

    public Component getButton() {
        return button;
    }

    public void onLostFocus() {

    }
}
