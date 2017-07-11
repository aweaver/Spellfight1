package com.knowware.aw.spellfight1.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import com.knowware.aw.spellfight1.R;
import com.knowware.aw.spellfight1.monster.Monster;
import java.util.HashMap;
import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by Aaron on 5/12/2017.
 */

public class SoundPoolManager implements SoundPool.OnLoadCompleteListener
{
    public static final int HUMAN_DIE_ID = R.raw.deathscream1;
    public static final int HUMAN_DIE1_ID = R.raw.deathscream;

    public static final int ENEMY_ATTACK_ID = R.raw.punch2;
    public static final int ENEMY_ATTACK1_ID = R.raw.bite;
    public static final int ENEMY_ATTACK2_ID = R.raw.punch;
    public static final int ENEMY_ATTACK3_ID = R.raw.slap;
    public static final int ENEMY_ATTACK4_ID = R.raw.slap2;
    public static final int ENEMY_ATTACK5_ID = R.raw.realisticpunch;


    public static final int FIRE_AOE_ID = R.raw.firewall;
    public static final int FIRE_LARGEFBALL_ID = R.raw.largefireball;
    public static final int FIRE_SMALLFBALL_ID = R.raw.smallfireball;

    public static final int ROACH_NOISE_ID = R.raw.insectnoise;


    public static final int HUMAN_HIT_ID = R.raw.moaningmoan;
    public static final int HUMAN_HIT1_ID = R.raw.drycough;
    public static final int HUMAN_HIT2_ID = R.raw.groanandgrunt;

    private  SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;
    private AudioAttributes attributes;
    private int complete=0,completeMax;
    private boolean bLoadComplete=false;
    private SoundPoolListener soundPoolListener;
    private float volume;

    public interface SoundPoolListener
    {
        void soundsLoaded();
    }

    public SoundPoolManager()
    {

        soundPoolMap=new HashMap<Integer, Integer>();//use sparse int array later

        attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(attributes)
                .build();

        soundPool.setOnLoadCompleteListener(this);

    }

    public void init(Activity sActivity)
    {
        float actualVolume;
        float maxVolume;

        soundPoolMap.put(HUMAN_DIE_ID,soundPool.load(sActivity, HUMAN_DIE_ID, 1));
        soundPoolMap.put(HUMAN_DIE1_ID,soundPool.load(sActivity, HUMAN_DIE1_ID, 1));
        soundPoolMap.put(ENEMY_ATTACK_ID,soundPool.load(sActivity, ENEMY_ATTACK_ID, 1));
        soundPoolMap.put(ENEMY_ATTACK1_ID,soundPool.load(sActivity, ENEMY_ATTACK1_ID, 1));
        soundPoolMap.put(ENEMY_ATTACK2_ID,soundPool.load(sActivity, ENEMY_ATTACK2_ID, 1));
        soundPoolMap.put(ENEMY_ATTACK3_ID,soundPool.load(sActivity, ENEMY_ATTACK3_ID, 1));

        soundPoolMap.put(ENEMY_ATTACK4_ID,soundPool.load(sActivity, ENEMY_ATTACK4_ID, 1));
        soundPoolMap.put(ENEMY_ATTACK5_ID,soundPool.load(sActivity, ENEMY_ATTACK5_ID, 1));
        soundPoolMap.put(FIRE_AOE_ID,soundPool.load(sActivity, FIRE_AOE_ID, 1));

        soundPoolMap.put(FIRE_LARGEFBALL_ID,soundPool.load(sActivity, FIRE_LARGEFBALL_ID, 1));
        soundPoolMap.put(FIRE_SMALLFBALL_ID,soundPool.load(sActivity, FIRE_SMALLFBALL_ID, 1));
        soundPoolMap.put(ROACH_NOISE_ID,soundPool.load(sActivity, ROACH_NOISE_ID, 1));

        soundPoolMap.put(HUMAN_HIT_ID,soundPool.load(sActivity, HUMAN_HIT_ID, 1));
        soundPoolMap.put(HUMAN_HIT1_ID,soundPool.load(sActivity, HUMAN_HIT1_ID, 1));
        soundPoolMap.put(HUMAN_HIT2_ID,soundPool.load(sActivity, HUMAN_HIT2_ID, 1));

        completeMax=soundPoolMap.size();

        AudioManager audioManager = (AudioManager) sActivity.getSystemService(AUDIO_SERVICE);
         actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
         maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
         volume = actualVolume / maxVolume;
    }

    public void addListener(SoundPoolListener ssoundPoolListener)
    {
        soundPoolListener=ssoundPoolListener;
    }

    public void play(int SoundID)
    {
        int id;

        id=soundPoolMap.get(SoundID);

        soundPool.play(id, volume, volume, 1, 0, 1f);
    }

    public void play(int SoundID,float frequency)
    {
        int id;//frequency 1.0 normal <1 slow >1 fast

        id=soundPoolMap.get(SoundID);

        soundPool.play(id, volume, volume, 1, 0, frequency);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int soundID, int status)
    {
        if(status==0)
        {
            complete++;
            if(complete==completeMax)
            {
                soundPoolListener.soundsLoaded();
            }
        }
    }
}
