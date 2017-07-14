package org.osscape.overlays;

import org.bot.component.screen.ScreenOverlay;
import org.osscape.api.data.Game;
import org.osscape.api.interactive.Camera;
import org.osscape.api.interactive.Menu;
import org.osscape.api.interactive.Players;
import org.osscape.util.Utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 7/10/2017.
 */
public class BasicInfoDebugger extends ScreenOverlay<String> {
	private final List<String> debuggedList = new ArrayList<>();

	@Override
	public String[] elements() {
		debuggedList.clear();

		drawText(Utilities.isBasicinfo(), "Animation -^^> " + Players.getLocal().getAnimation());
		drawText(Utilities.isBasicinfo(), "Logged in -^^> " + Game.isLoggedIn() + " : " + Game.getGameState());
		drawText(Utilities.isBasicinfo(), "Moving:  -^^> " + Players.getLocal().isMoving());
		drawText(Utilities.isBasicinfo(), "Player Location -^^> [" + Players.getLocal().getLocation().getX() + " - "
				+ Players.getLocal().getLocation().getY() + "]");
		drawText(Utilities.isBasicinfo(), "Floor -^^> " + Game.getPlane());
		drawText(Utilities.isBasicinfo(), "Map Base -^^> [" + Game.getBaseX() + " , " + Game.getBaseY() + "]");
		drawText(Utilities.isBasicinfo(),
				"Camera -^^> [" + Camera.getX() + " , " + Camera.getY() + " , " + Camera.getZ() + "] Pitch: "
						+ Camera.getPitch() + " Yaw: " + Camera.getYaw() + " Map Angle: " + Camera.getAngle() + "]");
		drawText(Utilities.isBasicinfo(), "Camera Angle -^^> " + Camera.getAngle());
		drawText(Utilities.isBasicinfo(), "Menu Rectangle -^^> " + Menu.getArea().toString());
		drawText(Utilities.isBasicinfo(), "Menu Open -^^> " + Menu.isOpen());
		drawText(Utilities.isBasicinfo(), "Menu");

		java.util.List<String> actions = Menu.getActions();
		java.util.List<String> options = Menu.getOptions();
		for (int i = 0; i < actions.size(); i++) {
			if (options.size() > i) {
				drawText(true, "-^^> A:" + actions.get(i) + " O:" + options.get(i));
			}
		}

		return debuggedList.toArray(new String[debuggedList.size()]);
	}

	@Override
	public boolean activate() {
		return Utilities.isBasicinfo();
	}

	@Override
	public void render(Graphics2D graphics) {
		graphics.setColor(Color.orange);
		int yOff = 30;

		for (String str : elements()) {
			graphics.drawString(str, 15, yOff);
			yOff += 15;
		}
	}

	private void drawText(boolean active, String debug) {
		if (active)
			debuggedList.add(debug);
	}

}