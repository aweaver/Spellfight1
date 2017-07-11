package com.knowware.aw.spellfight1.magic;

/**
 * Created by Aaron on 6/8/2017.
 */

public enum GestureType
{
    NONE(0),
    LEFT_FLING(1),
    RIGHT_FLING(2),
    UP_FLING(3),
    DN_FLING(4),
    TAP(5);

    private Integer id;

    private GestureType(final Integer id)
    {
        this.id = id;
    }

    public int getID()
    {
        return id;
    }

    static public GestureType fromEnum(int i)
    {
        switch (i)
        {
            case 1:
                return(GestureType.LEFT_FLING);

            case 2:
                return(GestureType.RIGHT_FLING);


            case 3:
                return(GestureType.UP_FLING);


            case 4:
                return(GestureType.DN_FLING);

            case 5:
                return(GestureType.TAP);

            default:
                return(GestureType.NONE);

        }
    }
}
