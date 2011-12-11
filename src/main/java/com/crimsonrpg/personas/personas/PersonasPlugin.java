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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.HumanNPC;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.npc.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.player.PlayerListener;
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
        PlayerListener pl = new PPlayerListener(this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, new PEntityListener(), Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, pl, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, pl, Priority.Highest, this);

        LOGGER.info("[Personas] Plugin enabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("This server is sporting " + this.getDescription().getFullName() + " "
                    + getDescription().getVersion() + " by " + getDescription().getAuthors() + ".");
            return true;
        }

        String function = args[0];

        if (function.equals("reload")) {

            if (!sender.hasPermission("personas.reload")) {
                sender.sendMessage(ChatColor.DARK_RED + "You're not allowed to use this command.");
                return false;
            }

            load();

        } else if (function.equals("save")) {

            if (!sender.hasPermission("personas.save")) {
                sender.sendMessage(ChatColor.DARK_RED + "You're not allowed to use this command.");
                return false;
            }

            save();

        }

        return true;
    }

    public void load() {
        LOGGER.info("[Personas] Loading NPCs...");
        
        FileConfiguration npcsConfig = getNPCsFile();
        if (npcsConfig == null) {
            LOGGER.severe("[Personas] The NPC file was unable to be loaded.");
            return;
        }
        
        Personas.getNPCManager().load(npcsConfig);

        for (NPC npc : Personas.getNPCManager().getList()) {
            FlagNPCCore flag = npc.getFlag(FlagNPCCore.class);
            
            //Check if the npc is manifested
            if (flag.getLocation() == null) {
                return;
            }
            
            Personas.getNPCManager().spawnNPC(npc, flag.getLocation());
            LivingEntity handle = npc.getBukkitHandle();
            
            //Valid health amount
            int health = flag.getHealth();
            if (health >= 0 && health <= 20) {
                handle.setHealth(health);
            }
            if (npc instanceof HumanNPC) {
                HumanNPC human = (HumanNPC) npc;
                //TODO: inventory loader
            }
        }

    }

    public void save() {
        LOGGER.info("[Personas] Saving NPCs...");
        
        FileConfiguration npcsConfig = getNPCsFile();
        if (npcsConfig == null) {
            LOGGER.severe("[Personas] The NPC file was unable to be loaded.");
            return;
        }
        
        List<NPC> npcList = Personas.getNPCManager().getList();
        for (NPC npc : npcList) {
            LivingEntity handle = npc.getBukkitHandle();

            if (handle != null) {
                npc.getFlag(FlagNPCCore.class).setLocation(handle.getLocation()).setHealth(handle.getHealth());
            } else {
                npc.getFlag(FlagNPCCore.class).reset();
            }
            if (npc instanceof HumanNPC) {
                HumanNPC human = (HumanNPC) npc;
                //TODO: save inventory
            }
        }

        Personas.getNPCManager().save(npcsConfig);
    }
    
    private FileConfiguration getNPCsFile() {
        File pluginFolder = new File("./plugins/Personas/");
        pluginFolder.mkdirs();

        File npcsFile = new File(pluginFolder.getPath() + File.separator + "npcs.yml");
        
        try {
            npcsFile.createNewFile();
        } catch (IOException ex) {
            LOGGER.severe("[Personas] Could not create the NPCs file.");
            return null;
        }

        return YamlConfiguration.loadConfiguration(npcsFile);
    }
}
