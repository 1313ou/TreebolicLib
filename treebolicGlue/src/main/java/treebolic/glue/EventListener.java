package treebolic.glue;

/**
 * Event listener
 *
 * @author Bernard Bou
 */
public abstract class EventListener implements treebolic.glue.iface.EventListener
{
	@Override
	abstract public boolean onDown(int x, int y, boolean rotate);

	@Override
	abstract public boolean onUp(int x, int y);

	@Override
	abstract public boolean onDragged(int x, int y);

	@Override
	abstract public boolean onHover(int x, int y);

	@Override
	abstract public boolean onSelect(int x, int y);

	@Override
	abstract public boolean onFocus(int x, int y);

	@Override
	abstract public boolean onLink(int x, int y);

	@Override
	abstract public boolean onMenu(int x, int y);

	@Override
	abstract public boolean onMount(int x, int y);


	@Override
	abstract public void onScale(float mapScale, float fontScale, float imageScale);
}
