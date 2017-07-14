package org.osscape.api.interfaces;

import org.osscape.api.wrappers.Tile;

import java.awt.*;

public interface Locatable {

	boolean isOnScreen();

	Point getPointOnScreen();

	int distanceTo();

	int distanceTo(Locatable locatable);

	int distanceTo(Tile tile);

	boolean turnTo();

	Tile getLocation();

	void draw(Graphics2D g, Color color);

	void draw(Graphics2D g);

	boolean canReach();

}
