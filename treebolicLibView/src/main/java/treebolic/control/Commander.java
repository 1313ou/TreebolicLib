package treebolic.control;

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
	{
		REFRESH, //
		HOME, NORTH, SOUTH, EAST, WEST, RADIAL, //
		ZOOMIN, ZOOMOUT, ZOOMONE, SCALEUP, SCALEDOWN, SCALEONE, //
		EXPAND, SHRINK, EXPANSIONRESET, WIDEN, NARROW, SWEEPRESET, //
		ARCEDGE, TOOLTIP, TOOLTIPCONTENT, FOCUSHOVER
	}

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
	static protected boolean hasTooltip = true;

	/**
	 * Whether tooltips display contents
	 */
	static protected boolean tooltipDisplaysContent = true;

	/**
	 * Whether tooltips are html
	 */
	@SuppressWarnings("CanBeFinal")
	static public boolean TOOLTIPHTML = true;

	/**
	 * Tooltip break
	 */
	@SuppressWarnings("CanBeFinal")
	static public int TOOLTIPLINESPAN = 50;

	// A C C E S S

	/**
	 * Get model
	 *
	 * @return model
	 */
	abstract protected Model getModel();

	/**
	 * Get view
	 *
	 * @return view
	 */
	abstract protected View getView();

	/**
	 * Get layout agent
	 *
	 * @return layout agent
	 */
	abstract protected AbstractLayerOut getLayerOut();

	// H E L P E R S

	/**
	 * Set north orientation
	 */
	private void setNorth()
	{
		if (!changeOrientation(Complex.SOUTH))
		{
			getView().setShift(0F, Commander.YSSHIFTSTEP, false, true);
		}
	}

	/**
	 * Set south orientation
	 */
	private void setSouth()
	{
		if (!changeOrientation(Complex.NORTH))
		{
			getView().setShift(0F, -Commander.YSSHIFTSTEP, false, true);
		}
	}

	/**
	 * Set east orientation
	 */
	private void setEast()
	{
		if (!changeOrientation(Complex.EAST))
		{
			getView().setShift(-Commander.XSHIFTSTEP, 0F, true, false);
		}
	}

	/**
	 * Set west orientation
	 */
	private void setWest()
	{
		if (!changeOrientation(Complex.WEST))
		{
			getView().setShift(+Commander.XSHIFTSTEP, 0F, true, false);
		}
	}

	/**
	 * Set radial orientation
	 */
	private void setRadial()
	{
		changeOrientation(Complex.ZERO);
		getView().setShift(0F, 0F, false, false);
	}

	/**
	 * Change orientation
	 *
	 * @param thisOrientation orientation
	 * @return true if successful
	 */
	synchronized private boolean changeOrientation(final Complex thisOrientation)
	{
		if (thisOrientation.equals(getLayerOut().getOrientation()))
		{
			return false;
		}
		getView().resetTransform();
		getLayerOut().setOrientation(thisOrientation);
		final boolean isRadial = thisOrientation == Complex.ZERO;
		getLayerOut().setDefaultRootSweep(isRadial);
		getLayerOut().setDefaultChildSweep(isRadial);
		getLayerOut().layout(getModel().theTree.getRoot());
		return true;
	}

	/**
	 * Change Expansion by given factor
	 *
	 * @param thisFactor factor
	 */
	private void changeExpansion(final float thisFactor)
	{
		if (thisFactor == .0)
		{
			getLayerOut().setDefaultExpansion();
		}
		else
		{
			double thisExpansion = getLayerOut().getExpansion();
			thisExpansion *= thisFactor;
			if (thisExpansion > MAXEXPANSION)
			{
				return;
			}
			getLayerOut().setExpansion(thisExpansion);
		}
		getView().resetTransform();
		getLayerOut().layout(getModel().theTree.getRoot());
	}

	/**
	 * Change sweep by given factor
	 *
	 * @param thisFactor factor
	 */
	private void changeSweep(final float thisFactor)
	{
		if (thisFactor == .0)
		{
			getLayerOut().setDefaultChildSweep();
		}
		else
		{
			double thisSweep = getLayerOut().getChildSweep();
			thisSweep *= thisFactor;
			if (thisSweep > MAXSWEEP)
			{
				return;
			}
			getLayerOut().setChildSweep(thisSweep);
		}
		getView().resetTransform();
		getLayerOut().layout(getModel().theTree.getRoot());
	}

	/**
	 * Enable/disable tooltips
	 *
	 * @param thisFlag whether to display tooltip (null toggles value)
	 */
	@SuppressWarnings({"boxing", "static-method"})
	public void setHasTooltip(final Boolean thisFlag)
	{
		Commander.hasTooltip = thisFlag != null ? thisFlag : !Commander.hasTooltip;
	}

	/**
	 * Enable/disable displaying content in tooltips
	 *
	 * @param thisFlag whether tooltip displays content (null toggles value)
	 */
	@SuppressWarnings("boxing")
	static public void setTooltipDisplaysContent(final Boolean thisFlag)
	{
		Commander.tooltipDisplaysContent = thisFlag != null ? thisFlag : !Commander.tooltipDisplaysContent;
	}

	// D O

	/**
	 * Perform repaint
	 */
	private void doRefresh()
	{
		getView().repaint();
	}

	/**
	 * Perform north orient
	 */
	private void doNorth()
	{
		setNorth();
		getView().repaint();
	}

	/**
	 * Perform south orient
	 */
	private void doSouth()
	{
		setSouth();
		getView().repaint();
	}

	/**
	 * Perform east orient
	 */
	private void doEast()
	{
		setEast();
		getView().repaint();
	}

	/**
	 * Perform west orient
	 */
	private void doWest()
	{
		setWest();
		getView().repaint();
	}

	/**
	 * Perform tree radial orient
	 */
	private void doRadial()
	{
		setRadial();
		getView().repaint();
	}

	/**
	 * Perform zoom in
	 */
	private void doZoomIn()
	{
		getView().setZoomFactor(Commander.SCALEUPFACTOR, Float.MAX_VALUE, Float.MAX_VALUE);
		getView().repaint();
	}

	/**
	 * Perform zoom out
	 */
	private void doZoomOut()
	{
		getView().setZoomFactor(Commander.SCALEDOWNFACTOR, Float.MAX_VALUE, Float.MAX_VALUE);
		getView().repaint();
	}

	/**
	 * Perform zoom one
	 */
	private void doZoomOne()
	{
		getView().setZoomFactor(1F, 0, 0);
		getView().repaint();
	}

	/**
	 * Perform scale up
	 */
	private void doScaleUp()
	{
		getView().setScaleFactors(0, Commander.SCALEUPFACTOR, Commander.SCALEUPFACTOR);
		getView().repaint();
	}

	/**
	 * Perform scale down
	 */
	private void doScaleDown()
	{
		getView().setScaleFactors(0, Commander.SCALEDOWNFACTOR, Commander.SCALEDOWNFACTOR);
		getView().repaint();
	}

	/**
	 * Perform scale reset
	 */
	private void doScaleOne()
	{
		getView().setScaleFactors(1F, 1F, 1F);
		getView().repaint();
	}

	/**
	 * Perform reset
	 */
	private void doHome()
	{
		getView().reset();
	}

	/**
	 * Perform change expansion by given factor
	 *
	 * @param thisFactor factor
	 */
	private void doChangeExpansion(final float thisFactor)
	{
		changeExpansion(thisFactor);
		getView().repaint();
	}

	/**
	 * Perform change sweep by given factor
	 *
	 * @param thisFactor factor
	 */
	private void doChangeSweep(final float thisFactor)
	{
		changeSweep(thisFactor);
		getView().repaint();
	}

	/**
	 * Set edges rendering as arcs
	 */
	private void doArcEdges()
	{
		getView().setArcEdges(null);
		getView().repaint();
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
		getView().setFocusOnHover(null);
	}

	// A P P L Y . S E T T I N G S

	/**
	 * Set view behaviour
	 *
	 * @param theseSettings settings
	 */
	public void apply(final Settings theseSettings)
	{
		// controller settings
		if (theseSettings.theHasToolTipFlag != null)
		{
			setHasTooltip(theseSettings.theHasToolTipFlag);
		}
		if (theseSettings.theToolTipDisplaysContentFlag != null)
		{
			setTooltipDisplaysContent(theseSettings.theToolTipDisplaysContentFlag);
		}
	}

	// C O M M A N D P R O C E S S O R

	/**
	 * Command dispatcher
	 *
	 * @param thisCommand     command
	 * @param theseParameters theseParameters
	 */
	public void execute(final Command thisCommand, final Object... theseParameters)
	{
		switch (thisCommand)
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
			case EXPANSIONRESET:
				doChangeExpansion(0F);
				break;
			case SHRINK:
				doChangeExpansion(1F / Commander.EXPANSIONFACTOR);
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
