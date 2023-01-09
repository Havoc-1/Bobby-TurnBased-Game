package com.keystone.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.keystone.game.building.*;
import com.keystone.game.units.*;

public class TileGrid {
    protected static Tile[][] map;

    protected ArrayList<Unit> units = new ArrayList<Unit>();
    protected ArrayList<Building> buildings = new ArrayList<Building>();

    private int grassCtr = 0, villageCtr = 0, forestCtr = 0, HQCtr = 0, factoryCtr = 0, cityCtr = 0, hillCtr = 0, mountainCtr = 0, dirtRoadCtr = 0, pavedRoadCtr = 0, tileCtr = 0;

    private boolean isRunning = true;

    public TileGrid(int width, int height) {
        map = new Tile[width][height];
        generateRandomMap();
        displayMap();
        try {
            drawMap(swingGame2D.root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateRandomMap(){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                Random r = new Random();
                int choice = r.nextInt(8); // gives a value between 0 inclusive and 9 exclusive

                if ((choice == 0 && forestCtr < 30)){
                    map[i][j] = new Tile(i, j, "Forest");
                    forestCtr++;
                    tileCtr++;
                }else if (choice == 1 && villageCtr < 4 && i >3){
                    map[i][j] = new Tile(i, j, "Village");
                    villageCtr++;
                    tileCtr++;
                }else if (choice == 2 && factoryCtr < 4 && i > 5){
                    map[i][j] = new Tile(i, j, "Factory");
                    factoryCtr++;
                    tileCtr++;
                }else if (choice == 3 && cityCtr < 6 && i > 3){
                    map[i][j] = new Tile(i, j, "City");
                    cityCtr++;
                    tileCtr++;
                }else if (choice == 4 && hillCtr < 8){
                    map[i][j] = new Tile(i, j, "Hill");
                    hillCtr++;
                    tileCtr++;
                }else if (choice == 5 && mountainCtr < 5){
                    map[i][j] = new Tile(i, j, "Mountain");
                    mountainCtr++;
                    tileCtr++;
                }else if (choice == 6 && dirtRoadCtr < 10) {
                    map[i][j] = new Tile(i, j, "Dirt Road");
                    dirtRoadCtr++;
                    tileCtr++;
                }else if (choice == 7 && pavedRoadCtr < 6){
                    map[i][j] = new Tile(i, j, "Paved Road");
                    pavedRoadCtr++;
                    tileCtr++;
                }else{
                    map[i][j] = new Tile(i, j, "Grass");
                    grassCtr++;
                }
            }
        }
    }

    public void displayMap(){
        for (int i = 0; i < map.length; i++){
            System.out.println(" ");
            for (int j = 0; j < map[i].length; j++)
                System.out.print(this.map[j][i].getType() + " ");
        }
        System.out.println("\nCounters -- Grass = " + grassCtr + ", Village = " + villageCtr + ", Forest = " + forestCtr + ", HQ = " + HQCtr + ", Factory = " + factoryCtr + ", City = " + cityCtr + ", Hill = " + hillCtr + ", Mountain = " + mountainCtr + ", Dirt Road = " + dirtRoadCtr + ", Paved Road = " + pavedRoadCtr);
    }

    public void drawMap(JComponent parent) throws IOException {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                BufferedImage img;
                if (this.map[j][i].getType() == "Grass") {
                    img = ImageIO.read(new File("sprites/environment/grass_sprite.png"));
                } else if (this.map[j][i].getType() == "Forest") {
                    img = ImageIO.read(new File("sprites/environment/forest.png"));
                } else if (this.map[j][i].getType() == "Hill") {
                    img = ImageIO.read(new File("sprites/environment/hill.png"));
                } else if (this.map[j][i].getType() == "Mountain") {
                    img = ImageIO.read(new File("sprites/environment/mountain.png"));
                } else if (this.map[j][i].getType() == "Paved Road") {
                    img = ImageIO.read(new File("sprites/environment/horiz_road.png"));
                } else if (this.map[j][i].getType() == "Dirt Road") {
                    img = ImageIO.read(new File("sprites/environment/dirt_road.png"));
                } else {
                    img = ImageIO.read(new File("sprites/environment/grass_sprite.png"));
                }
                ImageIcon icon = new ImageIcon(img.getScaledInstance(swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y,
                        java.awt.Image.SCALE_SMOOTH));
                JLabel label = new JLabel();
                label.setIcon(icon);
                int xpos = i * swingGame2D.TILE_SIZE_X;
                int ypos = j * swingGame2D.TILE_SIZE_Y;
                label.setBounds(xpos, ypos, swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y);
                parent.add(label);
            }
        }
    }

    public void setOccupy(boolean choice, Tile t) {
        t.isOccupied = choice;
    }

    public void setUnitID(int id, Tile t) {
        t.unitID = id;
    }

    public String getTileType(int x, int y) {
        return map[y][x].getType();
    }

    public int getTileUnitID(int x, int y) {
        return map[y][x].getUnitID();
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && y >= 0 && y < map.length && x < map[0].length)
            return map[y][x];
        return null;
    }

    public Tile[][] getMap() {
        return this.map;
    }

    public void update(float deltaTime) {
        try {
            if (!isRunning)
                return;
            for (Building building : buildings) {
                building.tick(deltaTime);
            }
            for (Unit unit : new ArrayList<Unit>(units)) {
                unit.tick(deltaTime);
            }

            swingGame2D.root.revalidate();
            swingGame2D.root.repaint();
        }catch(NullPointerException e){}
    }

    public void onTurn() {
        if (!isRunning)
            return;
        if (!buildings.isEmpty())
            for (Building building : buildings) {
                building.onTurn();
            }
        try {
            if (!units.isEmpty())
                for (Unit unit : units) {
                    unit.onTurn();
                }
        } catch (ConcurrentModificationException e){}
    }

    public void onBackgroundClicked() {
        if (!isRunning)
            return;
        if (!buildings.isEmpty())
            for (Building building : buildings) {
                building.onLostFocus();
            }
        if (!units.isEmpty())
            for (Unit unit : units) {
                unit.onLostFocus();
            }
    }

    public void addUnit(Unit unit, int x, int y, int team) {
        units.add(unit);
        unit.setPosition(x, y);
        unit.setTeam(team);
    }

    public void resetUnitAction() {
        for (Unit u: units)
            u.resetTurnBoolean();
    }

    public void destroyUnit(Unit unit) {
        swingGame2D.root.remove(unit.getButton());
        setOccupy(false, swingGame2D.getMap().getTile(unit.getLocation().first, unit.getLocation().second));
        units.remove(unit);
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public ArrayList<Unit> getUnits(){
        return units;
    }

    public Unit getUnitAt(int x, int y) {
        for (Unit u : new ArrayList<Unit>(units)) {
            if (u.getLocation().first == x && u.getLocation().second == y)
                return u;
        }
        return null;
    }

    public void stop() {
        isRunning = false;
    }

}