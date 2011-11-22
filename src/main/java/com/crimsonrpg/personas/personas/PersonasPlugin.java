/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main Persons plugin.
 */
public class PersonasPlugin extends JavaPlugin {
    public static final Logger LOGGER = Logger.getLogger("Minecraft");
    
    public void onDisable() {
        LOGGER.info("[Personas] Plugin disabled.");
    }

    public void onEnable() {
        LOGGER.info("[Personas] Plugin enabled.");
    }
    
}
