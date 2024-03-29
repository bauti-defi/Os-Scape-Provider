package org.osscape.api.wrappers;


import org.bot.Engine;
import org.osscape.api.interfaces.Nameable;

public class Player extends Actor implements Nameable {

	public Player(Object raw) {
		super(raw);
	}

	@Override
	public String getName() {
		if (getRaw() == null)
			return null;
		return (String) Engine.getReflectionEngine().getFieldHookValue("PlayerName", getRaw());
	}

	public int getCombatLevel() {
		if (getRaw() == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("CombatLevel", getRaw());
	}


}