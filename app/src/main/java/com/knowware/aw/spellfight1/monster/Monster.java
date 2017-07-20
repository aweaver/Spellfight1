package com.knowware.aw.spellfight1.monster;

import android.widget.ImageButton;

/**
 * Created by Aaron on 5/5/2017.
 */

public class Monster {
    private int ID;
    private int x;
    private int y;
    private ImageButton imgBtn;
    private boolean bDead;

    Monster(int sID, int sX, int sY, ImageButton sImgbtn) {
        ID = sID;
        x = sX;
        y = sY;
        imgBtn = sImgbtn;
        bDead = false;
    }

    public int getX() {
        return (x);
    }

    public int getY() {
        return (y);
    }

    public boolean getIfDead() {
        return (bDead);
    }

    public void setIfDead(boolean sIfDead) {
        bDead = sIfDead;
    }

    public ImageButton getImgBtn() {
        return (imgBtn);
    }

    public int getMonsterId()
    {
        return(ID);
    }

}
