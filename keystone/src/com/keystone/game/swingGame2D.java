package com.keystone.game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.keystone.game.building.*;
import com.keystone.game.units.*;
import java.util.ArrayList;

public class swingGame2D {
    public final static JFrame frame = new JFrame();
    public final static int WIDTH = 600;
    public final static int HEIGHT = 600;
    public final static JPanel root = new JPanel();
    final static JLabel fpsCounter = new JLabel();
    final static JLabel moneyLabel = new JLabel();
    final static JButton nextRoundButton = new JButton();
    public final static int MAP_WIDTH = 12;
    public final static int MAP_HEIGHT = 12;
    public static ArrayList<Unit> unitList;

    public static int TILE_SIZE_X = (WIDTH - (WIDTH / MAP_HEIGHT * 3 / 16)) / MAP_WIDTH;
    public static int TILE_SIZE_Y = (HEIGHT - (HEIGHT / MAP_HEIGHT) / 2) / MAP_HEIGHT;

    final static TileGrid map = new TileGrid(MAP_WIDTH, MAP_HEIGHT);

    static boolean isRunning = true;

    static boolean isAIEnabled = true;                  // true for Singleplayer, false for 2-Player

    public final static int[] BALANCE = new int[3];

    public static int currentTeam = 0;

    public static void main(String[] args) {
        //initializeUnitList();
        start();
        long t1, t2;
        t1 = t2 = System.nanoTime();
        while (true) {
            t2 = System.nanoTime();
            float deltaTime = ((float) (t2 - t1)) / 1000000000.0F;
            t1 = t2;
            update(deltaTime);
        }
    }

    public static void start() {
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setName("Swing game");
        frame.add(root);

        root.setLayout(null);
        root.setBounds(0, 0, WIDTH, HEIGHT);
        root.add(fpsCounter);
        root.add(moneyLabel);
        root.add(nextRoundButton);
        root.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                map.onBackgroundClicked();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });

        nextRoundButton.setBounds((int) (TILE_SIZE_X * (MAP_WIDTH - 1.5f)), (int) (TILE_SIZE_Y * (MAP_HEIGHT - 1.5f)),
                TILE_SIZE_X, TILE_SIZE_Y);
        nextRoundButton.setText(">");
        nextRoundButton.setBackground(currentTeam > 0 ? Color.red : Color.blue);
        nextRoundButton.setForeground(currentTeam > 0 ? Color.black : Color.white);
        nextRoundButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                map.resetUnitAction();
                nextRoundButton.setEnabled(false);
                map.onBackgroundClicked();
                map.onTurn();
                currentTeam = (currentTeam + 1) % 2;
                nextRoundButton.setBackground(currentTeam > 0 ? Color.red : Color.blue);
                nextRoundButton.setForeground(currentTeam > 0 ? Color.black : Color.white);
                moneyLabel.setText("Money: " + BALANCE[currentTeam]);
                nextRoundButton.setEnabled(true);
                if (isAIEnabled && currentTeam > 0)
                    nextRoundButton.doClick();
            }
        });

        fpsCounter.setBounds(0, 0, 150, 20);
        moneyLabel.setBounds(0, 0, 150, 20);
        moneyLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        try {
            placeBuildings();
            // buildings:
            // team 0
            map.addBuilding(new BaseBuilding(0, 11, 0));
            map.addBuilding(new FactoryBuilding(0, 10, 0));
            map.addBuilding(new FactoryBuilding(1, 11, 0));
            // team 1
            map.addBuilding(new BaseBuilding(11, 0, 1));
            map.addBuilding(new FactoryBuilding(10, 0, 1));
            map.addBuilding(new FactoryBuilding(11, 1, 1));

            // units:
            // team 0
            map.addUnit(new Rifleman(0), 0, 10, 0);
            map.addUnit(new Rifleman(0), 1, 11, 0);
            map.addUnit(new LightTank(0), 1, 10, 0);

            // team 1
            Unit e1 = new Rifleman(1);
            Unit e2 = new Rifleman(1);
            Unit e3 = new LightTank(1);
            Unit e4 = new MissileSpecialist(1);
            Unit e5 = new Commando(1);
            Unit e6 = new Recon(1);
            Unit e7 = new MediumTank(1);
            Unit e8 = new APC(1);
            Unit e9 = new Artillery(1);
            Unit e10 = new RocketTank(1);
            map.addUnit(e1, 10, 0, 1);
            map.addUnit(e2, 11, 1, 1);
            map.addUnit(e3, 10, 1, 1);
            // should team 1 be controlled by ai?
            if (isAIEnabled) {
                e1.enableAI();
                e2.enableAI();
                e3.enableAI();
                e4.enableAI();
                e5.enableAI();
                e6.enableAI();
                e7.enableAI();
                e8.enableAI();
                e9.enableAI();
                e10.enableAI();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        moneyLabel.setText("Money: " + BALANCE[currentTeam]);

        root.setComponentZOrder(fpsCounter, 0);
        root.setComponentZOrder(moneyLabel, 0);
        root.setComponentZOrder(nextRoundButton, 0);
    }

    public static void enableSkipping(boolean b) {
        nextRoundButton.setEnabled(b);
    }

    static float fpsTimer = .2f;

    public static void update(float deltaTime) {
        map.update(deltaTime);

        moneyLabel.setText("Money: " + BALANCE[currentTeam]);

        if (fpsTimer <= 0) {
            fpsCounter.setText(Float.toString(1.0F / deltaTime) + "FPS");
            fpsTimer = .2f;
        }
        fpsTimer -= deltaTime;

    }

    public static TileGrid getMap() {
        return map;
    }

    public static void win(int team, boolean isAI) {
        map.stop();
        if (isAI) {
            if (team > 0) {
                JOptionPane.showMessageDialog(root, "You lost!");
            } else {
                JOptionPane.showMessageDialog(root, "You won!");
            }
        } else {
            JOptionPane.showMessageDialog(root, "Team: " + team + " wins!");
        }
    }
    public static void placeBuildings() throws IOException {
        for (int i = 0; i < MAP_WIDTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                Tile t = map.getTile(i, j);
                if (t.getType().equals("Factory"))
                    map.addBuilding(new FactoryBuilding(i, j, 2));
                else if (t.getType().equals("City"))
                    map.addBuilding(new CityBuilding(i, j, 2));
                else if (t.getType().equals("Village"))
                    map.addBuilding(new VillageBuilding(i, j, 2));
            }
        }
    }

    public static boolean isAIEnabled() {
        return isAIEnabled;
    }

}

