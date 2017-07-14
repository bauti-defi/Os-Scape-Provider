package org.osscape.api.interactive;

import org.bot.Engine;
import org.bot.component.inputs.Keyboard;
import org.bot.util.Condition;
import org.bot.util.Random;
import org.osscape.api.interfaces.Locatable;
import org.osscape.api.wrappers.Player;
import org.osscape.api.wrappers.Tile;

import java.awt.event.KeyEvent;

/**
 * Created by Ethan on 7/9/2017.
 */
public class Camera {


	public static int angleTo(final int degrees) {
		int ca = getCameraYaw();
		if (ca < degrees) {
			ca += 360;
		}
		int da = ca - degrees;
		if (da > 180) {
			da -= 360;
		}
		return da;
	}

	public static boolean setPitch(final boolean up) {
		return setPitch(up ? 100 : 0);
	}

	public static int getAngleTo(final Locatable locatable) {
		final Player local = Players.getLocal();
		final Tile t1 = local != null ? local.getLocation() : null;
		final Tile t2 = locatable.getLocation();
		return t1 != null && t2 != null ? ((int) Math.toDegrees(Math.atan2(t2.getY() - t1.getY(), t2.getX() - t1.getX()))) - 90 : 0;
	}

	public static boolean setPitch(final int percent) {
		if (percent == getPitch()) {
			return true;
		}
		final boolean up = getPitch() < percent;
		Keyboard.press(up ? KeyEvent.VK_UP : KeyEvent.VK_DOWN);
		for (; ; ) {
			final int tp = getPitch();
			if (!Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return getPitch() != tp;
				}
			}, 10, 10)) {
				break;
			}
			final int p = getPitch();
			if (up && p >= percent) {
				break;
			} else if (!up && p <= percent) {
				break;
			}
		}
		Keyboard.release(up ? KeyEvent.VK_UP : KeyEvent.VK_DOWN);
		return Math.abs(percent - getPitch()) <= 8;
	}

	public static boolean setAngle(final int degrees) {
		final int d = degrees % 360;
		final int a = angleTo(d);
		if (Math.abs(a) <= 5) {
			return true;
		}
		final boolean l = a > 5;

		Keyboard.press(l ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT);
		final float dir = Math.signum(angleTo(d));
		for (; ; ) {
			final int a2 = angleTo(d);
			if (!Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return angleTo(d) != a2;
				}
			}, 10, 10)) {
				break;
			}
			final int at = angleTo(d);
			if (Math.abs(at) <= 15 || Math.signum(at) != dir) {
				break;
			}
		}
		Keyboard.release(l ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT);
		return Math.abs(angleTo(d)) <= 15;
	}

	public static boolean turnTo(final Locatable locatable) {
		return turnPitchTo(locatable) && turnAngleTo(locatable, 0);
	}

	public static boolean turnPitchTo(final Locatable locatable) {
		int pitch = (90 - (locatable.distanceTo() * 7));
		int factor = Random.nextInt(0, 1) == 0 ? -1 : 1;
		pitch = pitch + factor * Random.nextInt(5, 10);

		if (pitch > 90) {
			pitch = 90;
		} else if (pitch < 0) {
			pitch = Random.nextInt(5, 10);
		}

		return setPitch(pitch);
	}

	public static boolean turnAngleTo(final Locatable l, final int dev) {
		final int a = getAngleTo(l);
		if (dev == 0) {
			return setAngle(a);
		} else {
			return setAngle(Random.nextInt(a - dev, a + dev + 1));
		}
	}

	public static int getAngle() {
		return Math.abs((int) (getYaw() / 5.68) - 360);
	}

	public static int getCameraPitch() {
		return (int) (((int) (Engine.getReflectionEngine().getFieldHookValue("CameraPitch", null)) - 128) / 2.56);
	}

	public static int getPitch() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("CameraPitch", null);
	}

	public static int getYaw() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("CameraYaw", null);
	}

	public static int getCameraYaw() {
		return (int) ((int) Engine.getReflectionEngine().getFieldHookValue("CameraYaw", null) / 5.68);
	}

	public static int getX() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("CameraX", null);
	}

	public static int getY() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("CameraY", null);
	}

	public static int getZ() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("CameraZ", null);
	}


}
