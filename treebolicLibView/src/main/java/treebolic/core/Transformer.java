/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.core;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private HyperTransform transform;

	/**
	 * Whether orientation is preserved
	 */
	private boolean preserveOrientationFlag;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public Transformer()
	{
		this.preserveOrientationFlag = true;
		this.transform = new HyperTransform();
	}

	// A C C E S S

	/**
	 * Get transform
	 *
	 * @return transform
	 */
	public HyperTransform getTransform()
	{
		return this.transform;
	}

	/**
	 * Get whether transform preserves orientation
	 *
	 * @return true if transform preserves orientation
	 */
	public boolean getPreserveOrientation()
	{
		return this.preserveOrientationFlag;
	}

	/**
	 * Get whether transform preserves orientation
	 *
	 * @param flag if transform is to preserve orientation
	 */
	public void setPreserveOrientation(final boolean flag)
	{
		this.preserveOrientationFlag = flag;
	}

	// S E T

	/**
	 * Set current transform
	 *
	 * @param transform new transform
	 */
	public void setTransform(final HyperTransform transform)
	{
		this.transform = transform;
	}

	/**
	 * Set current transform as composition of current transform and this transform
	 *
	 * @param transform transform
	 */
	public void composeTransform(@NonNull final HyperTransform transform)
	{
		this.transform = this.transform.compose(transform);
	}

	// O P E R A T I O N

	/**
	 * Transform
	 *
	 * @param node node to apply transform to
	 */
	public synchronized void transform(@NonNull final INode node)
	{
		applyTransform(node, new HyperOptimizedTransform(this.transform));
		node.getLocation().hyper.isBorder = false;
	}

	/**
	 * Reset
	 *
	 * @param node node to apply reset to
	 */
	public synchronized void reset(final INode node)
	{
		applyReset(node);
		setTransform(HyperTransform.NULLTRANSFORM);
	}

	// T R A N S F O R M F A C T O R Y

	/**
	 * Make transform
	 *
	 * @param from        translation from-point
	 * @param to          translation to-point
	 * @param orientation orientation
	 * @return transform
	 */
	@NonNull
	public HyperTransform makeTransform(@NonNull final Complex from, @NonNull final Complex to, @NonNull final Complex orientation)
	{
		if (!this.preserveOrientationFlag)
		{
			return new HyperTransform(new HyperTranslation(from, to, true));
		}

		// orientation preserving
		if (orientation == Complex.ZERO)
		{
			return new HyperRadialOrientationPreservingTransform(from, to, getTransform().map(new Complex(Complex.ZERO)));
		}
		return new HyperOrientationPreservingTransform(from, to, orientation);
	}

	// A P P L Y

	/**
	 * Apply transform
	 *
	 * @param node      node to apply transform to
	 * @param transform transform to apply
	 */
	private void applyTransform(@Nullable final INode node, @NonNull final IHyperTransform transform)
	{
		if (node == null)
		{
			return;
		}

		// this node
		Transformer.transform(transform, node.getLocation().hyper);

		// recurse on children
		final List<INode> children = node.getChildren();
		if (children != null)
		{
			for (final INode child : children)
			{
				applyTransform(child, transform);
			}
		}
	}

	/**
	 * Apply reset
	 *
	 * @param node node to apply reset to
	 */
	private void applyReset(@Nullable final INode node)
	{
		if (node == null)
		{
			return;
		}

		node.getLocation().hyper.reset();

		final List<INode> children = node.getChildren();
		if (children != null)
		{
			for (final INode child : children)
			{
				applyReset(child);
			}
		}
	}

	// T R A N S F O R M

	/**
	 * Apply transform to this hypercircle
	 *
	 * @param t           transform
	 * @param hyperCircle hypercircle
	 */
	static private void transform(@NonNull final IHyperTransform t, @NonNull final HyperCircle hyperCircle)
	{
		// map
		t.map(hyperCircle.center.set(hyperCircle.center0));

		// distance to (0,0)
		hyperCircle.dist = hyperCircle.center.mag();

		// normalization (should not occur with proper transform)
		if (hyperCircle.dist > 1.)
		{
			hyperCircle.center.normalize();
			hyperCircle.dist = hyperCircle.center.mag();
		}

		// compute if off-limit
		hyperCircle.isBorder = hyperCircle.dist > HyperCircle.BORDER;
		hyperCircle.isDirty = true;
	}
}
