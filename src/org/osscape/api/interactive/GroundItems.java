package org.osscape.api.interactive;


import org.bot.Engine;
import org.bot.util.Filter;
import org.bot.util.Random;
import org.bot.util.Utilities;
import org.osscape.api.data.Game;
import org.osscape.api.wrappers.GroundItem;
import org.osscape.api.wrappers.Tile;

import java.util.ArrayList;
import java.util.List;


public class GroundItems {


	public static GroundItem[] getAll(Filter<GroundItem> filter) {
		List<GroundItem> groundItems = new ArrayList<GroundItem>();

		Object[][][] groundArrayObjects = (Object[][][]) Engine.getReflectionEngine().getFieldHookValue("GroundItems", null);

		int z = Game.getPlane();
		for (int x = 0; x < 104; x++) {
			for (int y = 0; y < 104; y++) {
				Object nl = groundArrayObjects[z][x][y];
				if (nl != null) {
					Object holder = Engine.getReflectionEngine().getFieldHookValue("Head", nl);
					Object curNode = Engine.getReflectionEngine().getFieldHookValue("Next", holder);
					while (curNode != null && curNode != holder && curNode != Engine.getReflectionEngine().getFieldHookValue("Head", nl)) {
						GroundItem groundItem = new GroundItem(curNode, new Tile(Game.getBaseX() + x, Game.getBaseY() + y, Game.getPlane()));
						if (filter == null || filter.accept(groundItem)) {
							groundItems.add(groundItem);
						}
						curNode = Engine.getReflectionEngine().getFieldHookValue("Next", curNode);
					}
				}
			}
		}
		return groundItems.toArray(new GroundItem[groundItems.size()]);
	}

	public static GroundItem[] getAll(final String... names) {
		return getAll(new Filter<GroundItem>() {
			@Override
			public boolean accept(GroundItem groundItem) {
				return groundItem.isValid() && groundItem.getName() != null && Utilities.inArray(groundItem.getName(), names);
			}
		});
	}

	public static GroundItem[] getAll(final int... ids) {
		return getAll(new Filter<GroundItem>() {
			@Override
			public boolean accept(GroundItem groundItem) {
				return groundItem.isValid() && Utilities.inArray(groundItem.getId(), ids);
			}
		});
	}

	public static GroundItem[] getAll() {
		return getAll(new Filter<GroundItem>() {
			@Override
			public boolean accept(GroundItem groundItem) {
				return true;
			}
		});
	}


	public static GroundItem getNearest(Filter<GroundItem> filter) {
		return getNearest(Players.getLocal().getLocation(), filter);
	}

	public static GroundItem getNearest(Tile start, Filter<GroundItem> filter) {
		GroundItem closet = new GroundItem(null, null);
		int distance = 16;
		for (GroundItem groundItem : getAll(filter)) {
			if (groundItem.isValid() && distance > groundItem.distanceTo(start)) {
				closet = groundItem;
				distance = groundItem.distanceTo(start);
			}
		}
		return closet;
	}

	public static GroundItem getNearest(final int... ids) {
		return getNearest(Players.getLocal().getLocation(), new Filter<GroundItem>() {
			@Override
			public boolean accept(GroundItem groundItem) {
				return groundItem.isValid() && Utilities.inArray(groundItem.getId(), ids);
			}
		});
	}

	public static GroundItem getNearest(final String... names) {
		return getNearest(Players.getLocal().getLocation(), new Filter<GroundItem>() {
			@Override
			public boolean accept(GroundItem groundItem) {
				return groundItem.isValid() && Utilities.inArray(groundItem.getName(), names);
			}
		});
	}

	public static GroundItem getAt(final Tile tile) {
		return getNearest(Players.getLocal().getLocation(), new Filter<GroundItem>() {

			@Override
			public boolean accept(GroundItem obj) {
				return obj != null && tile.equals(obj.getLocation());
			}
		});
	}

	public static GroundItem getNext(Filter<GroundItem> filter) {
		GroundItem[] groundItems = getAll(filter);
		if (groundItems == null || groundItems.length < 1)
			return nil();
		return groundItems[Random.nextInt(0, groundItems.length)];
	}

	public static GroundItem getNext(final String... names) {
		return getNext(new Filter<GroundItem>() {
			@Override
			public boolean accept(GroundItem groundItem) {
				return groundItem.isValid() && groundItem.getName() != null && Utilities.inArray(groundItem.getName(), names);
			}
		});
	}

	public static GroundItem getNext(final int... ids) {
		return getNext(new Filter<GroundItem>() {
			@Override
			public boolean accept(GroundItem groundItem) {
				return groundItem.isValid() && Utilities.inArray(groundItem.getId(), ids);
			}
		});
	}

	public static GroundItem nil() {
		return new GroundItem(null, null);
	}

}