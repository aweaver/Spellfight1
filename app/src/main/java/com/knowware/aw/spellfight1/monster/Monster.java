package com.knowware.aw.spellfight1.monster;

import android.widget.ImageButton;

/**
 * Created by Aaron on 5/5/2017.
 */

public class Monster {
    private int ID;
    private ImageButton imgBtn;
    private boolean bDead;
    private EnemyType monsterType;

    Monster(int sID,EnemyType monType)
    {
        ID = sID;
        monsterType=monType;
        bDead = false;
    }

    /**
     * getIfDead
     *
     * @return
     */
    public boolean getIfDead() {
        return (bDead);
    }

    /**
     * setIfDead
     *
     * @param sIfDead
     */
    public void setIfDead(boolean sIfDead) {
        bDead = sIfDead;
    }

    /**
     * getMonsterId
     *
     * @return
     */
    public int getMonsterId()
    {
        return(ID);
    }

    /**
     * getType
     *
     * @return
     */
    public EnemyType getType()
    {
        return monsterType;
    }

}
