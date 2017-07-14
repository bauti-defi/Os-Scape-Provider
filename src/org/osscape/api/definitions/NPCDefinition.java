package org.osscape.api.definitions;

import org.bot.Engine;

public class NPCDefinition {

	private Object raw;

	public NPCDefinition(Object raw) {
		this.raw = raw;

	}

	public int getId() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("NpcID", raw);
	}

	public int getCombatLevel() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("NpcCombatLevel", raw);
	}

	public String getName() {
		if (raw == null)
			return null;
		return (String) Engine.getReflectionEngine().getFieldHookValue("NpcName", raw);
	}

	public String[] getActions() {
		if (raw == null)
			return null;
		return (String[]) Engine.getReflectionEngine().getFieldHookValue("NpcActions", raw);
	}

	public boolean isValid() {
		return raw != null;
	}
}