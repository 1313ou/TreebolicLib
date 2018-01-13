package treebolic.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import treebolic.core.location.Complex;
import treebolic.core.location.HyperCircle;
import treebolic.core.transform.HyperOptimizedTransform;
import treebolic.core.transform.HyperOrientationPreservingTransform;
import treebolic.core.transform.HyperRadialOrientationPreservingTransform;
import treebolic.core.transform.HyperTransform;
import treebolic.core.transform.HyperTranslation;
import treebolic.core.transform.IHyperTransform;
import treebolic.model.INode;

/**
 * Transformer
 *
 * @author Bernard Bou
 */
public class Transformer
{
	// D A T A

	/**
	 * Current transform
	 */
	private HyperTransform theTransform;

	/**
	 * Whether orientation is preserved
	 */
	private boolean thePreserveOrientationFlag;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public Transformer()
	{
		this.thePreserveOrientationFlag = true;
		this.theTransform = new HyperTransform();
	}

	// A C C E S S

	/**
	 * Get transform
	 *
	 * @return transform
	 */
	public HyperTransform getTransform()
	{
		return this.theTransform;
	}

	/**
	 * Get whether transform preserves orientation
	 *
	 * @return true if transform preserves orientation
	 */
	public boolean getPreserveOrientation()
	{
		return this.thePreserveOrientationFlag;
	}

	/**
	 * Get whether transform preserves orientation
	 *
	 * @param thisFlag if transform is to preserve orientation
	 */
	public void setPreserveOrientation(final boolean thisFlag)
	{
		this.thePreserveOrientationFlag = thisFlag;
	}

	// S E T

	/**
	 * Set current transform
	 *
	 * @param thisTransform new transform
	 */
	public void setTransform(final HyperTransform thisTransform)
	{
		this.theTransform = thisTransform;
	}

	/**
	 * Set current transform as composition of current transform and this transform
	 *
	 * @param thisTransform transform
	 */
	public void composeTransform(@NonNull final HyperTransform thisTransform)
	{
		this.theTransform = this.theTransform.compose(thisTransform);
	}

	// O P E R A T I O N

	/**
	 * Transform
	 *
	 * @param thisNode node to apply transform to
	 */
	public synchronized void transform(@NonNull final INode thisNode)
	{
		applyTransform(thisNode, new HyperOptimizedTransform(this.theTransform));
		thisNode.getLocation().hyper.isBorder = false;
	}

	/**
	 * Reset
	 *
	 * @param thisNode node to apply reset to
	 */
	public synchronized void reset(final INode thisNode)
	{
		applyReset(thisNode);
		setTransform(HyperTransform.NULLTRANSFORM);
	}

	// T R A N S F O R M F A C T O R Y

	/**
	 * Make transform
	 *
	 * @param from            translation from-point
	 * @param to              translation to-point
	 * @param thisOrientation orientation
	 * @return transform
	 */
	@NonNull
	public HyperTransform makeTransform(@NonNull final Complex from, @NonNull final Complex to, @NonNull final Complex thisOrientation)
	{
		if (!this.thePreserveOrientationFlag)
		{
			return new HyperTransform(new HyperTranslation(from, to, true));
		}

		// orientation preserving
		if (thisOrientation == Complex.ZERO)
		{
			return new HyperRadialOrientationPreservingTransform(from, to, getTransform().map(new Complex(Complex.ZERO)));
		}
		return new HyperOrientationPreservingTransform(from, to, thisOrientation);
	}

	// A P P L Y

	/**
	 * Apply transform
	 *
	 * @param thisNode      node to apply transform to
	 * @param thisTransform transform to apply
	 */
	private void applyTransform(@Nullable final INode thisNode, @NonNull final IHyperTransform thisTransform)
	{
		if (thisNode == null)
		{
			return;
		}

		// this node
		Transformer.transform(thisTransform, thisNode.getLocation().hyper);

		// recurse on children
		final List<INode> theseChildren = thisNode.getChildren();
		if (theseChildren != null)
		{
			for (final INode thisChild : theseChildren)
			{
				applyTransform(thisChild, thisTransform);
			}
		}
	}

	/**
	 * Apply reset
	 *
	 * @param thisNode node to apply reset to
	 */
	private void applyReset(@Nullable final INode thisNode)
	{
		if (thisNode == null)
		{
			return;
		}

		thisNode.getLocation().hyper.reset();

		final List<INode> theseChildren = thisNode.getChildren();
		if (theseChildren != null)
		{
			for (final INode thisChild : theseChildren)
			{
				applyReset(thisChild);
			}
		}
	}

	// T R A N S F O R M

	/**
	 * Apply transform to this hypercircle
	 *
	 * @param t               transform
	 * @param thisHyperCircle hypercircle
	 */
	static private void transform(@NonNull final IHyperTransform t, @NonNull final HyperCircle thisHyperCircle)
	{
		// map
		t.map(thisHyperCircle.center.set(thisHyperCircle.center0));

		// distance to (0,0)
		thisHyperCircle.dist = thisHyperCircle.center.mag();

		// normalization (should not occur with proper transform)
		if (thisHyperCircle.dist > 1.)
		{
			thisHyperCircle.center.normalize();
			thisHyperCircle.dist = thisHyperCircle.center.mag();
		}

		// compute if off-limit
		thisHyperCircle.isBorder = thisHyperCircle.dist > HyperCircle.BORDER;
		thisHyperCircle.isDirty = true;
	}
}
