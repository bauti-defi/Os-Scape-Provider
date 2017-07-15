package org.osscape.api.interfaces;

import java.awt.*;

/**
 * Created by Kenneth on 7/30/2014.
 */
public interface Interactable {

	Point getInteractPoint();

	boolean interact(final String action, final String option);

	boolean interact(final String action);

	boolean click(final boolean left);

	boolean click();

}