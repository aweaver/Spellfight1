package com.knowware.aw.spellfight1.util;

import java.util.Random;

/**
 * Created by Aaron on 5/14/2017.
 */

public class RandNum
{
   private Random r;

    public  RandNum()
    {
       r=new Random();
    }

    public int getInt(int min, int max)
    {
        int i1 = r.nextInt(max - min + 1) + min;

        return(i1);
    }
}
