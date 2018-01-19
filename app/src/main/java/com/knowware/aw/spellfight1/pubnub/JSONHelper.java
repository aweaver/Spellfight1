package com.knowware.aw.spellfight1.pubnub;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.knowware.aw.spellfight1.magic.GesturePak;
import com.knowware.aw.spellfight1.magic.GestureType;
import com.knowware.aw.spellfight1.magic.Range;
import com.knowware.aw.spellfight1.magic.SpellType;
import com.knowware.aw.spellfight1.magic.UserSpellLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 4/21/2017.
 *
 * JSON helper class that packs and unpacks JSON msgs
 * Also there are convenience methods
 */

public class JSONHelper
{

    /*******************************************************************************
     * makeLoginMsg- packs up login message
     *
     * @param srcUUID - sender of message
     * @return
     */

   public ObjectNode makeLoginMsg(String srcUUID)
    {
       // JSONObject jsonObject;
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode logimsg = factory.objectNode();

        logimsg.put(SpellFightKeys.FORMAT_KEY,"JSON");
        logimsg.put(SpellFightKeys.MSGTYPE_KEY,SpellMsgs.LOGIN.getID());
        logimsg.put(SpellFightKeys.SRC_KEY,srcUUID);

        // logimsg.put("format","JSON");

        //  jsonObject=new JSONObject();
        // jsonObject.put("format","JSON");
        //  jsonObject.put("msgtype",SpellMsgs.LOGIN.getID());
        //  jsonObject.put("src",srcUUID);

        return(logimsg);
    }

    /**
     *
     * makeGetCharMsg- makes the GetChar message
     *
     * @param srcUUID
     * @param dest
     * @return
     */
    public ObjectNode makeGetCharMsg(String srcUUID,String dest)
    {
        //JSONObject  jsonObject;

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode getcharmsg = factory.objectNode();

        getcharmsg.put(SpellFightKeys.FORMAT_KEY,"JSON");
        getcharmsg.put(SpellFightKeys.MSGTYPE_KEY,SpellMsgs.REQUEST_CHAR.getID());
        getcharmsg.put(SpellFightKeys.SRC_KEY,srcUUID);
        getcharmsg.put(SpellFightKeys.RECEIVER_KEY,dest);

        return(getcharmsg);
    }

    /**
     *
     * makeDebugMsg
     *
     * @param srcUUID
     * @param msg
     * @return
     */
   public ObjectNode makeDebugMsg(String srcUUID, String msg)
    {
        //JSONObject  jsonObject;
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode logimsg = factory.objectNode();

        logimsg.put(SpellFightKeys.FORMAT_KEY,"JSON");
        logimsg.put(SpellFightKeys.MSGTYPE_KEY,SpellMsgs.DEBUG.getID());
        logimsg.put(SpellFightKeys.SRC_KEY,srcUUID);
        logimsg.put(SpellFightKeys.DEBUG_KEY,msg);

        // logimsg.put("format","JSON");

        //  jsonObject=new JSONObject();
        // jsonObject.put("format","JSON");
        //  jsonObject.put("msgtype",SpellMsgs.LOGIN.getID());
        //  jsonObject.put("src",srcUUID);

        return(logimsg);
    }

    /**
     *
     * makeSpellCastMsg
     *
     * @param srcUUID
     * @param spellId
     * @param range
     * @param monsterId
     * @return
     */
   public ObjectNode makeSpellCastMsg(String srcUUID,
                                      SpellType spellId,
                                      int range,
                                      int monsterId)

    {
        //JSONObject  jsonObject;
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode spellcastmsg = factory.objectNode();

        spellcastmsg.put(SpellFightKeys.FORMAT_KEY,"JSON");
        spellcastmsg.put(SpellFightKeys.MSGTYPE_KEY,SpellMsgs.CAST_SPELL.getID());
        spellcastmsg.put(SpellFightKeys.SRC_KEY,srcUUID);

        spellcastmsg.put(SpellFightKeys.SPELL_TYPE_KEY,spellId.getID());
        spellcastmsg.put(SpellFightKeys.RANGE_KEY,range);
        spellcastmsg.put(SpellFightKeys.MONSTER_ID_KEY,monsterId);

        // logimsg.put("format","JSON");

        //  jsonObject=new JSONObject();
        // jsonObject.put("format","JSON");
        //  jsonObject.put("msgtype",SpellMsgs.LOGIN.getID());
        //  jsonObject.put("src",srcUUID);

        return(spellcastmsg);
    }

