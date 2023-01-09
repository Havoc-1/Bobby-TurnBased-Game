package com.keystone.game;

public class Tile {
    private int x, y;
    private String type;
    public boolean isOccupied = false, isActionDone = false, resourceProducer = false, unitProducer = false;
    public int unitID = 0;

    public Tile(int x, int y, String type){
        this.x = x;
        this.y = y;
        this.type = type;
        checkTileTypeBooleans();
    }

    public void printTileTypeBooleans(){
        System.out.println("isResourceProducer: " + isResourceProducer() + " isUnitProducer: " + isUnitProducer());
    }

    public void checkTileTypeBooleans(){
        if (this.type == "HQ"){
            resourceProducer = true;
            unitProducer = true;
        }else if (this.type == "City" || this.type == "Village")
            resourceProducer = true;
        else if (this.type == "Factory")
            unitProducer = true;
    }

    public boolean isResourceProducer(){
        return resourceProducer;
    }

    public boolean isUnitProducer(){
        return unitProducer;
    }

    public void displayTileStatus(){
        System.out.println(this.x + ", " + this.y + ", " + this.type + ", Occupied: " + this.isOccupied + " by unit ID " + unitID);
    }

    public int getUnitID(){
        return this.unitID;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String getType(){
        return this.type;
    }

    public boolean getOccupy(){
        return isOccupied;
    }
}