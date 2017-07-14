package org.osscape.api.definitions;


import java.util.Hashtable;


public class ItemDefinition {

	private static Hashtable<Integer, String> cache = new Hashtable<>();

	private String name;

	public ItemDefinition(int id) {
		if (cache.get(id) == null) {
			//  Object raw = Engine.getReflectionEngine().crudeGetItemMethod("getItemName", id);
			//String name = (String) Engine.getReflectionEngine().getFieldHookValue("ItemName", raw);
			cache.put(id, name);
		}
	}

	public String getName() {
		return name;
	}

	public boolean isValid() {
		return name != null;
	}
}