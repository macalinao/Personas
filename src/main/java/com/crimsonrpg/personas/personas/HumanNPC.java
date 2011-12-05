/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import com.crimsonrpg.flaggables.api.GenericFlaggable;

import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.martin.bukkit.npclib.NPCEntity;

import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.NPC;

/**
 * Represents a simple NPC.
 */
public final class HumanNPC extends GenericFlaggable implements NPC<HumanEntity> {

    private NPCEntity handle;

    private HumanEntity bukkitHandle;

    HumanNPC(String id) {
        super(id);
    }

    public void spawn(Location location) {
        Personas.getNPCManager().spawnNPC(this, location);
    }

    public void despawn() {
        Personas.getNPCManager().despawnNPC(this);
    }

    public void delete() {
        Personas.getNPCManager().destroy(this);
    }

    public HumanEntity getBukkitHandle() {
        return bukkitHandle;
    }

    /**
     * Sets the NPC's handle.
     * 
     * @param handle The NPC handle.
     */
    void setHandle(NPCEntity handle) {
        this.handle = handle;
        this.bukkitHandle = (HumanEntity) handle.getBukkitEntity();
    }

}
