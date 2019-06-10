package treebolic.control;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import treebolic.core.AbstractLayerOut;
import treebolic.core.location.Complex;
import treebolic.model.Model;
import treebolic.model.Settings;
import treebolic.view.View;

/**
 * Commander executes commands
 *
 * @author Bernard Bou
 */
public abstract class Commander
{
	/**
	 * Commands
	 */
	public enum Command
	{REFRESH, //
		HOME, NORTH, SOUTH, EAST, WEST, RADIAL, //
		ZOOMIN, ZOOMOUT, ZOOMONE, SCALEUP, SCALEDOWN, SCALEONE, //
		EXPAND, SHRINK, EXPANSIONRESET, WIDEN, NARROW, SWEEPRESET, EXPANSIONSWEEPRESET, //
		ARCEDGE, TOOLTIP, TOOLTIPCONTENT, FOCUSHOVER}

	/**
	 * East/West shift step
	 */
	static private final float XSHIFTSTEP = 0.1F;

	/**
	 * North/south shift step
	 */
	static private final float YSSHIFTSTEP = 0.1F;

	/**
	 * Sweep step factor
	 */
	static private final float SWEEPFACTOR = 1.1F;

	/**
	 * Max sweep
	 */
	static private final double MAXSWEEP = Math.PI;

	/**
	 * Expansion step factor
	 */
	static private final float EXPANSIONFACTOR = 1.1F;

	/**
	 * Max expansion
	 */
	static private final double MAXEXPANSION = .4F;

	/**
	 * Scale up step factor
	 */
	static private final float SCALEUPFACTOR = -1.2F;

	/**
	 * Scale down step factor
	 */
	static private final float SCALEDOWNFACTOR = 1F / Commander.SCALEUPFACTOR;

	/**
	 * Whether it has tooltip
	 */
	@SuppressWarnings("WeakerAccess")
	static protected boolean hasTooltip = true;

	/**
	 * Whether tooltips display contents
	 */
	@SuppressWarnings("WeakerAccess")
	static protected boolean tooltipDisplaysContent = true;

	/**
	 * Whether tooltips are html
	 */
	@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
	static public boolean TOOLTIPHTML = true;

	/**
	 * Tooltip break
	 */
	@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
	static public int TOOLTIPLINESPAN = 50;

	// A C C E S S

	/**
	 * Get model
	 *
	 * @return model
	 */
	@Nullable
	abstract protected Model getModel();

	/**
	 * Get view
	 *
	 * @return view
	 */
	@Nullable
	abstract protected View getView();

	/**
	 * Get layout agent
	 *
	 * @return layout agent
	 */
	@Nullable
	abstract protected AbstractLayerOut getLayerOut();

	// H E L P E R S

	/**
	 * Set north orientation
	 */
	private void setNorth()
	{
		if (!changeOrientation(Complex.SOUTH))
		{
			final View view = getView();
			assert view != null;
			view.setYShift(Commander.YSSHIFTSTEP, true);
		}
	}

	/**
	 * Set south orientation
	 */
	private void setSouth()
	{
		if (!changeOrientation(Complex.NORTH))
		{
			final View view = getView();
			assert view != null;
			view.setYShift(-Commander.YSSHIFTSTEP, true);
		}
	}

	/**
	 * Set east orientation
	 */
	private void setEast()
	{
		if (!changeOrientation(Complex.EAST))
		{
			final View view = getView();
			assert view != null;
			view.setXShift(-Commander.XSHIFTSTEP, true);
		}
	}

	/**
	 * Set west orientation
	 */
	private void setWest()
	{
		if (!changeOrientation(Complex.WEST))
		{
			final View view = getView();
			assert view != null;
			view.setXShift(+Commander.XSHIFTSTEP, true);
		}
	}

	/**
	 * Set radial orientation
	 */
	private void setRadial()
	{
		changeOrientation(Complex.ZERO);
		final View view = getView();
		assert view != null;
		view.setXShift(0F, false);
		view.setYShift(0F, false);
	}

	/**
	 * Change orientation
	 *
	 * @param orientation orientation
	 * @return true if successful
	 */
	synchronized private boolean changeOrientation(@NonNull final Complex orientation)
	{
		final AbstractLayerOut layerOut = getLayerOut();
		assert layerOut != null;
		if (orientation.equals(layerOut.getOrientation()))
		{
			return false;
		}

		final View view = getView();
		assert view != null;
		final Model model = getModel();
		assert model != null;
		view.resetTransform();
		view.setXShift(0, false);
		view.setYShift(0, false);

		layerOut.setOrientation(orientation);
		final boolean isRadial = orientation == Complex.ZERO;
		layerOut.setDefaultRootSweep(isRadial);
		layerOut.setDefaultChildSweep(isRadial);
		layerOut.layout(model.tree.getRoot());
		return true;
	}

