package com.knowware.aw.spellfight1.UI;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.knowware.aw.spellfight1.GameDimensions;
import com.knowware.aw.spellfight1.R;
import com.knowware.aw.spellfight1.monster.EnemyType;
import com.knowware.aw.spellfight1.monster.Monster;
import com.knowware.aw.spellfight1.pubnub.JSONHelper;
import com.knowware.aw.spellfight1.util.BitmapManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Aaron on 8/9/2017.
 * MonsterDisplay- a cloas that handles the button graphics
 */

public class MonsterDisplay implements View.OnClickListener
{
    private JSONHelper jsonhelper;
    private ArrayList<ImageButtonMonster> imageButtonList;// list of buttons
    private ArrayList<Handler> HandlerList;//list of handlers that refresh the button graphics
    private PlayerSelectedMonsterListener playerSelMonsterListener;
    private BitmapManager bitmapManager;


    //When users click/tap on a button
    public interface PlayerSelectedMonsterListener
    {
        void selectedMonster(int Id);
    }

    public class ImageButtonMonster
    {
        public ImageButton imgBtn;
        public int monsterID;//0 means no monster >0 legit id
        public EnemyType monsterType;
    }

    public MonsterDisplay()
    {
        jsonhelper=new JSONHelper();
        imageButtonList=new ArrayList<ImageButtonMonster>();//list of Image buttons for monsters

        HandlerList= new ArrayList<Handler>();
    }

    /**
     * fillCtrlList-Make a list of Imagebuttons
     *
     * @param parentActivity - parent activity
     */

    public void fillCtrlList(Activity parentActivity)
    {
        ImageButton tempBtn;
        int btnwidth;
       // LinearLayout.LayoutParams params;
        ImageButtonMonster imageBtnMonster;

        btnwidth=(GameDimensions.ScreenHeight/8)-2;

        tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton);
        tempBtn.setOnClickListener(this);
        tempBtn.setMaxWidth(btnwidth);//image.getWidth()
        tempBtn.setMaxHeight(btnwidth);//image.getHeight()

        imageBtnMonster= new ImageButtonMonster();
        imageBtnMonster.imgBtn=  tempBtn;
        imageBtnMonster.monsterID=0;

        imageButtonList.add(imageBtnMonster);

        tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton2);
        tempBtn.setOnClickListener(this);
        tempBtn.setMaxWidth(btnwidth);//image.getWidth()
        tempBtn.setMaxHeight(btnwidth);//image.getHeight()

        imageBtnMonster= new ImageButtonMonster();
        imageBtnMonster.imgBtn=  tempBtn;
        imageBtnMonster.monsterID=0;


        imageButtonList.add(imageBtnMonster);

        tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton3);
        tempBtn.setOnClickListener(this);
        tempBtn.setMaxWidth(btnwidth);//image.getWidth()
        tempBtn.setMaxHeight(btnwidth);//image.getHeight()

        imageBtnMonster= new ImageButtonMonster();
        imageBtnMonster.imgBtn=  tempBtn;
        imageBtnMonster.monsterID=0;


        imageButtonList.add(imageBtnMonster);

        tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton4);
        tempBtn.setOnClickListener(this);
        tempBtn.setMaxWidth(btnwidth);//image.getWidth()
        tempBtn.setMaxHeight(btnwidth);//image.getHeight()

        imageBtnMonster= new ImageButtonMonster();
        imageBtnMonster.imgBtn=  tempBtn;
        imageBtnMonster.monsterID=0;


        imageButtonList.add(imageBtnMonster);

        tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton5);
        tempBtn.setOnClickListener(this);
        tempBtn.setMaxWidth(btnwidth);//image.getWidth()
        tempBtn.setMaxHeight(btnwidth);//image.getHeight()

        imageBtnMonster= new ImageButtonMonster();
        imageBtnMonster.imgBtn=  tempBtn;
        imageBtnMonster.monsterID=0;

        imageButtonList.add(imageBtnMonster);

        tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton6);
        tempBtn.setOnClickListener(this);
        tempBtn.setMaxWidth(btnwidth);//image.getWidth()
        tempBtn.setMaxHeight(btnwidth);//image.getHeight()

        imageBtnMonster= new ImageButtonMonster();
        imageBtnMonster.imgBtn=  tempBtn;


        imageButtonList.add(imageBtnMonster);

        tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton7);
        tempBtn.setOnClickListener(this);
        tempBtn.setMaxWidth(btnwidth);//image.getWidth()
        tempBtn.setMaxHeight(btnwidth);//image.getHeight()

        imageBtnMonster= new ImageButtonMonster();
        imageBtnMonster.imgBtn=  tempBtn;


        imageButtonList.add(imageBtnMonster);

        tempBtn=(ImageButton)parentActivity.findViewById(R.id.imageButton8);
        tempBtn.setOnClickListener(this);
        tempBtn.setMaxWidth(btnwidth);//image.getWidth()
        tempBtn.setMaxHeight(btnwidth);//image.getHeight()

        imageBtnMonster= new ImageButtonMonster();
        imageBtnMonster.imgBtn=  tempBtn;

        imageButtonList.add(imageBtnMonster);

        hideAll();
    }

    /**
     * setBitmapManager- sets a ptr to Bitmap manager class
     *
     * @param sbitmapManager - bitmapmanager class
     */
    public void setBitmapManager(BitmapManager sbitmapManager)
    {
        bitmapManager=sbitmapManager;
    }

    /**
     *
     * associateMonsterToButtons- ties buttons to monsters
     *
     * @param monsterlist - list of monsters from server
     */

    public void associateMonsterToButtons(Map<Integer, Monster> monsterlist)
    {
        Monster tempMonster;
        Bitmap image;
        int a=0,btnwidth;

        if(monsterlist.size()<=0)
            return;

        btnwidth=(GameDimensions.ScreenHeight/8)-2;

        for (Map.Entry<Integer, Monster> entry : monsterlist.entrySet())
        {

            tempMonster=entry.getValue();
            imageButtonList.get(a).monsterID=tempMonster.getMonsterId();
            imageButtonList.get(a).monsterType=tempMonster.getType();

            image=bitmapManager.getMonsterBmp(tempMonster.getType());

            imageButtonList.get(a).imgBtn.setImageBitmap(image);
            imageButtonList.get(a).imgBtn.setMaxWidth(btnwidth);//image.getWidth()
            imageButtonList.get(a).imgBtn.setMaxHeight(btnwidth);//image.getHeight()

            imageButtonList.get(a).imgBtn.setVisibility(View.VISIBLE);
            a++;

            //  System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
        }

    }

    /**
     * addListener- listener for the player touch message on button
     *
     * @param splayerSelListener - listener for one selected monster
     */
    public void addListener(PlayerSelectedMonsterListener splayerSelListener)
    {
        playerSelMonsterListener=splayerSelListener;
    }

    /**
     *
     * hideAll- hides all buttons
     *
     */
    private void hideAll()
    {
        int a,cnt;

        cnt=imageButtonList.size();
        for(a=0;a<cnt;a++)
        {
            imageButtonList.get(a).imgBtn.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * onClick- When users touch a button, get this message
     *
     *
     * @param view - window
     */

    @Override
    public void onClick(View view)
    {
        int Btnid;
        int id;// monster id
        ImageButton tempbutton;
        ImageButtonMonster tempMonster;
        Bitmap bmpOverlay;

        tempbutton=(ImageButton)view;

        Btnid = tempbutton.getId();

        id=getMonsterIDFromBtn(Btnid);

        if(playerSelMonsterListener!=null)
        {
            playerSelMonsterListener.selectedMonster(id);

            tempMonster=getMonsterFromMonsterID(id);

           //set monster bmp on fire
            bmpOverlay= overlayMonsterWithAttackEffect(bitmapManager.getMonsterResId(tempMonster.monsterType),
                    BitmapManager.PLAYERFIREBALL_IMG);

            tempbutton.setImageBitmap(bmpOverlay);
            //make an asynchtask to update this button two seconds later
           // updateBtn(tempbutton,BitmapManager.ROACH_IMG,2000);
        }


        // hurtOneMonster(tempMonster);
    }

    /**
     * overlayAllMonsters- Overlays player attack spell effects over monster bitmaps
     *
     * @param ResID- resource id
     */

    public void overlayAllMonsters(int ResID)
    {
        ImageButtonMonster tempBtn;
        Bitmap bmpOverlay;
        int a,amt;

        if(imageButtonList.size()<=0)
            return;

        amt=imageButtonList.size();

        for (a=0;a<amt;a++)
        {
            tempBtn=imageButtonList.get(a);

            if(tempBtn.imgBtn.isShown())
            {
                bmpOverlay=overlayMonsterWithAttackEffect(bitmapManager.getMonsterResId(tempBtn.monsterType),
                        ResID);

                tempBtn.imgBtn.setImageBitmap(bmpOverlay);
            }

            //  System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * overlayMonsterWithAttackEffect- Overlays one button with a graphic
     *
     * @param srcBmpId - source res id
     * @param destBmpID - dest res id
     * @return - bitmap overlayed with attack graphic
     */
    Bitmap overlayMonsterWithAttackEffect(int srcBmpId, int destBmpID)
    {
        Bitmap resImage;

        resImage= bitmapManager.overlay(bitmapManager.getBmapCopy(srcBmpId),
                bitmapManager.getBmapCopy(destBmpID));

        return(resImage);
    }

    /**
     * getMonsterIDFromBtn-
     *
     * @param BtnID- id of button
     * @return - monster id
     */
    public int  getMonsterIDFromBtn(int BtnID)
    {
        int a, cnt;

        cnt=imageButtonList.size();

        for (a=0;a<cnt;a++)
        {
            if(BtnID==imageButtonList.get(a).imgBtn.getId() )
                return(imageButtonList.get(a).monsterID);
        }

        return (-1);
    }

    /**
     * getMonsterFromMonsterID
     *
     * @param monsterID - id of monster
     * @return - ImageButtonMonster
     */
    public ImageButtonMonster  getMonsterFromMonsterID(int monsterID)
    {
        int a, cnt;

        cnt=imageButtonList.size();

        if(cnt==0)
            return (null);

        for (a=0;a<cnt;a++)
        {
            if(monsterID==imageButtonList.get(a).monsterID )
                return(imageButtonList.get(a));
        }

        return (null);
    }

    /**
     * monsterDead
     *
     * @param monsterID- monster id
     */
    public void monsterDead(int monsterID)
    {
        ImageButtonMonster tempImageBtn;

        tempImageBtn=  getMonsterFromMonsterID(monsterID);

        if(tempImageBtn!=null)
        {
            tempImageBtn.imgBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *monsterAllDead
     *
     */
    public void monsterAllDead()
    {

        hideAll();
    }


    /**
     * updateMonsterBtn
     *
     * @param monsterID- id number of monster
     * @param bitmapid- bitmap res id
     * @param time- how long to show the overlay
     */
    public void updateMonsterBtn(int monsterID,int bitmapid, int time)
    {
        ImageButtonMonster tempBtnMonster;

        tempBtnMonster=getMonsterFromMonsterID(monsterID);

        updateBtn(tempBtnMonster.imgBtn,bitmapid,time);
    }

    /**
     * updateBtn
     *
     * @param imgBtn - btn control
     * @param bitmapid- res id
     * @param time- how long the overlay lasts
     */
    void updateBtn(ImageButton imgBtn, int bitmapid, int time)
    {
       final int bitmapID=bitmapid;
       final ImageButton ibtn;
       final Handler handler;

        ibtn=imgBtn;

        handler=getHandler();

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Bitmap image;
                image=bitmapManager.getBmapCopy(bitmapID);

                ibtn.setImageBitmap(image);
                removeHandler(handler);

            }
        }, time);

       /*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Bitmap image;
                image=bitmapManager.getBmapCopy(bitmapID);


                ibtn.setImageBitmap(image);
            }
        }, time);

        */
    }

    /**
     * getHandler
     *
     * @return - handler to update btn
     */
   private Handler getHandler()
    {
        Handler handler;

        handler = new Handler();

        HandlerList.add(handler);
        return(handler);
    }

    /**
     * removeHandler
     *
     * @param handler - handler that updates the image button
     */
   private void removeHandler(Handler handler)
    {
        HandlerList.remove(handler);
    }

    public class UpdateBtnInfo
    {
        public ImageButton imgBtn;
        public int imgID;
        public int time;
    }

    class ImgBtnUpdateTask extends AsyncTask<UpdateBtnInfo,Integer, Long>//data, last param is result gets passed to postexecute
    {

        @Override
        protected Long doInBackground(UpdateBtnInfo... updateBtnInfos)
        {

            return null;
        }

        protected void onPreExecute ()
        {
            super.onPreExecute();
            //Log.d(TAG + " PreExceute","On pre Exceute......");
        }

      /*  protected String doInBackground(UpdateBtnInfo updateBtnInfo)
        {
           // Log.d(TAG + " DoINBackGround","On doInBackground...");

            for(int i=0; i<10; i++){
                Integer in = new Integer(i);
                publishProgress(i);
            }
            return "You are at PostExecute";
        }*/

        protected void onProgressUpdate(UpdateBtnInfo updateBtnInfo)
        {

           // Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(UpdateBtnInfo updateBtnInfo)
        {
            //super.onPostExecute(result);
           // Log.d(TAG + " onPostExecute", "" + result);
        }
    }



    /*
    asynchtask
    https://stackoverflow.com/questions/9671546/asynctask-android-example
    https://developer.android.com/reference/android/os/AsyncTask.html

     */

/*

final Handler handler = new Handler();
final Runnable r = new Runnable()
{
    public void run()
    {
        <some task>
    }
};
handler.postDelayed(r, 15000);

 */

    /*
    Monster findMonsterByBtnId(int btnId)
    {
        Monster tempMonster=null;

        if(imageButtonList.size()<=0)
            return(null);

        for (Map.Entry<Integer, Monster> entry : imageButtonList.entrySet())
        {

            tempMonster=entry.getValue();
            if(btnId==tempMonster.getImgBtn().getId())
                return(tempMonster);

            //  System.out.printf("%s -> %s%n", entry.getKey(), entry.getValue());
        }
        return(null);
    }
    */
}
