package org.osscape.overlays;

import org.bot.Engine;
import org.bot.component.screen.ScreenOverlay;
import org.osscape.api.data.Game;
import org.osscape.api.interactive.Camera;
import org.osscape.api.interactive.Menu;
import org.osscape.api.interactive.Players;

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

		drawText("Animation -^^> " + Players.getLocal().getAnimation());
		drawText("Logged in -^^> " + Game.isLoggedIn() + " : " + Game.getGameState());
		drawText("Moving:  -^^> " + Players.getLocal().isMoving());
		drawText("Player Location -^^> [" + Players.getLocal().getLocation().getX() + " - "
				+ Players.getLocal().getLocation().getY() + "]");
		drawText("Floor -^^> " + Game.getPlane());
		drawText("Map Base -^^> [" + Game.getBaseX() + " , " + Game.getBaseY() + "]");
		drawText(
				"Camera -^^> [" + Camera.getX() + " , " + Camera.getY() + " , " + Camera.getZ() + "] Pitch: "
						+ Camera.getPitch() + " Yaw: " + Camera.getYaw() + " Map Angle: " + Camera.getAngle() + "]");
		drawText("Camera Angle -^^> " + Camera.getAngle());
		drawText("Menu Rectangle -^^> " + Menu.getArea().toString());
		drawText("Menu Open -^^> " + Menu.isOpen());
		drawText("Menu");

		java.util.List<String> actions = Menu.getActions();
		java.util.List<String> options = Menu.getOptions();
		for (int i = 0; i < actions.size(); i++) {
			if (options.size() > i) {
				drawText("-^^> A:" + actions.get(i) + " O:" + options.get(i));
			}
		}

		return debuggedList.toArray(new String[debuggedList.size()]);
	}

	@Override
	public boolean activate() {
		return Engine.getServerProvider().isDebugGameInfo();
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

	private void drawText(String debug) {
		debuggedList.add(debug);
	}

}