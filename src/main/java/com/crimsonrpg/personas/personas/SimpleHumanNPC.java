/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.persona.Persona;
import org.bukkit.Location;
import org.martin.bukkit.npclib.NPCEntity;

import com.crimsonrpg.flaggables.api.GenericFlaggable;
import com.crimsonrpg.personas.personas.flag.FlagNPCName;
import com.crimsonrpg.personas.personas.flag.FlagNPCPersona;
import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.HumanNPC;
import org.bukkit.entity.Player;

/**
 * Represents a simple NPC.
 */
public final class SimpleHumanNPC extends GenericFlaggable implements HumanNPC {

    private NPCEntity handle;

    SimpleHumanNPC(String id) {
        super(id);
    }

    public Persona getPersona() {
        return getFlag(FlagNPCPersona.class).getPersona();
    }

    public NPC<Player> setPersona(String persona) {
        getFlag(FlagNPCPersona.class).setPersonaName(persona);
        return this;
    }

    public String getName() {
        return getFlag(FlagNPCName.class).getName();
    }

    public String getFullName() {
        return getFlag(FlagNPCName.class).getFullName();
    }

    public NPC<Player> setName(String name) {
        getFlag(FlagNPCName.class).setFullName(name);
        return this;
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

    public Player getBukkitHandle() {
        return (Player) handle.getBukkitEntity();
    }

    /**
     * Sets the NPC's handle.
     * 
     * @param handle The NPC handle.
     */
    void setHandle(NPCEntity handle) {
        this.handle = handle;
    }

    @Override
    public String toString() {
        return "SimpleHumanNPC{" + "handle=" + handle + ",flags=" + getFlags() + '}';
    }
    
}
