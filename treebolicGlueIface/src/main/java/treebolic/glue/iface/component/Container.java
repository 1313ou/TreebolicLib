package treebolic.glue.iface.component;

/**
 * Glue interface for Container
 *
 * @author Bernard Bou
 */
public interface Container<C>
{
	// POSITIONS

	int PANE = 0;

	int VIEW = 1;

	int TOOLBAR = 2;

	int STATUSBAR = 3;

	/**
	 * Add component
	 *
	 * @param component
	 *            component to add
	 * @param position
	 *            position (one of above constants)
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
