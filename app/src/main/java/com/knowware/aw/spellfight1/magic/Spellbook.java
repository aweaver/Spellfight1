package com.knowware.aw.spellfight1.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aaron on 6/8/2017.
 */

public class Spellbook
{
    private Map<SpellType,UserSpellLevel> spellbook;

    public Spellbook()
    {
        spellbook=new HashMap<SpellType,UserSpellLevel>();
    }

    public UserSpellLevel getPage(SpellType spellType)
    {
        return(spellbook.get(spellType));
    }

    public void addPage(SpellType spellType,UserSpellLevel usrSpellLvl)
    {
        spellbook.put(spellType, usrSpellLvl);
    }

    public void removePage(SpellType spellType)
    {
        spellbook.remove(spellType);
    }

    public List<UserSpellLevel> getSpellList()
    {
        List<UserSpellLevel> spelllist;
        UserSpellLevel tempUsrLvl;
        int cnt;

        spelllist= new ArrayList<UserSpellLevel>();

        cnt=spellbook.size();

        if(cnt==0)
            return(null);//empty spellbook

        for (Map.Entry<SpellType,UserSpellLevel> entry:spellbook.entrySet())
        {
            tempUsrLvl=entry.getValue();

            spelllist.add(tempUsrLvl);
        }

        return(spelllist);
    }

    public void updateSpellList(List<UserSpellLevel> sspellList)
    {
        int a,cnt;
        SpellType iTemp;

        if(sspellList==null)
            return;

        cnt=sspellList.size();

        for(a=0;a<cnt;a++)
        {

            addPage(sspellList.get(a).type,
                    sspellList.get(a));

        }
    }

    public int getCost(SpellType spellType, Range range)
    {
        UserSpellLevel userSpell;

        userSpell=spellbook.get(spellType);

        if(userSpell==null)
            return (0);

        if(range==Range.AOE)
        {
            return(userSpell.costAOE);
        }
        else
        {
            return(userSpell.effectTGT);
        }
    }

    public void clearBook()
    {
        spellbook.clear();
    }
}
