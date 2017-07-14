package org.osscape.api.interactive;

import org.bot.Engine;

/**
 * Created by Ethan on 7/10/2017.
 */
public class Walking {
	public static byte[][][] getTileFlags() {
		return (byte[][][]) Engine.getReflectionEngine().getFieldHookValue("TileSettings", null);
	}

	public static int[][] getCollisionFlags(int plane) {

		final Object collisionMap = ((Object[]) Engine.getReflectionEngine().getFieldHookValue("getCollisionMaps", null))[plane];
		return (int[][]) Engine.getReflectionEngine().getFieldHookValue("getFlags", collisionMap);

	}

}
