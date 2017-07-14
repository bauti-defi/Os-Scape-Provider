package org.osscape.util;

import org.bot.Engine;
import org.osscape.api.data.Game;
import org.osscape.api.wrappers.Model;

import java.awt.*;
import java.util.Hashtable;

/**
 * Created by Ethan on 7/9/2017.
 */
public class Utilities {
	private static boolean debugNpcs, debugPlayers, basicinfo, debugInventory;
	private static Hashtable<Object, Model> modelCache = new Hashtable<>();

	public static Hashtable<Object, Model> getModelCache() {
		return modelCache;
	}

	public static boolean isDebugInventory() {
		return debugInventory;
	}

	public static void setDebugInventory(boolean debugInventory) {
		Utilities.debugInventory = debugInventory;
	}

	public static boolean isDebugNpcs() {
		return debugNpcs;
	}

	public static void setDebugNpcs(boolean debugNpcs) {
		Utilities.debugNpcs = debugNpcs;
	}

	public static boolean isDebugPlayers() {
		return debugPlayers;
	}

	public static void setDebugPlayers(boolean debugPlayers) {
		Utilities.debugPlayers = debugPlayers;
	}

	public static boolean isBasicinfo() {
		return basicinfo;
	}

	public static void setBasicinfo(boolean basicinfo) {
		Utilities.basicinfo = basicinfo;
	}

	public static boolean inViewport(final Point p) {
		if (!Game.isLoggedIn())
			return false;
		return pointInViewport(p.x, p.y);
	}

	public static boolean pointInViewport(final int x, final int y) {
		if (!Game.isLoggedIn())
			return false;
		return Engine.getGameViewport().contains(new Point(x, y));
	}

	public static boolean isPointValid(Point point) {
		final Rectangle GAME_SCREEN = new Rectangle(0, 0, Engine.getGameComponent().getWidth(),
				Engine.getGameComponent().getHeight());
		return GAME_SCREEN.contains(point);
	}

	public static Point generatePoint(Shape region) {
		Rectangle r = region.getBounds();
		double x, y;
		do {
			x = r.getX() + r.getWidth() * Math.random();
			y = r.getY() + r.getHeight() * Math.random();
		} while (!region.contains(x, y));

		return new Point((int) x, (int) y);
	}
}
