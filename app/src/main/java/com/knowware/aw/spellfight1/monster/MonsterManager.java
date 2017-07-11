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
import com.knowware.aw.spellfight1.pubnub.JSONHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Aaron on 5/5/2017.
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

public class MonsterManager implements View.OnClickListener
{
    public interface MonsterAttackListener
    {
        void selectedMonster(int Id);
    }


    private JSONHelper jsonhelper;
    private ArrayList<ImageButton> imageButtonList;// list of buttons
    private Map<Integer, Monster> monsterlist;
    private MonsterAttackListener monsterAttackListener;

   public MonsterManager()
   {
       jsonhelper=new JSONHelper();
       imageButtonList=new ArrayList<ImageButton>();

       monsterlist=new HashMap<Integer, Monster>();//use SparseArray later


   }

    public void addListener(MonsterAttackListener smonsterAttackListener)
    {
        monsterAttackListener=smonsterAttackListener;
    }

   public void fillCtrlList(Activity parentActivity)
   {
       ImageButton tempBtn=null;
       int btnwidth, xposinc,xpos=0;
       LinearLayout.LayoutParams params;


       tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton);
       tempBtn.setOnClickListener(this);

       imageButtonList.add(tempBtn);

       tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton2);
       tempBtn.setOnClickListener(this);
       imageButtonList.add(tempBtn);


       tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton3);
       tempBtn.setOnClickListener(this);
       imageButtonList.add(tempBtn);


       tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton4);
       tempBtn.setOnClickListener(this);

       imageButtonList.add(tempBtn);


       tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton5);
       tempBtn.setOnClickListener(this);

       imageButtonList.add(tempBtn);


       tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton6);
       tempBtn.setOnClickListener(this);

       imageButtonList.add(tempBtn);


       tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton7);
       tempBtn.setOnClickListener(this);

       imageButtonList.add(tempBtn);


       tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton8);
       tempBtn.setOnClickListener(this);

       imageButtonList.add(tempBtn);


       hideAll();
   }

   void hideAll()
   {
       int a,cnt;

       cnt=imageButtonList.size();
       for(a=0;a<cnt;a++)
       {
           imageButtonList.get(a).setVisibility(View.INVISIBLE);
       }

   }

    public void makenewMonsterList(Activity sActivity,String jsonMsg)
    {
        Monster tempMonster=null;
        int a,cnt;
        int id;
        int btnwidth;
        int xposinc;
        int xpos=0;

        btnwidth=(GameDimensions.ScreenHeight/8)-2;

        if(monsterlist.size()>0)
            monsterlist.clear();

        cnt =jsonhelper.getMonsterArraySize(jsonMsg);

        for(a=0;a<cnt;a++)
        {
            id=jsonhelper.getIDfromMonsterArray(a,jsonMsg);

            tempMonster= new Monster(id,
                    (int)imageButtonList.get(a).getX(),(int)imageButtonList.get(a).getY(),
                    imageButtonList.get(a)
                    );


           Bitmap image = BitmapFactory.decodeResource(sActivity.getResources(),R.drawable.roach);
            imageButtonList.get(a).setImageBitmap(image);
            imageButtonList.get(a).setMaxWidth(btnwidth);//image.getWidth()
            imageButtonList.get(a).setMaxHeight(btnwidth);//image.getHeight()

            imageButtonList.get(a).setVisibility(View.VISIBLE);
            monsterlist.put(id,tempMonster);//Integer.valueOf(id)

        }

    }

    public void setDead(int Id)
    {
        Monster tempMonster=null;
        ImageButton imgBtn;

        tempMonster= monsterlist.get(new Integer(Id));

        if(tempMonster!= null)
        {
            tempMonster.setIfDead(true);
            imgBtn=tempMonster.getImgBtn();
            imgBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void clearMonsterList()
    {
        monsterlist.clear();
        hideAll();
    }


    @Override
    public void onClick(View view)
    {
        int id;
        ImageButton tempbutton;
        Monster tempMonster;

        tempbutton=(ImageButton)view;

        id = tempbutton.getId();

        tempMonster= findMonsterByBtnId(id);

        if(tempMonster!=null)
        {
            monsterAttackListener.selectedMonster(id);
        }
           // hurtOneMonster(tempMonster);

    }

    public void hurtOneMonster(Monster tempMonster)
    {
        //send the targeted attack against one enemy message
    }

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

}
