package treebolic.glue;

/**
 * Event listener
 *
 * @author Bernard Bou
 */
public abstract class EventListener implements treebolic.glue.iface.EventListener
{
	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onDown(int, int, boolean)
	 */
	@Override
	abstract public boolean onDown(int x, int y, boolean rotate);

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onUp(int, int)
	 */
	@Override
	abstract public boolean onUp(int x, int y);

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onDragged(int, int)
	 */
	@Override
	abstract public boolean onDragged(int x, int y);

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onHover(int, int)
	 */
	@Override
	abstract public boolean onHover(int x, int y);

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onSelect()
	 */
	@Override
	abstract public boolean onSelect(int x, int y);

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onFocus(int, int)
	 */
	@Override
	abstract public boolean onFocus(int x, int y);

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onLink(int, int)
	 */
	@Override
	abstract public boolean onLink(int x, int y);

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onMenu(int, int)
	 */
	@Override
	abstract public boolean onMenu(int x, int y);

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.EventListener#onMount(int, int)
	 */
	@Override
	abstract public boolean onMount(int x, int y);

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.EventListener#onScale(float, float, float)
	 */
	@Override
	abstract public void onScale(float mapScale, float fontScale, float imageScale);
}
