package treebolic.component;

import treebolic.control.Controller;
import treebolic.glue.ActionListener;
import treebolic.glue.component.Component;

/**
 * Tool bar
 *
 * @author Bernard Bou
 */
public class Toolbar extends treebolic.glue.component.Toolbar implements Component
{
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 2278779010201821356L;

	// D A T A

	/**
	 * Controller to send action requests to
	 */
	private final Controller theController;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param thisHandle Handle required for component creation
	 */
	public Toolbar(final Controller thisController, final boolean hasTooltip, final boolean tooltipDisplaysContent, final boolean arcEdges, final boolean focusOnHover, final Object thisHandle)
	{
		super(thisHandle);
		this.theController = thisController;

		for (final Button button : toolbar())
		{
			ActionListener listener = null;
			final String action = button.name();
			try
			{
				final Controller.Command command = Controller.Command.valueOf(action);
				listener = makeListener(command);
			}
			catch (IllegalArgumentException ignored)
			{
				//
			}
			addButton(button, listener);
		}
	}

	private ActionListener makeListener(final Controller.Command thisCommand)
	{
		return new ActionListener()
		{
			@Override
			public boolean onAction(final Object... theseParams)
			{
				Toolbar.this.theController.execute(thisCommand);
				return true;
			}
		};
	}
}
