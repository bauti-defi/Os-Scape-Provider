package org.osscape.overlays;

import org.bot.Engine;
import org.bot.component.screen.ScreenOverlay;
import org.bot.util.Filter;
import org.osscape.api.data.Game;
import org.osscape.api.interactive.Players;
import org.osscape.api.wrappers.Player;

import java.awt.*;

/**
 * Created by Ethan on 7/9/2017.
 */
public class PlayerInfoOverlay extends ScreenOverlay<Player> {

	@Override
	public Player[] elements() {
		return Players.getAll(new Filter<Player>() {
			@Override
			public boolean accept(Player n) {
				return n.isValid() && n.distanceTo() < 7;
			}
		});
	}

	@Override
	public boolean activate() {
		return Game.isLoggedIn() && Engine.getServerProvider().isDebugPlayers();
	}

	@Override
	public void render(Graphics2D graphics) {
		final FontMetrics metrics = graphics.getFontMetrics();

		for (Player p : refresh()) {
			if (p != null && p.isValid()) {

				Point point = p.getPointOnScreen();

				graphics.setColor(Color.BLUE);
				graphics.fillRect(point.x, point.y, 5, 5);
				graphics.setColor(Color.black);
				String name = "[" + p.getName() + "]";
				graphics.drawString(name, point.x - (metrics.stringWidth(name) / 2), point.y - 5);
				p.getLocation().draw(graphics, Color.CYAN);

			}

		}

	}
}
