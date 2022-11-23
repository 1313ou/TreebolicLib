/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import java.io.Serializable;
import java.util.List;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;

/**
 * Mount point
 *
 * @author Bernard Bou
 */
@SuppressWarnings("ClassReferencesSubclass")
public class MountPoint implements Serializable
{
	private static final long serialVersionUID = 8846293010235597970L;

	/**
	 * Mountpoint in the mounting side
	 */
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
		@Nullable
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

	/**
	 * Mountpoint in the mounted side
	 */
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
	@SuppressWarnings("EmptyMethod")
	private MountPoint()
	{
		// do nothing
	}

	/**
	 * Follow mounted-&gt;mounting or mounting-&gt;mounted
	 *
	 * @param node source node
	 * @param up   allow mounted -&gt; mounting
	 * @param down allow mounting -&gt; mounted
	 * @return target node (or source if no mounting)
	 */
	@NonNull
	public static INode follow(@NonNull final INode node, @SuppressWarnings("SameParameterValue") boolean up, @SuppressWarnings("SameParameterValue") boolean down)
	{
		@Nullable MountPoint mountPoint = node.getMountPoint();

		// mounted mountpoint must be non-null
		if (mountPoint != null)
		{
			// if mounting mountpoint: mounting -> mounted (down)
			//noinspection InstanceofConcreteClass
			if (down && mountPoint instanceof Mounting)
			{
				@NonNull final Mounting mountingMountPoint = (Mounting) mountPoint;

				// mounting mountpoint must be reference a mounted node
				@Nullable final INode mountedNode = mountingMountPoint.mountedNode;
				if (mountedNode != null)
				{
					// mounted mountpoint must be non-null
					mountPoint = mountedNode.getMountPoint();
					if (mountPoint != null)
					{
						// mounted mountpoint must be mounted
						//noinspection InstanceofConcreteClass
						if (mountPoint instanceof Mounted)
						{
							@NonNull final Mounted mountedMountPoint = (Mounted) mountPoint;

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
			else
				//noinspection InstanceofConcreteClass
				if (up && mountPoint instanceof Mounted)
				{
					@NonNull final Mounted mountedMountPoint = (Mounted) mountPoint;

					// mounted mountpoint must be reference a mounting node
					@Nullable final INode mountingNode = mountedMountPoint.mountingNode;
					if (mountingNode != null)
					{
						// mounting mountpoint must be non-null
						mountPoint = mountingNode.getMountPoint();
						if (mountPoint != null)
						{
							// mounting mountpoint must be mounting
							//noinspection InstanceofConcreteClass
							if (mountPoint instanceof Mounting)
							{
								@NonNull final Mounting mountingMountPoint = (Mounting) mountPoint;

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
