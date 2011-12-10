/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import com.crimsonrpg.personas.personasapi.persona.GenericPersona;
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
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.HumanNPC;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.npc.NPCManager;
import org.bukkit.Bukkit;
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
        Personas.getPersonaManager().registerPersona(new GenericPersona("null"));

        Flaggables.getFlagManager().registerFlags(
                FlagNPCCore.class,
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
        Personas.getNPCManager().load(npcsConfig);

        for (NPC npc : Personas.getNPCManager().getList()) {
            FlagNPCCore flag = npc.getFlag(FlagNPCCore.class);
            LivingEntity handle = npc.getBukkitHandle();
            
            handle.setHealth(flag.getHealth());
            
            if (flag.getLocation() != null) {
                Personas.getNPCManager().spawnNPC(npc, flag.getLocation());
            }
            if (npc instanceof HumanNPC) {
                HumanNPC human = (HumanNPC) npc;
                //TODO: inventory loader
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

        List<NPC> npcList = Personas.getNPCManager().getList();
        for (NPC npc : npcList) {
            npc.getFlag(FlagNPCCore.class)
                    .setLocation(npc.getBukkitHandle().getLocation())
                    .setHealth(npc.getBukkitHandle().getHealth());
            if (npc instanceof HumanNPC) {
                HumanNPC human = (HumanNPC) npc;
                //TODO: save inventory
            }
        }

        Personas.getNPCManager().save(npcsConfig);
    }
}
