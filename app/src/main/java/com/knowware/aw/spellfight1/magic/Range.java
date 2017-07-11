package com.knowware.aw.spellfight1.magic;

/**
 * Created by Aaron on 6/8/2017.
 */

public enum Range
{
    AOE(1),
    TGT(2);

    private Integer id;

    Range(final Integer id)
    {
        this.id = id;
    }

    public int getID()
    {
        return id;
    }
}
