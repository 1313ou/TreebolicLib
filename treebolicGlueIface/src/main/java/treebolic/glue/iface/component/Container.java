package treebolic.glue.iface.component;

/**
 * Glue interface for Container
 *
 * @author Bernard Bou
 */
public interface Container<C>
{
	// POSITIONS

	public static final int PANE = 0;

	public static final int VIEW = 1;

	public static final int TOOLBAR = 2;

	public static final int STATUSBAR = 3;

	/**
	 * Add component
	 *
	 * @param component
	 *            component to add
	 * @param position
	 *            position (one of above constants)
	 */
	public void addComponent(final C component, final int position);

	/**
	 * Remove all components
	 */
	public void removeAll();

	/**
	 * Validate layout
	 */
	public void validate();
}
