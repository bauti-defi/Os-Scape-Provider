package org.osscape.api.wrappers;


import org.bot.util.PolygonUtils;
import org.bot.util.Random;
import org.osscape.api.data.Calculations;
import org.osscape.api.data.Game;
import org.osscape.util.Utilities;

import java.awt.*;
import java.util.LinkedList;


public class Model extends org.bot.provider.osrs.models.Model {

	public Model(Object model) {
		super(model);
	}

	public Model(org.bot.provider.osrs.models.Model wrapper, int orientation, int x, int y, int z) {
		super(wrapper, orientation, x, y, z);
	}

	@Override
	public void draw(Graphics2D graphics, Color color) {
		if (Game.isLoggedIn() && isOnScreen()) {
			graphics.setColor(color);
			Polygon[] tangles = getTriangles();
			for (Polygon triangle : tangles) {
				graphics.draw(triangle);
			}

			graphics.setColor(Color.YELLOW);

		}
	}


	@Override

	public Polygon[] getTriangles() {

		LinkedList<Polygon> polygons = new LinkedList<>();

		int[] indices1 = getXTriangles();
		int[] indices2 = getYTriangles();
		int[] indices3 = getZTriangles();

		int[] xPoints = getXVertices();
		int[] yPoints = getYVertices();
		int[] zPoints = getZVertices();

		int len = indices1.length;

		for (int i = 0; i < len; ++i) {
			Point p1 = Calculations.tileToCanvas(this.getGridX() + xPoints[indices1[i]], this.getGridY() + zPoints[indices1[i]],
					-yPoints[indices1[i]] + this.getZ());
			Point p2 = Calculations.tileToCanvas(this.getGridX() + xPoints[indices2[i]], this.getGridY() + zPoints[indices2[i]],
					-yPoints[indices2[i]] + this.getZ());
			Point p3 = Calculations.tileToCanvas(this.getGridX() + xPoints[indices3[i]], this.getGridY() + zPoints[indices3[i]],
					-yPoints[indices3[i]] + this.getZ());
			if (p1 != null && p2 != null && p3 != null) {
				if (p1.x >= 0 && p2.x >= 0 && p3.x >= 0) {
					polygons.add(new Polygon(new int[] {p1.x, p2.x, p3.x}, new int[] {p1.y, p2.y, p3.y}, 3));
				}
			}
		}

		return polygons.toArray(new Polygon[polygons.size()]);
	}

	public Point getCenterPoint() {
		Polygon[] triangles = getTriangles();
		if (triangles.length > 0) {
			Polygon p = triangles[Random.nextInt(0, triangles.length)];
			Point point = PolygonUtils.polygonCenterOfMass(p);
			if (Utilities.inViewport(point)) {
				return point;
			}
		}

		return new Point(-1, -1);
	}

	public Point getRandomPoint() {

		Polygon[] triangles = getTriangles();
		if (triangles.length > 0) {
			for (int i = 0; i < 100; i++) {
				Polygon p = triangles[Random.nextInt(0, triangles.length)];
				Point point = new Point(p.xpoints[Random.nextInt(0, p.xpoints.length)],
						p.ypoints[Random.nextInt(0, p.ypoints.length)]);
				if (Utilities.inViewport(point)) {
					return point;
				}
			}
		}

		return new Point(-1, -1);
	}

	@Override
	public boolean isOnScreen() {
		return Utilities.inViewport(getRandomPoint());
	}

}