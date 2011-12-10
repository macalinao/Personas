/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.personas.personas;

import com.crimsonrpg.personas.personasapi.persona.Persona;
import com.crimsonrpg.personas.personasapi.persona.PersonaManager;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author simplyianm
 */
public class SimplePersonaManager implements PersonaManager {
    private Map<String, Persona> personas = new HashMap<String, Persona>();
    
    public void registerPersona(Persona persona) {
        if (personas.containsKey(persona.getName())) {
            PersonasPlugin.LOGGER.info("[Personas] Duplicate persona registration! " + persona.getName() + " as " + persona.getClass() + " !");
        }
        personas.put(persona.getName(), persona);
    }

    public Persona getPersona(String id) {
        return personas.get(id);
    }
    
}
