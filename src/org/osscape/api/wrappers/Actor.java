package org.osscape.api.wrappers;

import org.bot.Engine;
import org.bot.component.inputs.Mouse;
import org.bot.provider.osrs.models.ModelCallBack;
import org.bot.util.Condition;
import org.bot.util.Random;
import org.osscape.api.data.Calculations;
import org.osscape.api.data.Game;
import org.osscape.api.interactive.Menu;
import org.osscape.api.interactive.Walking;
import org.osscape.api.interfaces.Interactable;
import org.osscape.api.interfaces.Locatable;
import org.osscape.util.Utilities;

import java.awt.*;


public class Actor implements Locatable, Interactable {

	private final Object raw;

	public Actor(Object raw) {
		this.raw = raw;
	}

	protected Object getRaw() {
		return raw;
	}

	public int getLocalX() {
		if (!isValid())
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("WorldX", raw);
	}

	public int getLocalY() {
		if (!isValid())
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("WorldY", raw);
	}

	public int getX() {
		if (!isValid())
			return -1;
		return ((((int) Engine.getReflectionEngine().getFieldHookValue("WorldX", raw)) >> 7) + Game.getBaseX());
	}

	public int getY() {
		if (!isValid())
			return -1;
		return ((((int) Engine.getReflectionEngine().getFieldHookValue("WorldY", raw)) >> 7) + Game.getBaseY());
	}

	public int getQueueSize() {
		if (!isValid())
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("QueueSize", raw);
	}

	public boolean isMoving() {
		if (!isValid())
			return false;
		return getQueueSize() > 0;
	}

	public int getHealth() {
		if (!isValid())
			return -1;
		int hp = (int) Engine.getReflectionEngine().getFieldHookValue("Health", raw);
		if (hp == -1) {
			return 100;
		} else {
			return hp;
		}
	}

	public int getMaxHealth() {
		if (!isValid())
			return -1;
		int hp = (int) Engine.getReflectionEngine().getFieldHookValue("MaxHealth", raw);
		if (hp == -1) {
			return 100;
		} else {
			return hp;
		}
	}

	public int getOrientation() {
		if (!isValid())
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("ActorOrientation", raw);
	}

