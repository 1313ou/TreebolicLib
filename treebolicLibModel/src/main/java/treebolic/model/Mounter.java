package treebolic.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Mounter
 *
 * @author Bernard Bou
 */
public class Mounter
{
	/**
	 * Graft mounted node onto mounting node
	 *
	 * @param thisMountingNode  grafting node
	 * @param thisMountedNode   grafted node
	 * @param theseEdges        edge list from mounting model
	 * @param theseMountedEdges edge list from mounted model
	 * @return true if successful, null otherwise
	 */
	public static synchronized boolean graft(@NonNull final INode thisMountingNode, @NonNull final INode thisMountedNode, @NonNull final List<IEdge> theseEdges, @Nullable final List<IEdge> theseMountedEdges)
	{
		// REQUISITES

		// mounting node must have a parent
		final INode thisMountingParent = thisMountingNode.getParent();
		if (thisMountingParent == null)
		{
			return false;
		}

		// mounting mountpoint must be non null
		final MountPoint thisMountPoint = thisMountingNode.getMountPoint();
		if (thisMountPoint == null)
		{
			return false;
		}

		// mounting mountpoint must be mounting
		if (!(thisMountPoint instanceof MountPoint.Mounting))
		{
			return false;
		}
		final MountPoint.Mounting thisMountingMountPoint = (MountPoint.Mounting) thisMountPoint;

		// mounted mountpoint must null
		if (thisMountedNode.getMountPoint() != null)
		{
			return false;
		}

		// ALLOCATE

		// setup mounted mountpoint
		final MountPoint.Mounted thisMountedMountPoint = new MountPoint.Mounted();
		thisMountedNode.setMountPoint(thisMountedMountPoint);

		// TREE

		// tree down connect
		final List<INode> theseMountingParentChildren = thisMountingParent.getChildren();
		if (theseMountingParentChildren != null)
		{
			final int thisIndex = theseMountingParentChildren.indexOf(thisMountingNode);
			theseMountingParentChildren.remove(thisIndex);
			theseMountingParentChildren.add(thisIndex, thisMountedNode);
		}

		// tree up connect
		thisMountedNode.setParent(thisMountingParent);
		thisMountedNode.setEdgeLabel(thisMountingNode.getEdgeLabel());
		thisMountedNode.setEdgeStyle(thisMountingNode.getEdgeStyle());
		thisMountedNode.setEdgeColor(thisMountingNode.getEdgeColor());
		thisMountedNode.setEdgeImageIndex(thisMountingNode.getEdgeImageIndex());
		thisMountedNode.setEdgeImage(thisMountingNode.getEdgeImage());

		// STATE

		// cross reference mounting node and mounted
		thisMountedMountPoint.theMountingNode = thisMountingNode;
		thisMountingMountPoint.theMountedNode = thisMountedNode;

		// EDGES
		thisMountedMountPoint.theMountedEdges = theseMountedEdges;
		if (theseMountedEdges != null)
		{
			theseEdges.addAll(theseMountedEdges);
		}

		return true;
	}

	/**
	 * Prune mounted children nodes, and remove orphaned edges
	 *
	 * @param thisMountedNode node
	 * @param theseEdges      edge list to scan for orphaned edges
	 * @return mounting node if successful, null otherwise
	 */
	public static synchronized INode prune(@NonNull final INode thisMountedNode, @Nullable final List<IEdge> theseEdges)
	{
		// REQUISITES

		// mounting node must have a parent
		final INode thisMountedParent = thisMountedNode.getParent();
		if (thisMountedParent == null)
		{
			return null;
		}

		// mounted mountpoint must be non-null
		MountPoint thisMountPoint = thisMountedNode.getMountPoint();
		if (thisMountPoint == null)
		{
			return null;
		}

		// mounted mountpoint must not be mounting
		if (!(thisMountPoint instanceof MountPoint.Mounted))
		{
			return null;
		}
		final MountPoint.Mounted thisMountedMountPoint = (MountPoint.Mounted) thisMountPoint;

		// mounted mountpoint must be reference a mounting node
		final INode thisMountingNode = thisMountedMountPoint.theMountingNode;
		if (thisMountingNode == null)
		{
			return null;
		}

		// mounting mountpoint must be non null
		thisMountPoint = thisMountingNode.getMountPoint();
		if (thisMountPoint == null)
		{
			return null;
		}

		// mounting mountpoint must be mounting
		if (!(thisMountPoint instanceof MountPoint.Mounting))
		{
			return null;
		}
		final MountPoint.Mounting thisMountingMountPoint = (MountPoint.Mounting) thisMountPoint;

		// mounting mountpoint must reference mounted node
		if (thisMountingMountPoint.theMountedNode != thisMountedNode)
		{
			return null;
		}

		// TREE CONNECT

		// tree down connect
		final List<INode> theseMountedParentChildren = thisMountedParent.getChildren();
		if (theseMountedParentChildren != null)
		{
			final int thisIndex = theseMountedParentChildren.indexOf(thisMountedNode);
			theseMountedParentChildren.remove(thisIndex);
			theseMountedParentChildren.add(thisIndex, thisMountingNode);
		}

		// tree up connect
		thisMountingNode.setParent(thisMountedParent);

		// STATE

		// cross reference mounting node and mounted
		thisMountedMountPoint.theMountingNode = null;
		thisMountingMountPoint.theMountedNode = null;

		// EDGES
		if (theseEdges != null)
		{
			if (thisMountedMountPoint.theMountedEdges != null)
			{
				theseEdges.removeAll(thisMountedMountPoint.theMountedEdges);
			}
			Mounter.removeSubtreeEdges(theseEdges, thisMountedNode);
		}

		// FREE

		// dispose mounted node mountpoint
		thisMountedNode.setMountPoint(null);

		return thisMountingNode;
	}

	static private void removeSubtreeEdges(@NonNull final List<IEdge> theseEdges, @NonNull final INode thisMountedNode)
	{
		final List<INode> theseMountedNodeChildren = thisMountedNode.getChildren();
		if (theseMountedNodeChildren != null)
		{
			for (final INode thisChildNode : theseMountedNodeChildren)
			{
				// if mounted mount point having edges
				final MountPoint thisMountPoint = thisChildNode.getMountPoint();
				if (thisMountPoint != null && thisMountPoint instanceof MountPoint.Mounted)
				{
					final MountPoint.Mounted thisMountedMountPoint = (MountPoint.Mounted) thisMountPoint;
					if (thisMountedMountPoint.theMountedEdges != null)
					{
						theseEdges.removeAll(thisMountedMountPoint.theMountedEdges);
					}
				}
				// recurse
				Mounter.removeSubtreeEdges(theseEdges, thisChildNode);
			}
		}
	}
}
