package org.osscape.api.wrappers;


import org.bot.Engine;
import org.osscape.api.definitions.NPCDefinition;
import org.osscape.api.interfaces.Identifiable;
import org.osscape.api.interfaces.Nameable;

public class NPC extends Actor implements Identifiable, Nameable {

	private NPCDefinition npcDefinition;

	public NPC(Object raw) {
		super(raw);
		if (raw != null) {
			this.npcDefinition = new NPCDefinition(Engine.getReflectionEngine().getFieldHookValue("NpcDef", raw));
		}
	}

	public String getName() {
		return npcDefinition.getName();
	}

	public int getId() {
		return npcDefinition.getId();
	}

	public int getCombatLevel() {
		return npcDefinition.getCombatLevel();
	}

	public String[] getActions() {
		return npcDefinition.getActions();
	}


}