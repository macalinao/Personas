/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;


import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.PlayerInventory;

import com.crimsonrpg.personas.personasapi.npc.Trait;
import com.crimsonrpg.personas.personasapi.npc.TraitId;
import org.bukkit.Bukkit;

/**
 * The core trait. Developers don't need to access this.
 */
@TraitId ("personas-core")
public class TraitCore implements Trait {
    private String name;
    private Location location;
    private int health;
    private PlayerInventory inventory;
    
    public void load(ConfigurationSection cs) {
        name = cs.getString("name");
        location = new Location(
                Bukkit.getWorld(cs.getString("loc.world")),
                cs.getDouble("loc.x"),
                cs.getDouble("loc.y"),
                cs.getDouble("loc.z"),
                (float) cs.getDouble("loc.pitch"),
                (float) cs.getDouble("loc.yaw"));
        health = cs.getInt("health");
        //TODO: inventory = inventory.
    }

    public void save(ConfigurationSection cs) {
        cs.set("name", name);
        cs.set("loc.world", location.getWorld().getName());
        cs.set("loc.x", location.getX());
        cs.set("loc.y", location.getY());
        cs.set("loc.z", location.getZ());
        cs.set("loc.pitch", location.getPitch());
        cs.set("loc.yaw", location.getYaw());
        cs.set("health", health);
    }

    public void reset() {
        name = "undefined";
        location = new Location(null, 0, 0, 0);
        health = 0;
        inventory = null;
    }
    
}
