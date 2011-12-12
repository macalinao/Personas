/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas.npc;

import com.crimsonrpg.flaggables.api.GenericFlaggable;
import com.crimsonrpg.personas.personas.flag.FlagNPCName;
import com.crimsonrpg.personas.personas.flag.FlagNPCPersona;
import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.persona.Persona;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author simplyianm
 */
public abstract class SimpleNPC<T extends LivingEntity> extends GenericFlaggable implements NPC<T> {

    public SimpleNPC(String id) {
        super(id);
    }

    public Persona getPersona() {
        return getFlag(FlagNPCPersona.class).getPersona();
    }

    public NPC<T> setPersona(String persona) {
        getFlag(FlagNPCPersona.class).setPersonaName(persona);
        return this;
    }

    public String getName() {
        return getFlag(FlagNPCName.class).getName();
    }

    public String getFullName() {
        return getFlag(FlagNPCName.class).getFullName();
    }

    public NPC<T> setName(String name) {
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
}
