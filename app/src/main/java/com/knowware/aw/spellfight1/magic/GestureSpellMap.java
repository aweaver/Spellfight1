package com.knowware.aw.spellfight1.magic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron on 6/8/2017.
 */

public class GestureSpellMap
{
    private Map<GestureType,SpellType> gestureMap;

    //if we need to go through the gestures as a list
    //look at client monstermanager for an example of traversing a map

    public GestureSpellMap()
    {

        gestureMap= new HashMap<GestureType,SpellType>();

    }

    public void addGesture(GestureType gestureId, SpellType spellType)
    {
        gestureMap.put(gestureId,spellType);
    }

    public SpellType getgestureMap(GestureType gestureId)
    {
        return(gestureMap.get(gestureId));
    }


}
