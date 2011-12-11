package com.crimsonrpg.personas.personas.flag;

import com.crimsonrpg.flaggables.api.FlagId;
import com.crimsonrpg.flaggables.api.GenericFlag;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Contains the NPC's name.
 */
@FlagId("npc-name")
public class FlagNPCName extends GenericFlag<NPC> {

    private String fullName;

    @Override
    public void load(ConfigurationSection section) {
        fullName = section.getString("name");
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("name", fullName);
    }

    @Override
    public void reset() {
        fullName = "Unnamed";
    }

    public String getName() {
        String stripped = ChatColor.stripColor(fullName);
        return (stripped.length() > 16) ? stripped.substring(0, 16) : stripped;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "FlagNPCName{" + "name=" + fullName + '}';
    }
}
