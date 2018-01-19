package com.knowware.aw.spellfight1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.knowware.aw.spellfight1.UI.MonsterDisplay;
import com.knowware.aw.spellfight1.UI.MsgOkDlgFrag;
import com.knowware.aw.spellfight1.magic.GesturePak;
import com.knowware.aw.spellfight1.magic.GestureSpellMap;
import com.knowware.aw.spellfight1.magic.GestureType;
import com.knowware.aw.spellfight1.magic.Range;
import com.knowware.aw.spellfight1.magic.SpellType;
import com.knowware.aw.spellfight1.magic.Spellbook;
import com.knowware.aw.spellfight1.magic.UserSpellLevel;
import com.knowware.aw.spellfight1.monster.MonsterManager;
import com.knowware.aw.spellfight1.pubnub.JSONHelper;
import com.knowware.aw.spellfight1.pubnub.PubnubService;
import com.knowware.aw.spellfight1.pubnub.SpellMsgs;
import com.knowware.aw.spellfight1.util.BitmapManager;
import com.knowware.aw.spellfight1.util.MakeUniqueId;
import com.knowware.aw.spellfight1.util.RandNum;
import com.knowware.aw.spellfight1.util.SoundPoolManager;
import com.knowware.aw.spellfight1.util.Spellgesture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//horizontal pbar color code https://android--code.blogspot.com/2015/08/android-progressbar-color.html

//some parts will be moved out eventually.
//Player stuff should be moved out as well as game related stuff.

