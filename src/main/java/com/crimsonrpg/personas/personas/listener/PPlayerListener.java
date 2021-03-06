/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas.listener;

import com.crimsonrpg.personas.personas.PersonasEventFactory;
import com.crimsonrpg.personas.personas.PersonasPlugin;
import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.event.npc.NPCRightClickEvent;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author simplyianm
 */
public class PPlayerListener extends PlayerListener {

    private PersonasPlugin plugin;
    private boolean loaded = false;

    public PPlayerListener(PersonasPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity r = event.getRightClicked();
        if (!(r instanceof LivingEntity)) {
            return;
        }

        //Get the entity
        NPC npc = Personas.getNPCManager().fromBukkitEntity((LivingEntity) r);
        if (npc == null) {
            return;
        }

        //Call event
        NPCRightClickEvent ev = PersonasEventFactory.callNPCRightClickEvent(npc, event.getPlayer());
        if (ev.isCancelled()) {
            return;
        }

        //Trigger personas stuff
        npc.getPersona().onNPCRightClick(ev);
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!loaded) {
            plugin.load();
            loaded = true;
        }
    }
}
