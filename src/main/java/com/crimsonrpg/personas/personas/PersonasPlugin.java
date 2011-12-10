/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import com.crimsonrpg.personas.personasapi.persona.GenericPersona;
import com.crimsonrpg.flaggables.api.FlaggableLoader;
import com.crimsonrpg.flaggables.api.Flaggables;
import com.crimsonrpg.personas.personas.flag.FlagNPCCore;
import com.crimsonrpg.personas.personas.flag.FlagNPCName;
import com.crimsonrpg.personas.personas.flag.FlagNPCPersona;
import com.crimsonrpg.personas.personas.listener.PEntityListener;
import com.crimsonrpg.personas.personas.listener.PPlayerListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.npc.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;

/**
 * The main Persons plugin.
 */
public class PersonasPlugin extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("Minecraft");

    public PersonasPlugin() {
        super();
        Personas.getInstance().setNPCManager(new SimpleNPCManager());
        Personas.getInstance().setPersonaManager(new SimplePersonaManager());
    }

    public void onDisable() {
        save();
        LOGGER.info("[Personas] Plugin disabled.");
    }

    public void onEnable() {
        NPCManager npcManager = Personas.getNPCManager();
        ((SimpleNPCManager) npcManager).load(this);
        Personas.getPersonaManager().registerPersona(new GenericPersona());

        Flaggables.getFlagManager().registerFlags(
                FlagNPCName.class,
                FlagNPCPersona.class);

        //Register events
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, new PEntityListener(), Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, new PPlayerListener(), Priority.Highest, this);
        
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
        List<NPC> npcList = Flaggables.getFlagManager().readFlaggables(npcsConfig, new FlaggableLoader<NPC>() {

            public NPC create(String id) {
                return new SimpleHumanNPC(id);
            }

        });

        Personas.getNPCManager().load(npcList);
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
        
        List<NPC> npcList = Personas.getNPCManager().getList();
        for (NPC npc : npcList) {
            npc.getFlag(FlagNPCCore.class)
                    .setLocation(npc.getBukkitHandle().getLocation())
                    .setHealth(npc.getBukkitHandle().getHealth())
                    .setInventory(((Player) npc.getBukkitHandle()).getInventory());
        }
        
        Flaggables.getFlagManager().writeFlaggables(Personas.getNPCManager().getList(), npcsConfig);
    }

}
