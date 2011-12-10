package com.crimsonrpg.personas.personas.flag;

import com.crimsonrpg.flaggables.api.FlagId;
import com.crimsonrpg.flaggables.api.GenericFlag;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.PlayerInventory;

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

    public FlagNPCCore setHealth(int health) {
        this.health = health;
        return this;
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public FlagNPCCore setInventory(PlayerInventory inventory) {
        this.inventory = inventory;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public FlagNPCCore setLocation(Location location) {
        this.location = location;
        return this;
    }
}
