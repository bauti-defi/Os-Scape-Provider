package org.osscape.overlays;

import org.bot.component.screen.ScreenOverlay;
import org.bot.util.Filter;
import org.osscape.api.data.Game;
import org.osscape.api.data.Inventory;
import org.osscape.api.interactive.Item;

import java.awt.*;

/**
 * Created by Ethan on 7/12/2017.
 */
public class InventoryOverylay extends ScreenOverlay<Item> {

	@Override
	public Item[] elements() {
		return Inventory.getAllItems(new Filter<Item>() {
			@Override
			public boolean accept(Item i) {
				return i.isValid() && i != null;
			}
		});
	}

	@Override
	public boolean activate() {
		return org.osscape.util.Utilities.isDebugInventory() && Game.isLoggedIn();
	}

	@Override
	public void render(Graphics2D graphics) {
		final FontMetrics metrics = graphics.getFontMetrics();

		for (Item i : refresh()) {
			if (i.isValid()) {
				System.out.println(i.getName());
				graphics.setColor(Color.GREEN);
				Point point = i.getCentralPoint();
				graphics.setFont(new Font("default", Font.BOLD, 14));
				String id = String.valueOf(i.getId());
				graphics.drawString(id,
						point.x - (metrics.stringWidth(String.valueOf(i.getId())) / 2), point.y + 5);
				graphics.drawRect(i.getArea().x, i.getArea().y, i.getArea().width, i.getArea().height);
			}
		}
	}

}