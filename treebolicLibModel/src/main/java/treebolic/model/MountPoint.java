package treebolic.model;

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
		public String theURL;

		/**
		 * Mount now (for editing only)
		 */
		public Boolean now;

		/**
		 * Saved allocated half wedge
		 */
		public double theHalfWedge;

		/**
		 * Saved orientation
		 */
		public double theOrientation;

		/**
		 * Mounted node reference
		 */
		public INode theMountedNode;

		/**
		 * Constructor
		 */
		@SuppressWarnings("synthetic-access")
		public Mounting()
		{
			super();
			this.theURL = null;
			this.theHalfWedge = 0.;
			this.theOrientation = 0.;
			this.theMountedNode = null;
		}
	}

	static public class Mounted extends MountPoint
	{
		private static final long serialVersionUID = 7749263337626673995L;

		/**
		 * Mounting node reference
		 */
		public INode theMountingNode;

		/**
		 * Mounted edges
		 */
		public List<IEdge> theMountedEdges;

		/**
		 * Constructor
		 */
		@SuppressWarnings("synthetic-access")
		public Mounted()
		{
			super();
			this.theMountingNode = null;
			this.theMountedEdges = null;
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
	 * @param thisINode
	 *        source node
	 * @param up
	 *        allow mounted -> mounting
	 * @param down
	 *        allow mounting -> mounted
	 * @return target node (or source if no mounting)
	 */
	public static INode follow(final INode thisINode, boolean up, boolean down)
	{
		MountPoint thisMountPoint = thisINode.getMountPoint();

		// mounted mountpoint must be non-null
		if (thisMountPoint != null)
		{
			// if mounting mountpoint: mounting -> mounted (down)
			if (down && thisMountPoint instanceof MountPoint.Mounting)
			{
				final MountPoint.Mounting thisMountingMountPoint = (MountPoint.Mounting) thisMountPoint;

				// mounting mountpoint must be reference a mounted node
				final INode thisMountedNode = thisMountingMountPoint.theMountedNode;
				if (thisMountedNode != null)
				{
					// mounted mountpoint must be non null
					thisMountPoint = thisMountedNode.getMountPoint();
					if (thisMountPoint != null)
					{
						// mounted mountpoint must be mounted
						if (thisMountPoint instanceof MountPoint.Mounted)
						{
							final MountPoint.Mounted thisMountedMountPoint = (MountPoint.Mounted) thisMountPoint;

							// mounted mountpoint must reference mounting node
							if (thisMountedMountPoint.theMountingNode == thisINode)
							{
								return thisMountedNode;
							}
						}
					}
				}
			}

			// if mounted mountpoint: mounted -> mounting (up)
			else if (up && thisMountPoint instanceof MountPoint.Mounted)
			{
				final MountPoint.Mounted thisMountedMountPoint = (MountPoint.Mounted) thisMountPoint;

				// mounted mountpoint must be reference a mounting node
				final INode thisMountingNode = thisMountedMountPoint.theMountingNode;
				if (thisMountingNode != null)
				{
					// mounting mountpoint must be non null
					thisMountPoint = thisMountingNode.getMountPoint();
					if (thisMountPoint != null)
					{
						// mounting mountpoint must be mounting
						if (thisMountPoint instanceof MountPoint.Mounting)
						{
							final MountPoint.Mounting thisMountingMountPoint = (MountPoint.Mounting) thisMountPoint;

							// mounting mountpoint must reference mounted node
							if (thisMountingMountPoint.theMountedNode == thisINode)
							{
								return thisMountingNode;
							}
						}
					}
				}
			}
		}
		return thisINode;
	}
}
