package org.osscape.ui.menu;

import org.bot.Engine;
import org.osscape.util.Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ethan on 7/10/2017.
 */
public class ePopUpMenu extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JMenu view;
	private final JCheckBoxMenuItem mouse, npcs, players, text, inventory;

	public ePopUpMenu() {
		view = new JMenu("Debugging");

		mouse = new JCheckBoxMenuItem("Mouse");
		mouse.addActionListener(this);

		npcs = new JCheckBoxMenuItem("NPCs");
		npcs.addActionListener(this);

		text = new JCheckBoxMenuItem("Text");
		text.addActionListener(this);

		players = new JCheckBoxMenuItem("Players");
		players.addActionListener(this);

		inventory = new JCheckBoxMenuItem("Inventory");
		inventory.addActionListener(this);


		view.add(npcs);
		view.add(players);
		view.add(text);
		view.add(inventory);
		view.add(mouse);


		add(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "Mouse":
				Engine.setDebugMouse(!Engine.isDebugMouse());
				System.out.println(Engine.isDebugMouse() ? "Enabled mouse drawing." : "Disabled mouse drawing.");
				break;
			case "Players":
				Utilities.setDebugPlayers(!Utilities.isDebugPlayers());
				System.out.println(Utilities.isDebugPlayers() ? "Enabled player drawing." : "Disabled player drawing.");
				break;
			case "NPCs":
				Utilities.setDebugNpcs(!Utilities.isDebugNpcs());
				System.out.println(Utilities.isDebugNpcs() ? "Enabled npc drawing." : "Disabled npc drawing.");
				break;
			case "Text":
				// Engine.getReflectionEngine().crudeGetItemMethod("getItemName", 2);
				Utilities.setBasicinfo(!Utilities.isBasicinfo());
				System.out.println(Utilities.isBasicinfo() ? "Enabled text drawing." : "Disabled text drawing.");
				break;
			case "Inventory":
				Utilities.setDebugInventory(!Utilities.isDebugInventory());
				System.out.println(Utilities.isDebugInventory() ? "Enabled inventory drawing." : "Disabled inventory drawing.");
				break;
		}
	}

}
