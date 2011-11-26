/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.crimsonrpg.personas.personas.npc.SimpleNPCManager;
import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.npc.NPCManager;
import com.crimsonrpg.personas.personasapi.npc.Trait;
import com.crimsonrpg.personas.personasapi.npc.TraitId;

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
        save();
        LOGGER.info("[Personas] Plugin disabled.");
    }

    public void onEnable() {
        ((SimpleNPCManager) Personas.getNPCManager()).load(this);
        load();
        LOGGER.info("[Personas] Plugin enabled.");
    }
    
    public void load() {
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
        npcsConfig.getConfigurationSection("npcs");
        
        //Check if the section has been created yet
        if (npcsConfig == null) return;
        
        //Loop through all listed NPCs
        for (String key : npcsConfig.getKeys(false)) {
            String npcId = key;
            NPC spawned = null; //TODO: this
            
            //Get traits
            ConfigurationSection traitsSection = npcsConfig.getConfigurationSection("npcs." + npcId + ".traits");
            if (traitsSection == null) continue;
            for (String traitKey : traitsSection.getKeys(false)) {
                
                //Prepare for trait creation
                ConfigurationSection traitSection = traitsSection.getConfigurationSection(traitKey);
                Class type = manager.getTraitType(traitKey);
                
                //Create the trait object
                try {
                    Trait trait = (Trait) type.newInstance();
                    trait.load(traitSection);
                    spawned.setTrait(trait); //And set it
                } catch (InstantiationException ex) {
                    LOGGER.severe("[Personas] Could not instantiate a new Trait.");
                } catch (IllegalAccessException ex) {
                    LOGGER.severe("[Personas] Could not instantiate a new Trait.");
                }
            }
        }
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
        npcsConfig.createSection("npcs"); //Clear all NPCs in the file
        
        //Save all NPCs
        for (NPC npc : manager.getNPCs()) {
            String npcId = npc.getId();
            for (Trait trait : npc.getTraits()) {
                
                //The section of the particular trait
                String sectionPath = "npcs." + npcId + ".traits." + trait.getClass().getAnnotation(TraitId.class).value();
                ConfigurationSection traitSection = npcsConfig.getConfigurationSection(sectionPath);
                if (traitSection == null) traitSection = npcsConfig.createSection(sectionPath);
                
                //Save the trait
                trait.save(traitSection);
                
            }
            
            //TODO: save location, name, id, etc.
        }
    }
}
