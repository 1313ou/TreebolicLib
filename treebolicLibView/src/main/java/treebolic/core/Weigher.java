/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.core;

import java.util.List;

import androidx.annotation.NonNull;
import treebolic.model.INode;

/**
 * Node Weigher
 *
 * @author Bernard Bou
 */
public class Weigher
{
	// C O N S T R U C T I O N

	/**
	 * Constructor
	 */
	public Weigher()
	{
		// do nothing
	}

	// O P E R A T I O N

	/**
	 * Weigh this node
	 *
	 * @param node node
	 */
	public void weigh(@NonNull final INode node)
	{
		final List<INode> children = node.getChildren();
		if (children == null || children.isEmpty())
		{
			node.setChildrenWeight(0.);
			node.setMinWeight(1.);

			// negative weights are considered preset and can't be changed
			if (node.getWeight() >= 0.)
			{
				node.setWeight(1.);
			}
		}
		else
		{
			double childrenWeightSum = 0.;
			double minWeight = 1000.;
			for (final INode child : node.getChildren())
			{
				// compute this child's weight
				weigh(child);

				// sum
				final double weight = Math.abs(child.getWeight());
				childrenWeightSum += weight;

				// min
				if (weight < minWeight)
				{
					minWeight = weight;
				}
			}
			node.setChildrenWeight(childrenWeightSum);
			node.setMinWeight(minWeight);

			// negative weights are considered preset and can't be changed
			if (node.getWeight() >= 0.)
			{
				node.setWeight(Math.max(1., Math.log(1. + childrenWeightSum)));
			}
		}
	}
}
