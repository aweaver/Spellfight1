package com.knowware.aw.spellfight1.monster;

/**
 * Created by Aaron on 8/18/2017.
 */

public enum EnemyType
{
    ERROR(-1),
    NONE(0),
    ROACH(1);

    private Integer id;

    private EnemyType(final Integer id)
    {
        this.id = id;
    }

    public int getID()
    {
        return id;
    }

   static public EnemyType toEnum(int val)
    {
        switch (val)
        {
            case 0:
                return(EnemyType.NONE);

            case 1:
                return(EnemyType.ROACH);

            default:
                return(EnemyType.ERROR);
        }
    }
}