	/**
	 * Change Expansion by given factor
	 *
	 * @param factor factor
	 */
	private void changeExpansion(final float factor)
	{
		final AbstractLayerOut layerOut = getLayerOut();
		assert layerOut != null;
		if (factor == .0)
		{
			layerOut.setDefaultSettingsExpansion();
		}
		else
		{
			double expansion = layerOut.getExpansion();
			expansion *= factor;
			if (expansion > MAXEXPANSION)
			{
				return;
			}
			layerOut.setExpansion(expansion);
		}
		final View view = getView();
		assert view != null;
		final Model model = getModel();
		assert model != null;
		view.resetTransform();
		layerOut.layout(model.tree.getRoot());
	}

	/*
	 * Reset expansion
	 */
	/*
	private void resetExpansion()
	{
		getLayerOut().setDefaultExpansion();
		getView().resetTransform();
		getLayerOut().layout(getModel().tree.getRoot());
	}
	*/

	/**
	 * Change sweep by given factor
	 *
	 * @param factor factor
	 */
	private void changeSweep(final float factor)
	{
		final AbstractLayerOut layerOut = getLayerOut();
		assert layerOut != null;
		if (factor == .0)
		{
			layerOut.setDefaultSettingsSweep();
		}
		else
		{
			double sweep = layerOut.getChildSweep();
			sweep *= factor;
			if (sweep > MAXSWEEP)
			{
				return;
			}
			layerOut.setChildSweep(sweep);
		}
		final View view = getView();
		assert view != null;
		final Model model = getModel();
		assert model != null;
		view.resetTransform();
		layerOut.layout(model.tree.getRoot());
	}

	/*
	 * Reset sweep
	 */
	/*
	private void resetSweep()
	{
		getLayerOut().setDefaultChildSweep();
		getView().resetTransform();
		getLayerOut().layout(getModel().tree.getRoot());
	}
	*/

	/**
	 * Reset expansion and sweep
	 */
	private void resetExpansionSweep()
	{
		final AbstractLayerOut layerOut = getLayerOut();
		assert layerOut != null;
		final View view = getView();
		assert view != null;
		final Model model = getModel();
		assert model != null;
		layerOut.setDefaultExpansion();
		layerOut.setDefaultChildSweep();
		view.resetTransform();
		layerOut.layout(model.tree.getRoot());
	}

	/**
	 * Enable/disable tooltips
	 *
	 * @param flag whether to display tooltip (null toggles value)
	 */
	@SuppressWarnings("WeakerAccess")
	public void setHasTooltip(@Nullable final Boolean flag)
	{
		Commander.hasTooltip = flag != null ? flag : !Commander.hasTooltip;
	}

	/**
	 * Enable/disable displaying content in tooltips
	 *
	 * @param flag whether tooltip displays content (null toggles value)
	 */
	@SuppressWarnings({"WeakerAccess"})
	static public void setTooltipDisplaysContent(@Nullable final Boolean flag)
	{
		Commander.tooltipDisplaysContent = flag != null ? flag : !Commander.tooltipDisplaysContent;
	}

	// D O

