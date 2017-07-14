package org.osscape.api.wrappers;

import org.bot.Engine;
import org.bot.component.inputs.Mouse;
import org.bot.util.Condition;
import org.bot.util.Random;
import org.osscape.api.interactive.Menu;
import org.osscape.api.interactive.Widgets;
import org.osscape.api.interfaces.Interactable;
import org.osscape.util.Utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WidgetChild implements Interactable {

	private Object raw;
	private int index;
	private int widgetX;
	private int widgetY;

	public WidgetChild(Object raw, int index) {
		this.raw = raw;
		this.index = index;
	}

	public int getX() {
		if (raw == null)
			return -1;
		if (widgetX > 0)
			return widgetX;
		int staticPosition = getStaticPosition();
		int relX = (int) Engine.getReflectionEngine().getFieldHookValue("AbsoluteX", raw);
		int[] posX = (int[]) Engine.getReflectionEngine().getFieldHookValue("WidgetPositionX", null);
		WidgetChild parent = getParent();
		int x = 0;
		if (parent != null) {
			x = parent.getX() - parent.getScrollX();
		} else {
			if (staticPosition != -1 && posX[staticPosition] > 0) {
				x = (posX[staticPosition] + (getType() > 0 ? relX : 0));
				widgetX = Utilities.isPointValid(new Point(x, 0)) ? x : relX;
				return widgetX;
			}
		}
		widgetX = (relX + x);
		return widgetX;
	}


	public int getY() {
		if (raw == null) {
			return -1;
		}
		if (widgetY > 0)
			return widgetY;
		int staticPosition = getStaticPosition();
		int relY = (int) Engine.getReflectionEngine().getFieldHookValue("AbsoluteY", raw);
		int[] posY = (int[]) (int[]) Engine.getReflectionEngine().getFieldHookValue("WidgetPositionY", null);
		WidgetChild parent = getParent();
		int y = 0;
		if (parent != null) {
			y = parent.getY() - parent.getScrollY();
		} else {
			if (staticPosition != -1 && posY[staticPosition] > 0) {
				y = (posY[staticPosition] + (getType() > 0 ? relY : 0));
				widgetY = Utilities.isPointValid(new Point(0, y)) ? y : relY;
				return widgetY;
			}
		}
		widgetY = (y + relY);
		return widgetY;
	}

	public WidgetChild getParent() {
		if (raw == null) {
			return null;
		}

		int uid = getParentId();
		if (uid == -1) {
			int groupIdx = getId() >>> 16;
			final HashTableIterator hti = new HashTableIterator(Engine.getReflectionEngine().getFieldHookValue("WidgetNodeCache", null));
			for (Object n = hti.getFirst(); n != null; n = hti.getNext()) {
				if ((Engine.getReflectionEngine().getFieldHookValue("Id", n) + "").equalsIgnoreCase(groupIdx + "")) {
					uid = Integer.parseInt((Engine.getReflectionEngine().getFieldHookValue("UID", n)) + "");


				}
			}
		}

		if (uid == -1) {
			return null;
		}
		int parent = uid >> 16;
		int child = uid & 0xffff;

		return Widgets.get(parent, child);
	}

	public int getId() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("WidgetID", raw);

	}

	public String[] getActions() {
		return (String[]) Engine.getReflectionEngine().getFieldHookValue("WidgetActions", raw);
	}

	public String getText() {
		if (raw == null)
			return null;
		return (String) Engine.getReflectionEngine().getFieldHookValue("Text", raw);
	}

	public String getName() {
		return ((String) Engine.getReflectionEngine().getFieldHookValue("WidgetName", raw)).replaceAll("<col=(.*?)>", "");
	}

	public int getRelativeX() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("RelativeX", raw);
	}

	public int getTextureID() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("TextureID", raw);
	}

	public int getRelativeY() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("RelativeY", raw);
	}

	public int getWidth() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("WidgetWidth", raw);
	}

	public int getHeight() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("WidgetHeight", raw);
	}

	public boolean isVisible() {
		if (raw == null) {
			return false;
		}
		if ((boolean) Engine.getReflectionEngine().getFieldHookValue("IsHidden", raw)) {
			return false;
		}
		int parentId = this.getParentId();
		if (parentId == -1) {
			return true;
		}
		if (parentId == 0) {
			return false;
		}
		final WidgetChild parent = Widgets.get(parentId >> 16, parentId & 65535);
		if (!parent.isVisible()) {
			return false;
		}
		return true;
	}

	public int getScrollX() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("ScrollX", raw);
	}

	public int getScrollY() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("ScrollY", raw);
	}


	public int[] getWidgetPositionX() {
		if (raw == null)
			return null;
		return (int[]) Engine.getReflectionEngine().getFieldHookValue("WidgetPositionX", raw);
	}

	public int[] getWidgetPositionY() {
		if (raw == null)
			return null;
		return (int[]) Engine.getReflectionEngine().getFieldHookValue("WidgetPositionY", raw);
	}

	public int getType() {
		if (raw == null)
			return -1;

		return (int) Engine.getReflectionEngine().getFieldHookValue("WidgetType", raw);

	}


	public int getParentId() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("ParentID", raw);
	}


	public int getItemStack() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("ItemAmount", raw);
	}

	public int getStaticPosition() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("BoundsIndex", raw);
	}

	public int getParentIndex() {
		return getId() >> 16;
	}

	public int getItemId() {
		if (raw == null)
			return -1;
		return (int) Engine.getReflectionEngine().getFieldHookValue("WidgetItemID", raw);
	}

	public int[] getSlotContentIds() {
		return (int[]) Engine.getReflectionEngine().getFieldHookValue("InvIDs", raw);
	}

	public int[] getStackSizes() {
		return (int[]) Engine.getReflectionEngine().getFieldHookValue("WidgetStackSizes", raw);
	}

	public int getIndex() {
		return index;
	}

	public WidgetChild[] getChildren() {
		List<WidgetChild> list = new ArrayList<>();
		Object[] children = (Object[]) Engine.getReflectionEngine().getFieldHookValue("Children", raw);
		if (children == null)
			return list.toArray(new WidgetChild[list.size()]);
		for (int i = 0; i < children.length; i++) {
			list.add(new WidgetChild(children[i], i));
		}
		return list.toArray(new WidgetChild[list.size()]);
	}

	public WidgetChild getChild(int index) {
		Object[] children = (Object[]) Engine.getReflectionEngine().getFieldHookValue("Children", raw);
		if (children == null || children != null && children.length <= index)
			return new WidgetChild(null, index);
		return new WidgetChild(children[index], index);
	}


	public Rectangle bounds() {
		WidgetChild localWidget = getParent();
		if (localWidget == null) {
			return new Rectangle(0, 0, 0, 0);
		}
		int i = localWidget.getRelativeX();
		int j = localWidget.getRelativeY();
		int k = localWidget.getWidth();
		int m = localWidget.getHeight();

		return new Rectangle(i, j, k, m);
	}

	@Override
	public Point getInteractPoint() {
		Rectangle rect = getArea();
		return new Point(Random.nextInt(rect.x, rect.x + rect.width), Random.nextInt(rect.y, rect.y + rect.height));
	}

	public Point getLocation() {
		return new Point(getX(), getY());
	}

	public Rectangle getArea() {
		return new Rectangle(getLocation().x, getLocation().y, getWidth(), getHeight());
	}

	public boolean containsAction(final String phrase) {
		if (this.getActions() == null || this.getActions().length == 0) {
			return false;
		}

		for (int i = 0; i < this.getActions().length; i++) {
			if (this.getActions()[i] != null && this.getActions()[i].contains(phrase)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean interact(String action) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < 2000) {

			Point ip = null;
			if (this != null)
				ip = this.getInteractPoint();
			if (Mouse.getLocation().distance(ip) <= 5) {
				ip = this.getInteractPoint();
				Mouse.move(ip);
				Condition.sleep(75);
			} else {
				Mouse.move(ip);
				Condition.sleep(75);
			}

			if (this != null) {
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
		}

		return false;

	}

	@Override
	public boolean interact(String action, String name) {
		return interact(action, name);
	}

	@Override
	public boolean click(boolean left) {
		Point interactingPoint;
		Rectangle bounds;
		for (int i = 0; i < 3; i++) {
			interactingPoint = this.getInteractPoint();
			bounds = getArea();
			Mouse.move(interactingPoint);
			if (bounds == null || bounds.contains(Mouse.getLocation())) {
				Mouse.click(left);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean click() {
		return click(true);
	}

	class HashTableIterator {

		private Object hashTable;
		private int currindex;
		private Object curr;

		HashTableIterator(Object hashTable) {
			this.hashTable = hashTable;
		}

		final Object getFirst() {
			currindex = 0;
			return getNext();
		}

		final Object getNext() {
			if (hashTable == null)
				return null;
			final Object[] buckets = (Object[]) Engine.getReflectionEngine().getFieldHookValue("Buckets", hashTable);
			if (buckets == null)
				return null;
			if (currindex > 0 && curr != buckets[currindex - 1]) {
				final Object node = curr;
				if (node == null) {
					return null;
				}
				curr = Engine.getReflectionEngine().getFieldHookValue("Prev", node);
				return node;
			}
			while (currindex < buckets.length) {
				final Object node1 = Engine.getReflectionEngine().getFieldHookValue("Prev", buckets[currindex++]);
				if (node1 != null) {
					if (buckets[currindex - 1] != node1) {
						curr = Engine.getReflectionEngine().getFieldHookValue("Prev", node1);
						return node1;
					}
				}
			}
			return null;
		}
	}


}