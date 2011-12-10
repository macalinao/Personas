/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import com.crimsonrpg.personas.personasapi.event.PersonasEvent;
import com.crimsonrpg.personas.personasapi.event.npc.NPCCreateEvent;
import com.crimsonrpg.personas.personasapi.event.npc.NPCDespawnEvent;
import com.crimsonrpg.personas.personasapi.event.npc.NPCDestroyEvent;
import com.crimsonrpg.personas.personasapi.event.npc.NPCLeftClickEvent;
import com.crimsonrpg.personas.personasapi.event.npc.NPCRightClickEvent;
import com.crimsonrpg.personas.personasapi.event.npc.NPCSpawnEvent;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Creates events for Personas.
 */
public class PersonasEventFactory {
    public static <T extends PersonasEvent> T callEvent(T event) {
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
    
    public static NPCCreateEvent callNPCCreateEvent(NPC npc) {
        return callEvent(new NPCCreateEvent(npc));
    }
    
    public static NPCDespawnEvent callNPCDespawnEvent(NPC npc) {
        return callEvent(new NPCDespawnEvent(npc));
    }
    
    public static NPCDestroyEvent callNPCDestroyEvent(NPC npc) {
        return callEvent(new NPCDestroyEvent(npc));
    }
    
    public static NPCLeftClickEvent callNPCLeftClickEvent(NPC npc, Player player) {
        return callEvent(new NPCLeftClickEvent(npc, player));
    }
    
    public static NPCRightClickEvent callNPCRightClickEvent(NPC npc, Player player) {
        return callEvent(new NPCRightClickEvent(npc, player));
    }
    
    public static NPCSpawnEvent callNPCSpawnEvent(NPC npc, Location location) {
        return callEvent(new NPCSpawnEvent(npc, location));
    }
}
