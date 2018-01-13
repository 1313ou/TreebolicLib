package treebolic.control;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
		EXPAND, SHRINK, EXPANSIONRESET, WIDEN, NARROW, SWEEPRESET, EXPANSIONSWEEPRESET, //
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
			final View thisView = getView();
			assert thisView != null;
			thisView.setYShift(Commander.YSSHIFTSTEP, true);
		}
	}

	/**
	 * Set south orientation
	 */
	private void setSouth()
	{
		if (!changeOrientation(Complex.NORTH))
		{
			final View thisView = getView();
			assert thisView != null;
			thisView.setYShift(-Commander.YSSHIFTSTEP, true);
		}
	}

	/**
	 * Set east orientation
	 */
	private void setEast()
	{
		if (!changeOrientation(Complex.EAST))
		{
			final View thisView = getView();
			assert thisView != null;
			thisView.setXShift(-Commander.XSHIFTSTEP, true);
		}
	}

	/**
	 * Set west orientation
	 */
	private void setWest()
	{
		if (!changeOrientation(Complex.WEST))
		{
			final View thisView = getView();
			assert thisView != null;
			thisView.setXShift(+Commander.XSHIFTSTEP, true);
		}
	}

	/**
	 * Set radial orientation
	 */
	private void setRadial()
	{
		changeOrientation(Complex.ZERO);
		final View thisView = getView();
		assert thisView != null;
		thisView.setXShift(0F, false);
		thisView.setYShift(0F, false);
	}

	/**
	 * Change orientation
	 *
	 * @param thisOrientation orientation
	 * @return true if successful
	 */
	synchronized private boolean changeOrientation(@NonNull final Complex thisOrientation)
	{
		final AbstractLayerOut thisLayerOut = getLayerOut();
		assert thisLayerOut != null;
		if (thisOrientation.equals(thisLayerOut.getOrientation()))
		{
			return false;
		}

		final View thisView = getView();
		assert thisView != null;
		final Model thisModel = getModel();
		assert thisModel != null;
		thisView.resetTransform();
		thisView.setXShift(0, false);
		thisView.setYShift(0, false);

		thisLayerOut.setOrientation(thisOrientation);
		final boolean isRadial = thisOrientation == Complex.ZERO;
		thisLayerOut.setDefaultRootSweep(isRadial);
		thisLayerOut.setDefaultChildSweep(isRadial);
		thisLayerOut.layout(thisModel.theTree.getRoot());
		return true;
	}

	/**
	 * Change Expansion by given factor
	 *
	 * @param thisFactor factor
	 */
	private void changeExpansion(final float thisFactor)
	{
		final AbstractLayerOut thisLayerOut = getLayerOut();
		assert thisLayerOut != null;
		if (thisFactor == .0)
		{
			thisLayerOut.setDefaultSettingsExpansion();
		}
		else
		{
			double thisExpansion = thisLayerOut.getExpansion();
			thisExpansion *= thisFactor;
			if (thisExpansion > MAXEXPANSION)
			{
				return;
			}
			thisLayerOut.setExpansion(thisExpansion);
		}
		final View thisView = getView();
		assert thisView != null;
		final Model thisModel = getModel();
		assert thisModel != null;
		thisView.resetTransform();
		thisLayerOut.layout(thisModel.theTree.getRoot());
	}

	/*
	 * Reset expansion
	 */
	/*
	private void resetExpansion()
	{
		getLayerOut().setDefaultExpansion();
		getView().resetTransform();
		getLayerOut().layout(getModel().theTree.getRoot());
	}
	*/

	/**
	 * Change sweep by given factor
	 *
	 * @param thisFactor factor
	 */
	private void changeSweep(final float thisFactor)
	{
		final AbstractLayerOut thisLayerOut = getLayerOut();
		assert thisLayerOut != null;
		if (thisFactor == .0)
		{
			thisLayerOut.setDefaultSettingsSweep();
		}
		else
		{
			double thisSweep = thisLayerOut.getChildSweep();
			thisSweep *= thisFactor;
			if (thisSweep > MAXSWEEP)
			{
				return;
			}
			thisLayerOut.setChildSweep(thisSweep);
		}
		final View thisView = getView();
		assert thisView != null;
		final Model thisModel = getModel();
		assert thisModel != null;
		thisView.resetTransform();
		thisLayerOut.layout(thisModel.theTree.getRoot());
	}

	/*
	 * Reset sweep
	 */
	/*
	private void resetSweep()
	{
		getLayerOut().setDefaultChildSweep();
		getView().resetTransform();
		getLayerOut().layout(getModel().theTree.getRoot());
	}
	*/

	/**
	 * Reset expansion and sweep
	 */
	private void resetExpansionSweep()
	{
		final AbstractLayerOut thisLayerOut = getLayerOut();
		assert thisLayerOut != null;
		final View thisView = getView();
		assert thisView != null;
		final Model thisModel = getModel();
		assert thisModel != null;
		thisLayerOut.setDefaultExpansion();
		thisLayerOut.setDefaultChildSweep();
		thisView.resetTransform();
		thisLayerOut.layout(thisModel.theTree.getRoot());
	}

	/**
	 * Enable/disable tooltips
	 *
	 * @param thisFlag whether to display tooltip (null toggles value)
	 */
	@SuppressWarnings("WeakerAccess")
	public void setHasTooltip(@Nullable final Boolean thisFlag)
	{
		Commander.hasTooltip = thisFlag != null ? thisFlag : !Commander.hasTooltip;
	}

	/**
	 * Enable/disable displaying content in tooltips
	 *
	 * @param thisFlag whether tooltip displays content (null toggles value)
	 */
	@SuppressWarnings({"WeakerAccess"})
	static public void setTooltipDisplaysContent(@Nullable final Boolean thisFlag)
	{
		Commander.tooltipDisplaysContent = thisFlag != null ? thisFlag : !Commander.tooltipDisplaysContent;
	}

	// D O

	/**
	 * Perform repaint
	 */
	private void doRefresh()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
	}

	/**
	 * Perform north orient
	 */
	private void doNorth()
	{
		setNorth();
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
	}

	/**
	 * Perform south orient
	 */
	private void doSouth()
	{
		setSouth();
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
	}

	/**
	 * Perform east orient
	 */
	private void doEast()
	{
		setEast();
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
	}

	/**
	 * Perform west orient
	 */
	private void doWest()
	{
		setWest();
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
	}

	/**
	 * Perform tree radial orient
	 */
	private void doRadial()
	{
		setRadial();
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
	}

	/**
	 * Perform zoom in
	 */
	private void doZoomIn()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.setZoomFactor(Commander.SCALEUPFACTOR, Float.MAX_VALUE, Float.MAX_VALUE);
		thisView.repaint();
	}

	/**
	 * Perform zoom out
	 */
	private void doZoomOut()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.setZoomFactor(Commander.SCALEDOWNFACTOR, Float.MAX_VALUE, Float.MAX_VALUE);
		thisView.repaint();
	}

	/**
	 * Perform zoom one
	 */
	private void doZoomOne()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.setZoomFactor(1F, 0, 0);
		thisView.repaint();
	}

	/**
	 * Perform scale up
	 */
	private void doScaleUp()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.setScaleFactors(0, Commander.SCALEUPFACTOR, Commander.SCALEUPFACTOR);
		thisView.repaint();
	}

	/**
	 * Perform scale down
	 */
	private void doScaleDown()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.setScaleFactors(0, Commander.SCALEDOWNFACTOR, Commander.SCALEDOWNFACTOR);
		thisView.repaint();
	}

	/**
	 * Perform scale reset
	 */
	private void doScaleOne()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.setScaleFactors(1F, 1F, 1F);
		thisView.repaint();
	}

	/**
	 * Perform reset
	 */
	private void doHome()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.reset();
	}

	/**
	 * Perform change expansion by given factor
	 *
	 * @param thisFactor factor
	 */
	private void doChangeExpansion(final float thisFactor)
	{
		changeExpansion(thisFactor);
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
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
	 * @param thisFactor factor
	 */
	private void doChangeSweep(final float thisFactor)
	{
		changeSweep(thisFactor);
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
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
		final View thisView = getView();
		assert thisView != null;
		thisView.repaint();
	}

	/**
	 * Set edges rendering as arcs
	 */
	private void doArcEdges()
	{
		final View thisView = getView();
		assert thisView != null;
		thisView.setArcEdges(null);
		thisView.repaint();
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
		final View thisView = getView();
		assert thisView != null;
		thisView.setFocusOnHover(null);
	}

	// A P P L Y . S E T T I N G S

	/**
	 * Set view behaviour
	 *
	 * @param theseSettings settings
	 */
	public void apply(@NonNull final Settings theseSettings)
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
	public void execute(@NonNull final Command thisCommand, final Object... theseParameters)
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
