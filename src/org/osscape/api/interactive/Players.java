package org.osscape.api.interactive;


import org.bot.Engine;
import org.bot.util.Filter;
import org.osscape.api.wrappers.Player;
import org.osscape.api.wrappers.Tile;

import java.util.ArrayList;
import java.util.List;


public class Players {

	public static Player getLocal() {
		return new Player(Engine.getReflectionEngine().getFieldHookValue("LocalPlayer", null));
	}

	public static Player[] getAll() {
		return getAll(null);
	}

	public static Player[] getAll(Filter<Player> filter) {
		List<Player> list = new ArrayList<>();
		final Object[] objects = (Object[]) Engine.getReflectionEngine().getFieldHookValue("LocalPlayers", null);
		for (Object player : objects) {
			if (player != null) {
				Player wrapper = new Player(player);
				if ((filter == null || filter.accept(wrapper))) {
					list.add(wrapper);
				}
			}
		}
		return list.toArray(new Player[list.size()]);
	}

	public static Player getNearest(Tile location, Filter<Player> filter) {
		Player closet = new Player(null);
		int distance = 9999;
		for (Player player : getAll(filter)) {
			if (distance > player.distanceTo(location)) {
				closet = player;
			}
		}
		return closet;
	}

	public static Player getNearest(Filter<Player> filter) {
		return getNearest(Players.getLocal().getLocation(), filter);
	}

	public static Player nil() {
		return new Player(null);
	}

}

