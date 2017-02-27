/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 * <p>
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.core;

import java.util.List;

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
	 * Contructor
	 */
	public LayerOut()
	{
		// do nothing
	}

	// O P E R A T I O N

	@Override
	public synchronized void layout(final INode thisNode)
	{
		if (thisNode == null)
		{
			return;
		}

		// handle root node
		thisNode.getLocation().hyper.set(Complex.ZERO, this.theRadius);

		// handle its children
		layoutChildren(thisNode, this.theRootSweep, this.theRootOrientation.arg());
	}

	@Override
	public synchronized void layout(final INode thisNode, final Complex thisCenter, final double thisHalfWedge, final double thisOrientation)
	{
		if (thisNode == null)
		{
			return;
		}

		// layout node
		thisNode.getLocation().hyper.set(thisCenter, this.theRadius);

		// handle its children
		layoutChildren(thisNode, thisHalfWedge, thisOrientation);
	}

	// O P E R A T I O N S

	/**
	 * Lay out children
	 *
	 * @param thisNode
	 *            starting node
	 * @param thisHalfWedge
	 *            half wedge allocated to this node
	 * @param thisOrientation
	 *            orientation of this node
	 */
	private void layoutChildren(final INode thisNode, final double thisHalfWedge, final double thisOrientation)
	{
		// children
		final List<INode> theseChildren = thisNode.getChildren();
		if (theseChildren == null || theseChildren.isEmpty())
		{
			return;
		}

		// center
		final Complex thisCenter = thisNode.getLocation().hyper.center;

		// compute node distance
		final double thisNodeDistance = computeDistance(theseChildren.size());
		final double thisRadius = Distance.distanceToOrigin_e2h(thisNodeDistance / 2.);

		// iterate
		double thisChildSweeper = thisOrientation - (this.clockwise ? thisHalfWedge : -thisHalfWedge);
		for (final INode thisChild : theseChildren)
		{
			// compute child's share of the parent's wedge as per weight
			final double thisShare = Math.abs(thisChild.getWeight()) / thisNode.getChildrenWeight();
			final double thisChildHalfWedgeShare = thisHalfWedge * thisShare;

			// set child sweeper to child's orientation
			thisChildSweeper += this.clockwise ? thisChildHalfWedgeShare : -thisChildHalfWedgeShare;

			// translate by parent's coordinates
			final Complex thisChildCenter = HyperTranslation.map(Complex.makeFromArgAbs(thisChildSweeper, thisNodeDistance), thisCenter);

			// set child's center and radius
			thisChild.getLocation().hyper.set(thisChildCenter, thisRadius);

			// compute child's orientation
			final double thisChildOrientation = LayerOut.computeOrientation(thisCenter, thisChildCenter, thisChildSweeper);

			// compute child's wedge
			final double thisChildHalfWedge = LayerOut.computeWedge(thisNodeDistance, thisChildHalfWedgeShare);

			// mountpoint handling
			MountPoint thisMountPoint = thisChild.getMountPoint();
			while (thisMountPoint != null)
			{
				if (thisMountPoint instanceof MountPoint.Mounting)
				{
					final MountPoint.Mounting thisMountingPoint = (MountPoint.Mounting) thisMountPoint;
					thisMountingPoint.theHalfWedge = thisChildHalfWedge;
					thisMountingPoint.theOrientation = thisChildOrientation;
					break;
				}
				final MountPoint.Mounted thisMountedPoint = (MountPoint.Mounted) thisMountPoint;
				thisMountPoint = thisMountedPoint.theMountingNode.getMountPoint();
			}

			// recurse
			layoutChildren(thisChild, thisChildHalfWedge, thisChildOrientation);

			// sweep to next
			thisChildSweeper += this.clockwise ? thisChildHalfWedgeShare : -thisChildHalfWedgeShare;
		}
	}

	/**
	 * Compute distance
	 *
	 * @param thisChildCount child count
	 * @return distance
	 */
	private double computeDistance(final int thisChildCount)
	{
		// <BOUTHIER>
		// double l1 = (0.95 - this.theNodeDistance);
		// double l2 = Math.cos((20. * Math.PI) / (2. * thisChildCount + 38.));
		// </BOUTHIER>

		// <BOUTHIER>
		final double l1 = 1 - 1 / LayerOut.ksi - this.theNodeDistance;
		final double l2 = Math.cos(LayerOut.ksi * this.theSweepFactor / (LayerOut.ksi - 1. + thisChildCount));
		final double delta = l1 * l2;
		return this.theNodeDistance + delta;
		// </BOUTHIER>
	}

	/**
	 * Compute orientation
	 *
	 * @param thisParentCenter
	 *            parent node hypercircle center
	 * @param thisCenter
	 *            this node's hypercircle
	 * @param thisOrientation
	 *            orientation
	 * @return orientation
	 */
	private static double computeOrientation(final Complex thisParentCenter, final Complex thisCenter, final double thisOrientation)
	{
		// <BOUTHIER>
		// compute the new orientation (oc)
		// e(i oc) = T(-z) o T(zp) (e(i op))
		// e(i op) = theta
		final Complex theta = Complex.makeFromArg(thisOrientation);
		final Complex nz = new Complex(thisCenter).neg();
		HyperTranslation.map2(theta, thisParentCenter, nz);
		// </BOUTHIER>

		return theta.arg();
	}

	/**
	 * Compute wedge
	 *
	 * @param thisNodeDistance
	 *            node distance
	 * @param thisWedge
	 *            wedge
	 * @return wedge
	 */
	private static double computeWedge(final double thisNodeDistance, final double thisWedge)
	{
		// <BOUTHIER>
		// compute the new wedge from the child's share of the parent's wedge
		// e(i w) = T(-length) (e(i wp))
		final Complex theta = Complex.makeFromArg(-thisWedge);
		final Complex ro = new Complex(-thisNodeDistance, 0);
		HyperTranslation.map(theta, ro);
		return Math.abs(theta.arg());

		// <OPTIMIZED>
		// double wx = Math.cos(thisWedge);
		// double a = 1 + thisNodeDistance * thisNodeDistance;
		// double b = 2 * thisNodeDistance;
		// return Math.abs(Math.acos((a * wx - b) / (a - b * wx)));
		// </OPTIMIZED>
		// </BOUTHIER>
	}
}
