package treebolic.glue;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.animation.LinearInterpolator;

/**
 * Animator
 *
 * @author Bernard Bou
 */
public class Animator implements treebolic.glue.iface.Animator<ActionListener>, AnimatorUpdateListener, android.animation.Animator.AnimatorListener
{
	/**
	 * Log tag
	 */
	// static private final String TAG = "Animator";

	/**
	 * Animation time slice
	 */
	static private final int ANIMATIONTIMESLICE = 250;

	/**
	 * Animation listener
	 */
	private ActionListener theListener;

	/**
	 * Android animator
	 */
	private ValueAnimator theAnimator;

	/**
	 * Android last step
	 */
	private int theLastStep;

	/**
	 * Constructor
	 */
	public Animator()
	{
		ValueAnimator.setFrameDelay(Animator.ANIMATIONTIMESLICE);
		// Log.d(Animator.TAG, "animate frame delay set=" + ANIMATIONTIMESLICE + " get=" + ValueAnimator.getFrameDelay());
	}

	@Override
	public boolean run(final ActionListener thisListener, final int theseSteps, final int startDelay)
	{
		// Log.d(Animator.TAG, "animate steps " + theseSteps);
		this.theLastStep = theseSteps - 1;
		this.theListener = thisListener;
		this.theAnimator = ValueAnimator.ofInt(0, this.theLastStep);
		this.theAnimator.setRepeatCount(0);
		this.theAnimator.setDuration(1000);
		this.theAnimator.setStartDelay(startDelay);
		this.theAnimator.setInterpolator(new LinearInterpolator());
		this.theAnimator.addUpdateListener(this);
		this.theAnimator.addListener(this);
		this.theAnimator.start();
		return true;
	}

	@Override
	public boolean isRunning()
	{
		return this.theAnimator.isRunning();
	}

	@SuppressWarnings("boxing")
	@Override
	public void onAnimationUpdate(final ValueAnimator animator)
	{
		int thisStep = (int) animator.getAnimatedValue();
		this.theListener.onAction(thisStep);
		// Log.d(Animator.TAG, "animate update value=" + animator.getAnimatedValue());
	}

	@Override
	public void onAnimationStart(android.animation.Animator animator)
	{
		// Log.d(Animator.TAG, "animate start " + Animator.frameCount + " value=" + ((ValueAnimator) animator).getAnimatedValue());
	}

	@SuppressWarnings("boxing")
	@Override
	public void onAnimationEnd(android.animation.Animator animator)
	{
		this.theListener.onAction(this.theLastStep);
		// Log.d(Animator.TAG, "animate end value=" + this.theLastStep);
	}

	@SuppressWarnings("boxing")
	@Override
	public void onAnimationCancel(android.animation.Animator animator)
	{
		this.theListener.onAction(this.theLastStep);
		// Log.d(Animator.TAG, "animate cancel value=" + ((ValueAnimator) animator).getAnimatedValue());
	}

	@Override
	public void onAnimationRepeat(android.animation.Animator animator)
	{
		// Log.d(Animator.TAG, "animate repeat " + Animator.frameCount + " value=" + ((ValueAnimator) animator).getAnimatedValue());
	}
}
