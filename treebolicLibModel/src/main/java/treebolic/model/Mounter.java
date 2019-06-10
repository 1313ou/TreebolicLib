package treebolic.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
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
	 * @param mountingNode grafting node
	 * @param mountedNode  grafted node
	 * @param edges        edge list from mounting model
	 * @param mountedEdges edge list from mounted model
	 * @return true if successful, null otherwise
	 */
	public static synchronized boolean graft(@NonNull final INode mountingNode, @NonNull final INode mountedNode, @NonNull final Collection<IEdge> edges, @Nullable final List<IEdge> mountedEdges)
	{
		// REQUISITES

		// mounting node must have a parent
		final INode mountingParent = mountingNode.getParent();
		if (mountingParent == null)
		{
			return false;
		}

		// mounting mountpoint must be non null
		final MountPoint mountPoint = mountingNode.getMountPoint();
		if (mountPoint == null)
		{
			return false;
		}

		// mounting mountpoint must be mounting
		//noinspection InstanceofConcreteClass
		if (!(mountPoint instanceof MountPoint.Mounting))
		{
			return false;
		}
		final MountPoint.Mounting mountingMountPoint = (MountPoint.Mounting) mountPoint;

		// mounted mountpoint must null
		if (mountedNode.getMountPoint() != null)
		{
			return false;
		}

		// ALLOCATE

		// setup mounted mountpoint
		final MountPoint.Mounted mountedMountPoint = new MountPoint.Mounted();
		mountedNode.setMountPoint(mountedMountPoint);

		// TREE

		// tree down connect
		final List<INode> mountingParentChildren = mountingParent.getChildren();
		if (mountingParentChildren != null)
		{
			final int index = mountingParentChildren.indexOf(mountingNode);
			mountingParentChildren.remove(index);
			mountingParentChildren.add(index, mountedNode);
		}

		// tree up connect
		mountedNode.setParent(mountingParent);
		mountedNode.setEdgeLabel(mountingNode.getEdgeLabel());
		mountedNode.setEdgeStyle(mountingNode.getEdgeStyle());
		mountedNode.setEdgeColor(mountingNode.getEdgeColor());
		mountedNode.setEdgeImageIndex(mountingNode.getEdgeImageIndex());
		mountedNode.setEdgeImage(mountingNode.getEdgeImage());

		// STATE

		// cross reference mounting node and mounted
		mountedMountPoint.mountingNode = mountingNode;
		mountingMountPoint.mountedNode = mountedNode;

		// EDGES
		mountedMountPoint.mountedEdges = mountedEdges;
		if (mountedEdges != null)
		{
			edges.addAll(mountedEdges);
		}

		return true;
	}

	/**
	 * Prune mounted children nodes, and remove orphaned edges
	 *
	 * @param mountedNode node
	 * @param edges       edge list to scan for orphaned edges
	 * @return mounting node if successful, null otherwise
	 */
	public static synchronized INode prune(@NonNull final INode mountedNode, @Nullable final List<IEdge> edges)
	{
		// REQUISITES

		// mounting node must have a parent
		final INode mountedParent = mountedNode.getParent();
		if (mountedParent == null)
		{
			return null;
		}

		// mounted mountpoint must be non-null
		MountPoint mountPoint = mountedNode.getMountPoint();
		if (mountPoint == null)
		{
			return null;
		}

		// mounted mountpoint must not be mounting
		//noinspection InstanceofConcreteClass
		if (!(mountPoint instanceof MountPoint.Mounted))
		{
			return null;
		}
		final MountPoint.Mounted mountedMountPoint = (MountPoint.Mounted) mountPoint;

		// mounted mountpoint must be reference a mounting node
		final INode mountingNode = mountedMountPoint.mountingNode;
		if (mountingNode == null)
		{
			return null;
		}

		// mounting mountpoint must be non null
		mountPoint = mountingNode.getMountPoint();
		if (mountPoint == null)
		{
			return null;
		}

		// mounting mountpoint must be mounting
		//noinspection InstanceofConcreteClass
		if (!(mountPoint instanceof MountPoint.Mounting))
		{
			return null;
		}
		final MountPoint.Mounting mountingMountPoint = (MountPoint.Mounting) mountPoint;

		// mounting mountpoint must reference mounted node
		if (mountingMountPoint.mountedNode != mountedNode)
		{
			return null;
		}

		// TREE CONNECT

		// tree down connect
		final List<INode> mountedParentChildren = mountedParent.getChildren();
		if (mountedParentChildren != null)
		{
			final int index = mountedParentChildren.indexOf(mountedNode);
			mountedParentChildren.remove(index);
			mountedParentChildren.add(index, mountingNode);
		}

		// tree up connect
		mountingNode.setParent(mountedParent);

		// STATE

		// cross reference mounting node and mounted
		mountedMountPoint.mountingNode = null;
		mountingMountPoint.mountedNode = null;

		// EDGES
		if (edges != null)
		{
			if (mountedMountPoint.mountedEdges != null)
			{
				edges.removeAll(mountedMountPoint.mountedEdges);
			}
			Mounter.removeSubtreeEdges(edges, mountedNode);
		}

		// FREE

		// dispose mounted node mountpoint
		mountedNode.setMountPoint(null);

		return mountingNode;
	}

	static private void removeSubtreeEdges(@NonNull final List<IEdge> edges, @NonNull final INode mountedNode)
	{
		final List<INode> mountedNodeChildren = mountedNode.getChildren();
		if (mountedNodeChildren != null)
		{
			for (final INode childNode : mountedNodeChildren)
			{
				// if mounted mount point having edges
				final MountPoint mountPoint = childNode.getMountPoint();
				//noinspection InstanceofConcreteClass
				if (mountPoint instanceof MountPoint.Mounted)
				{
					final MountPoint.Mounted mountedMountPoint = (MountPoint.Mounted) mountPoint;
					if (mountedMountPoint.mountedEdges != null)
					{
						edges.removeAll(mountedMountPoint.mountedEdges);
					}
				}
				// recurse
				Mounter.removeSubtreeEdges(edges, childNode);
			}
		}
	}
}
