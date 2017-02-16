package org.treebolic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import treebolic.control.Controller;
import treebolic.glue.Color;
import treebolic.glue.Image;
import treebolic.model.IEdge;
import treebolic.model.INode;
import treebolic.model.MenuItem;
import treebolic.model.Model;
import treebolic.model.MountPoint;
import treebolic.model.MutableEdge;
import treebolic.model.MutableNode;
import treebolic.model.Settings;
import treebolic.model.Tree;

/**
 * Convenience class to parcel model
 */
public class ParcelableModel implements Parcelable
{
	/**
	 * Log tag
	 */
	static private final String TAG = "Model parcelization"; //$NON-NLS-1$

	/**
	 * Whether model is marshalled with standard Java mechanism or android parcelization
	 */
	static public boolean SERIALIZE = false;

	/**
	 * Image marshaling types
	 */
	private enum ImageMarshaling
	{
		IMAGE_SERIALIZE, IMAGE_ASBYTEARRAY, IMAGE_PARCEL
	}

	/**
	 * Image marshaling
	 */
	private static final ImageMarshaling IMAGEMETHOD = ImageMarshaling.IMAGE_ASBYTEARRAY;

	/**
	 * Wrapped model
	 */
	private final Model theModel;

	// C O N S T R U C T O R

	/**
	 * Null constructor
	 */
	public ParcelableModel()
	{
		this.theModel = null;
	}

	/**
	 * Constructor
	 */
	public ParcelableModel(final Model model)
	{
		this.theModel = model;
	}

	/**
	 * Constructor from parcel
	 *
	 * @param parcel
	 *            parcel to build from
	 */
	public ParcelableModel(final Parcel parcel)
	{
		if (ParcelableModel.SERIALIZE)
		{
			this.theModel = (Model) parcel.readSerializable();
		}
		else
		{
			this.theModel = ParcelableModel.readModel(parcel);
		}
	}