    /**
     *
     * makePauseMsg
     *
     * @param srcUUID
     * @return
     */
    public ObjectNode makePauseMsg(String srcUUID)
    {
       // JSONObject  jsonObject;
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode spellcastmsg = factory.objectNode();

        spellcastmsg.put(SpellFightKeys.FORMAT_KEY,"JSON");
        spellcastmsg.put(SpellFightKeys.MSGTYPE_KEY,SpellMsgs.PAUSE.getID());
        spellcastmsg.put(SpellFightKeys.SRC_KEY,srcUUID);

        return(spellcastmsg);
    }

    /**
     *
     * makeUnpauseMsg
     *
     * @param srcUUID
     * @return
     */
    public ObjectNode makeUnpauseMsg(String srcUUID)
    {
        // JSONObject  jsonObject;
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode spellcastmsg = factory.objectNode();

        spellcastmsg.put(SpellFightKeys.FORMAT_KEY,"JSON");
        spellcastmsg.put(SpellFightKeys.MSGTYPE_KEY,SpellMsgs.UN_PAUSE.getID());
        spellcastmsg.put(SpellFightKeys.SRC_KEY,srcUUID);

        return(spellcastmsg);
    }

    /**
     *
     * getMsgType
     *
     * @param sMsg
     * @return
     */
    public int getMsgType(String sMsg)
    {
        JSONObject jsonObj;
        int msgType=0;

        try
        {
            jsonObj= new JSONObject(sMsg);
            msgType= jsonObj.getInt(SpellFightKeys.MSGTYPE_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return(msgType);
    }

    /**
     *
     * getSender
     *
     * @param sMsg
     * @return
     */
    public String getSender(String sMsg)
    {
        JSONObject jsonObj;
        String sender=null;
        String stemp;

        // jsonParser= new JSONParser();
        //jsonObj=jsonParser.parse();
// jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
        stemp=sMsg.substring(sMsg.indexOf("{"), sMsg.lastIndexOf("}") + 1);

        try
        {
            jsonObj= new JSONObject(stemp);
            sender= jsonObj.getString(SpellFightKeys.SRC_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        return(sender);
    }



    public String getServerUUID(String sMsg)
    {
        JSONObject jsonObj;
        String uuid=null;

        try
        {
            jsonObj= new JSONObject(sMsg);
            uuid= jsonObj.getString(SpellFightKeys.SRC_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return(uuid);
    }

    public boolean ifJSONMsg(String msg)
    {
        if(msg.indexOf(SpellFightKeys.FORMAT_KEY)>0 && msg.indexOf("JSON")>0)
            return(true);
        else
            return(false);
    }

    public String getReceiver(String sMsg)
    {
        JSONObject jsonObj;
        String uuid=null;

        try
        {
            jsonObj= new JSONObject(sMsg);
            uuid= jsonObj.getString(SpellFightKeys.RECEIVER_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return(uuid);
    }


   public int getHealth(String sMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try
        {
            jsonObj= new JSONObject(sMsg);
            val= jsonObj.getInt(SpellFightKeys.HEALTH_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        return(val);
    }

    public int getHealthMax(String sMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try
        {
            jsonObj= new JSONObject(sMsg);
            val= jsonObj.getInt(SpellFightKeys.HEALTH_MAX_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return(val);
    }

    public int getMana(String sMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try
        {
            jsonObj= new JSONObject(sMsg);
            val= jsonObj.getInt(SpellFightKeys.MANA_KEY);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

   public int getManaMax(String sMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try {
            jsonObj= new JSONObject(sMsg);
            val= jsonObj.getInt(SpellFightKeys.MANA_MAX_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return(val);
    }

    public int getMonsterID(String sjsonMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            val= jsonObj.getInt(SpellFightKeys.MONSTER_ID_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public int getMonsterType(String sjsonMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            val= jsonObj.getInt(SpellFightKeys.ENEMY_TYPE_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public int getAttackType(String sjsonMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            val= jsonObj.getInt(SpellFightKeys.ENEMY_ATTACK_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public int getEnemyHurtId(String sjsonMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            val= jsonObj.getInt(SpellFightKeys.ENEMY_HURT_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public int getOneEnemyDeadId(String sjsonMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            val= jsonObj.getInt(SpellFightKeys.ENEMY_ONE_DEATH_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public int getOneEnemyUnaffectedId(String sjsonMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try
        {
            jsonObj= new JSONObject(sjsonMsg);
            val= jsonObj.getInt(SpellFightKeys.ENEMY_UNAFFECTED_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return(val);
    }

    public int getIDfromMonsterArray(int index,String sjsonMsg)
    {
        JSONObject jsonObj;
        JSONArray jArray=null;
        int val=0,cnt;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            jArray = jsonObj.getJSONArray(SpellFightKeys.MONSTER_LIST_ARRAY_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        cnt=jArray.length();

        if(index>=cnt || index<0)
            return(-1);

        try {
            val=jArray.getJSONObject(index).getInt(SpellFightKeys.MONSTER_ID_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public int getTypefromMonsterArray(int index,String sjsonMsg)
    {
        JSONObject jsonObj;
        JSONArray jArray=null;
        int val=0,cnt;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            jArray = jsonObj.getJSONArray(SpellFightKeys.MONSTER_LIST_ARRAY_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        cnt=jArray.length();

        if(index>=cnt)
            return(-1);

        try {
            val=jArray.getJSONObject(index).getInt(SpellFightKeys.ENEMY_TYPE_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public int getMonsterArraySize(String sjsonMsg)
    {
        JSONObject jsonObj;
        JSONArray jArray=null;
        int cnt;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            jArray = jsonObj.getJSONArray(SpellFightKeys.MONSTER_LIST_ARRAY_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cnt=jArray.length();

        return(cnt);
    }

    public int getCredits(String sjsonMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            val= jsonObj.getInt(SpellFightKeys.CREDITS_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public int getXp(String sjsonMsg)
    {
        JSONObject jsonObj;
        int val=0;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            val= jsonObj.getInt(SpellFightKeys.XP_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return(val);
    }

    public List<UserSpellLevel> makeSpellList(String sjsonMsg)
    {
        JSONObject jsonObj;
        JSONArray jArray=null;
        int a=0,cnt,val=0;
        List<UserSpellLevel> spelllist;
        UserSpellLevel tempLvl;
        SpellType tempspell;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            jArray = jsonObj.getJSONArray(SpellFightKeys.SPELL_LIST_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cnt=jArray.length();

        if(cnt<=0)
            return(null);

        spelllist=new ArrayList<UserSpellLevel>();

        for(a=0;a<cnt;a++)
        {
            tempLvl= new  UserSpellLevel();

            try
            {

                //tempLvl.type=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_TYPE_KEY);

                val=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_TYPE_KEY);
                tempLvl.type=SpellType.fromEnum(val);

                tempLvl.SpellLevel=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_LEVEL_KEY);
                tempLvl.costAOE=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_COST_AOF_KEY);
                tempLvl.costTGT=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_COST_TGT_KEY);
                tempLvl.effectAOE=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_EFF_AOF_KEY);
                tempLvl.effectTGT=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_EFF_TGT_KEY);

                spelllist.add(tempLvl);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        return(spelllist);
    }


    public List<GesturePak> makeGestureMapList(String sjsonMsg)
    {
        JSONObject jsonObj;
        JSONArray jArray=null;
        int a=0,cnt,val=0;
        List<GesturePak> spelllist;
        GesturePak tempGestPak;
        SpellType tempspell;

        try {
            jsonObj= new JSONObject(sjsonMsg);
            jArray = jsonObj.getJSONArray(SpellFightKeys.GESTURE_MAPS_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cnt=jArray.length();

        if(cnt<=0)
            return(null);

        spelllist=new ArrayList<GesturePak>();

        for(a=0;a<cnt;a++)
        {
            tempGestPak= new  GesturePak();

            try
            {
                val=jArray.getJSONObject(a).getInt(SpellFightKeys.GESTURE_TYPE_KEY);
                tempGestPak.gestureType= GestureType.fromEnum(val);

                val=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_TYPE_KEY);
                tempGestPak.spellType=SpellType.fromEnum(val);

                //tempGestPak.gestureType=jArray.getJSONObject(a).getInt(SpellFightKeys.GESTURE_TYPE_KEY);
                //tempGestPak.spellType=jArray.getJSONObject(a).getInt(SpellFightKeys.SPELL_TYPE_KEY);

                spelllist.add(tempGestPak);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        return(spelllist);
    }



    /*
    JSONArray jArr = jObj.getJSONArray("list");

	for (int i=0; i < jArr.length(); i++) {

	    JSONObject obj = jArr.getJSONObject(i);

	    ....
	}
     */
}
