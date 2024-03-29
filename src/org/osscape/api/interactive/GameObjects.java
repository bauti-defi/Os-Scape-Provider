package org.osscape.api.interactive;

import org.bot.Engine;
import org.bot.util.Filter;
import org.bot.util.Random;
import org.bot.util.Utilities;
import org.osscape.api.data.Game;
import org.osscape.api.wrappers.GameObject;
import org.osscape.api.wrappers.Tile;

import java.util.LinkedHashSet;
import java.util.Set;

public class GameObjects {

	public static GameObject[] getAll() {
		return getAll(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				return true;
			}
		});
	}

	public static GameObject[] getAll(final String... names) {
		return getAll(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				return gameObject.isValid() && gameObject.getName() != null
						&& Utilities.inArray(gameObject.getName(), names);
			}
		});
	}

	public static GameObject[] getAll(final int... ids) {
		return getAll(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				return gameObject.isValid() && Utilities.inArray(gameObject.getId(), ids);
			}
		});
	}

	public static GameObject[] getAll(Filter<GameObject> filter) {
		Set<GameObject> objects = new LinkedHashSet<>();
		Object region = Engine.getReflectionEngine().getFieldHookValue("Region", null);
		Object[][][] tiles = (Object[][][]) Engine.getReflectionEngine().getFieldHookValue("SceneTiles", region);
		if (tiles == null) {
			return objects.toArray(new GameObject[objects.size()]);
		}

		int z = Game.getPlane();
		int baseX = Game.getBaseX(), baseY = Game.getBaseY();

		for (int x = 0; x < 104; x++) {
			for (int y = 0; y < 104; y++) {
				Object groundTile = tiles[z][x][y];
				if (groundTile != null) {

					Object[] gameObjects = (Object[]) Engine.getReflectionEngine().getFieldHookValue("GameObject", groundTile);
					if (gameObjects != null) {
						for (Object j : gameObjects) {
							if (j != null) {
								GameObject obj = new GameObject(j, GameObject.Type.INTERACTIVE, x + baseX, y + baseY,
										z);
								if (obj != null && (filter == null || filter.accept(obj)))
									objects.add(obj);

							}
						}
					}

					Object boundary = Engine.getReflectionEngine().getFieldHookValue("BoundaryObject", groundTile);
					if (boundary != null) {
						GameObject obj = new GameObject(boundary, GameObject.Type.BOUNDARY, x + baseX, y + baseY, z);
						if (obj != null && (filter == null || filter.accept(obj)))
							objects.add(obj);
					}


				}

			}
		}
		return objects.toArray(new GameObject[objects.size()]);
	}

	public static GameObject getNearest(Filter<GameObject> filter) {
		return getNearest(Players.getLocal().getLocation(), filter);
	}

	public static GameObject getNearest(Tile start, Filter<GameObject> filter) {
		GameObject closet = new GameObject(null, GameObject.Type.INTERACTIVE, -1, -1, -1);
		int distance = 255;
		for (GameObject gameObject : getAll(filter)) {
			if (gameObject.isValid() && distance > gameObject.distanceTo(start)) {
				closet = gameObject;
				distance = gameObject.distanceTo(start);
			}
		}
		return closet;
	}

	public static GameObject getNearest(final int... ids) {
		return getNearest(Players.getLocal().getLocation(), new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				return gameObject.isValid() && Utilities.inArray(gameObject.getId(), ids);
			}
		});
	}

	public static GameObject getNearest(final String... names) {
		return getNearest(Players.getLocal().getLocation(), new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject groundItem) {
				return groundItem.isValid() && Utilities.inArray(groundItem.getName(), names);
			}
		});
	}

	public static GameObject getAt(final Tile tile) {
		return getNearest(Players.getLocal().getLocation(), new Filter<GameObject>() {

			@Override
			public boolean accept(GameObject obj) {
				return obj != null && tile.equals(obj.getLocation());
			}
		});
	}

/*	public static GameObject getNearbyBank() {
        GameObject current = null;
		for (GameObject n : GameObjects.getAll()) {
			if (Game.isLoggedIn() && n != null && n.isValid() && n.getLocation().distanceTo() < 15)				
				if (n.hasAction("Bank", n.getActions()) && (current == null || current.getLocation().distanceTo() > Players.getLocal()
									.distanceTo(n.getLocation()))) {
						current = n;
					}
		}
		return current;
	}*/

	public static GameObject getNext(Filter<GameObject> filter) {
		GameObject[] gameObjects = getAll(filter);
		if (gameObjects == null || gameObjects.length < 1)
			return nil();
		return gameObjects[Random.nextInt(0, gameObjects.length)];
	}

	public static GameObject getNext(final String... names) {
		return getNext(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				return gameObject.isValid() && gameObject.getName() != null
						&& Utilities.inArray(gameObject.getName(), names);
			}
		});
	}

	public static GameObject getNext(final int... ids) {
		return getNext(new Filter<GameObject>() {
			@Override
			public boolean accept(GameObject gameObject) {
				return gameObject.isValid() && Utilities.inArray(gameObject.getId(), ids);
			}
		});
	}

	public static GameObject nil() {
		return new GameObject(null, GameObject.Type.INTERACTIVE, -1, -1, -1);
	}
}