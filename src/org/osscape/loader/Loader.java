package org.osscape.loader;

import org.bot.Engine;
import org.bot.component.screen.ScreenOverlay;
import org.bot.provider.loader.ServerLoader;
import org.bot.provider.manifest.HookType;
import org.bot.provider.manifest.Revision;
import org.bot.provider.manifest.ServerManifest;
import org.bot.util.injection.Injector;
import org.bot.util.reflection.ReflectedClass;
import org.bot.util.reflection.ReflectedField;
import org.osscape.api.injectables.ModelInjector;
import org.osscape.overlays.*;
import org.osscape.parameters.OSParameters;
import org.osscape.ui.menu.ePopUpMenu;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 7/6/2017.
 */
@ServerManifest(author = "Ethan", serverName = "OS-Scape", info = "Os-Scape botting that will blow your god damn mind.", type = Applet.class, version = 0.1D, revision = Revision.OSRS, hookType = HookType.XML)
public class Loader extends ServerLoader<Applet> {
	private JPanel panel;
	private OSParameters parameters;

	public Loader() throws IOException {
		super("https://cdn.os-scape.com/clients/game.jar", "https://pastebin.com/raw/A1uwxUhj", "OS-Scape");
	}

	@Override
	public JPopupMenu getPopUpMenu() {
		return new ePopUpMenu();
	}

	@Override
	public List<Injector> getInjectables() {
		List<Injector> injectors = new ArrayList<>();
		injectors.add(new ModelInjector());
		return injectors;
	}

	@Override
	public List<ScreenOverlay> getOverlays() {
		List<ScreenOverlay> overlays = new ArrayList<>();
		overlays.add(new PlayerInfoOverlay());
		overlays.add(new MouseOverlay());
		overlays.add(new NPCInfoOverlay());
		overlays.add(new BasicInfoDebugger());
		overlays.add(new InventoryOverylay());
		return overlays;
	}

	@Override
	protected Applet loadComponent() throws IllegalArgumentException, IllegalAccessException {
		parameters = new OSParameters();
		panel = new JPanel();
		try {
			panel.setLayout(new BorderLayout());
			panel.setPreferredSize(new Dimension(765, 503));

			ReflectedClass loaderClass;
			Object loaderInstance;

			try {
				loaderClass = Engine.getReflectionEngine().getClass(Engine.getReflectionEngine().getClassName("Loader"));
				loaderInstance = loaderClass.getNewInstance();
				ReflectedField loaderField = Engine.getReflectionEngine().getField("Loader");
				loaderField.setValue(loaderInstance);

				ReflectedField paramsField = Engine.getReflectionEngine().getField("Params");
				paramsField.setValue(parameters.paramsString);

				ReflectedField propField = Engine.getReflectionEngine().getField("Properties");
				propField.setValue(parameters.prop);

				ReflectedField ip = Engine.getReflectionEngine().getField("IpAddress");
				ip.setValue("93.158.237.2");

			} catch (Exception e) {
				e.printStackTrace();
			}
			Object clientInstance = Engine.getReflectionEngine().getClass("oss.iIIiIIiIII").getNewInstance();
			Applet applet = (Applet) clientInstance;
			applet.setStub(new LoaderStub(parameters));
			return applet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