	/**
	 * Perform repaint
	 */
	private void doRefresh()
	{
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/**
	 * Perform north orient
	 */
	private void doNorth()
	{
		setNorth();
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/**
	 * Perform south orient
	 */
	private void doSouth()
	{
		setSouth();
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/**
	 * Perform east orient
	 */
	private void doEast()
	{
		setEast();
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/**
	 * Perform west orient
	 */
	private void doWest()
	{
		setWest();
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/**
	 * Perform tree radial orient
	 */
	private void doRadial()
	{
		setRadial();
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/**
	 * Perform zoom in
	 */
	private void doZoomIn()
	{
		final View view = getView();
		assert view != null;
		view.setZoomFactor(Commander.SCALEUPFACTOR, Float.MAX_VALUE, Float.MAX_VALUE);
		view.repaint();
	}

	/**
	 * Perform zoom out
	 */
	private void doZoomOut()
	{
		final View view = getView();
		assert view != null;
		view.setZoomFactor(Commander.SCALEDOWNFACTOR, Float.MAX_VALUE, Float.MAX_VALUE);
		view.repaint();
	}

	/**
	 * Perform zoom one
	 */
	private void doZoomOne()
	{
		final View view = getView();
		assert view != null;
		view.setZoomFactor(1F, 0, 0);
		view.repaint();
	}

	/**
	 * Perform scale up
	 */
	private void doScaleUp()
	{
		final View view = getView();
		assert view != null;
		view.setScaleFactors(0, Commander.SCALEUPFACTOR, Commander.SCALEUPFACTOR);
		view.repaint();
	}

	/**
	 * Perform scale down
	 */
	private void doScaleDown()
	{
		final View view = getView();
		assert view != null;
		view.setScaleFactors(0, Commander.SCALEDOWNFACTOR, Commander.SCALEDOWNFACTOR);
		view.repaint();
	}

	/**
	 * Perform scale reset
	 */
	private void doScaleOne()
	{
		final View view = getView();
		assert view != null;
		view.setScaleFactors(1F, 1F, 1F);
		view.repaint();
	}

	/**
	 * Perform reset
	 */
	private void doHome()
	{
		final View view = getView();
		assert view != null;
		view.reset();
	}

	/**
	 * Perform change expansion by given factor
	 *
	 * @param factor factor
	 */
	private void doChangeExpansion(final float factor)
	{
		changeExpansion(factor);
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/*
	 * Perform reset expansion
	 */
	/*
	private void doResetExpansion()
	{
		resetExpansion();
		getView().repaint();
	}
	*/

	/**
	 * Perform change sweep by given factor
	 *
	 * @param factor factor
	 */
	private void doChangeSweep(final float factor)
	{
		changeSweep(factor);
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/*
	 * Perform reset sweep
	 */
	/*
	private void doResetSweep()
	{
		resetSweep();
		getView().repaint();
	}
	*/

	/**
	 * Perform reset expansion and sweep
	 */
	private void doResetExpansionSweep()
	{
		resetExpansionSweep();
		final View view = getView();
		assert view != null;
		view.repaint();
	}

	/**
	 * Set edges rendering as arcs
	 */
	private void doArcEdges()
	{
		final View view = getView();
		assert view != null;
		view.setArcEdges(null);
		view.repaint();
	}

	/**
	 * Set tooltips
	 */
	private void doTooltip()
	{
		setHasTooltip(null);
	}

	/**
	 * Set tooltip displays content flag
	 */
	static private void doTooltipContent()
	{
		setTooltipDisplaysContent(null);
	}

	/**
	 * Set focus on hover flag
	 */
	private void doFocusHover()
	{
		final View view = getView();
		assert view != null;
		view.setFocusOnHover(null);
	}

	// A P P L Y . S E T T I N G S

	/**
	 * Set view behaviour
	 *
	 * @param settings settings
	 */
	public void apply(@NonNull final Settings settings)
	{
		// controller settings
		if (settings.hasToolTipFlag != null)
		{
			setHasTooltip(settings.hasToolTipFlag);
		}
		if (settings.toolTipDisplaysContentFlag != null)
		{
			setTooltipDisplaysContent(settings.toolTipDisplaysContentFlag);
		}
	}

	// C O M M A N D P R O C E S S O R

	/**
	 * Command dispatcher
	 *
	 * @param command    command
	 * @param parameters parameters
	 */
	public void execute(@NonNull final Command command, final Object... parameters)
	{
		switch (command)
		{
			case REFRESH:
				doRefresh();
				break;
			case HOME:
				doHome();
				break;
			case NORTH:
				doNorth();
				break;
			case SOUTH:
				doSouth();
				break;
			case EAST:
				doEast();
				break;
			case WEST:
				doWest();
				break;
			case RADIAL:
				doRadial();
				break;
			case ZOOMIN:
				doZoomIn();
				break;
			case ZOOMOUT:
				doZoomOut();
				break;
			case ZOOMONE:
				doZoomOne();
				break;
			case SCALEUP:
				doScaleUp();
				break;
			case SCALEDOWN:
				doScaleDown();
				break;
			case SCALEONE:
				doScaleOne();
				break;
			case EXPAND:
				doChangeExpansion(Commander.EXPANSIONFACTOR);
				break;
			case SHRINK:
				doChangeExpansion(1F / Commander.EXPANSIONFACTOR);
				break;
			case EXPANSIONRESET:
				doChangeExpansion(0F);
				break;
			case WIDEN:
				doChangeSweep(Commander.SWEEPFACTOR);
				break;
			case NARROW:
				doChangeSweep(1F / Commander.SWEEPFACTOR);
				break;
			case SWEEPRESET:
				doChangeSweep(0F);
				break;
			case EXPANSIONSWEEPRESET:
				doResetExpansionSweep();
				break;
			case ARCEDGE:
				doArcEdges();
				break;
			case TOOLTIP:
				doTooltip();
				break;
			case TOOLTIPCONTENT:
				doTooltipContent();
				break;
			case FOCUSHOVER:
				doFocusHover();
				break;
			default:
				break;
		}
	}
}
