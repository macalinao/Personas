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
 *
 * @author simplyianm
 */
@FlagId("npc-persona")
public class FlagNPCPersona extends GenericFlag<NPC> {

    private Persona persona;

    @Override
    public void load(ConfigurationSection section) {
        persona = Personas.getPersonaManager().getPersona(section.getString("name"));
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("name", persona.getName());
    }

    @Override
    public void reset() {
        persona = Personas.getPersonaManager().getPersona("default");
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
