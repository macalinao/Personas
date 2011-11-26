/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.martin.bukkit.npclib.NPCEntity;

import com.crimsonrpg.personas.personas.PersonasPlugin;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.npc.NPCManager;
import com.crimsonrpg.personas.personasapi.npc.Trait;
import com.crimsonrpg.personas.personasapi.npc.TraitId;
import com.crimsonrpg.personas.personasapi.persona.Persona;

/**
 * The default NPC manager implementation.
 */
public class SimpleNPCManager implements NPCManager {
    private Map<String, NPC> npcs = new HashMap<String, NPC>();
    private Map<String, Class<? extends Trait>> registeredTraits = new HashMap<String, Class<? extends Trait>>();
    private org.martin.bukkit.npclib.NPCManager handle;
    
    public SimpleNPCManager() {
        
    }
    
    public void load(PersonasPlugin plugin) {
        handle = new org.martin.bukkit.npclib.NPCManager(plugin);
    }

    public Class<? extends Trait> getTraitType(String name) {
        return registeredTraits.get(name);
    }
    
    public void registerTrait(Class<? extends Trait> type) {
        //Check if the name of the trait has been set
        if (!type.isAnnotationPresent(TraitId.class)) {
            PersonasPlugin.LOGGER.warning("[Personas] The trait '" + type.getName() + "' does not have a name registered.");
            return;
        }
        
        //Get the name of the trait via annotations
        String name = type.getAnnotation(TraitId.class).value();
        
        //Check if the trait is already registered
        if (registeredTraits.containsKey(name) || registeredTraits.containsValue(type)) {
            PersonasPlugin.LOGGER.warning("[Personas] The trait '" + type.getName() + "' was attempted to be registered under '" + name + "' but already exists; cancelling registration...");
            return;
        }
        
        registeredTraits.put(name, type);
    }

    public NPC createNPC(String name, List<Trait> traits, Persona persona) {
        //Create an ID
        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(name).append('-').append(persona.getName());
        
        //Append a different number to the string if the id is taken
        for (int i = 0; npcs.containsKey(idBuilder.toString()); i++) {
            idBuilder = (new StringBuilder()).append(idBuilder.toString()).append(i);
        }
        
        //Spawn it
        return createNPC(idBuilder.toString(), name, traits, persona);
    }

    public NPC createNPC(String id, String name, List<Trait> traits, Persona persona) {
        if (npcs.containsKey(id)) {
            PersonasPlugin.LOGGER.warning("[Personas] An NPC with the id '" + id + "' already exists; returning the existing NPC.");
            return npcs.get(id);
        }
        
        NPC theNpc = new SimpleNPC(id, name, traits, persona);

//        String title = theNpc.getName() + "\n"
//                + ChatColor.AQUA + "<" + theNpc.getType().name() + ">";
        
        //TODO: spout support?
        //SpoutManager.getAppearanceManager().setGlobalTitle((LivingEntity) theNpc.getHandle().getBukkitEntity(), title);

        npcs.put(id, theNpc);
        return theNpc;
    }

    public boolean deleteNPC(String id) {
        //Check if the NPC exists
        if (!npcs.containsKey(id)) return false;
        
        //Despawn
        handle.despawnById(id);
        npcs.remove(id);
        return true;
    }

    public void deleteNPC(NPC npc) {
        this.deleteNPC(npc.getId());
    }

    public void spawnNPC(String id, Location location) {
        NPC npc = this.getNPC(id);
        spawnNPC(npc, location);
    }

    public void spawnNPC(NPC npc, Location location) {
        String trimmedName = npc.getName().substring(0, 16);
        NPCEntity handleNPC = handle.spawnNPC(trimmedName, location, npc.getId());
        ((SimpleNPC) npc).setHandle(handleNPC);
    }

    public void despawnNPC(String id) {
        handle.despawnById(id);
    }

    public void despawnNPC(NPC npc) {
        handle.despawnById(npc.getId());
    }

    public NPC getNPC(String id) {
        return npcs.get(id);
    }

    public List<NPC> getNPCs() {
        return new ArrayList(npcs.values());
    }
}
