/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.core;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.core.location.Complex;
import treebolic.core.math.Distance;
import treebolic.core.transform.HyperTranslation;
import treebolic.model.INode;
import treebolic.model.MountPoint;

/**
 * Layout agent
 *
 * @author Bernard Bou
 */
public class LayerOut extends AbstractLayerOut
{
	// C O N S T A N T
	/**
	 * Constant used in computations, determining compromise between widening and lengthening
	 */
	static private final double ksi = 4.;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public LayerOut()
	{
		// do nothing
	}

	// O P E R A T I O N

	@Override
	public synchronized void layout(@Nullable final INode node)
	{
		if (node == null)
		{
			return;
		}

		// handle root node
		node.getLocation().hyper.set(Complex.ZERO, this.radius);

		// handle its children
		layoutChildren(node, this.rootSweep, this.rootOrientation.arg());
	}

	@Override
	public synchronized void layout(@Nullable final INode node, @NonNull final Complex center, final double halfWedge, final double orientation)
	{
		if (node == null)
		{
			return;
		}

		// layout node
		node.getLocation().hyper.set(center, this.radius);

		// handle its children
		layoutChildren(node, halfWedge, orientation);
	}

	// O P E R A T I O N S

	/**
	 * Lay out children
	 *
	 * @param node        starting node
	 * @param halfWedge   half wedge allocated to this node
	 * @param orientation orientation of this node
	 */
	private void layoutChildren(@NonNull final INode node, final double halfWedge, final double orientation)
	{
		// children
		final List<INode> children = node.getChildren();
		if (children == null || children.isEmpty())
		{
			return;
		}

		// center
		final Complex center = node.getLocation().hyper.center;

		// compute node distance
		final double nodeDistance = computeDistance(children.size());
		final double radius = Distance.distanceToOrigin_e2h(nodeDistance / 2.);

		// iterate
		double childSweeper = orientation - (this.clockwise ? halfWedge : -halfWedge);
		for (final INode child : children)
		{
			// compute child's share of the parent's wedge as per weight
			final double share = Math.abs(child.getWeight()) / node.getChildrenWeight();
			final double childHalfWedgeShare = halfWedge * share;

			// set child sweeper to child's orientation
			childSweeper += this.clockwise ? childHalfWedgeShare : -childHalfWedgeShare;

			// translate by parent's coordinates
			final Complex childCenter = HyperTranslation.map(Complex.makeFromArgAbs(childSweeper, nodeDistance), center);

			// set child's center and radius
			child.getLocation().hyper.set(childCenter, radius);

			// compute child's orientation
			final double childOrientation = LayerOut.computeOrientation(center, childCenter, childSweeper);

			// compute child's wedge
			final double childHalfWedge = LayerOut.computeWedge(nodeDistance, childHalfWedgeShare);

			// mountpoint handling
			MountPoint mountPoint = child.getMountPoint();
			while (mountPoint != null)
			{
				if (mountPoint instanceof MountPoint.Mounting)
				{
					final MountPoint.Mounting mountingPoint = (MountPoint.Mounting) mountPoint;
					mountingPoint.halfWedge = childHalfWedge;
					mountingPoint.orientation = childOrientation;
					break;
				}
				final MountPoint.Mounted mountedPoint = (MountPoint.Mounted) mountPoint;
				assert mountedPoint.mountingNode != null;
				mountPoint = mountedPoint.mountingNode.getMountPoint();
			}

			// recurse
			layoutChildren(child, childHalfWedge, childOrientation);

			// sweep to next
			childSweeper += this.clockwise ? childHalfWedgeShare : -childHalfWedgeShare;
		}
	}

	/**
	 * Compute distance
	 *
	 * @param childCount child count
	 * @return distance
	 */
	private double computeDistance(final int childCount)
	{
		// <BOUTHIER>
		// double l1 = (0.95 - this.nodeDistance);
		// double l2 = Math.cos((20. * Math.PI) / (2. * childCount + 38.));
		// </BOUTHIER>

		// <BOUTHIER>
		final double l1 = 1 - 1 / LayerOut.ksi - this.nodeDistance;
		final double l2 = Math.cos(LayerOut.ksi * this.sweepFactor / (LayerOut.ksi - 1. + childCount));
		final double delta = l1 * l2;
		return this.nodeDistance + delta;
		// </BOUTHIER>
	}

	/**
	 * Compute orientation
	 *
	 * @param parentCenter parent node hypercircle center
	 * @param center       this node's hypercircle
	 * @param orientation  orientation
	 * @return orientation
	 */
	private static double computeOrientation(@NonNull final Complex parentCenter, @NonNull final Complex center, final double orientation)
	{
		// <BOUTHIER>
		// compute the new orientation (oc)
		// e(i oc) = T(-z) o T(zp) (e(i op))
		// e(i op) = theta
		final Complex theta = Complex.makeFromArg(orientation);
		final Complex nz = new Complex(center).neg();
		HyperTranslation.map2(theta, parentCenter, nz);
		// </BOUTHIER>

		return theta.arg();
	}

	/**
	 * Compute wedge
	 *
	 * @param nodeDistance node distance
	 * @param wedge        wedge
	 * @return wedge
	 */
	private static double computeWedge(final double nodeDistance, final double wedge)
	{
		// <BOUTHIER>
		// compute the new wedge from the child's share of the parent's wedge
		// e(i w) = T(-length) (e(i wp))
		final Complex theta = Complex.makeFromArg(-wedge);
		final Complex ro = new Complex(-nodeDistance, 0);
		HyperTranslation.map(theta, ro);
		return Math.abs(theta.arg());

		// <OPTIMIZED>
		// double wx = Math.cos(wedge);
		// double a = 1 + nodeDistance * nodeDistance;
		// double b = 2 * nodeDistance;
		// return Math.abs(Math.acos((a * wx - b) / (a - b * wx)));
		// </OPTIMIZED>
		// </BOUTHIER>
	}
}