public class BattleActivity extends AppCompatActivity implements
        Spellgesture.SpellGestureListener,
        SoundPoolManager.SoundPoolListener,
        MsgOkDlgFrag.MsgOkDlgFragmentListener,
        View.OnClickListener,
        MonsterDisplay.PlayerSelectedMonsterListener
{
    public GameDimensions gameDimensions;
    public PubnubService mService;
    public BitmapManager bitmapManager;

    private IntentFilter PubnubResponseFilter;
    private JSONHelper jsonhelper;
    private boolean mBound = false;

    private String localUUID;
    private String ServerUUID;
    private ProgressBar manaPbar;
    private ProgressBar healthPbar;
    private ProgressBar xpPbar;
    private MakeUniqueId makeUniqueID;
    private MonsterManager monsterManager;
    private GestureDetector mGestureDetector;
    private Spellgesture spellgesture;
    private SoundPoolManager soundPoolManager;
    private RandNum rndnum;
    private int attackType;
    private TextView msgView;
    private MonsterDisplay monsterDisplay;

    //for player
    private int hp;
    private int hpMax=10;
    private int mana;
    private int manaMax;
    private Spellbook spellbook;
    private GestureSpellMap gestureSpellMap;

    //static final int AOE =1;
    //static final int SEL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        calcDimensions();
        makeUtilFunctions();
        setupPubNubIntent();
        createPlayerBits();

    }

    //////////////////////////////////////////////////////////////////
    //
    // setupUI- creates UI elements
    //

    private void setupUI()
    {
        manaPbar= (ProgressBar) findViewById(R.id.manapb);
        healthPbar= (ProgressBar) findViewById(R.id.healthpb);
        xpPbar=(ProgressBar) findViewById(R.id.xppb);
        msgView=(TextView)findViewById(R.id.textView);

        //setupSelections();// not being handled here

    }

    //////////////////////////////////////////////////////////////////
    //
    // createPlayerBits- create stuff for players.
    //

    private void createPlayerBits()
    {
        spellbook= new Spellbook();
        gestureSpellMap= new GestureSpellMap();
    }

    //////////////////////////////////////////////////////////////////
    //
    // onStart-
    //

    @Override
    protected void onStart()
    {
        super.onStart();

        // Bind to LocalService
        Intent intent = new Intent(this, PubnubService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        startService(intent);

        setupUI();
    }

    //////////////////////////////////////////////////////////////////
    //
    // makeUtilFunctions- create Util functions
    //

    private void makeUtilFunctions()
    {
        jsonhelper= new JSONHelper();
        rndnum= new RandNum();

        spellgesture= new Spellgesture();
        spellgesture.setListener(this);

        mGestureDetector = new GestureDetector(this, spellgesture);

        soundPoolManager= new SoundPoolManager();
        soundPoolManager.addListener(this);
        soundPoolManager.init(this);

        bitmapManager= new BitmapManager();
        bitmapManager.init();
        bitmapManager.setParentActivity(this);

        monsterManager= new MonsterManager();

        monsterDisplay= new MonsterDisplay();
        monsterDisplay.addListener(this);
        monsterDisplay.fillCtrlList(this);
        monsterDisplay.setBitmapManager(bitmapManager);

    }

    //////////////////////////////////////////////////////////////////
    //
    // setupPubNubIntent-
    //

    private void setupPubNubIntent()
    {
        PubnubResponseFilter = new IntentFilter(PubnubService.ACTION_PUBNUB_MSG_RESPONSE);
        registerReceiver(mPubNubResponseReceiver, PubnubResponseFilter);
    }

    //////////////////////////////////////////////////////////////////
    //
    // calcDimensions-
    //

    private void calcDimensions()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GameDimensions.ScreenHeight= metrics.heightPixels;
        GameDimensions.ScreenWidth = metrics.widthPixels;
        GameDimensions.vChunk=GameDimensions.ScreenHeight/4;
        GameDimensions.L1=0;
        GameDimensions.L2=GameDimensions.vChunk;
        GameDimensions.L3=GameDimensions.vChunk*2;
        GameDimensions.StatsLevel=GameDimensions.vChunk*3;
    }

    //////////////////////////////////////////////////////////////////
    //
    // doLoginMsg-
    //

    private void doLoginMsg()
    {

        String stemp;

        makeUniqueID= new MakeUniqueId();

        stemp=makeUniqueID.getGameUUID(this);

        if(stemp.equals("0"))//no UUID saved
        {
            localUUID=  mService.getLocalUUID();
            makeUniqueID.setGameUUID(this,localUUID);
        }
        else
            localUUID= stemp;

        mService.sendMsgs(jsonhelper.makeLoginMsg(localUUID));
    }


    ////////////////////////////////////////////////////////////
    //ServiceConnection- this is where service connected and service disconnected messages are handled
    //
    //@param -
    //@return - ref to service

    private ServiceConnection mConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service)
        {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PubnubService.LocalBinder binder = (PubnubService.LocalBinder) service;

            mService = binder.getService();

            doLoginMsg();

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            mBound = false;
        }
    };


    ////////////////////////////////////////////////////////////
    //mPubNubResponseReceiver- This area gets the messages from the service
    //
    //
    //@note -

    private BroadcastReceiver mPubNubResponseReceiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            JSONObject jsonReply=null;
            String sTemp;
            String stype;

            //updateText("Got Peer Response");

            sTemp=intent.getStringExtra("data");
            stype=intent.getStringExtra("type");

            //sSource=intent.getStringExtra("source");
            //sLSource=intent.getStringExtra("Local source");

           // dbgSender.sendMessage("In peer response");

            try
            {
                jsonReply=new JSONObject(sTemp);

                if(stype.equals("s"))
                    handleMsgs(sTemp);
                else
                                                                                                                                                                        handleData(sTemp);

                //dbgSender.sendMessage("Message from:"+" "+sSource+" "+jsonReply.getString("msg"));
                //dbgSender.sendMessage("Local                  Source: "+sLSource);

            }
            catch(JSONException e)
            {
                e.printStackTrace()                                                                                                                                ;
            }

        }

    };


    ////////////////////////////////////////////////////////////
    //handleMsgs- This area gets the messages from the service
    //
    // @param- String smsgs- incoming json msg
    // @note -

    void handleMsgs(String smsgs)
    {
        JSONObject jsonReply=null;

        Log.d("BattleActivity",smsgs);
    }

    /*****'
     *
     * handleData
     *
     * @param smsgs- json info
     *
     * @note since this is a party line socket, sent messages also show up. Have to ignore our sent messages
     * can be used later for debug purposes.
     */
   void handleData(String smsgs)
   {
       JSONObject jsonReply=null;
       String dest;
       String src;
       int imsgType;

       if(!jsonhelper.ifJSONMsg(smsgs))
           return;

       //dest=jsonhelper.getReceiver(smsgs);
                               src=jsonhelper.getSender(smsgs);

       //picked up our own message
       if(src==null)//dest
           return;

       if(src.equals(localUUID))//bail if this is from us
           return;

       try
       {
           jsonReply=new JSONObject(smsgs);
           imsgType=jsonhelper.getMsgType(smsgs);

           mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                   "Msg id "+imsgType));

           processMsgs(imsgType,smsgs);

       }
       catch (JSONException e)
       {
           e.printStackTrace();
       }
   }

    /*******************************************************************************
     *
     * processMsgs- processes messages from server
     *
     * @param msgType-message id
     * @param msg - JSON message
     */

    private void processMsgs(int msgType, String msg)
    {
        if(SpellMsgs.ERROR.getID()==msgType)
        {
            // notifyListeners(0,(String)jsonObject.get("errormsg"));
        }
        else if(SpellMsgs.LOGIN_OK.getID()==msgType)
        {
            // notifyListeners(1,(String)jsonObject.get("loginok"));
            //next send player info
            // doLogin(jsonObject);
            ServerUUID=jsonhelper.getServerUUID(msg);

            mService.sendMsgs(jsonhelper.makeGetCharMsg(localUUID,ServerUUID));

            //turn off enemy attacks
            mService.sendMsgs(jsonhelper.makePauseMsg(localUUID));



            // pubnubComms.sendMsgs(jsonhelper.makePauseMsg(localUUID));
        }
        else if(SpellMsgs.UPDATE_PLAYER_HEALTH.getID()==msgType)
        {
            float hpPct;
            int hpPcti;

            hp=jsonhelper.getHealth(msg);
            hpMax=jsonhelper.getHealthMax(msg);

            hpPct= ((float) hp/ (float)hpMax);//100.0f

           // mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
            //        "hppct used "+hpPct+"hp "+hp ));

            hpPcti= (int)(hpPct*100.0f);

            // healthPbarV2.setValue(hpPcti);//hpPcti
            // LifePbar.setValue( 1 );
            //hpbar.setValue( 100 );
            healthPbar.setProgress(hpPcti);
            healthPbar.setMax(100);//percentage based

           // HpForg.setSize(hpPct*200,26);

        }
        else if(SpellMsgs.UPDATE_PLAYER_MANA.getID()==msgType)
        {
            float manapct;

            mana=jsonhelper.getMana(msg);
            manaMax=jsonhelper.getManaMax(msg);
            manapct= (float) mana/(float)manaMax;
            manapct=manapct * 100;

          //  mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
           //         "manapct "+manapct ));

            manaPbar.setProgress((int)manapct);
            manaPbar.setMax(100);//percentage based

           // manaPbar.setValue((int)manapct);//mana

        }
        else if(SpellMsgs.ENEMY_LIST.getID()==msgType)//list of enemies
        {
            monsterManager.makenewMonsterList(this,msg);
            monsterDisplay.associateMonsterToButtons(monsterManager.getMonsterlist());

            makeIntroDialog();
        }
        else if(SpellMsgs.PLAYER_DEATH.getID()==msgType)
        {
            playerDeath();

            //turn off enemy attacks
            mService.sendMsgs(jsonhelper.makePauseMsg(localUUID));
        }
        else if(SpellMsgs.ENEMY_HURT.getID()==msgType)
        {
            int id;

            soundPoolManager.play(SoundPoolManager.ROACH_NOISE_ID,0.5f);

            id=jsonhelper.getEnemyHurtId(msg);
            monsterDisplay.updateMonsterBtn(id,bitmapManager.getMonsterResId(monsterManager.getType(id)),
                    50);

            sendDbgMsg("Enemy hurt "+id);

           // soundList.playsound(SoundList.INSECT_HURT_ID);

        }
        else if(SpellMsgs.ENEMY_ONE_DEATH.getID()==msgType)
        {
            int id;
            soundPoolManager.play(SoundPoolManager.ROACH_NOISE_ID,1.5f);

            id=jsonhelper.getOneEnemyDeadId(msg);

            monsterDisplay.updateMonsterBtn(id,bitmapManager.getMonsterResId(monsterManager.getType(id)),
                    50);

            monsterDisplay.monsterDead(id);
            monsterManager.setDead(id);

          //  mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
          //          " Dead Monster id "+id ));

            sendDbgMsg("Enemy dead "+id);
        }
        else if(SpellMsgs.ENEMY_UNAFFECTED.getID()==msgType)
        {
            int id;

            // mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
            //       "All dead!" ));

            sendDbgMsg("Not hurt");

            id=jsonhelper.getOneEnemyUnaffectedId(msg);
            monsterDisplay.updateMonsterBtn(id,bitmapManager.getMonsterResId(monsterManager.getType(id)),
                    50);

        }
        else if(SpellMsgs.ENEMY_ALL_DEATH.getID()==msgType)
        {
           //  int id;

           // mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
             //       "All dead!" ));

            sendDbgMsg("All dead ");

            monsterDisplay.monsterAllDead();

        }
        else if(SpellMsgs.ENEMY_ATTACK.getID()==msgType)//attacks
        {
           updateAttackType(msg);
        }
        //this is how we tell if the client got the info
        //after this, move on to the next message/mode
        else if(SpellMsgs.VICTORY.getID()==msgType)
        {
            handleVictory(msg);
        }
        else if(SpellMsgs.SPELLBOOK.getID()==msgType)
        {
            loadSpellbook(msg);
        }
        else if(SpellMsgs.GESTURE_MAP.getID()==msgType)
        {
            loadGestureMap(msg);

          //  mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
          //          "Got gesture" ));

        }
        else if(SpellMsgs.NOERROR.getID()==msgType)
        {

        }
        else //error unknown msg
        {
            // notifyListeners(0,(String)jsonObject.toJSONString());
        }
    }

    /**********************************************************************
     *
     * makeIntroDialog-
     *
     */

    private void makeIntroDialog()
    {
        MsgOkDlgFrag dlg;

        dlg=MsgOkDlgFrag.newInstance("Intro","Left swipe or tap on enemies to attack.",MsgOkDlgFrag.INTRO);

        dlg.show(getFragmentManager(), "dialog");

    }

    /**********************************************************************
     *
     * makeDeathDialog-
     *
     */

    private void makeDeathDialog()
    {
        MsgOkDlgFrag dlg;

        dlg=MsgOkDlgFrag.newInstance("Death","You are dead.",MsgOkDlgFrag.DEATH);

        dlg.show(getFragmentManager(), "dialog");

    }

    /**********************************************************************
     *
     * makeVictoryDialog-
     *
     * @param title- dialog box title
     * @param msg- message to display
     *
     */

    private void makeVictoryDialog(String title, String msg)
    {
        MsgOkDlgFrag dlg;

        dlg=MsgOkDlgFrag.newInstance(title,msg,MsgOkDlgFrag.VICTORY);

        dlg.show(getFragmentManager(), "dialog");

    }

    private void sendDbgMsg(String msg)
    {
        mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                msg ));
    }


    /******************************************************************************
     * updateAttackType-
     *
     * @param smsgs-
     */

    private void updateAttackType(String smsgs)
    {
       // int id;
       // int size=0;


        /*
        //soundList.playsound(SoundList.INSECT_ATTACK_ID);

        attackTypeSprite=spriteList.getEnemyAttackTypeSprite(jsonhelper.getAttackType(smsgs));
        countLength+= 100;

        id=jsonhelper.getMonsterID(smsgs);
        tempMonster=monsterManager.findMonsterByID(id);

        if(tempMonster!=null)
        {
            size=  DisplayList.add(spriteList.getEnemyAttackTypeSprite(jsonhelper.getAttackType(smsgs)),
                    (float)tempMonster.getX(),
                    GameDimensions.StatsLevel+ 100,
                    100
            );

            //   pubnubComms.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
            //           "add: "+size)
            //                );
        }
        */

        pickPlayerHit();
        pickPlayerReaction();
    }

    /**
     *
     * playerDeath-
     *
     */
    void playerDeath()
    {
        pickPlayerDeath();
        makeDeathDialog();
    }


    /**
     *
     * pickPlayerHit-
     *
     */

    private void pickPlayerHit()
    {
        int n;

        n= rndnum.getInt(0, 4);

        switch(n)
        {
            case 0:
                soundPoolManager.play(SoundPoolManager.ENEMY_ATTACK1_ID);
                break;

            case 1:
                soundPoolManager.play(SoundPoolManager.ENEMY_ATTACK2_ID);
                break;

            case 2:
                soundPoolManager.play(SoundPoolManager.ENEMY_ATTACK3_ID);
                break;

            case 3:
                soundPoolManager.play(SoundPoolManager.ENEMY_ATTACK4_ID);
                break;

            case 4:
                soundPoolManager.play(SoundPoolManager.ENEMY_ATTACK5_ID);
                break;
        }

        //  if(soundInfo.sound!=null)
        //      soundInfo.sound.play();
    }

    /**
     *
     * pickPlayerReaction-
     *
     */

    private void pickPlayerReaction()
    {
        int n;

        n= rndnum.getInt(0, 2);

        switch(n)
        {
            case 0:
                soundPoolManager.play(SoundPoolManager.HUMAN_HIT_ID);
                break;

            case 1:
                soundPoolManager.play(SoundPoolManager.HUMAN_HIT1_ID);
                break;

            case 2:
                soundPoolManager.play(SoundPoolManager.HUMAN_HIT2_ID);
                break;
        }

        //  if(soundInfo.sound!=null)
        //      soundInfo.sound.play();
    }

    /******************************************************************
     *
     * pickPlayerDeath-
     *
     */
    void pickPlayerDeath()
    {
        int n;

        n= rndnum.getInt(0, 1);

            switch(n)
            {
                case 0:
                    soundPoolManager.play(SoundPoolManager.HUMAN_DIE_ID);
                    break;

                case 1:
                    soundPoolManager.play(SoundPoolManager.HUMAN_DIE1_ID);
                    break;

            }

        // if(soundInfo.sound!=null)
        //    soundInfo.sound.play();
    }

    /**
     *
     * onTouchEvent-
     *
     * @param event - event
     * @return-
     */

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.mGestureDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    /**********************************************************************
     *
     * flingMsg-
     *
     * @param iDir- direction of the flings
     */

    @Override
    public void flingMsg(int iDir)
    {
        int manaused;
        int spellId;
        int cost;
        int resID;
        SpellType spellType;

        //all flings are AOE attacks

        switch(iDir)
        {
            case Spellgesture.RT:
               // mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
               //         "right manaused "+1 ));

                //spellId=  gestureSpellMap.getgestureMap(GestureType.RIGHT_FLING.getID());

                spellCastGesture(GestureType.RIGHT_FLING,-1);
                break;

            case Spellgesture.LT:

                spellType=  gestureSpellMap.getgestureMap(GestureType.LEFT_FLING);

                resID= bitmapManager.getPlayerAttackSpellResID(spellType,Range.AOE);

                monsterDisplay.overlayAllMonsters(resID);
                spellCastGesture(GestureType.LEFT_FLING,-1);



               // mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                 //       "left manaused "+1 ));


                /*
                spellId=  gestureSpellMap.getgestureMap(GestureType.LEFT_FLING.getID());

                cost= spellbook.getCost(spellId,Range.AOE);

                if(mana>=cost)
                {//cast spell if mana more than cost
                    soundPoolManager.play(SoundPoolManager.FIRE_AOE_ID);

                    //send msg to server, needs new msg
                    mService.sendMsgs(  jsonhelper.makeSpellCastMsg(localUUID,
                            spellId,
                           Range.AOE.getID())
                            );
                }
                else
                {//not enough
                  // print not enough message and play noise
                }


                attackType= Range.AOE.getID();
                manaused=calcManaUsed(SpellType.FIRE.getID(),attackType);

                if(manaused>0)
                {

                    soundPoolManager.play(SoundPoolManager.FIRE_AOE_ID);

                    //send aoe attack msg
                    //mService.sendMsgs(  jsonhelper.makeSpellCastMsg(localUUID, 5 ));
                }
*/

                break;

            case Spellgesture.UP:
                //mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                //        "up manaused "+1 ));
                break;

            case Spellgesture.DN:
               // mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                 //       "DN manaused "+1 ));
                break;
        }


            /*
             pubnubComms.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                        "left"));

                manaUse=calcActualManaUse(velocityX);

                actualmanaUse=manaUse*mana;

                pubnubComms.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                        "mana used "+actualmanaUse ));

                pubnubComms.sendMsgs(  jsonhelper.makeSpellCastMsg(localUUID,(int) actualmanaUse ));
             */

    }

    /**
     *
     * calcManaUsed
     *
     * @param spellType
     * @param range
     * @return
     */

    private int calcManaUsed(int spellType,int range)
    {
        //eventually will need to calculate actual mana use
        // need that to determine if spell can be cast.
        //don't make spell cast noise if you don't have enough mana

        if(range==Range.AOE.getID())
        {
            if (mana>=5)
            {
                return(5);
            }
            else
                return(0); //not enough mana, don't cast
        }
        else //area of effect
        {
            if (mana>=7)
            {
                return(7);
            }
            else
                return(0); //not enough mana, don't cast
        }

    }

    /**
     *handleVictory
     *
     *
     * @param msg
     */

    private void handleVictory(String msg)
    {
        int xp, credits;
        String vicMsg;

        xp = jsonhelper.getXp(msg);
        credits = jsonhelper.getCredits(msg);

        vicMsg = "You've won " + credits + " credits " + xp + " XP";

        makeVictoryDialog("Victory", vicMsg);

        //send back credits
        mService.sendMsgs(jsonhelper.makeDebugMsg(localUUID,
                "Credits " + credits + " Xp " + xp)
        );

        monsterManager.clearMonsterList();
    }


    //////////////////////////////////////
    //
    //
    //@note- for tapping on blank space, no buttons

    @Override
    public void tapMsg()
    {
        mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                "Blank space Tap manaused "));
    }

    /**
     *
     * soundsLoaded-
     *
     */

    @Override
    public void soundsLoaded()
    {
        Log.d("BattleActivity","Sound loaded");

    }

    /******************************************************************
     *
     * loadSpellbook- Converts the json stuff to a spellbook
     *
     * @param msg
     */

    private void loadSpellbook(String msg)
    {
        List<UserSpellLevel> spelllist;

        spelllist= jsonhelper.makeSpellList(msg);

        if(spelllist!=null)
        {
            spellbook.updateSpellList(spelllist);
        }
    }

    /***************************************************************************
     *
     * loadGestureMap - Convert json info to a gesture map
     *
     * @param msg - json msg
     */

    private void loadGestureMap(String msg)
    {
        List<GesturePak> gestureMaplist;
        int i, cnt=0;
        GesturePak gesturePak;

        gestureMaplist= jsonhelper.makeGestureMapList(msg);

        if(gestureMaplist!=null)
        {
            cnt=gestureMaplist.size();

            if(cnt>0)
            {
                for(i=0;i<cnt;i++)
                {
                    gesturePak=gestureMaplist.get(i);

                    gestureSpellMap.addGesture(gesturePak.gestureType,
                            gesturePak.spellType);
                }
            }

        }

        mService.sendMsgs(  jsonhelper.makeDebugMsg(localUUID,
                "loaded local gesture pak "+cnt ));
    }

    /*************************************************************************************
     *
     * spellCastGesture-
     *
     * @param gestureType
     * @param monsterID
     */

    void spellCastGesture(GestureType gestureType,int monsterID)
    {
        SpellType spellId;
        int cost;
        int range;
        Range lrange;


        if(gestureType==GestureType.LEFT_FLING ||
                gestureType==GestureType.RIGHT_FLING||
                gestureType== GestureType.DN_FLING||
                gestureType== GestureType.UP_FLING)
        {
            range=Range.AOE.getID();
            lrange=Range.AOE;
        }
        else
        {
            range=Range.TGT.getID();
            lrange=Range.TGT;
        }

        spellId=  gestureSpellMap.getgestureMap(gestureType);

        if(spellId==null)
            return;

        cost= spellbook.getCost(spellId,lrange);

        if(mana>=cost)
        {//cast spell if mana more than cost

            if(lrange==Range.AOE)
            {
                soundPoolManager.play(SoundPoolManager.FIRE_AOE_ID);



                //send msg to server, needs new msg
                mService.sendMsgs( jsonhelper.makeSpellCastMsg(localUUID,
                        spellId,
                        Range.AOE.getID(),-1));
            }

            else
            {
                soundPoolManager.play(SoundPoolManager.FIRE_SMALLFBALL_ID);

                //send msg to server, needs new msg
                mService.sendMsgs( jsonhelper.makeSpellCastMsg(localUUID,
                        spellId,
                        Range.TGT.getID(),monsterID));
            }

        }
        else
        {//not enough
            // print not enough message and play noise
            msgView.setText("not enough mana.");
        }

    }

    /*******************************************************************
     *
     * onOk
     *
     * @param type
     */

    @Override
    public void onOk(int type)
    {// send unpause msg

        if(type==MsgOkDlgFrag.INTRO)
        {
            //turn on enemy attacks
            mService.sendMsgs(jsonhelper.makeUnpauseMsg(localUUID));
        }
        else if(type==MsgOkDlgFrag.VICTORY)
        {
            doVictory();
        }
        else
        {

        }
    }

    void doVictory()
    {

    }

    public void setupSelections()
    {

        findViewById(R.id.imageButton).setOnClickListener(this);
        findViewById(R.id.imageButton2).setOnClickListener(this);
        findViewById(R.id.imageButton3).setOnClickListener(this);
        findViewById(R.id.imageButton4).setOnClickListener(this);
        findViewById(R.id.imageButton5).setOnClickListener(this);
        findViewById(R.id.imageButton6).setOnClickListener(this);
        findViewById(R.id.imageButton7).setOnClickListener(this);
        findViewById(R.id.imageButton8).setOnClickListener(this);

    }


    /////////////////////////////////////////////////
    //
    // selectedMonster- handles when users touch monster buttons.
    //
    //@param- int Id
    //@note- for tapping on monster buttons

    @Override
    public void onClick(View view)
    {
        int Id;
        Id=monsterDisplay.getMonsterIDFromBtn(view.getId());

        spellCastGesture(GestureType.TAP,Id);
    }

    @Override
    public void selectedMonster(int Id)
    {
        spellCastGesture(GestureType.TAP,Id);

    }
}//End of BattleActivity