	public int getAnimation() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("Animation", raw);
	}

	public boolean hasAction(String action) {
		if (this instanceof NPC) {
			String[] actions = ((NPC) this).getActions();
			for (String s : actions) {
				if (s != null && s.length() > 0) {
					if (s.equalsIgnoreCase(action)) {
						return true;
					}
				}
			}
		} else if (this instanceof Player) {
			return false;
		}
		return false;
	}

	@Override
	public boolean isOnScreen() {
		Model m = getModel();
		if (m == null)
			return false;
		if (Utilities.inViewport(m.getRandomPoint())) {
			return true;
		}
		return false;
	}

	@Override
	public Point getPointOnScreen() {
		return getLocation().getPointOnScreen();
	}

	@Override
	public int distanceTo() {
		return Calculations.distanceTo(getLocation());
	}

	@Override
	public int distanceTo(Locatable locatable) {
		return Calculations.distanceBetween(getLocation(), locatable.getLocation());
	}

	@Override
	public int distanceTo(Tile tile) {
		return Calculations.distanceBetween(getLocation(), tile);
	}

	@Override
	public void draw(Graphics2D g, Color color) {
		getModel().draw(g, color);
	}

	@Override
	public void draw(Graphics2D g) {
		draw(g, Color.WHITE);
	}

	@Override
	public boolean turnTo() {
		return false;
	}

	@Override
	public Tile getLocation() {
		return new Tile(getX(), getY());
	}

	@Override
	public boolean canReach() {

		return false;
	}

	public Model getModel() {
		try {
			if (raw == null)
				return null;

			org.bot.provider.osrs.models.Model model = ModelCallBack.get(raw);

			if (model == null) {
				System.out.println("model == null");
				return null;
			}

			int tileByte = Walking.getTileFlags()[Game.getPlane()][getLocation().x - Game.getBaseX()][getLocation().y
					- Game.getBaseY()];
			if (this instanceof NPC) {
				NPC npc = (NPC) this;
				if (npc.isValid() && npc.getName() != null && npc.getName().toLowerCase().contains("fishing"))
					tileByte = 0;
			}

			return new Model(model, getOrientation(), getLocalX(), getLocalY(), tileByte == 1 ? 210 : 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getOverHeadText() {
		if (raw == null)
			return null;
		return (String) Engine.getReflectionEngine().getFieldHookValue("SpokenText", raw);
	}

	public String getName() {
		String name = null;
		if (this instanceof NPC) {
			name = (this).getName();
		} else if (this instanceof Player) {
			name = (this).getName();
		}
		return name;
	}

	public Actor getInteracting() {
		if (raw == null)
			return null;
		int interactingIndex = (int) Engine.getReflectionEngine().getFieldHookValue("InteractingIndex", raw);
		if (interactingIndex == -1)
			return new Actor(null);
		if (interactingIndex < 32768) {
			Object[] localNpcs = (Object[]) Engine.getReflectionEngine().getFieldHookValue("LocalNpcs", null);
			if (localNpcs.length > interactingIndex)
				return new NPC(localNpcs[interactingIndex]);
		} else {
			interactingIndex -= 32768;
			Object[] localPlayers = (Object[]) Engine.getReflectionEngine().getFieldHookValue("LocalPlayers", null);
			if (localPlayers.length > interactingIndex)
				return new Player(localPlayers[interactingIndex]);
		}
		return new Actor(null);
	}

	public boolean isValid() {
		return getRaw() != null;
	}

	@Override
	public boolean equals(Object a) {
		if (a != null && a instanceof Actor) {
			Actor t = (Actor) a;
			boolean x = this.getLocation().equals(t.getLocation()) && this.getAnimation() == t.getAnimation();
			if (t instanceof Player && this instanceof Player) {
				Player j = (Player) t;
				return x & j.getName().equals(((Player) this).getName());
			}
			return false;
		}
		return false;
	}

	@Override
	public String toString() {

		return "TODO";
	}

	@Override
	public Point getInteractPoint() {
		Model bounds = getModel();
		if (bounds != null) {
			System.out.println("Bounds != null");
			final Point p = bounds.getRandomPoint();
			if (bounds.contains(p)) {

				return p;
			} else {

				return bounds.getRandomPoint();
			}
		}
		return getPointOnScreen();
	}

	@Override
	public boolean interact(String action) {
		if (!this.isValid() || this == null)
			return false;
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < 2000) {
			if (this.isOnScreen()) {
				Point ip = null;
				if (this.isValid())
					ip = this.getInteractPoint();
				if (Mouse.getLocation().distance(ip) <= 5) {
					ip = this.getInteractPoint();
					Mouse.move(ip);
					Condition.sleep(75);
				} else {
					Mouse.move(ip);
					Condition.sleep(75);
				}

				if (this.isValid()) {
					if (!Menu.isOpen() && Menu.contains(action, this.getName())) {

						int index = Menu.index(action, this.getName());
						if (index == 0) {
							if (Menu.contains(action, this.getName())) {
								Condition.sleep(Random.nextInt(80, 150));
								Mouse.click(true);
								return true;
							}
						} else {
							Mouse.click(false);
							Condition.wait(new Condition.Check() {
								public boolean poll() {
									return Menu.isOpen();
								}
							}, 100, 20);
						}
					}
					if (Menu.isOpen() && Menu.contains(action, this.getName())) {
						int index = Menu.index(action, this.getName());
						Point p = Menu.getSuitablePoint(index);
						if (p.x > 5 && p.y > 5)
							Mouse.move(p.x, p.y);
						if (Mouse.getLocation().distance(p) <= Random.nextInt(1, 5)) {
							Condition.sleep(Random.nextInt(60, 100));
							Mouse.click(true);
							return true;
						}
					} else if (Menu.isOpen() && !Menu.contains(action, this.getName())) {
						int index = Menu.index("Cancel");
						Point p = Menu.getSuitablePoint(index);
						if (p.x > 5 && p.y > 5)
							Mouse.move(p.x, p.y);
						if (Mouse.getLocation().distance(p) <= Random.nextInt(1, 5)) {
							Condition.sleep(Random.nextInt(60, 100));
							Mouse.click(true);
							return true;
						}
					}
				}
			} else {
				if (this.distanceTo() >= 6) {

					continue;
				} else {
					this.turnTo();
					continue;
				}
			}
		}
		return false;

	}

	@Override
	public boolean interact(String action, String option) {
		return interact(action);

	}

	@Override
	public boolean click(boolean left) {
		Point interactingPoint = this.getInteractPoint();
		Model bounds = getModel();
		for (int i = 0; i < 3; i++) {
			if (bounds == null || bounds.contains(Mouse.getLocation())) {
				Mouse.click(left);
				return true;
			}
			if (bounds == null || !bounds.contains(interactingPoint)) {
				interactingPoint = this.getInteractPoint();
			}
			Mouse.move(interactingPoint);
		}
		return false;
	}

	@Override
	public boolean click() {
		return click(true);
	}
}