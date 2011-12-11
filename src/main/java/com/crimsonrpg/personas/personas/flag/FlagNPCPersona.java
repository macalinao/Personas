package com.crimsonrpg.personas.personas.flag;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.bukkit.configuration.ConfigurationSection;

import com.crimsonrpg.flaggables.api.FlagId;
import com.crimsonrpg.flaggables.api.GenericFlag;
import com.crimsonrpg.personas.personasapi.Personas;
import com.crimsonrpg.personas.personasapi.npc.NPC;
import com.crimsonrpg.personas.personasapi.persona.Persona;

/**
 * Contains the name of the persona.
 */
@FlagId("npc-persona")
public class FlagNPCPersona extends GenericFlag<NPC> {

    private String personaName;

    @Override
    public void load(ConfigurationSection section) {
        personaName = section.getString("name");
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("name", personaName);
    }

    @Override
    public void reset() {
        personaName = "null";
    }

    public Persona getPersona() {
        Persona persona = Personas.getPersonaManager().getPersona(personaName);
        if (persona == null) {
            return Personas.getPersonaManager().getPersona("null");
        }
        return persona;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String persona) {
        this.personaName = persona;
    }

    @Override
    public String toString() {
        return "FlagNPCPersona{" + "personaName=" + personaName + '}';
    }
}
