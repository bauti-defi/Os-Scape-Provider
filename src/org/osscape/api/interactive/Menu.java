package org.osscape.api.interactive;


import org.bot.Engine;
import org.bot.component.inputs.Mouse;
import org.bot.util.Condition;
import org.bot.util.Random;
import org.osscape.api.wrappers.Actor;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class Menu {

	private static final Pattern htmlTags = Pattern.compile("\\<.+?\\>");

	public static int getX() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("MenuX", null);
	}

	public static int getY() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("MenuY", null);
	}

	public static int getWidth() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("MenuWidth", null);
	}

	public static int getHeight() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("MenuHeight", null);
	}

	public static int getMenuSize() {
		return (int) Engine.getReflectionEngine().getFieldHookValue("MenuCount", null);
	}

	public static boolean isOpen() {
		return (Boolean) Engine.getReflectionEngine().getFieldHookValue("IsMenuOpen", null);
	}

	public static Rectangle getArea() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public static java.util.List<String> getActions() {
		ArrayList<String> actions = new ArrayList<>();
		String[] menuActions = (String[]) Engine.getReflectionEngine().getFieldHookValue("MenuActions", null);

		for (int i = Menu.getMenuSize() - 1; i >= 0; i--) {
			if (menuActions[i] != null) {
				actions.add(htmlTags.matcher(menuActions[i]).replaceAll(""));
			}
		}
		return actions;
	}

	public static java.util.List<String> getOptions() {
		ArrayList<String> options = new ArrayList<>();
		String[] menuOptions = (String[]) Engine.getReflectionEngine().getFieldHookValue("MenuOptions", null);
		for (int i = Menu.getMenuSize() - 1; i >= 0; i--) {
			if (menuOptions[i] != null) {
				options.add(htmlTags.matcher(menuOptions[i]).replaceAll(""));
			}
		}
		return options;
	}

	public static int index(String action) {
		return index(action, null);
	}


	public static int index(String action, String option) {
		int index = -1;
		int i = 0;
		for (String a : Menu.getActions()) {
			try {
				if (a.equalsIgnoreCase(action) && (option == null || Menu.getOptions().get(i).toLowerCase().contains(option.toLowerCase()))) {
					index = i;
					break;
				}
				i++;
			} catch (IndexOutOfBoundsException e) {

			}
		}
		return index;
	}

	public static Point getSuitablePoint(int index) {
		Rectangle r = Menu.getOptionRectangle(index);
		return new Point(Random.nextInt(r.x + 10, r.x + r.width - 10),
				Random.nextInt(r.y + 3, r.y + r.height - 3));
	}

	public static Rectangle getOptionRectangle(int idx) {
		Rectangle r = new Rectangle(-1, -1, -1, -1);
		if (getX() <= 0 || getY() <= 0 || getWidth() <= 0 || idx < 0)
			return r;

		final Rectangle rectangle = new Rectangle(getX(), getY() + 19 + idx * 15, getWidth(), 15);

		return rectangle;
	}

	public static int index(Actor n, String action) {
		return index(action, n.getName());
	}

	public static boolean contains(String action, String option) {
		return index(action, option) != -1;
	}

	public static boolean contains(String action, Actor option) {
		return index(action, option.getName()) != -1;
	}

	public static boolean contains(String action) {
		return index(action, null) != -1;
	}

	public static boolean interact(String action) {
		return interact(action, null, false);
	}

	public static boolean interact(String action, String option) {
		return interact(action, option, false);
	}

	public static boolean interact(String action, String option, boolean rightClick) {
		for (int i = 0; i < 10 && !contains(action, option); i++, Condition.sleep(Random.nextInt(40, 55))) ;
		int menuIndex = index(action, option);
		if (menuIndex < 0)
			return false;
		if (menuIndex == 0 && !rightClick) {
			Mouse.click(true);
			return true;
		}
		if (!Menu.isOpen()) {
			Mouse.click(false);
			for (int i = 0; i < 20 && !isOpen(); i++, Condition.sleep(Random.nextInt(40, 50))) ;
		}
		menuIndex = index(action, option);
		if (isOpen() && menuIndex > -1) {
			Condition.sleep(Random.nextInt(50, 200));
			Rectangle area = new Rectangle(getX(), getY() + 18 + menuIndex * 15, getWidth(), 15);
			Point p = Mouse.getLocation();
			int min = -1, max = -1;
			if (p.getX() > area.getX() && p.getX() < area.getX() + area.getWidth()) {
				min = (int) Math.max(area.getX(), p.getX() - ((int) 7 * Math.sqrt(menuIndex + 1)));
				max = (int) Math.min(area.getX() + area.getWidth(), p.getX() + ((int) 7 * Math.sqrt(menuIndex + 1)));
			}
			if (min >= max || min == -1) {
				min = (int) area.getX();
				max = (int) (area.getX() + area.getWidth());
			}
			Mouse.move(
					Random.nextInt(min, max),
					(int) (area.getY() + Random.nextInt(5, 10)));
			Condition.sleep(Random.nextInt(20, 40));
			Mouse.click(true);
			return true;
		}
		return false;
	}
}