/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.component;

import androidx.annotation.NonNull;
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
	// private static final long serialVersionUID = 2278779010201821356L;

	// D A T A

	/**
	 * Controller to send action requests to
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private final Controller controller;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param handle Handle required for component creation
	 */
	public Toolbar(final Controller controller, final boolean hasTooltip, final boolean tooltipDisplaysContent, final boolean arcEdges, final boolean focusOnHover, final Object handle)
	{
		super(handle);
		this.controller = controller;

		for (final Button button : toolbar())
		{
			ActionListener listener;
			final String action = button.name();
			try
			{
				final Controller.Command command = Controller.Command.valueOf(action);
				listener = makeListener(command);
				addButton(button, listener);
			}
			catch (IllegalArgumentException ignored)
			{
				//
			}
		}
	}

	@NonNull
	private ActionListener makeListener(@NonNull final Controller.Command command)
	{
		return new ActionListener()
		{
			@Override
			public boolean onAction(final Object... params)
			{
				Toolbar.this.controller.execute(command);
				return true;
			}
		};
	}
}
