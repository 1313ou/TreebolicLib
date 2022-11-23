/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.component;

import treebolic.annotations.NonNull;
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
	 * @param controller             controller
	 * @param hasTooltip             whether toolbar displays tooltips
	 * @param tooltipDisplaysContent whether tooltip displays content
	 * @param arcEdges               whether toolbar has display as arcs/lines for edges
	 * @param focusOnHover           whether toolbar has enable/disable focus-on-hover option
	 * @param handle                 handle Handle required for component creation
	 */
	public Toolbar(final Controller controller, @SuppressWarnings("unused") final boolean hasTooltip, @SuppressWarnings("unused") final boolean tooltipDisplaysContent, @SuppressWarnings("unused") final boolean arcEdges, @SuppressWarnings("unused") final boolean focusOnHover, final Object handle)
	{
		super(handle);
		this.controller = controller;

		for (@NonNull final Button button : getButtons())
		{
			ActionListener listener;
			@NonNull final String action = button.name();
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
