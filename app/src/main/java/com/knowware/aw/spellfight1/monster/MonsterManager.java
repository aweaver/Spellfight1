package com.knowware.aw.spellfight1.monster;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.knowware.aw.spellfight1.GameDimensions;
import com.knowware.aw.spellfight1.R;
import com.knowware.aw.spellfight1.UI.MonsterDisplay;
import com.knowware.aw.spellfight1.pubnub.JSONHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Aaron on 5/5/2017.
 * MonsterManager - manages hostile monsters
 *
 */

//for loading bitmaps from resources
  //  Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image);
    //needs an activity

    /*
    Bitmap image = BitmapFactory.decodeResource(R.drawable.eye);
  myButton.setBitmap(image);
  myButton.setMinimumWidth(image.getWidth());
  myButton.setMinimumHeight(image.getHeight());
     */

public class MonsterManager implements  MonsterDisplay.PlayerSelectedMonsterListener
{
    private JSONHelper jsonhelper;
    private Map<Integer, Monster> monsterlist;
    private MonsterDisplay.PlayerSelectedMonsterListener playerSelListener;
    private MonsterDisplay monsterDisp;

   public MonsterManager()
   {

       jsonhelper=new JSONHelper();
       monsterlist=new HashMap<Integer, Monster>();//use SparseArray later

   }

    /**
     * setMonsterDisp
     *
     * @param smonsterDisp
     */
   public void setMonsterDisp(MonsterDisplay smonsterDisp)
   {
       monsterDisp= smonsterDisp;
   }

    /**
     * makenewMonsterList
     *
      * @param sActivity
     * @param jsonMsg
     */
    public void makenewMonsterList(Activity sActivity,String jsonMsg)
    {
        Monster tempMonster=null;
        int a,cnt;
        int id;
        int btnwidth;
        int xposinc;
        int xpos=0;
        int monsterType;

        btnwidth=(GameDimensions.ScreenHeight/8)-2;

        if(monsterlist.size()>0)
            monsterlist.clear();

        cnt =jsonhelper.getMonsterArraySize(jsonMsg);

        for(a=0;a<cnt;a++)
        {
            id=jsonhelper.getIDfromMonsterArray(a,jsonMsg);

            monsterType=jsonhelper.getTypefromMonsterArray(a,jsonMsg);

            tempMonster= new Monster(id,EnemyType.toEnum(monsterType));

            monsterlist.put(id,tempMonster);//Integer.valueOf(id)

        }

    }

    /**
     * getMonsterlist
     *
     * @return
     */
    public Map<Integer, Monster> getMonsterlist()
    {
        return(monsterlist);
    }

    /**
     * setDead
     *
     * @param Id
     */
    public void setDead(int Id)
    {
        Monster tempMonster=null;
        ImageButton imgBtn;

        tempMonster= monsterlist.get(Integer.valueOf(Id));//new Integer(Id

        if(tempMonster!= null)
        {
            tempMonster.setIfDead(true);
           // imgBtn=tempMonster.getImgBtn();
           // imgBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * clearMonsterList
     *
     */
    public void clearMonsterList()
    {
        monsterlist.clear();
    }


    /**
     * hurtOneMonster
     *
     * @param tempMonster
     */
    public void hurtOneMonster(Monster tempMonster)
    {
        //send the targeted attack against one enemy message
    }

    /*
    Monster findMonsterByBtnId(int btnId)
    {
        Monster tempMonster=null;

        if(monsterlist.size()<=0)
            return(null);

        for (Map.Entry<Integer, Monster> entry : monsterlist.entrySet())
        {

            tempMonster=entry.getValue();
            if(btnId==tempMonster.getImgBtn().getId())
                return(tempMonster);

          //  System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
        }
        return(null);
    }
*/

    /**
     * findMonsterById
     *
     * @param Id
     * @return
     */
   public Monster findMonsterById(int Id)
    {
        Monster tempMonster=null;

        if(monsterlist.size()<=0)
            return(null);

        tempMonster= monsterlist.get(Id);

        return(tempMonster);
    }

    /**
     * getIfDead
     *
     * @param iD
     * @return
     */
    public boolean getIfDead(int iD)
    {
           return(monsterlist.get(iD).getIfDead());
    }

    public EnemyType getType(int iD)
    {
        Monster tempMonster;

        tempMonster=(monsterlist.get(iD));

        if (tempMonster==null)
            return(EnemyType.ERROR);

        return(tempMonster.getType());
    }

    /**
     * selectedMonster
     *
     * @param Id
     */
    @Override
    public void selectedMonster(int Id)
    {

    }
}
