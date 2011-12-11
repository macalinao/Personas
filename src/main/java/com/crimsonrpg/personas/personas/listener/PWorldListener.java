/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas.listener;

import com.crimsonrpg.personas.personas.PersonasPlugin;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldListener;

/**
 * The world listener.
 */
public class PWorldListener extends WorldListener {
    private boolean loaded = false;
    private PersonasPlugin plugin;
    
    public PWorldListener(PersonasPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!loaded) {
            plugin.load();
            loaded = true;
        }
    }
}
