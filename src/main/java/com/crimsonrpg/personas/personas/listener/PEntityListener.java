/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas.listener;

import com.crimsonrpg.personas.personas.PersonasEventFactory;
import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.event.npc.NPCLeftClickEvent;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

/**
 *
 * @author simplyianm
 */
public class PEntityListener extends EntityListener {

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return;
        }
        EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent) event;
        
        //Check if we have a player damager
        Entity damagerEntity = ede.getDamager();
        if (!(damagerEntity instanceof Player)) {
            return;
        }
        Player player = (Player) damagerEntity;
        
        //Check if we have an NPC damagee
        Entity damagedEntity = ede.getEntity();
        if (!(damagedEntity instanceof LivingEntity)) {
            return;
        }
        
        NPC npc = Personas.getNPCManager().fromBukkitEntity((LivingEntity) damagedEntity);
        if (npc == null) {
            return;
        }
        
        //Call the event
        NPCLeftClickEvent ev = PersonasEventFactory.callNPCLeftClickEvent(npc, player);
        if (ev.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        
        //Check for damage
        if (!ev.isDamaging()) {
            event.setCancelled(true);
        }
        
        //Do the stuff
        npc.getPersona().onNPCLeftClick(ev);
    }
    
}
