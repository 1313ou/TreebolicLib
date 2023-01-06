/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

/**
 * Animator
 *
 * @author Bernard Bou
 */
public class Animator implements treebolic.glue.iface.Animator<ActionListener>, AnimatorUpdateListener, android.animation.Animator.AnimatorListener
{
	// static private final String TAG = "Animator";

	/**
	 * Animation time slice
	 */
	static private final int ANIMATIONTIMESLICE = 250;

	/**
	 * Animation listener
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private ActionListener listener;

	/**
	 * Android animator
	 */
	private ValueAnimator animator;

	/**
	 * Android last step
	 */
	private int lastStep;

	/**
	 * Constructor
	 */
	public Animator()
	{
		ValueAnimator.setFrameDelay(Animator.ANIMATIONTIMESLICE);
		// Log.d(Animator.TAG, "animate frame delay set=" + ANIMATIONTIMESLICE + " get=" + ValueAnimator.getFrameDelay());
	}

	@SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
	@Override
	public boolean run(final ActionListener listener, final int steps, final int startDelay)
	{
		// Log.d(Animator.TAG, "animate steps " + steps);
		this.lastStep = steps - 1;
		this.listener = listener;
		this.animator = ValueAnimator.ofInt(0, this.lastStep);
		this.animator.setRepeatCount(0);
		this.animator.setDuration(1000);
		this.animator.setStartDelay(startDelay);
		this.animator.setInterpolator(new LinearInterpolator());
		this.animator.addUpdateListener(this);
		this.animator.addListener(this);
		this.animator.start();
		return true;
	}

	@Override
	public boolean isRunning()
	{
		return this.animator.isRunning();
	}

	@SuppressWarnings("boxing")
	@Override
	public void onAnimationUpdate(@NonNull final ValueAnimator animator)
	{
		int step = (int) animator.getAnimatedValue();
		this.listener.onAction(step);
		// Log.d(Animator.TAG, "animate update value=" + animator.getAnimatedValue());
	}

	@SuppressWarnings("EmptyMethod")
	@Override
	public void onAnimationStart(@NonNull android.animation.Animator animator)
	{
		// Log.d(Animator.TAG, "animate start " + Animator.frameCount + " value=" + ((ValueAnimator) animator).getAnimatedValue());
	}

	@SuppressWarnings("boxing")
	@Override
	public void onAnimationEnd(@NonNull android.animation.Animator animator)
	{
		this.listener.onAction(this.lastStep);
		// Log.d(Animator.TAG, "animate end value=" + this.lastStep);
	}

	@SuppressWarnings("boxing")
	@Override
	public void onAnimationCancel(@NonNull android.animation.Animator animator)
	{
		this.listener.onAction(this.lastStep);
		// Log.d(Animator.TAG, "animate cancel value=" + ((ValueAnimator) animator).getAnimatedValue());
	}

	@SuppressWarnings("EmptyMethod")
	@Override
	public void onAnimationRepeat(@NonNull android.animation.Animator animator)
	{
		// Log.d(Animator.TAG, "animate repeat " + Animator.frameCount + " value=" + ((ValueAnimator) animator).getAnimatedValue());
	}
}
