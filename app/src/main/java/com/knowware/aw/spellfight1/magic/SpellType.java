package com.knowware.aw.spellfight1.magic;

/**
 * Created by Aaron on 6/8/2017.
 */

public enum SpellType
{
    NONE(0),
    FIRE(1);

    private Integer id;

    SpellType( Integer id)
    {
        this.id = id;
    }

    public int getID()
    {
        return id;
    }

    static public SpellType fromEnum(int i)
    {
        switch (i)
        {
            case 1:
                return(SpellType.FIRE);

            default:
                return(SpellType.NONE);

        }
    }

}
