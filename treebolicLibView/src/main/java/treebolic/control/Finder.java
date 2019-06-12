/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.control;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
	 * @param start start node (only descendants are considered)
	 * @param point point
	 * @return node if found, null otherwise
	 */
	@Nullable
	static public INode findNodeAt(@Nullable final INode start, @NonNull final Complex point)
	{
		if (start == null)
		{
			return null;
		}

		INode result = start;
		Location resultLocation = start.getLocation();

		// find nearest (using squares as a measure)
		double distance = Distance.getEuclideanDistanceSquared(resultLocation.hyper.center, point);
		final List<INode> children = start.getChildren();
		if (children != null)
		{
			for (final INode child : children)
			{
				final INode targetNode = Finder.findNodeAt(child, point);
				if (targetNode != null)
				{
					final Location targetLocation = targetNode.getLocation();
					if (!targetLocation.hyper.isBorder)
					{
						final double distance2 = Distance.getEuclideanDistanceSquared(targetLocation.hyper.center, point);
						if (distance2 < distance)
						{
							result = targetNode;
							resultLocation = targetLocation;
							distance = distance2;
						}
					}
				}
			}
		}

		if (resultLocation.euclidean.radius * resultLocation.euclidean.radius * Surface.FINDERRORMARGINFACTOR <= distance)
		{
			return null;
		}
		return result;
	}

	/**
	 * Find node by id
	 *
	 * @param start start node
	 * @param id    target id
	 * @return node if found, null otherwise
	 */
	@Nullable
	static public INode findNodeById(@Nullable final INode start, @NonNull final String id)
	{
		if (start == null)
		{
			return null;
		}

		// node test
		if (id.equals(start.getId()))
		{
			return start;
		}

		// children
		final List<INode> childNodes = start.getChildren();
		if (childNodes != null)
		{
			for (final INode childNode : childNodes)
			{
				final INode node = Finder.findNodeById(childNode, id);
				if (node != null)
				{
					return node;
				}
			}
		}
		return null;
	}
}
