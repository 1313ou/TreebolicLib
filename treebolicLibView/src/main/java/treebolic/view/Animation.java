/**
 *
 */
package treebolic.view;

import java.util.List;

import treebolic.core.transform.HyperTransform;
import treebolic.glue.ActionListener;

public class Animation extends ActionListener
{
	/**
	 * Animation start delay
	 */
	static public final int ANIMATION_START_DELAY = 1000;

	/**
	 * Animation iterator
	 */
	private final List<HyperTransform> theTransforms;

	/**
	 * Current index
	 */
	private int theIndex;

	/**
	 * View
	 */
	public final View theView;

	/**
	 * Constructor
	 *
	 * @param thisAnimation
	 *            animation (with update callback)
	 * @param thisView
	 *            the view being animated
	 */
	public Animation(final AnimationTransforms thisAnimation, final View thisView)
	{
		this.theTransforms = thisAnimation.theTransforms;
		this.theIndex = -1;
		this.theView = thisView;
	}

	/**
	 * Get steps
	 * 
	 * @return number of steps
	 */
	public int getSteps()
	{
		return this.theTransforms.size();
	}

	@SuppressWarnings("boxing")
	@Override
	public boolean onAction(final Object... theseParams)
	{
		final Integer thisSuggestedIndex = theseParams == null || theseParams.length == 0 ? null : (Integer) theseParams[0];
		int thisIndex;
		if (thisSuggestedIndex == null)
			thisIndex = this.theIndex + 1;
		else
			thisIndex = thisSuggestedIndex;
		if (thisIndex != this.theIndex)
		{
			if (thisIndex >= 0 && thisIndex < this.theTransforms.size())
			{
				this.theIndex = thisIndex;
				final HyperTransform thisTransform = this.theTransforms.get(this.theIndex);
				this.theView.applyTransform(thisTransform);
				this.theView.repaint();
				return true;
			}
		}
		return false;
	}
}