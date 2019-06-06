package treebolic.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * Mount point
 *
 * @author Bernard Bou
 */
public class MountPoint implements Serializable
{
	private static final long serialVersionUID = 8846293010235597970L;

	static public class Mounting extends MountPoint
	{
		private static final long serialVersionUID = 5222027905366742742L;

		/**
		 * URL
		 */
		@Nullable
		@SuppressWarnings("CanBeFinal")
		public String url;

		/**
		 * Mount now (for editing only)
		 */
		public Boolean now;

		/**
		 * Saved allocated half wedge
		 */
		public double halfWedge;

		/**
		 * Saved orientation
		 */
		public double orientation;

		/**
		 * Mounted node reference
		 */
		@Nullable
		public INode mountedNode;

		/**
		 * Constructor
		 */
		public Mounting()
		{
			super();
			this.url = null;
			this.halfWedge = 0.;
			this.orientation = 0.;
			this.mountedNode = null;
		}
	}

	static public class Mounted extends MountPoint
	{
		private static final long serialVersionUID = 7749263337626673995L;

		/**
		 * Mounting node reference
		 */
		@Nullable
		public INode mountingNode;

		/**
		 * Mounted edges
		 */
		@Nullable
		public List<IEdge> mountedEdges;

		/**
		 * Constructor
		 */
		public Mounted()
		{
			super();
			this.mountingNode = null;
			this.mountedEdges = null;
		}
	}

	/**
	 * Constructor
	 */
	private MountPoint()
	{
		// do nothing
	}

	/**
	 * Follow mounted->mounting or mounting->mounted
	 *
	 * @param node source node
	 * @param up        allow mounted -> mounting
	 * @param down      allow mounting -> mounted
	 * @return target node (or source if no mounting)
	 */
	@NonNull
	public static INode follow(@NonNull final INode node, @SuppressWarnings("SameParameterValue") boolean up, @SuppressWarnings("SameParameterValue") boolean down)
	{
		MountPoint mountPoint = node.getMountPoint();

		// mounted mountpoint must be non-null
		if (mountPoint != null)
		{
			// if mounting mountpoint: mounting -> mounted (down)
			if (down && mountPoint instanceof MountPoint.Mounting)
			{
				final MountPoint.Mounting mountingMountPoint = (MountPoint.Mounting) mountPoint;

				// mounting mountpoint must be reference a mounted node
				final INode mountedNode = mountingMountPoint.mountedNode;
				if (mountedNode != null)
				{
					// mounted mountpoint must be non null
					mountPoint = mountedNode.getMountPoint();
					if (mountPoint != null)
					{
						// mounted mountpoint must be mounted
						if (mountPoint instanceof MountPoint.Mounted)
						{
							final MountPoint.Mounted mountedMountPoint = (MountPoint.Mounted) mountPoint;

							// mounted mountpoint must reference mounting node
							if (mountedMountPoint.mountingNode == node)
							{
								return mountedNode;
							}
						}
					}
				}
			}

			// if mounted mountpoint: mounted -> mounting (up)
			else if (up && mountPoint instanceof MountPoint.Mounted)
			{
				final MountPoint.Mounted mountedMountPoint = (MountPoint.Mounted) mountPoint;

				// mounted mountpoint must be reference a mounting node
				final INode mountingNode = mountedMountPoint.mountingNode;
				if (mountingNode != null)
				{
					// mounting mountpoint must be non null
					mountPoint = mountingNode.getMountPoint();
					if (mountPoint != null)
					{
						// mounting mountpoint must be mounting
						if (mountPoint instanceof MountPoint.Mounting)
						{
							final MountPoint.Mounting mountingMountPoint = (MountPoint.Mounting) mountPoint;

							// mounting mountpoint must reference mounted node
							if (mountingMountPoint.mountedNode == node)
							{
								return mountingNode;
							}
						}
					}
				}
			}
		}
		return node;
	}
}
