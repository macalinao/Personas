package com.crimsonrpg.personas.personas.flag;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.crimsonrpg.flaggables.api.FlagId;
import com.crimsonrpg.flaggables.api.GenericFlag;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.PlayerInventory;

import com.crimsonrpg.personas.personasapi.persona.Persona;
import org.bukkit.Bukkit;

/**
 * The core trait. Developers don't need to access this.
 */
@FlagId("npc-core")
public class FlagNPCCore extends GenericFlag<NPC> {

    private Location location;

    private int health;

    private PlayerInventory inventory;

    public void load(ConfigurationSection cs) {
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
        cs.set("loc.world", location.getWorld().getName());
        cs.set("loc.x", location.getX());
        cs.set("loc.y", location.getY());
        cs.set("loc.z", location.getZ());
        cs.set("loc.pitch", location.getPitch());
        cs.set("loc.yaw", location.getYaw());
        cs.set("health", health);
    }

    public void reset() {
        location = new Location(null, 0, 0, 0);
        health = 0;
        inventory = null;
    }

    public int getHealth() {
        return health;
    }

    protected void setHealth(int health) {
        this.health = health;
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    protected void setInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    public Location getLocation() {
        return location;
    }

    protected void setLocation(Location location) {
        this.location = location;
    }
}
