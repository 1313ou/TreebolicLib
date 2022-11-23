/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface.component;

/**
 * Glue interface for Container
 *
 * @param <C> platform component type
 * @author Bernard Bou
 */
public interface Container<C>
{
	// POSITIONS

	/**
	 * Position as pane
	 */
	int PANE = 0;

	/**
	 * Position as view
	 */
	int VIEW = 1;

	/**
	 * Position as toolbar
	 */
	int TOOLBAR = 2;

	/**
	 * Position as status bar
	 */
	int STATUSBAR = 3;

	/**
	 * Add component
	 *
	 * @param component component to add
	 * @param position  position (one of above constants)
	 */
	void addComponent(final C component, final int position);

	/**
	 * Remove all components
	 */
	void removeAll();

	/**
	 * Validate layout
	 */
	void validate();
}
