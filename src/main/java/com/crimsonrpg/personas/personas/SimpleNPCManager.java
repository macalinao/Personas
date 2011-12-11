/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import com.crimsonrpg.flaggables.api.Flag;
import com.crimsonrpg.personas.personasapi.event.npc.NPCDestroyEvent;
import com.crimsonrpg.personas.personasapi.event.npc.NPCSpawnEvent;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.crimsonrpg.flaggables.api.FlaggableLoader;
import com.crimsonrpg.flaggables.api.GenericFlaggableManager;
import com.crimsonrpg.personas.personasapi.event.npc.NPCCreateEvent;
import com.crimsonrpg.personas.personasapi.event.npc.NPCDespawnEvent;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.npc.NPCManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.martin.bukkit.npclib.NPCEntity;

/**
 * The default NPC manager implementation.
 */
public class SimpleNPCManager extends GenericFlaggableManager<NPC> implements NPCManager {

    private Map<LivingEntity, NPC> bukkitMappings = new HashMap<LivingEntity, NPC>();
    private org.martin.bukkit.npclib.NPCManager handle;

    SimpleNPCManager() {
        super(new FlaggableLoader<NPC>() {

            public NPC create(String id, List<Flag> flags) {
                //TODO: load npcs other than humans
                if (flags == null) {
                    return new SimpleHumanNPC(id);
                }
                NPC npc = new SimpleHumanNPC(id);
                npc.addFlags(flags);
                return npc;
            }
        });
    }

    void load(PersonasPlugin plugin) {
        handle = new org.martin.bukkit.npclib.NPCManager(plugin);
    }

    public NPC createNPC(String name) {
        return createNPC(name, "null");
    }

    public NPC createNPC(String name, String persona) {

        //Create an ID
        StringBuilder idBuilder = new StringBuilder(persona);
        idBuilder.append("_").append(name);

        //Append a different number to the string if the id is taken
        for (int i = 0; idExists(idBuilder.toString()); i++) {
            idBuilder = (new StringBuilder()).append("_").append(idBuilder.toString()).append(i);
        }

        //Spawn it
        return createNPC(idBuilder.toString(), name, persona);
    }

    public NPC createNPC(String id, String name, String persona) {
        if (idExists(id)) {
            PersonasPlugin.LOGGER.warning("[Personas] An NPC with the id '" + id + "' already exists; returning the existing NPC.");
            return get(id);
        }

        NPC npc = create(id);
        npc.setName(name);

        if (persona == null) {
            npc.setPersona("null");
        } else {
            npc.setPersona(persona);
        }

        //Call the event
        NPCCreateEvent event = PersonasEventFactory.callNPCCreateEvent(npc);
        //TODO: make this not cancellable

        //TODO: spout support?
        //SpoutManager.getAppearanceManager().setGlobalTitle((LivingEntity) theNpc.getHandle().getBukkitEntity(), title);

        npc.getPersona().onNPCCreate(event);
        return npc;
    }

    @Override
    public NPC destroy(String id) {
        return destroy(get(id));
    }

    @Override
    public NPC destroy(NPC npc) {
        //Call the event
        NPCDestroyEvent event = PersonasEventFactory.callNPCDestroyEvent(npc);
        if (event.isCancelled()) {
            return null;
        }

        npc.getPersona().onNPCDestroy(event);
        despawnNPC(npc);
        return super.destroy(npc.getId());
    }

    public void spawnNPC(String id, Location location) {
        spawnNPC(get(id), location);
    }

    public void spawnNPC(NPC npc, Location location) {
        //Call the event
        NPCSpawnEvent event = PersonasEventFactory.callNPCSpawnEvent(npc, location);
        if (event.isCancelled()) {
            return;
        }

        npc.getPersona().onNPCSpawn(event);
        NPCEntity handel = handle.spawnNPC(npc.getName(), event.getLocation(), npc.getId());
        ((SimpleHumanNPC) npc).setHandle(handel); //As in the composer
        bukkitMappings.put((LivingEntity) handel.getBukkitEntity(), npc);
    }

    public void despawnNPC(String id) {
        despawnNPC(get(id));
    }

    public void despawnNPC(NPC npc) {
        //Call the event
        NPCDespawnEvent event = PersonasEventFactory.callNPCDespawnEvent(npc);
        if (event.isCancelled()) {
            return;
        }

        npc.getPersona().onNPCDespawn(event);
        handle.despawnById(npc.getId());
    }

    public NPC fromBukkitEntity(LivingEntity le) {
        return bukkitMappings.get(le);
    }
}
