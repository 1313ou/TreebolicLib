package treebolic.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import treebolic.core.transform.HyperTransform;
import treebolic.glue.ActionListener;

@SuppressWarnings("WeakerAccess")
public class Animation extends ActionListener
{
	/**
	 * Animation start delay
	 */
	static public final int ANIMATION_START_DELAY = 1000;

	/**
	 * Animation iterator
	 */
	private final List<HyperTransform> transforms;

	/**
	 * Current index
	 */
	private int index;

	/**
	 * View
	 */
	@SuppressWarnings("WeakerAccess")
	public final View view;

	/**
	 * Constructor
	 *
	 * @param animation animation (with update callback)
	 * @param view      the view being animated
	 */
	public Animation(@NonNull final AnimationTransforms animation, final View view)
	{
		this.transforms = animation.transforms;
		this.index = -1;
		this.view = view;
	}

	/**
	 * Get steps
	 *
	 * @return number of steps
	 */
	public int getSteps()
	{
		return this.transforms.size();
	}

	@Override
	public boolean onAction(@Nullable final Object... params)
	{
		final Integer suggestedIndex = params == null || params.length == 0 ? null : (Integer) params[0];
		int index;
		if (suggestedIndex == null)
		{
			index = this.index + 1;
		}
		else
		{
			index = suggestedIndex;
		}
		if (index != this.index)
		{
			if (index >= 0 && index < this.transforms.size())
			{
				this.index = index;
				final HyperTransform transform = this.transforms.get(this.index);
				this.view.applyTransform(transform);
				this.view.repaint();
				return true;
			}
		}
		return false;
	}
}
