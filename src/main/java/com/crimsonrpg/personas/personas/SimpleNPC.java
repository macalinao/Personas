/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.martin.bukkit.npclib.NPCEntity;

import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.npc.Trait;
import com.crimsonrpg.personas.personasapi.persona.Persona;

/**
 * Represents a simple NPC.
 */
public final class SimpleNPC implements NPC {

    private String id;

    private String name;

    private Map<Class, Trait> traits = new HashMap<Class, Trait>();

    private Persona persona;

    private NPCEntity handle;

    private HumanEntity bukkitHandle;

    public SimpleNPC(String id, String name, List<Trait> traits, Persona persona) {
        this.id = id;
        this.name = name;
        this.persona = persona;
        this.handle = null;

        //Add in the traits
        for (Trait trait : traits) {
            this.traits.put(trait.getClass(), trait);
        }

        //Add the bukkit handle
        bukkitHandle = null;
        
        updateCoreTrait();
    }
    
    void updateCoreTrait() {
        //Add the core trait
        TraitCore trait = this.getTrait(TraitCore.class);
        trait.setHealth(this.getHealth());
        trait.setInventory(this.getInventory());
        trait.setLocation(this.getLocation());
        trait.setName(this.getName());
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public <T extends Trait> T getTrait(Class<T> type) {
        Trait trait = traits.get(type);
        if (trait != null) {
            return type.cast(trait);
        }

        //Creates a default trait if it is not found
        try {
            T theTrait = type.newInstance();
            theTrait.reset();
            this.setTrait(theTrait);
            return theTrait;
        } catch (InstantiationException ex) {
            PersonasPlugin.LOGGER.severe("[Personas] Could not instantiate a new Trait for '" + type.getName() + "' in the NPC " + id + ".");
        } catch (IllegalAccessException ex) {
            PersonasPlugin.LOGGER.severe("[Personas] Could not access a new Trait for '" + type.getName() + "' in the NPC " + id + ".");
        }

        return null;
    }

    public void setTrait(Trait trait) {
        traits.put(trait.getClass(), trait);
    }

    public List<Trait> getTraits() {
        return new ArrayList(traits.values());
    }

    public Persona getPersona() {
        return persona;
    }

    public void spawn(Location location) {
        Personas.getNPCManager().spawnNPC(this, location);
    }

    public void despawn() {
        Personas.getNPCManager().despawnNPC(this);
    }

    public void delete() {
        //Bye bye
        Personas.getNPCManager().deleteNPC(id);
    }

    public HumanEntity getBukkitHandle() {
        return bukkitHandle;
    }

    /**
     * Sets the NPC's handle.
     * 
     * @param handle The NPC handle.
     */
    protected void setHandle(NPCEntity handle) {
        this.handle = handle;
        this.bukkitHandle = (HumanEntity) handle.getBukkitEntity();
    }
}
