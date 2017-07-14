package org.osscape.api.data;

import org.bot.Engine;
import org.osscape.api.interactive.Camera;
import org.osscape.api.interactive.Players;
import org.osscape.api.interfaces.Locatable;
import org.osscape.api.wrappers.Tile;

import java.awt.*;

/**
 * Created by Ethan on 7/7/2017.
 */
public class Calculations {
	public static int[] SINE = new int[2048];
	public static int[] COSINE = new int[2048];

	static {
		for (int i = 0; i < SINE.length; i++) {
			SINE[i] = ((int) (65536.0D * Math.sin(i * 0.0030679615D)));
			COSINE[i] = ((int) (65536.0D * Math.cos(i * 0.0030679615D)));
		}
	}

	public static int distanceBetween(int x, int y, int x1, int y1) {
		return (int) Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
	}

	public static int distanceBetween(Point a, Point b) {
		return distanceBetween(a.x, a.y, b.x, b.y);
	}

	public static int distanceBetween(Tile a, Tile b) {
		return distanceBetween(a.getX(), a.getY(), b.getX(), b.getY());
	}

	public static Point tileToMap(Tile tile) {
		int xMapTile = tile.getX() - Game.getBaseX();
		int yMapTile = tile.getY() - Game.getBaseY();
		Object myPlayer = Engine.getReflectionEngine().getFieldHookValue("LocalPlayer", null);

		return worldToMap((xMapTile * 4 + 2) - (int) Engine.getReflectionEngine().getFieldHookValue("WorldX", myPlayer) / 32,
				(yMapTile * 4 + 2) - (int) Engine.getReflectionEngine().getFieldHookValue("WorldY", myPlayer) / 32);
	}

	public static Point worldToMap(int regionX, int regionY) {
		int mapScale = (int) Engine.getReflectionEngine().getFieldHookValue("MapScale", null);
		int mapOffset = (int) Engine.getReflectionEngine().getFieldHookValue("MapOffset", null);
		int angle = (int) Engine.getReflectionEngine().getFieldHookValue("MapAngle", null);
		int i = mapOffset + angle & 2047;
		int j = regionX * regionX + regionY * regionY;

		if (j <= 6400) {
			int sin = Calculations.SINE[i] * 256 / (mapScale + 256);
			int cos = Calculations.COSINE[i] * 256 / (mapScale + 256);

			int xMap = regionY * sin + regionX * cos >> 16;
			int yMap = regionY * cos - regionX * sin >> 16;

			return new Point(644 + xMap, 85 - yMap);
		}
		return new Point(-1, -1);
	}

	public static Point tileToCanvas(int regionX, int regionY, double double1, double double2, int regionZ) {
		return tileToCanvas((int) ((regionX - Game.getBaseX() + double1) * 128.0D),
				(int) ((regionY - Game.getBaseY() + double2) * 128.0D), regionZ);
	}

	public static Point tileToCanvas(Tile tile, double double1, double double2, int height) {
		return tileToCanvas((int) ((tile.getX() - Game.getBaseX() + double1) * 128.0D),
				(int) ((tile.getY() - Game.getBaseY() + double2) * 128.0D), height);
	}

	public static final Point tileToCanvas(int regionX, int regionY, int regionZ) {
		if ((regionX < 128) || (regionY < 128) || (regionX > 13056) || (regionY > 13056)) {
			return null;
		}
		int i = getTileHeight(Game.getPlane(), regionX, regionY) - regionZ;
		regionX -= Camera.getX();
		i -= Camera.getZ();
		regionY -= Camera.getY();
		int j = SINE[Camera.getPitch()];
		int k = COSINE[Camera.getPitch()];
		int m = SINE[Camera.getYaw()];
		int n = COSINE[Camera.getYaw()];
		int i1 = regionY * m + regionX * n >> 16;
		regionY = regionY * n - regionX * m >> 16;
		regionX = i1;
		i1 = i * k - regionY * j >> 16;
		regionY = i * j + regionY * k >> 16;
		i = i1;
		if (regionY >= 50) {
			int scale = (int) Engine.getReflectionEngine().getFieldHookValue("CameraScale", null);
			int width = (int) Engine.getReflectionEngine().getFieldHookValue("ViewportWidth", null);
			int height = (int) Engine.getReflectionEngine().getFieldHookValue("ViewportHeight", null);
			int i2 = regionX * scale / regionY + width / 2;
			int i3 = scale * i1 / regionY + height / 2;
			return new Point(i2, i3);
		}
		return null;
	}

	public static final int getTileHeight(int regionX, int regionY, int regionZ) {
		int i = regionY >> 7;
		int j = regionZ >> 7;
		if ((i < 0) || (j < 0) || (i > 103) || (j > 103)) {
			return 0;
		}
		int k = regionX;
		final byte[][][] meta = (byte[][][]) Engine.getReflectionEngine().getFieldHookValue("TileSettings", null);
		final int[][][] heights = (int[][][]) Engine.getReflectionEngine().getFieldHookValue("TileHeights", null);
		if ((k < 3) && ((meta[1][i][j] & 0x2) == 2)) {
			k++;
		}
		int m = heights[k][i][j] * (128 - (regionY & 0x7F)) + heights[k][(i + 1)][j] * (regionY & 0x7F) >> 7;
		int n = heights[k][i][(j + 1)] * (128 - (regionY & 0x7F))
				+ heights[k][(i + 1)][(j + 1)] * (regionY & 0x7F) >> 7;
		return m * (128 - (regionZ & 0x7F)) + n * (regionZ & 0x7F) >> 7;
	}

	public static int distanceTo(Locatable a) {
		final Tile loc = Players.getLocal().getLocation();
		return distanceBetween(a.getLocation().getX(), a.getLocation().getY(), loc.getX(), loc.getY());
	}

	public static boolean isOnMap(Tile tile) {
		return Calculations.distanceTo(tile) < 16;
	}

	public static int distanceTo(Tile a) {
		final Tile loc = Players.getLocal().getLocation();
		return (int) distanceBetween(a.getX(), a.getY(), loc.getX(), loc.getY());
	}

	public static int angleToTile(Tile t) {
		Tile me = Players.getLocal().getLocation();

		int angle = (int) Math.toDegrees(Math.atan2(t.getY() - me.getY(), t.getX() - me.getX()));
		return angle >= 0 ? angle : 360 + angle;
	}

}
