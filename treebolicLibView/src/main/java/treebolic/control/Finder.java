package treebolic.control;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import treebolic.core.location.Complex;
import treebolic.core.math.Distance;
import treebolic.glue.component.Surface;
import treebolic.model.INode;
import treebolic.model.Location;

/**
 * Node Finder
 *
 * @author Bernard Bou
 */
public class Finder
{
	/**
	 * Find node nearest to point
	 *
	 * @param thisStart start node (only descendants are considered)
	 * @param thisPoint point
	 * @return node if found, null otherwise
	 */
	@Nullable
	static public INode findNodeAt(@Nullable final INode thisStart, @NonNull final Complex thisPoint)
	{
		if (thisStart == null)
		{
			return null;
		}

		INode thisResult = thisStart;
		Location thisResultLocation = thisStart.getLocation();

		// find nearest (using squares as a measure)
		double thisDistance = Distance.getEuclideanDistanceSquared(thisResultLocation.hyper.center, thisPoint);
		final List<INode> theseChildren = thisStart.getChildren();
		if (theseChildren != null)
		{
			for (final INode thisChild : theseChildren)
			{
				final INode thisTargetNode = Finder.findNodeAt(thisChild, thisPoint);
				if (thisTargetNode != null)
				{
					final Location thisTargetLocation = thisTargetNode.getLocation();
					if (!thisTargetLocation.hyper.isBorder)
					{
						final double thatDistance = Distance.getEuclideanDistanceSquared(thisTargetLocation.hyper.center, thisPoint);
						if (thatDistance < thisDistance)
						{
							thisResult = thisTargetNode;
							thisResultLocation = thisTargetLocation;
							thisDistance = thatDistance;
						}
					}
				}
			}
		}

		if (thisResultLocation.euclidean.radius * thisResultLocation.euclidean.radius * Surface.FINDERRORMARGINFACTOR <= thisDistance)
		{
			return null;
		}
		return thisResult;
	}

	/**
	 * Find node by id
	 *
	 * @param thisStart start node
	 * @param thisId    target id
	 * @return node if found, null otherwise
	 */
	@Nullable
	static public INode findNodeById(@Nullable final INode thisStart, @NonNull final String thisId)
	{
		if (thisStart == null)
		{
			return null;
		}

		// node test
		if (thisId.equals(thisStart.getId()))
		{
			return thisStart;
		}

		// children
		final List<INode> theseChildNodes = thisStart.getChildren();
		if (theseChildNodes != null)
		{
			for (final INode thisChildNode : theseChildNodes)
			{
				final INode thisNode = Finder.findNodeById(thisChildNode, thisId);
				if (thisNode != null)
				{
					return thisNode;
				}
			}
		}
		return null;
	}
}
