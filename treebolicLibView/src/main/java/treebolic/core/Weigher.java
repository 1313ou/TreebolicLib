package treebolic.core;

import android.support.annotation.NonNull;

import java.util.List;

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
	 * @param thisNode node
	 */
	public void weigh(@NonNull final INode thisNode)
	{
		final List<INode> theseChildren = thisNode.getChildren();
		if (theseChildren == null || theseChildren.isEmpty())
		{
			thisNode.setChildrenWeight(0.);
			thisNode.setMinWeight(1.);

			// negative weights are considered preset and can't be changed
			if (thisNode.getWeight() >= 0.)
			{
				thisNode.setWeight(1.);
			}
		}
		else
		{
			double thisChildrenWeightSum = 0.;
			double thisMinWeight = 1000.;
			for (final INode thisChild : thisNode.getChildren())
			{
				// compute this child's weight
				weigh(thisChild);

				// sum
				final double thisWeight = Math.abs(thisChild.getWeight());
				thisChildrenWeightSum += thisWeight;

				// min
				if (thisWeight < thisMinWeight)
				{
					thisMinWeight = thisWeight;
				}
			}
			thisNode.setChildrenWeight(thisChildrenWeightSum);
			thisNode.setMinWeight(thisMinWeight);

			// negative weights are considered preset and can't be changed
			if (thisNode.getWeight() >= 0.)
			{
				thisNode.setWeight(Math.max(1., Math.log(1. + thisChildrenWeightSum)));
			}
		}
	}
}
