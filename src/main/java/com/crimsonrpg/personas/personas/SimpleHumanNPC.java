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
    private Player bukkitHandle;

    SimpleHumanNPC(String id) {
        super(id);
    }

    public Persona getPersona() {
        return getFlag(FlagNPCPersona.class).getPersona();
    }

    public NPC<Player> setPersona(Persona persona) {
        getFlag(FlagNPCPersona.class).setPersona(persona);
        return this;
    }

    public String getName() {
        return getName(false);
    }

    public String getName(boolean compatible) {
        return (compatible) ? getFlag(FlagNPCName.class).getCompatibleName() : getFlag(FlagNPCName.class).getName();
    }

    public NPC<Player> setName(String name) {
        getFlag(FlagNPCName.class).setName(name);
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
        return bukkitHandle;
    }

    /**
     * Sets the NPC's handle.
     * 
     * @param handle The NPC handle.
     */
    void setHandle(NPCEntity handle) {
        this.handle = handle;
        this.bukkitHandle = (Player) handle.getBukkitEntity();
    }
}
