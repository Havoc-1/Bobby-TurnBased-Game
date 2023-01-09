package com.keystone.game.units;


import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.*;
import java.lang.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.keystone.game.*;

import static com.keystone.game.swingGame2D.frame;
import static com.keystone.game.swingGame2D.root;

public class Unit {
    final static Icon[] healthIcons = new Icon[16];
    static Icon explosionIcon;
    static {
        for (int i = 1; i <= 15; i++) {
            try {
                healthIcons[i] = new ImageIcon(
                        ImageIO.read(new File("sprites/Unit_HPcount/HP_" + i + ".png")).getScaledInstance(
                                swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y, java.awt.Image.SCALE_SMOOTH));
                explosionIcon = new ImageIcon(
                        ImageIO.read(new File("sprites/explosion-dead.png")).getScaledInstance(
                                swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y, java.awt.Image.SCALE_SMOOTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected int atk, HP, moveRange, cost, maxHP, id, atkRange;
    protected String name;
    protected boolean transport;
    protected int x, y, teamID, moveBuff, defenseBuff;
    protected Stack<Unit> transportedUnits = new Stack<>();
    protected static int idCount = 0;
    protected boolean firstInstance = true, atkAction = true, moveAction = true, actionDone = false;
    protected boolean isLoaded = false;
    protected int team;
    protected int transportStorage = 0;

    protected BufferedImage img;
    protected Icon img_left;
    protected Icon img_right;

    JComponent parent;
    protected JButton button;
    protected JLabel healthLabel;
    protected boolean isLeft = true;

    protected boolean isSelected = false;
    protected boolean movedThisTurn = false;
    protected boolean[][] possibleMovesVisited;
    private final ArrayList<JButton> possibleMoves = new ArrayList<>();

    private boolean isAIControlled = false;
    private Unit AITarget = null;
    private final Random r = new Random();

    protected boolean shouldWait = false;
    protected float waitTime = .5f;

    public Unit(String name, int atk, int HP, int moveRange, int atkRange, int cost, boolean transport, int team, BufferedImage img_l, JComponent parent) {
        this.name = name;
        this.atk = atk;
        this.HP = HP;
        this.maxHP = HP;
        this.moveRange = moveRange;
        this.atkRange = atkRange;
        this.cost = cost;
        this.transport = transport;
        this.parent = parent;
        this.team = team;
        this.img = img_l;
        this.img_left = new ImageIcon(
                img_l.getScaledInstance(swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y, java.awt.Image.SCALE_SMOOTH));
        this.img_right = new ImageIcon(Utils.flip(img_l).getScaledInstance(swingGame2D.TILE_SIZE_X,
                swingGame2D.TILE_SIZE_Y, java.awt.Image.SCALE_SMOOTH));
        button = new JButton(team > 0 ? img_right : img_left);
        button.setBorder(BorderFactory.createLineBorder(team > 0 ? Color.red : Color.blue, 2));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onClick();
            }
        });
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBounds(0, 0, swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y);
        button.setContentAreaFilled(false);
        parent.add(button, 0);

        healthLabel = new JLabel(healthIcons[this.HP]);
        healthLabel.setBounds(0, 0, swingGame2D.TILE_SIZE_X, swingGame2D.TILE_SIZE_Y);
        parent.add(healthLabel, 0);
        button.setLocation(-200, -200);
        healthLabel.setLocation(-200, -200);
    }

    public Unit(Unit u) {
        this(u.getName(), u.getAtk(), u.getHP(), u.getMoveRange(), u.getAtkRange(), u.getCost(), u.isTransport(), u.getTeam(), u.img, u.parent);
    }

    public void enableAI() {
        isAIControlled = true;
        AITarget = searchForNearestTarget(x, y);
        if (AITarget == null)
            swingGame2D.win(this.team, true);
    }

    public void setTeam(int team) {
        this.team = team;
        this.button.setBorder(BorderFactory.createLineBorder(this.team > 0 ? Color.red : Color.blue, 2));
    }

    public void onClick() {

        if (isSelected) {
            if (this.name.equals("APC")) {
                if(transportedUnits.size() > 0) {
                    int size = transportedUnits.size();
                    for (int i = 0; i < size; i++) {
                        unloadUnit(transportedUnits.pop());
                    }
                    System.out.println("Units unloaded!");
                }
            }
            removePossibleMoves();
        } else
            drawPossibleMoves();
        isSelected = !isSelected;
    }

    public void changeDirection() {
        if (isLeft) {
            this.button.setIcon(img_right);
        } else {
            this.button.setIcon(img_left);
        }
        isLeft = !isLeft;
    }

    public void loadUnitToTransport(Unit targetUnit){
        if (targetUnit.getTeamID() == this.teamID){
            if (targetUnit.isTransport()){
                if (!isLoaded){
                    if (targetUnit.getTransportedUnits().size() < 2) {
                        System.out.println("You have loaded " + name + id);
                        swingGame2D.getMap().setUnitID(0, swingGame2D.getMap().getTile(this.x, this.y));
                        swingGame2D.getMap().setOccupy(false, swingGame2D.getMap().getTile(this.x, this.y));
                        isLoaded = true;
                        targetUnit.getTransportedUnits().push(this);
                        swingGame2D.root.remove(this.getButton());
                        swingGame2D.getMap().getUnits().remove(this);
                        healthLabel.setVisible(false);
                    } else
                        System.out.println("Transport unit is full!");
                }else
                    System.out.println("Unit is already loaded!");
            }else
                System.out.println("Target unit is not a transport unit.");
        }else if (targetUnit.getID() == this.id)
            System.out.println("Cannot perform action. The unit you are targetting is the same unit.");
        else
            System.out.println("Cannot perform action. The unit you are targetting is not with the same team.");
    }

    public void unloadUnit(Unit targetUnit){
        int xChange = 0, yChange = 0;
        boolean goSignal = false;
        if (swingGame2D.getMap().getTile(this.x+1, this.y).isOccupied == false){
            xChange = 1;
            goSignal = true;
        }else if (swingGame2D.getMap().getTile(this.x-1, this.y).isOccupied == false){
            xChange = -1;
            goSignal = true;
        }else if (swingGame2D.getMap().getTile(this.x, this.y+1).isOccupied == false){
            yChange = 1;
            goSignal = true;
        }else if (swingGame2D.getMap().getTile(this.x, this.y-1).isOccupied == false){
            yChange = -1;
            goSignal = true;
        }
        if (goSignal){
            System.out.println("Unit is unloaded at " + (x+xChange) + ", " + (y+yChange));
            swingGame2D.getMap().setOccupy(true, swingGame2D.getMap().getTile((x+xChange), (y+yChange)));
            swingGame2D.getMap().setUnitID(targetUnit.id, swingGame2D.getMap().getTile((x+xChange), (y+yChange)));
            targetUnit.x = (x+xChange);
            targetUnit.y = (y+yChange);
            targetUnit.isLoaded = false;
            swingGame2D.getMap().addUnit(targetUnit, targetUnit.x, targetUnit.y, targetUnit.team);
            root.add(targetUnit.button,0); //put this after swingGame2d.getmap....
            targetUnit.button.setLocation(targetUnit.x * swingGame2D.TILE_SIZE_X, targetUnit.y * swingGame2D.TILE_SIZE_Y);
            targetUnit.button.repaint();
            targetUnit.healthLabel.setLocation(targetUnit.x * swingGame2D.TILE_SIZE_X, targetUnit.y * swingGame2D.TILE_SIZE_Y);
            targetUnit.healthLabel.setVisible(true);
            root.setComponentZOrder(targetUnit.healthLabel, 0); //put this before the else statement
            SwingUtilities.updateComponentTreeUI(frame);
        }else
            System.out.println("Unit cannot be unloaded. Adjacent tiles are occupied");
        targetUnit.displayCoordinates();
    }

    public void setPosition(int x, int y) {
        Tile t = swingGame2D.getMap().getTile(x, y);
        if (t != null) {
            if (!t.isOccupied) {
                int totalMoves = computeMoves(this.x, x) + computeMoves(this.y, y);
                System.out.println("Total Moves: " + totalMoves + ", Move Range: " + moveRange);
                if (firstInstance) {
                    Unit.idCount++;
                    this.id = idCount;
                    firstInstance = false;

                    swingGame2D.getMap().setUnitID(0, swingGame2D.getMap().getTile(this.x, this.y));
                    swingGame2D.getMap().setOccupy(false, swingGame2D.getMap().getTile(this.x, this.y));
                    this.x = x;
                    this.y = y;
                    swingGame2D.getMap().setOccupy(true, swingGame2D.getMap().getTile(this.x, this.y));
                    swingGame2D.getMap().setUnitID(this.id, swingGame2D.getMap().getTile(this.x, this.y));
                    System.out.println("Unit " + this.id + " moves to " + this.x + ", " + this.y);
                    this.button.setLocation(x * swingGame2D.TILE_SIZE_X, y * swingGame2D.TILE_SIZE_Y);
                    this.healthLabel.setLocation(x * swingGame2D.TILE_SIZE_X, y * swingGame2D.TILE_SIZE_Y);
                    atkAction = false;
                    moveAction = false;
                    removePossibleMoves();
                }
                applyTileBuff();
                if (totalMoves <= this.moveRange) {
                    swingGame2D.getMap().setUnitID(0, swingGame2D.getMap().getTile(this.x, this.y));
                    swingGame2D.getMap().setOccupy(false, swingGame2D.getMap().getTile(this.x, this.y));
                    this.x = x;
                    this.y = y;
                    swingGame2D.getMap().setOccupy(true, swingGame2D.getMap().getTile(this.x, this.y));
                    swingGame2D.getMap().setUnitID(this.id, swingGame2D.getMap().getTile(this.x, this.y));
                    System.out.println("Unit " + this.id + " moves to " + this.x + ", " + this.y);
                    this.button.setLocation(x * swingGame2D.TILE_SIZE_X, y * swingGame2D.TILE_SIZE_Y);
                    this.healthLabel.setLocation(x * swingGame2D.TILE_SIZE_X, y * swingGame2D.TILE_SIZE_Y);
                    moveAction = false;
                    removePossibleMoves();
                }else
                    System.out.println("Location is outside unit's move range.");
            } else {
                Unit other = swingGame2D.getMap().getUnitAt(x, y);
                System.out.println("You can't move here unit " + id + ", its occupied by " + other.id + "!");
                if (other.getTeam() != this.getTeam()) {
                    attack(other);
                    removePossibleMoves();
                }else if (other.getName().equals("APC")) {
                    loadUnitToTransport(other);
                    removePossibleMoves();
                }
            }
        } else
            System.out.println("Your unit has moved already!");
        resetTileBuff();
        checkActionDone();
    }

    public void onUnitInteracted(Unit other) {

    }

    public void displayCoordinates() {
        System.out.println(this.name + " is in (" + x + ", " + y + ")");
    }

    public void attack(Unit targetUnit) {
        System.out.println("Current unit team id: " + getTeam() + ", Target Unit team ID: " + targetUnit.getTeam());
        if (targetUnit == null)
            return;

        if (targetUnit.isLoaded) {
            System.out.println("Unit is loaded!");
            return;
        }

        if (targetUnit.getTeam() != this.team){
            if (!this.isLoaded){
                if (atkAction == true){
                    int totalMoves = computeMoves(this.x, targetUnit.x) + computeMoves(this.y, targetUnit.y);
                    //System.out.println("Total moves" + totalMoves);				debugging
                    if (totalMoves <= atkRange){
                        if (atkRange > 1) {
                            if (totalMoves == 1)
                                System.out.println("Ranged units cannot directly attack tiles adjacent to them.");
                            else {
                                targetUnit.applyTileBuff();
                                System.out.println(this.name + "(" + this.HP + ") attacks " + targetUnit.getName() + "(" + targetUnit.getHP() + ")!");
                                int newHP = targetUnit.getHP() - this.atk + targetUnit.defenseBuff;
                                targetUnit.setHP(newHP);

                                if (newHP <= 0) {
                                    AITarget = searchForNearestTarget(x, y);
                                    if (AITarget == null)
                                        swingGame2D.win(this.team, true);
                                }

                                System.out.println(targetUnit.getName() + " is now at " + "(" + targetUnit.getHP() + ")!");
                                atkAction = false;
                                targetUnit.resetTileBuff();
                                checkActionDone();
                            }
                        }else {
                            targetUnit.applyTileBuff();
                            System.out.println(this.name + "(" + this.HP + ") attacks " + targetUnit.getName() + "(" + targetUnit.getHP() + ")!");
                            int newHP = targetUnit.getHP() - this.atk + targetUnit.defenseBuff;
                            targetUnit.setHP(newHP);

                            if (newHP <= 0) {
                                AITarget = searchForNearestTarget(x, y);
                                if (AITarget == null)
                                    swingGame2D.win(this.team, true);
                            }

                            System.out.println(targetUnit.getName() + " is now at " + "(" + targetUnit.getHP() + ")!");
                            atkAction = false;
                            targetUnit.resetTileBuff();
                            checkActionDone();
                        }
                    } else
                        System.out.println("Target unit is outside of the current unit's attack range.");
                } else
                    System.out.println("Cannot perform action. The unit has already attacked.");
            }else
                System.out.println("Unit is in transport!");
        }else if (targetUnit.getID() == this.id)
            System.out.println("Cannot perform action. The unit you are targetting is the same unit.");
        else
            System.out.println("Cannot perform action. The unit you are targetting is with the same team.");
    }

    public void tick(float deltaTime) {
        if (shouldWait) {
            waitTime -= deltaTime;
            if (waitTime <= 0.0f) {
                shouldWait = false;
                swingGame2D.getMap().destroyUnit(this);
                swingGame2D.enableSkipping(true);
            }
        }

    }

    public void onTurn() {
        allowMovement();
        if (isAIControlled) {
            onAITurn();
        }
    }

    private void onAITurn() {
        if(button.isEnabled())
            pickRandomMove();
    }

    private void pickRandomMove() {
        AITarget = searchForNearestTarget(x, y);
        int pct = r.nextInt(101);
        if (AITarget == null)
            return;
        int AIAggressiveness = 70;
        if (pct < AIAggressiveness
                && Math.sqrt((x - AITarget.x) * (x - AITarget.x) + (y - AITarget.y) * (y - AITarget.y)) < moveRange) {
            setPosition(AITarget.x, AITarget.y);
        } else {
            moveToRandomPos();
        }
    }

    public void moveToRandomPos() {
        try {
            double a = r.nextDouble() * Math.PI * 2.0d;
            int posx = x + Math.round((float) Math.cos(a) * (r.nextInt(moveRange) + 1));
            Utils u = new Utils();
            u.clamp(posx, 0, swingGame2D.MAP_HEIGHT);
            int posy = y + Math.round((float) Math.sin(a) * (r.nextInt(moveRange) + 1));
            u.clamp(posx, 0, swingGame2D.MAP_HEIGHT);
            setPosition(posx, posy);
        } catch(IllegalArgumentException e){}
    }


    private Unit searchForNearestTarget(int xPos, int yPos) {
        boolean[][] targetSearchVisited = new boolean[swingGame2D.MAP_WIDTH][swingGame2D.MAP_HEIGHT];
        // Stores indices of the matrix cells
        Queue<Utils.pair> q = new LinkedList<>();

        // Mark the starting cell as visited
        // and push it into the queue
        q.add(new Utils.pair(xPos, yPos));
        targetSearchVisited[xPos][yPos] = true;

        // Iterate while the queue
        // is not empty
        while (!q.isEmpty()) {
            Utils.pair cell = q.peek();
            int x = cell.first;
            int y = cell.second;

            Unit u = swingGame2D.getMap().getUnitAt(x, y);
            if (u != null) {
                if (u.getTeam() != this.team && u.getHP() > 0) {
                    return u;
                }
            }
            q.remove();

            // Go to the adjacent cells
            for (int i = 0; i < 4; i++) {
                int adjx = x + Utils.dRow[i];
                int adjy = y + Utils.dCol[i];

                if (Utils.isValid(targetSearchVisited, adjx, adjy)) {
                    q.add(new Utils.pair(adjx, adjy));
                    targetSearchVisited[adjx][adjy] = true;
                }
            }
        }
        return null;
    }

    public void applyTileBuff(){
        switch (swingGame2D.getMap().getTileType(this.x, this.y)) {
            case "Forest":
                this.moveBuff = -1;
                this.defenseBuff = 3;
                break;
            case "Grass":
                this.moveBuff = 0;
                this.defenseBuff = 0;
                break;
            case "City":
                this.moveBuff = 2;
                this.defenseBuff = 3;
                break;
            case "Factory":
                this.moveBuff = 2;
                this.defenseBuff = 1;
                break;
            case "HQ":
                this.moveBuff = 2;
                this.defenseBuff = 3;
                break;
            case "Village":
                this.moveBuff = 2;
                this.defenseBuff = 2;
                break;
            case "Hill":
                this.moveBuff = -2;
                this.defenseBuff = 2;
                break;
            case "Mountain":
                this.moveBuff = -3;
                this.defenseBuff = 3;
                break;
            default:
                this.defenseBuff = 0;
                this.moveBuff = 0;
                break;
        }
        System.out.println(swingGame2D.getMap().getTileType(this.x, this.y) + " " + this.moveBuff + " move, " + this.defenseBuff + " defense");
        this.moveRange += this.moveBuff;

    }

    protected int computeMoves(int unitPosition, int targetPosition){
        if (unitPosition > targetPosition)					// 5 > 3
            return unitPosition - targetPosition;
        else if (unitPosition < targetPosition)				// 3 < 5
            return targetPosition - unitPosition;
        else												// 5 = 5
            return 0;
    }

    public void resetTileBuff(){
        this.moveRange -= this.moveBuff;
    }

    public void resetTurnBoolean(){
        atkAction = true;
        moveAction = true;
        actionDone = false;
    }

    public void printBooleans(){
        System.out.println("atk action: " + atkAction + " move action: " + moveAction + " actionDone: " + actionDone);
    }

    public void checkActionDone(){
        if (!atkAction && !moveAction){
            actionDone = true;
            //System.out.println("Unit turn done!");
        }
    }

    public void endActionEarly(){
        actionDone = true;
    }

    public boolean isAlive() {
        return this.HP > 0;
    }

    public String getName() {
        return this.name;
    }

    public int getAtk() {
        return this.atk;
    }

    public int getHP() {
        return this.HP;
    }

    public void setHP(int i) {
        this.HP = Math.max(i, 0);
        this.healthLabel.setIcon(healthIcons[this.HP]);
        if (this.HP <= 0) {
            this.button.setIcon(explosionIcon);
            swingGame2D.enableSkipping(false);
            shouldWait = true;
        }
    }

    public int getTeamID(){
        return this.teamID;
    }

    public void setTeamID(int i){
        this.teamID = i;
    }

    public int getMoveRange(){
        return this.moveRange;
    }

    public int getMaxHP(){
        return this.maxHP;
    }

    public int getID(){
        return this.id;
    }

    public int getAtkRange(){
        return this.atkRange;
    }

    public int getCost(){
        return this.cost;
    }

    public boolean isTransport(){
        return this.transport;
    }

    public int getTeam() {
        return this.team;
    }

    public JButton getButton() {
        return button;
    }

    public Utils.pair getLocation() {
        return new Utils.pair(x, y);
    }

    public Icon getIcon() {
        return img_right;
    }

    public int getTransportStorage() {
        return transportStorage;
    }

    public void displayStats(){
        System.out.println("Unit Stats");
        System.out.println("Name: " + getName());
        System.out.println("Atk: " + getAtk());
        System.out.println("HP: " + getHP() + "/" + getMaxHP());
        System.out.println("Move Range: " + getMoveRange());
        System.out.println("Atk Range: " + getAtkRange());
        System.out.println("Cost: " + getCost());
        System.out.println("Tranport: " + isTransport());
        System.out.println("ID: " + getID());
        System.out.println("Team ID: " + getTeamID());
    }

    public void drawPossibleMoves() {
        if (movedThisTurn)
            return;

        int totalMoves = computeMoves(this.x, x) + computeMoves(this.y, y);
        possibleMovesVisited = new boolean[swingGame2D.MAP_WIDTH][swingGame2D.MAP_HEIGHT];
        int x0 = this.x;
        int y0 = this.y;

        possibleMovesVisited[x0][y0] = true;

        if (x > 0)
            addPossibleMove(moveRange, x0 - 1, y0);
        if (x < swingGame2D.MAP_WIDTH - 1)
            addPossibleMove(moveRange, x0 + 1, y0);
        if (y > 0)
            addPossibleMove(moveRange, x0, y0 - 1);
        if (y < swingGame2D.MAP_HEIGHT - 1)
            addPossibleMove(moveRange, x0, y0 + 1);
    }

    public Stack<Unit> getTransportedUnits(){
        return transportedUnits;
    }

    private void addPossibleMove(float i, int x, int y) {

        switch (swingGame2D.getMap().getTileType(x, y)) {
            case "Forest":
                i -= .2f;
                break;
            case "Grass":
                break;
            case "Paved Road":
                i += .2f;
                break;
            case "Dirt Road":
                i += .1f;
                break;
            case "Hill":
                i -= .2f;
                break;
            case "Mountain":
                i -= .3f;
                break;
            case "City":
                i += .2f;
                break;
            case "Factory":
                i += .2f;
                break;
            case "Headquarters":
                i += .2f;
                break;
            case "Village":
                i += .2f;
                break;
            case "HQ":
                i += .2f;
                break;
            default:
                System.err.println("Unexpected tile: " + swingGame2D.getMap().getTileType(x, y));
        }

        if (i <= 0)
            return;

        possibleMovesVisited[x][y] = true;
        JButton button = new JButton();
        if (isAIControlled || this.team != swingGame2D.currentTeam)
            button.setEnabled(false);
        button.setBounds(x * swingGame2D.TILE_SIZE_X, y * swingGame2D.TILE_SIZE_Y, swingGame2D.TILE_SIZE_X,
                swingGame2D.TILE_SIZE_Y);
        button.setContentAreaFilled(false);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (button.getLocation().x < Unit.this.button.getLocation().x)
                    Unit.this.button.setIcon(img_left);
                if (button.getLocation().x > Unit.this.button.getLocation().x)
                    Unit.this.button.setIcon(img_right);
                removePossibleMoves();
                isSelected = false;
                preventMove();
                setPosition(button.getLocation().x / swingGame2D.TILE_SIZE_X,
                        button.getLocation().y / swingGame2D.TILE_SIZE_Y);
            }
        });
        parent.add(button, 0);
        possibleMoves.add(button);

        if (x > 0)
            if (!possibleMovesVisited[x - 1][y])
                addPossibleMove(i-1.0f, x - 1, y);
        if (x < swingGame2D.MAP_WIDTH - 1)
            if (!possibleMovesVisited[x + 1][y])
                addPossibleMove(i-1.0f, x + 1, y);
        if (y > 0)
            if (!possibleMovesVisited[x][y - 1])
                addPossibleMove(i - 1.0f, x, y - 1);
        if (y < swingGame2D.MAP_HEIGHT - 1)
            if (!possibleMovesVisited[x][y + 1])
                addPossibleMove(i - 1.0f, x, y + 1);
    }

    public void removePossibleMoves() {
        for (JButton move : possibleMoves) {
            parent.remove(move);
        }
        possibleMoves.clear();
    }

    public void preventMove() {
        movedThisTurn = true;
        Unit.this.button.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }

    public void allowMovement() {
        this.movedThisTurn = false;
        this.button.setBorder(BorderFactory.createLineBorder(this.team > 0 ? Color.red : Color.blue, 2));
    }

    public void onLostFocus() {
        isSelected = false;
        removePossibleMoves();
    }
}