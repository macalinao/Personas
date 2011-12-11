package com.crimsonrpg.personas.personas.flag;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.crimsonrpg.flaggables.api.FlagId;
import com.crimsonrpg.flaggables.api.GenericFlag;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Contains the NPC's name.
 */
@FlagId("npc-name")
public class FlagNPCName extends GenericFlag<NPC> {
    private String name;
    
    @Override
    public void load(ConfigurationSection section) {
        name = section.getString("name");
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("name", name);
    }

    @Override
    public void reset() {
        name = "Unnamed";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getCompatibleName() {
        return (name.length() > 16) ? name.substring(0, 16) : name;
    }

    @Override
    public String toString() {
        return "FlagNPCName{" + "name=" + name + '}';
    }
}
