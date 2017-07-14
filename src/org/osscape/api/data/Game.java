package org.osscape.api.data;

import org.bot.Engine;
import org.osscape.api.interactive.Widgets;
import org.osscape.api.wrappers.WidgetChild;

/**
 * Created by Ethan on 7/7/2017.
 */
public class Game {

	public static final int STATE_LOGGED_IN = 30;
	public static final int STATE_LOG_IN_SCREEN = 10;
	public static final int CONNECTING = 20;

	public static int getBaseX() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("BaseX", null);
	}

	public static int getBaseY() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("BaseY", null);
	}

	public static int getPlane() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("Plane", null);
	}

	public static int getEnergy() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("Energy", null);
	}

	public static int getGameState() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("LoginState", null);
	}

	public static int getCurrentWorld() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("CurrentWorld", null);
	}

	public static int getGameCycle() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("LoopCycle", null);
	}

	public static boolean isLoggedIn() {
		return getGameState() == STATE_LOGGED_IN;
	}

	public static boolean isUsingSpecialAttack() {
		return Settings.get(301) == 1;
	}

	public static int getSpecialAttackPercent() {
		if (Settings.get(300) > 0) {
			return Settings.get(300) / 10;
		}
		return 0;
	}

	public static boolean isRunning() {
		return Settings.get(173) > 0;
	}

	public static void toggleRun() {
		WidgetChild c = Widgets.get(160, 21);
		if (getEnergy() > 0) {
			if (c != null) {
				if (c.isVisible()) {
					c.interact("Toggle Run");
				}
			}
		}
	}

	public static Tab getCurrentTab() {
		final int WIDGET_PARENT = 548;
		final WidgetChild[] children = Widgets.get(WIDGET_PARENT).getChildren();
		if (children == null || children.length == 0)
			return null;
		for (WidgetChild p : children) {
			if (p.getTextureID() != -1 && p.getActions() != null) {
				String[] actions = p.getActions();
				for (Tab tab : Tab.values()) {
					for (String action : actions) {
						if (tab.getName().equalsIgnoreCase(action)) {
							return tab;
						}
					}
				}
			}
		}
		return null;
	}
}
