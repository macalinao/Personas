/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas.npc;

import com.crimsonrpg.personas.personasapi.npc.NPC;
import org.martin.bukkit.npclib.NPCEntity;

import com.crimsonrpg.personas.personasapi.npc.HumanNPC;
import org.bukkit.entity.Player;

/**
 * Represents a simple NPC.
 */
public class SimpleHumanNPC extends SimpleNPC<Player> implements HumanNPC {

    private NPCEntity handle;

    public SimpleHumanNPC(String id) {
        super(id);
    }

    public Player getBukkitHandle() {
        return (Player) handle.getBukkitEntity();
    }

    /**
     * Sets the NPC's handle.
     * 
     * @param handle The NPC handle.
     */
    public void setHandle(NPCEntity handle) {
        this.handle = handle;
    }

    @Override
    public NPC<Player> setName(String name) {
        if (handle != null) {
            handle.setName(getName());
        }
        return super.setName(name);
    }

    @Override
    public String toString() {
        return "SimpleHumanNPC{" + "handle=" + handle + ",flags=" + getFlags() + '}';
    }
}