	/**
	 * Get wrapped model
	 *
	 * @return model
	 */
	public Model getModel()
	{
		return this.theModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents()
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(final Parcel parcel, final int flags)
	{
		if (ParcelableModel.SERIALIZE)
		{
			parcel.writeSerializable(getModel());
		}
		else
		{
			ParcelableModel.writeToParcel(parcel, getModel());
		}
	}

	/**
	 * Creator
	 */
	public static final Parcelable.Creator<ParcelableModel> CREATOR = new Parcelable.Creator<ParcelableModel>()
			{
		/*
		 * (non-Javadoc)
		 *
		 * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
		 */
		@Override
		public ParcelableModel createFromParcel(final Parcel parcel)
		{
			return new ParcelableModel(parcel);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.os.Parcelable.Creator#newArray(int)
		 */
		@Override
		public ParcelableModel[] newArray(final int size)
		{
			return new ParcelableModel[size];
		}
			};

			// W R I T E H E L P E R S

			// MODEL

			/**
			 * Write model to parcel through serialization
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param model
			 *            model
			 */
			public static void writeSerializableToParcel(final Parcel parcel, final Model model)
			{
				parcel.writeSerializable(model);
			}

			/**
			 * Write model to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param model
			 *            model
			 */
			public static void writeToParcel(final Parcel parcel, final Model model)
			{
				if (model == null)
				{
					parcel.writeInt(0);
					return;
				}
				parcel.writeInt(1);
				ParcelableModel.writeToParcel(parcel, model.theTree);
				ParcelableModel.writeToParcel(parcel, model.theSettings);
				ParcelableModel.writeToParcel(parcel, model.theImages);
				Log.d(ParcelableModel.TAG, "parcel write size=" + parcel.dataSize() + " pos=" + parcel.dataPosition()); //$NON-NLS-1$ //$NON-NLS-2$
			}

			/**
			 * Write node to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param tree
			 *            tree
			 * @param flags
			 *            flags
			 */
			private static void writeToParcel(final Parcel parcel, final Tree tree)
			{
				ParcelableModel.writeToParcel(parcel, tree.getRoot());
				ParcelableModel.writeEdgesToParcel(parcel, tree.getEdges());
			}

			// NODE

			/**
			 * Write node to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param node
			 *            node
			 */
			private static void writeToParcel(final Parcel parcel, final INode node)
			{
				// volatile data:
				// public double getWeight();
				// public double getChildrenWeight();
				// public double getMinWeight();
				// public Location getLocation();

				// id
				ParcelableModel.writeToParcel(parcel, node.getId());

				// parent
				final INode parent = node.getParent();
				ParcelableModel.writeToParcel(parcel, parent == null ? "" : parent.getId()); //$NON-NLS-1$

				// functional
				final String label = node.getLabel();
				final String edgeLabel = node.getEdgeLabel();
				final String content = node.getContent();
				final Color backColor = node.getBackColor();
				final Color foreColor = node.getForeColor();
				final Color edgeColor = node.getEdgeColor();
				final Integer edgeStyle = node.getEdgeStyle();
				final String link = node.getLink();
				final String target = node.getTarget();
				final String imageFile = node.getImageFile();
				final int imageIndex = node.getImageIndex();
				final String edgeImageFile = node.getEdgeImageFile();
				final int edgeImageIndex = node.getEdgeImageIndex();
				final MountPoint mountPoint = node.getMountPoint();

				ParcelableModel.writeToParcel(parcel, label);
				ParcelableModel.writeToParcel(parcel, edgeLabel);
				ParcelableModel.writeToParcel(parcel, content);
				ParcelableModel.writeToParcel(parcel, backColor);
				ParcelableModel.writeToParcel(parcel, foreColor);
				ParcelableModel.writeToParcel(parcel, edgeColor);
				ParcelableModel.writeToParcel(parcel, edgeStyle);
				ParcelableModel.writeToParcel(parcel, link);
				ParcelableModel.writeToParcel(parcel, target);
				ParcelableModel.writeToParcel(parcel, imageFile);
				ParcelableModel.writeToParcel(parcel, Integer.valueOf(imageIndex));
				ParcelableModel.writeToParcel(parcel, edgeImageFile);
				ParcelableModel.writeToParcel(parcel, Integer.valueOf(edgeImageIndex));
				ParcelableModel.writeToParcel(parcel, mountPoint);

				// child recursion
				final List<INode> children = node.getChildren();
				if (children == null)
				{
					parcel.writeInt(-1);
					return;
				}
				final int n = children.size();
				parcel.writeInt(n);
				for (int i = 0; i < n; i++)
				{
					ParcelableModel.writeToParcel(parcel, children.get(i));
				}
			}

			/**
			 * Write mount point to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param mountPoint
			 *            mount point
			 * @param flags
			 *            flags
			 */
			private static void writeToParcel(final Parcel parcel, final MountPoint mountPoint)
			{
				if (mountPoint == null || !(mountPoint instanceof MountPoint.Mounting))
				{
					parcel.writeString(""); //$NON-NLS-1$
					return;
				}
				final MountPoint.Mounting thisMountPoint = (MountPoint.Mounting) mountPoint;
				parcel.writeString(thisMountPoint.theURL);
				ParcelableModel.writeToParcel(parcel, thisMountPoint.now);
			}

			/**
			 * Write edge list to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param edges
			 *            edge list
			 */
			private static void writeEdgesToParcel(final Parcel parcel, final List<IEdge> edges)
			{
				if (edges == null)
				{
					parcel.writeInt(-1);
					return;
				}
				final int n = edges.size();
				parcel.writeInt(n);
				for (int i = 0; i < n; i++)
				{
					ParcelableModel.writeToParcel(parcel, edges.get(i));
				}
			}

			// EDGE

			/**
			 * Write edge to parcel
			 *
			 * @param parcel
			 *            parcel
			 * @param edge
			 *            edge
			 */
			private static void writeToParcel(final Parcel parcel, final IEdge edge)
			{
				// structural
				final INode from = edge.getFrom();
				final INode to = edge.getTo();
				ParcelableModel.writeToParcel(parcel, from.getId());
				ParcelableModel.writeToParcel(parcel, to.getId());

				// functional
				final String label = edge.getLabel();
				final Color color = edge.getColor();
				final Integer style = edge.getStyle();
				final String imageFile = edge.getImageFile();
				final int imageIndex = edge.getImageIndex();

				ParcelableModel.writeToParcel(parcel, label);
				ParcelableModel.writeToParcel(parcel, color);
				ParcelableModel.writeToParcel(parcel, style);
				ParcelableModel.writeToParcel(parcel, imageFile);
				ParcelableModel.writeToParcel(parcel, Integer.valueOf(imageIndex));
			}

			// SETTINGS

			/**
			 * Write settings to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param settings
			 *            settings
			 */
			private static void writeToParcel(final Parcel parcel, final Settings settings)
			{
				ParcelableModel.writeToParcel(parcel, settings.theBackColor);
				ParcelableModel.writeToParcel(parcel, settings.theForeColor);
				ParcelableModel.writeToParcel(parcel, settings.theBackgroundImageFile);
				ParcelableModel.writeToParcel(parcel, settings.theFontFace);
				ParcelableModel.writeToParcel(parcel, settings.theFontSize);
				ParcelableModel.writeToParcel(parcel, settings.theDownscaleFontsFlag);
				ParcelableModel.writeToParcel(parcel, settings.theFontDownscaler);
				ParcelableModel.writeToParcel(parcel, settings.theDownscaleImagesFlag);
				ParcelableModel.writeToParcel(parcel, settings.theImageDownscaler);
				ParcelableModel.writeToParcel(parcel, settings.theOrientation);
				ParcelableModel.writeToParcel(parcel, settings.theExpansion);
				ParcelableModel.writeToParcel(parcel, settings.theSweep);
				ParcelableModel.writeToParcel(parcel, settings.thePreserveOrientationFlag);
				ParcelableModel.writeToParcel(parcel, settings.theEdgesAsArcsFlag);
				ParcelableModel.writeToParcel(parcel, settings.theBorderFlag);
				ParcelableModel.writeToParcel(parcel, settings.theEllipsizeFlag);
				ParcelableModel.writeToParcel(parcel, settings.theHasToolbarFlag);
				ParcelableModel.writeToParcel(parcel, settings.theHasStatusbarFlag);
				ParcelableModel.writeToParcel(parcel, settings.theHasPopUpMenuFlag);
				ParcelableModel.writeToParcel(parcel, settings.theHasToolTipFlag);
				ParcelableModel.writeToParcel(parcel, settings.theToolTipDisplaysContentFlag);
				ParcelableModel.writeToParcel(parcel, settings.theFocusOnHoverFlag);
				ParcelableModel.writeToParcel(parcel, settings.theFocus);
				ParcelableModel.writeToParcel(parcel, settings.theXMoveTo);
				ParcelableModel.writeToParcel(parcel, settings.theYMoveTo);
				ParcelableModel.writeToParcel(parcel, settings.theXShift);
				ParcelableModel.writeToParcel(parcel, settings.theYShift);
				ParcelableModel.writeToParcel(parcel, settings.theNodeBackColor);
				ParcelableModel.writeToParcel(parcel, settings.theNodeForeColor);
				ParcelableModel.writeToParcel(parcel, settings.theDefaultNodeImage);
				ParcelableModel.writeToParcel(parcel, settings.theTreeEdgeColor);
				ParcelableModel.writeToParcel(parcel, settings.theTreeEdgeStyle);
				ParcelableModel.writeToParcel(parcel, settings.theDefaultTreeEdgeImage);
				ParcelableModel.writeToParcel(parcel, settings.theEdgeColor);
				ParcelableModel.writeToParcel(parcel, settings.theEdgeStyle);
				ParcelableModel.writeToParcel(parcel, settings.theDefaultEdgeImage);
				ParcelableModel.writeMenuToParcel(parcel, settings.theMenu);
			}

			/**
			 * Write menu to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param menu
			 *            menu
			 */
			private static void writeMenuToParcel(final Parcel parcel, final List<MenuItem> menuItems)
			{
				if (menuItems == null)
				{
					parcel.writeInt(-1);
					return;
				}
				final int n = menuItems.size();
				parcel.writeInt(n);
				for (int i = 0; i < n; i++)
				{
					ParcelableModel.writeToParcel(parcel, menuItems.get(i), 0);
				}
			}

			/**
			 * Write menu item to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param menuItem
			 *            menu item
			 * @param flags
			 *            flags
			 */
			private static void writeToParcel(final Parcel parcel, final MenuItem menuItem, final int flags)
			{
				ParcelableModel.writeToParcel(parcel, menuItem.theAction);
				ParcelableModel.writeToParcel(parcel, menuItem.theLabel);
				ParcelableModel.writeToParcel(parcel, menuItem.theLink);
				ParcelableModel.writeToParcel(parcel, menuItem.theTarget);
				ParcelableModel.writeToParcel(parcel, menuItem.theMatchTarget);
				ParcelableModel.writeToParcel(parcel, menuItem.theMatchMode);
				ParcelableModel.writeToParcel(parcel, menuItem.theMatchScope);
			}

			// IMAGES

			private static void writeToParcel(final Parcel parcel, final Image[] images)
			{
				if (images == null)
				{
					parcel.writeInt(-1);
					return;
				}
				final int n = images.length;
				parcel.writeInt(n);
				for (int i = 0; i < n; i++)
				{
					ParcelableModel.writeToParcel(parcel, images[i]);
				}
			}

			// SPECIFIC

			/**
			 * Write string to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param e
			 *            enum value
			 */
			private static void writeToParcel(final Parcel parcel, final Enum<?> e)
			{
				if (e == null)
				{
					parcel.writeInt(-1);
					return;
				}
				parcel.writeInt(e.ordinal());
			}

			/**
			 * Write string to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param s
			 *            string
			 */
			private static void writeToParcel(final Parcel parcel, final String s)
			{
				if (s == null)
				{
					parcel.writeString(""); //$NON-NLS-1$
					return;
				}
				parcel.writeString(s);
			}

			/**
			 * Write integer to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param n
			 *            integer
			 * @param flags
			 *            flags
			 */
			@SuppressWarnings("boxing")
			private static void writeToParcel(final Parcel parcel, final Integer n)
			{
				if (n == null)
				{
					parcel.writeInt(0);
					return;
				}
				parcel.writeInt(1);
				parcel.writeInt(n);
			}

			/**
			 * Write boolean to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param b
			 *            boolean
			 */
			@SuppressWarnings("boxing")
			private static void writeToParcel(final Parcel parcel, final Boolean b)
			{
				if (b == null)
				{
					parcel.writeInt(-1);
					return;
				}
				parcel.writeInt(b ? 1 : 0);
			}

			/**
			 * Write float to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param f
			 *            float
			 */
			@SuppressWarnings("boxing")
			private static void writeToParcel(final Parcel parcel, final Float f)
			{
				if (f == null)
				{
					parcel.writeInt(0);
					return;
				}
				parcel.writeInt(1);
				parcel.writeFloat(f);
			}

	// /**
	// * Write double to parcel
	// *
	// * @param parcel
	// * parcel to write to
	// * @param d
	// * double
	// */
	// @SuppressWarnings("boxing")
	// private static void writeToParcel(final Parcel parcel, final Double d)
	// {
	// if (d == null)
	// {
	// parcel.writeInt(0);
	// return;
	// }
	// parcel.writeInt(1);
	// parcel.writeDouble(d);
	// }

			/**
			 * Write float array to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param f
			 *            float array
			 */
			private static void writeToParcel(final Parcel parcel, final float[] f)
			{
				parcel.writeFloatArray(f);
			}

			/**
			 * Write image to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param image
			 *            image
			 * @param flags
			 *            flags
			 */
			private static void writeToParcel(final Parcel parcel, final Image image)
			{
				if (image == null || image.bitmap == null)
				{
					parcel.writeInt(0);
					return;
				}

				switch (ParcelableModel.IMAGEMETHOD)
				{
				case IMAGE_SERIALIZE:
					parcel.writeInt(1);
					parcel.writeSerializable(image);
					break;
				case IMAGE_ASBYTEARRAY:
					try
					{
						final byte[] imageByteArray = image.getByteArray();
						parcel.writeInt(1);
						parcel.writeByteArray(imageByteArray);
					}
					catch (final IOException e)
					{
						parcel.writeInt(0);
					}
					break;
				case IMAGE_PARCEL:
				default:
					parcel.writeInt(1);
					parcel.writeParcelable(image.bitmap, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
					break;
				}
			}

			/**
			 * Write color to parcel
			 *
			 * @param parcel
			 *            parcel to write to
			 * @param c
			 *            color
			 * @param flags
			 *            flags
			 */
			private static void writeToParcel(final Parcel parcel, final Color color)
			{
				if (color == null || color.isNull())
				{
					parcel.writeInt(0);
					return;
				}
				parcel.writeInt(1);
				parcel.writeInt(color.getRGB());
			}

			// R E A D H E L P E R S

			// MODEL

			/**
			 * Id to Node map
			 */
			static private Map<String, MutableNode> id2node = new HashMap<String, MutableNode>();

			/**
			 * Read model from parcel (through serialization)
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return model
			 */
			public static Model readSerializableModel(final Parcel parcel)
			{
				return (Model) parcel.readSerializable();
			}

			/**
			 * Read model from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return model
			 */
			public static Model readModel(final Parcel parcel)
			{
				Log.d(ParcelableModel.TAG, "parcel read size=" + parcel.dataSize() + " pos=" + parcel.dataPosition()); //$NON-NLS-1$ //$NON-NLS-2$
				final int isNotNull = parcel.readInt();
				if (isNotNull != 0)
				{
					final Tree tree = ParcelableModel.readTree(parcel);
					final Settings settings = ParcelableModel.readSettings(parcel);
					final Image[] images = ParcelableModel.readImages(parcel);
					return new Model(tree, settings, images);
				}
				return null;
			}

			/**
			 * Read tree from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return tree
			 */
			private static Tree readTree(final Parcel parcel)
			{
				ParcelableModel.id2node.clear();
				final INode root = ParcelableModel.readNode(parcel);
				final List<IEdge> edges = ParcelableModel.readEdges(parcel);
				return new Tree(root, edges);
			}

			// NODE

			/**
			 * Read node from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return node
			 */
			@SuppressWarnings("boxing")
			private static INode readNode(final Parcel parcel)
			{
				// structural
				final String id = ParcelableModel.readString(parcel);
				final String parentId = ParcelableModel.readString(parcel);
				final INode parent = parentId == null ? null : ParcelableModel.id2node.get(parentId);
				final MutableNode node = new MutableNode(parent, id);
				ParcelableModel.id2node.put(id, node);

				// functional
				final String label = ParcelableModel.readString(parcel);
				final String edgeLabel = ParcelableModel.readString(parcel);
				final String content = ParcelableModel.readString(parcel);
				final Color backColor = ParcelableModel.readColor(parcel);
				final Color foreColor = ParcelableModel.readColor(parcel);
				final Color edgeColor = ParcelableModel.readColor(parcel);
				final Integer edgeStyle = ParcelableModel.readInteger(parcel);
				final String link = ParcelableModel.readString(parcel);
				final String target = ParcelableModel.readString(parcel);
				final String imageFile = ParcelableModel.readString(parcel);
				final int imageIndex = ParcelableModel.readInteger(parcel);
				final String edgeImageFile = ParcelableModel.readString(parcel);
				final int edgeImageIndex = ParcelableModel.readInteger(parcel);
				final MountPoint mountPoint = ParcelableModel.readMountPoint(parcel);

				if (label != null)
				{
					node.setLabel(label);
				}
				if (edgeLabel != null)
				{
					node.setEdgeLabel(edgeLabel);
				}
				if (content != null)
				{
					node.setContent(content);
				}
				if (backColor != null)
				{
					node.setBackColor(backColor);
				}
				if (foreColor != null)
				{
					node.setForeColor(foreColor);
				}
				if (edgeColor != null)
				{
					node.setEdgeColor(edgeColor);
				}
				if (edgeStyle != null)
				{
					node.setEdgeStyle(edgeStyle);
				}
				if (link != null)
				{
					node.setLink(link);
				}
				if (target != null)
				{
					node.setTarget(target);
				}
				if (imageFile != null)
				{
					node.setImageFile(imageFile);
				}
				if (imageIndex != -1)
				{
					node.setImageIndex(imageIndex);
				}
				if (edgeImageFile != null)
				{
					node.setEdgeImageFile(edgeImageFile);
				}
				if (edgeImageIndex != -1)
				{
					node.setEdgeImageIndex(edgeImageIndex);
				}
				if (mountPoint != null)
				{
					node.setMountPoint(mountPoint);
				}

				// child recursion
				final int n = parcel.readInt();
				if (n == -1)
					return null;
				final List<INode> children = new ArrayList<INode>();
				for (int i = 0; i < n; i++)
				{
					final INode child = ParcelableModel.readNode(parcel);
					children.add(child);
				}
				return node;
			}

			/**
			 * Read mount point from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return mount point
			 */
			private static MountPoint readMountPoint(final Parcel parcel)
			{
				final String url = parcel.readString();
				if (!url.isEmpty())
				{
					final MountPoint.Mounting mountPoint = new MountPoint.Mounting();
					mountPoint.theURL = url;
					mountPoint.now = ParcelableModel.readBoolean(parcel);
					return mountPoint;
				}
				return null;
			}

			/**
			 * Read edge list from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return edge list
			 */
			private static List<IEdge> readEdges(final Parcel parcel)
			{
				final int n = parcel.readInt();
				if (n == -1)
					return null;
				final List<IEdge> edges = new ArrayList<IEdge>();
				for (int i = 0; i < n; i++)
				{
					final IEdge edge = ParcelableModel.readEdge(parcel);
					edges.add(edge);
				}
				return edges;
			}

			// EDGE

			/**
			 * Read edge from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return edge
			 */
			@SuppressWarnings("boxing")
			private static IEdge readEdge(final Parcel parcel)
			{
				// structural
				final String fromId = ParcelableModel.readString(parcel);
				final INode from = ParcelableModel.id2node.get(fromId);
				final String toId = ParcelableModel.readString(parcel);
				final INode to = ParcelableModel.id2node.get(toId);
				final MutableEdge edge = new MutableEdge(from, to);

				// functional
				final String label = ParcelableModel.readString(parcel);
				final Color color = ParcelableModel.readColor(parcel);
				final Integer style = ParcelableModel.readInteger(parcel);
				final String imageFile = ParcelableModel.readString(parcel);
				final int imageIndex = ParcelableModel.readInteger(parcel);

				if (!label.isEmpty())
				{
					edge.setLabel(label);
				}
				if (color != null)
				{
					edge.setColor(color);
				}
				if (style != null)
				{
					edge.setStyle(style);
				}
				if (!imageFile.isEmpty())
				{
					edge.setImageFile(imageFile);
				}
				if (!imageFile.isEmpty())
				{
					edge.setImageFile(imageFile);
				}
				if (imageIndex != -1)
				{
					edge.setImageIndex(imageIndex);
				}
				return edge;
			}

			// SETTINGS

			/**
			 * Read settings from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return settings
			 */
			private static Settings readSettings(final Parcel parcel)
			{
				final Settings settings = new Settings();
				settings.theBackColor = ParcelableModel.readColor(parcel);
				settings.theForeColor = ParcelableModel.readColor(parcel);
				settings.theBackgroundImageFile = ParcelableModel.readString(parcel);
				settings.theFontFace = ParcelableModel.readString(parcel);
				settings.theFontSize = ParcelableModel.readInteger(parcel);
				settings.theDownscaleFontsFlag = ParcelableModel.readBoolean(parcel);
				settings.theFontDownscaler = ParcelableModel.readFloats(parcel);
				settings.theDownscaleImagesFlag = ParcelableModel.readBoolean(parcel);
				settings.theImageDownscaler = ParcelableModel.readFloats(parcel);
				settings.theOrientation = ParcelableModel.readString(parcel);
				settings.theExpansion = ParcelableModel.readFloat(parcel);
				settings.theSweep = ParcelableModel.readFloat(parcel);
				settings.thePreserveOrientationFlag = ParcelableModel.readBoolean(parcel);
				settings.theEdgesAsArcsFlag = ParcelableModel.readBoolean(parcel);
				settings.theBorderFlag = ParcelableModel.readBoolean(parcel);
				settings.theEllipsizeFlag = ParcelableModel.readBoolean(parcel);
				settings.theHasToolbarFlag = ParcelableModel.readBoolean(parcel);
				settings.theHasStatusbarFlag = ParcelableModel.readBoolean(parcel);
				settings.theHasPopUpMenuFlag = ParcelableModel.readBoolean(parcel);
				settings.theHasToolTipFlag = ParcelableModel.readBoolean(parcel);
				settings.theToolTipDisplaysContentFlag = ParcelableModel.readBoolean(parcel);
				settings.theFocusOnHoverFlag = ParcelableModel.readBoolean(parcel);
				settings.theFocus = ParcelableModel.readString(parcel);
				settings.theXMoveTo = ParcelableModel.readFloat(parcel);
				settings.theYMoveTo = ParcelableModel.readFloat(parcel);
				settings.theXShift = ParcelableModel.readFloat(parcel);
				settings.theYShift = ParcelableModel.readFloat(parcel);
				settings.theNodeBackColor = ParcelableModel.readColor(parcel);
				settings.theNodeForeColor = ParcelableModel.readColor(parcel);
				settings.theDefaultNodeImage = ParcelableModel.readString(parcel);
				settings.theTreeEdgeColor = ParcelableModel.readColor(parcel);
				settings.theTreeEdgeStyle = ParcelableModel.readInteger(parcel);
				settings.theDefaultTreeEdgeImage = ParcelableModel.readString(parcel);
				settings.theEdgeColor = ParcelableModel.readColor(parcel);
				settings.theEdgeStyle = ParcelableModel.readInteger(parcel);
				settings.theDefaultEdgeImage = ParcelableModel.readString(parcel);
				settings.theMenu = ParcelableModel.readMenu(parcel);
				return settings;
			}

			/**
			 * Read menu from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return menu
			 */
			private static List<MenuItem> readMenu(final Parcel parcel)
			{
				final int n = parcel.readInt();
				if (n == -1)
					return null;
				final List<MenuItem> menu = new ArrayList<MenuItem>();
				for (int i = 0; i < n; i++)
				{
					final MenuItem menuItem = ParcelableModel.readMenuItem(parcel);
					menu.add(menuItem);
				}
				return menu;
			}

			/**
			 * Read menu item from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return menu item
			 */
			private static MenuItem readMenuItem(final Parcel parcel)
			{
				final MenuItem menuItem = new MenuItem();
				int ordinal;
				// action
				ordinal = parcel.readInt();
				menuItem.theAction = ordinal == -1 ? null : MenuItem.Action.values()[ordinal];
				// label
				menuItem.theLabel = ParcelableModel.readString(parcel);
				// link
				menuItem.theLink = ParcelableModel.readString(parcel);
				// target
				menuItem.theTarget = ParcelableModel.readString(parcel);
				// match target
				menuItem.theMatchTarget = ParcelableModel.readString(parcel);
				// match mode
				ordinal = parcel.readInt();
				menuItem.theMatchScope = ordinal == -1 ? null : Controller.MatchScope.values()[ordinal];
				// match scope
				ordinal = parcel.readInt();
				menuItem.theMatchMode = ordinal == -1 ? null : Controller.MatchMode.values()[ordinal];
				return menuItem;
			}

			// IMAGES

			private static Image[] readImages(final Parcel parcel)
			{
				final int n = parcel.readInt();
				if (n == -1)
					return null;
				final Image[] images = new Image[n];
				for (int i = 0; i < n; i++)
				{
					images[i] = ParcelableModel.readImage(parcel);
				}
				return images;
			}

			// SPECIFIC

			/**
			 * Read string from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return string
			 */
			private static String readString(final Parcel parcel)
			{
				final String s = parcel.readString();
				if (!s.isEmpty())
					return s;
				return null;
			}

			/**
			 * Read boolean from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return boolean
			 */
			@SuppressWarnings("boxing")
			private static Boolean readBoolean(final Parcel parcel)
			{
				final int value = parcel.readInt();
				switch (value)
				{
				case 1:
					return true;
				case 0:
					return false;
				default:
					break;
				}
				return null;
			}

			/**
			 * Read integer from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return integer
			 */
			private static Integer readInteger(final Parcel parcel)
			{
				final int isNotNull = parcel.readInt();
				if (isNotNull != 0)
				{
					final int n = parcel.readInt();
					return Integer.valueOf(n);
				}
				return null;
			}

	// /**
	// * Read double from parcel
	// *
	// * @param parcel
	// * parcel to read from
	// * @return double
	// */
	// private static Double readDouble(final Parcel parcel)
	// {
	// final int isNotNull = parcel.readInt();
	// if (isNotNull != 0)
	// {
	// final double d = parcel.readDouble();
	// return Double.valueOf(d);
	// }
	// return null;
	// }

			/**
			 * Read double from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return double
			 */
			private static Float readFloat(final Parcel parcel)
			{
				final int isNotNull = parcel.readInt();
				if (isNotNull != 0)
				{
					final float d = parcel.readFloat();
					return Float.valueOf(d);
				}
				return null;
			}

			/**
			 * Read floats from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return array of floats
			 */
			private static float[] readFloats(final Parcel parcel)
			{
				return parcel.createFloatArray();
			}

			/**
			 * Read image from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return image
			 */
			private static Image readImage(final Parcel parcel)
			{
				final int isNotNull = parcel.readInt();
				if (isNotNull != 0)
				{
					switch (ParcelableModel.IMAGEMETHOD)
					{
					case IMAGE_SERIALIZE:
						return (Image) parcel.readSerializable();
					case IMAGE_ASBYTEARRAY:
						final byte[] imageByteArray = parcel.createByteArray();
						try
						{
							final Image image = new Image(null);
							image.setFromByteArray(imageByteArray);
							return image;
						}
						catch (final Exception e)
						{
							//
						}
						break;
					case IMAGE_PARCEL:
					default:
						final Bitmap bitmap = Bitmap.CREATOR.createFromParcel(parcel);
						return new Image(bitmap);
					}

				}
				return null;
			}

			/**
			 * Read color from parcel
			 *
			 * @param parcel
			 *            parcel to read from
			 * @return color
			 */
			private static Color readColor(final Parcel parcel)
			{
				final int isNotNull = parcel.readInt();
				if (isNotNull != 0)
				{
					final int color = parcel.readInt();
					return new Color(color);
				}
				return null;
			}

			// T E S T

			public static boolean parcelTest(final Bundle bundle, final String key)
			{
				// model1
				final ParcelableModel model1 = bundle.getParcelable(key);

				// write bundle to parcel
				final Parcel parcel = Parcel.obtain();
				bundle.writeToParcel(parcel, 0);

				// extract bundle from parcel
				parcel.setDataPosition(0);
				final Bundle bundle2 = parcel.readBundle();
				bundle2.setClassLoader(Model.class.getClassLoader());

				// model2
				final ParcelableModel model2 = bundle2.getParcelable(key);
				parcel.recycle();

				return model1.getModel().equals(model2.getModel());
			}
}
