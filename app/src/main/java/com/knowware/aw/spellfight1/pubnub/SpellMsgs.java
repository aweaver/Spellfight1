package com.knowware.aw.spellfight1.pubnub;

/**
 * Created by Aaron on 4/21/2017.
 */

public enum SpellMsgs
{
    ERROR(0),

    DEBUG(50),

    NOERROR(100),
    LOGIN(101),
    LOGIN_OK(102),
    REQUEST_CHAR(103),
    PAUSE(104),
    UN_PAUSE(105),

    UPDATE_PLAYER_HEALTH(205),
    UPDATE_PLAYER_MANA(210),
    UPDATE_PLAYER_XP(215),

    UPDATE_PLAYER_HEALTHMAX(206),
    UPDATE_PLAYER_MANAMAX(211),
    UPDATE_PLAYER_XPMAX(216),

    CAST_SPELL(300),
    ENEMY_LIST(400),
    ENEMY_ATTACK(410),

    PLAYER_DEATH(500),

    ENEMY_HURT(600),
    ENEMY_ONE_DEATH(610),
    ENEMY_ALL_DEATH(620),

    VICTORY(700),

    SPELLBOOK(800),
    GESTURE_MAP(900);

    private Integer id;

    SpellMsgs(final Integer id)
    {
        this.id = id;
    }

    public int getID()
    {
        return id;
    }
}
