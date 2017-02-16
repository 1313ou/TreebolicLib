/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
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
	/**
	 * Indexes to tooltips
	 */
	static public final int RESET = 0;
	static public final int RADIAL = 1;
	static public final int NORTH = 2;
	static public final int SOUTH = 3;
	static public final int EAST = 4;
	static public final int WEST = 5;
	static public final int EXPAND = 6;
	static public final int SHRINK = 7;
	static public final int WIDEN = 8;
	static public final int NARROW = 9;
	static public final int ZOOMIN = 10;
	static public final int ZOOMOUT = 11;
	static public final int ZOOMRESET = 12;
	static public final int SCALEUP = 13;
	static public final int SCALEDOWN = 14;
	static public final int SCALERESET = 15;
	static public final int ARC = 16;
	static public final int TOOLTIP = 17;
	static public final int TOOLTIPCONTENT = 18;
	static public final int FOCUSONHOVER = 19;

	// static String[] tooltips defined in glue for localization

	// D A T A

	/**
	 * Controller to send action requests to
	 */
	private final Controller theController;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param thisHandle
	 *            Handle required for component creation
	 */
	public Toolbar(final Controller thisController, final boolean hasTooltip, final boolean tooltipDisplaysContent, final boolean arcEdges,
			final boolean focusOnHover, final Object thisHandle)
	{
		super(thisHandle);
		this.theController = thisController;

		addButton(ImageIndices.HOME.ordinal(), tooltips[RESET], makeListener(Controller.Command.HOME));
		addSeparator();
		addButton(ImageIndices.RADIAL.ordinal(), tooltips[RADIAL], makeListener(Controller.Command.RADIAL));
		addButton(ImageIndices.NORTH.ordinal(), tooltips[NORTH], makeListener(Controller.Command.NORTH));
		addButton(ImageIndices.SOUTH.ordinal(), tooltips[SOUTH], makeListener(Controller.Command.SOUTH));
		addButton(ImageIndices.EAST.ordinal(), tooltips[EAST], makeListener(Controller.Command.EAST));
		addButton(ImageIndices.WEST.ordinal(), tooltips[WEST], makeListener(Controller.Command.WEST));
		addSeparator();
		addButton(ImageIndices.EXPAND.ordinal(), tooltips[EXPAND], makeListener(Controller.Command.EXPAND));
		addButton(ImageIndices.SHRINK.ordinal(), tooltips[SHRINK], makeListener(Controller.Command.SHRINK));
		addButton(ImageIndices.WIDEN.ordinal(), tooltips[WIDEN], makeListener(Controller.Command.WIDEN));
		addButton(ImageIndices.NARROW.ordinal(), tooltips[NARROW], makeListener(Controller.Command.NARROW));
		addSeparator();
		addButton(ImageIndices.ZOOMIN.ordinal(), tooltips[ZOOMIN], makeListener(Controller.Command.ZOOMIN));
		addButton(ImageIndices.ZOOMOUT.ordinal(), tooltips[ZOOMOUT], makeListener(Controller.Command.ZOOMOUT));
		addButton(ImageIndices.ZOOMONE.ordinal(), tooltips[ZOOMRESET], makeListener(Controller.Command.ZOOMONE));
		addSeparator();
		addButton(ImageIndices.SCALEUP.ordinal(), tooltips[SCALEUP], makeListener(Controller.Command.SCALEUP));
		addButton(ImageIndices.SCALEDOWN.ordinal(), tooltips[SCALEDOWN], makeListener(Controller.Command.SCALEDOWN));
		addButton(ImageIndices.SCALEONE.ordinal(), tooltips[SCALERESET], makeListener(Controller.Command.SCALEONE));
		addSeparator();
		addToggle(ImageIndices.ARC.ordinal(), ImageIndices.NO_ARC.ordinal(), tooltips[ARC], arcEdges, makeListener(Controller.Command.ARCEDGE));
		addSeparator();
		addToggle(ImageIndices.NODETOOLTIP.ordinal(), ImageIndices.NO_NODETOOLTIP.ordinal(), tooltips[TOOLTIP], hasTooltip,
				makeListener(Controller.Command.TOOLTIP));
		addToggle(ImageIndices.NODETOOLTIPCONTENT.ordinal(), ImageIndices.NO_NODETOOLTIPCONTENT.ordinal(), tooltips[TOOLTIPCONTENT], tooltipDisplaysContent,
				makeListener(Controller.Command.TOOLTIPCONTENT));
		addToggle(ImageIndices.HOVERFOCUS.ordinal(), ImageIndices.NO_HOVERFOCUS.ordinal(), tooltips[FOCUSONHOVER], focusOnHover,
				makeListener(Controller.Command.FOCUSHOVER));
	}

	private ActionListener makeListener(final Controller.Command thisCommand)
	{
		return new ActionListener()
		{
			@SuppressWarnings("synthetic-access")
			@Override
			public boolean onAction(final Object... theseParams)
			{
				Toolbar.this.theController.execute(thisCommand);
				return true;
			}
		};
	}
}
