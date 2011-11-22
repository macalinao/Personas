/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import com.crimsonrpg.personas.personas.npc.SimpleNPCManager;
import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.npc.NPCManager;
import com.crimsonrpg.personas.personasapi.npc.Trait;
import com.crimsonrpg.personas.personasapi.npc.TraitName;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main Persons plugin.
 */
public class PersonasPlugin extends JavaPlugin {
    public static final Logger LOGGER = Logger.getLogger("Minecraft");
    
    public PersonasPlugin() {
        super();
        Personas.getInstance().setNPCManager(new SimpleNPCManager());
    }
    
    public void onDisable() {
        LOGGER.info("[Personas] Plugin disabled.");
    }

    public void onEnable() {
        LOGGER.info("[Personas] Plugin enabled.");
    }
    
    public void save() {
        File npcsFile = new File("/plugins/Personas/npcs.yml");
        npcsFile.mkdirs();
        
        try {
            npcsFile.createNewFile();
        } catch (IOException ex) {
            LOGGER.severe("[Personas] Could not create the NPCs file.");
            return;
        }
        
        FileConfiguration npcsConfig = YamlConfiguration.loadConfiguration(npcsFile);
        
        NPCManager manager = Personas.getNPCManager();
        
        //Save all NPCs
        for (NPC npc : manager.getNPCs()) {
            String npcId = npc.getId();
            for (Trait trait : npc.getTraits()) {
                
                //The section of the particular trait
                String sectionPath = "npcs." + npcId + ".traits." + trait.getClass().getAnnotation(TraitName.class).value();
                ConfigurationSection traitSection = npcsConfig.getConfigurationSection(sectionPath);
                if (traitSection == null) traitSection = npcsConfig.createSection(sectionPath);
                
                //Save the trait
                trait.save(traitSection);
                
            }
            
            //TODO: save location, name, id, etc.
        }
    }
}
