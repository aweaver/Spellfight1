package com.knowware.aw.spellfight1.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.knowware.aw.spellfight1.R;
import com.knowware.aw.spellfight1.magic.Range;
import com.knowware.aw.spellfight1.magic.SpellType;
import com.knowware.aw.spellfight1.monster.EnemyType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron on 8/25/2017.
 */

//copy bitmaps
/*

Bitmap bmp1 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.image);
//then create a copy of bitmap bmp1 into bmp2
Bitmap bmp2 = bmp1.copy(bmp1.getConfig(), true);

//if previous copy code fails.
we need to have a clean original and make editable copies.
bmp1 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.image);
bmp2 = bmp1.copy(bmp1.getConfig(), true);
canvasBmp2 = new Canvas( bmp2 );

When I want to copy bmp1 to bmp2:

canvasBmp2.drawBitmap(bmp1, 0, 0, null);

@Override
protected void onDraw(Canvas canvas)
{
    canvas.drawBitmap(bmp2, 0, 0, null);
}
 */

/*
//combining bitmaps

public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }
 */


public class BitmapManager
{
    public static final int ROACH_IMG= R.drawable.roach;
    public static final int PLAYERFIREBALL_IMG= R.drawable.playerfireball;
    public static final int PLAYERFIREWALL_IMG= R.drawable.playerfirewall;

    private Map<Integer, Integer> monsterMaplist;
    private Activity parentActivity;

    public BitmapManager()
    {
        monsterMaplist =new HashMap<Integer,Integer>();//translates monster types to res ids for bitmap
    }

    public void init()
    {
        makeMonsterMapList();
    }

    void makeMonsterMapList()
    {
        monsterMaplist.put(EnemyType.ROACH.getID(),ROACH_IMG);

    }

    void makeBitmapList()
    {
        monsterMaplist.put(EnemyType.ROACH.getID(),ROACH_IMG);

    }

    public void setParentActivity(Activity srcActivity)
    {
        parentActivity= srcActivity;
    }

    public Bitmap getMonsterBmp(EnemyType enemyType)
    {
        int resId;
        Bitmap tempBmp;

        resId=monsterMaplist.get(enemyType.getID());

        tempBmp=getBmapCopy(resId);

        return(tempBmp);
    }

    public int getMonsterResId(EnemyType enemyType)
    {
        int resId;

        resId=monsterMaplist.get(enemyType.getID());
        return(resId);
    }

    /**
     *
     * getPlayerAttackSpellResID- maps spell types to graphics
     *
     * @param spellType
     * @param range
     * @return
     */
    public int getPlayerAttackSpellResID(SpellType spellType, Range range)
    {
        if (spellType==SpellType.FIRE && range==Range.AOE)
            return(BitmapManager.PLAYERFIREWALL_IMG);
        else if(spellType==SpellType.FIRE && range==Range.TGT)
            return(BitmapManager.PLAYERFIREBALL_IMG);
        return(0);// for errors

    }

    public Bitmap getMonsterBmp(int sresId)
    {
        Bitmap tempBmp;
        int resId;

        resId=monsterMaplist.get(sresId);

        tempBmp=getBmapCopy(resId);

        return(tempBmp);
    }

    public Bitmap  getBmapCopy(int resId)
    {
        Bitmap src;
        Bitmap cpy;

        src= BitmapFactory.decodeResource(parentActivity.getResources(),resId);
        cpy = src.copy(src.getConfig(), true);

        return(cpy);
    }

    public Bitmap overlay(Bitmap bmp1, Bitmap bmp2)
    {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(),
                                                bmp1.getHeight(), bmp1.getConfig());

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);

        return bmOverlay;
    }
}
