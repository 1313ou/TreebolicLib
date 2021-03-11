/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.glue.Color;
import treebolic.glue.Image;
import treebolic.model.INode;
import treebolic.model.TreeMutableNode;

/**
 * Node load balancing (by creating intermediary node)
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
@SuppressWarnings("WeakerAccess")
public class LoadBalancer
{
	// T A R G E T   T O   D I S C R I M I N A T E   D I R E C T O R I E S

	private static final String DIR_TARGET = "directory:";

	// S E T T I N G S D A T A

	private static final String NL = "<br>";

	// P A R A M E T E R S

	/**
	 * Max children nodes at level 0, 1 .. n. Level is just above leaves. Last value i holds for level i to n.
	 */
	@NonNull
	private final int[] limitNodesAtLevel;

	/**
	 * Truncation threshold
	 */
	private final int truncateLabelAt;

	/**
	 * Back color of intermediate node
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private Color backColor;

	/**
	 * Fore color of intermediate node
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private Color foreColor;

	/**
	 * Label of intermediate node
	 */
	private String label;

	/**
	 * Edge color of intermediate node
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private Color edgeColor;

	/**
	 * Edge style of intermediate node
	 */
	private int edgeStyle;

	/**
	 * Image index of intermediate node
	 */
	private int imageIndex = -1;

	/**
	 * Image of intermediate node
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private Image image;

	// C O N S T R U C T O R

	/**
	 * @param limitNodesAtLevel0 limit number of nodes at given level
	 * @param truncateLabelAt0   truncate label threshold
	 */
	public LoadBalancer(@Nullable final int[] limitNodesAtLevel0, final int truncateLabelAt0)
	{
		this.limitNodesAtLevel = limitNodesAtLevel0 != null ? limitNodesAtLevel0 : new int[]{10, 3};
		this.truncateLabelAt = truncateLabelAt0 > 0 ? truncateLabelAt0 : 3;
	}

	/**
	 * Set group node data
	 *
	 * @param label0      group node label
	 * @param backColor0  group node back color
	 * @param foreColor0  group node back color
	 * @param edgeColor0  group node edge color
	 * @param edgeStyle0  group node edge style
	 * @param imageIndex0 group node image index
	 * @param image0      group node image
	 */
	public void setGroupNode(final String label0, final Color backColor0, final Color foreColor0, final Color edgeColor0, final int edgeStyle0, final int imageIndex0, @SuppressWarnings("SameParameterValue") final Image image0)
	{
		this.label = label0;
		this.backColor = backColor0;
		this.foreColor = foreColor0;
		this.edgeColor = edgeColor0;
		this.edgeStyle = edgeStyle0;
		this.imageIndex = imageIndex0;
		this.image = image0;
	}

	// T R E E F A C T O R Y

	/**
	 * Build a list of tree parent nodes
	 *
	 * @param nodes      children nodes
	 * @param imageIndex image index (-1 is none and resolves to default)
	 * @param image      image (null is none and resolves to default)
	 * @param level      current level
	 * @return list of parent nodes
	 */
	@NonNull
	private List<INode> buildHierarchy1(@NonNull final List<? extends INode> nodes, final int imageIndex, @Nullable final Image image, final int level)
	{
		final List<INode> roots = new ArrayList<>();

		//noinspection ManualMinMaxCalculation
		int m0 = this.limitNodesAtLevel[level > this.limitNodesAtLevel.length - 1 ? this.limitNodesAtLevel.length - 1 : level];
		int z = nodes.size();

		// balance
		if (z % m0 == 1)
		{
			m0--;
		}
		@SuppressWarnings("UnusedAssignment") int m = m0; // actual length of segment
		for (int i = 0; i < z; i = i + m)
		{
			m = m0; // actual length of segment
			final String id = nodes.get(i).getId() + "~" + level + "-" + i; // composite id from first child node under root
			final TreeMutableNode root = new TreeMutableNode(null, id);
			root.setLink(null);
			root.setTarget(DIR_TARGET);
			root.setBackColor(this.backColor);
			root.setForeColor(this.foreColor);
			root.setLabel(this.label);

			// image
			if (imageIndex >= 0)
			{
				root.setImageIndex(imageIndex);
			}
			else if (this.imageIndex >= 0)
			{
				root.setImageIndex(this.imageIndex);
			}
			if (image != null)
			{
				root.setImage(image);
			}
			else if (this.image != null)
			{
				root.setImage(this.image);
			}

			int b = Math.min(i + m0, z);
			INode first = nodes.get(i);
			INode last = nodes.get(b - 1);

			// no adjacent tag pairs severed
			int b2 = Math.min(b + 1, z);
			if (b != b2)
			{
				final String tag = last.getTarget();
				final INode next = nodes.get(b2 - 1);
				final String nextTag = next.getTarget();
				if (tag != null && tag.equals(nextTag))
				{
					b = b2; // first of next segment = last index exclusive of current segment
					last = next; // last item of segment
					m++; // actual length of segment
				}
			}

			final String startLabel = left(first.getTarget(), this.truncateLabelAt);
			root.setEdgeLabel(startLabel);
			final String rangeLabel = makeRangeLabel(first, last);
			root.setTarget(rangeLabel);
			// root.setLabel(rangeLabel);
			// root.setContent(/* "root" + level + "-" + i + NL + */ rangeLabel + NL + getNodeLabel(first) + NL + getNodeLabel(last));
			root.setContent(rangeLabel + /* NL + (b - i) + " elements" + */ NL + "<small>" + first.getTarget() + NL + last.getTarget() + "</small>");

			for (int k = i; k < b; k++)
			{
				final INode node = nodes.get(k);
				node.setEdgeColor(this.edgeColor);
				node.setEdgeStyle(this.edgeStyle);

				/*
				 * if (!(node instanceof TreeMutableNode)) throw new IllegalArgumentException("Node is not tree mutable: " + node.getId()); final
				 * TreeMutableNode mutableNode = (TreeMutableNode) node; mutableNode.addToParent(root);
				 */
				root.addChild(node);
			}

			roots.add(root);
		}
		// System.out.println("<" + level + " roots " + roots.size());
		return roots;
	}

	/**
	 * Recursive build hierarchy
	 *
	 * @param nodes      nodes at level
	 * @param imageIndex image index (-1 is none and resolves to default)
	 * @param image      image (null is none and resolves to default)
	 * @param level      level
	 * @return list of tree nodes
	 */
	@Nullable
	@SuppressWarnings({"unchecked", "WeakerAccess"})
	public List<INode> buildHierarchy(@Nullable final List<? extends INode> nodes, final int imageIndex, final Image image, final int level)
	{
		//noinspection ManualMinMaxCalculation
		int m = this.limitNodesAtLevel[level > this.limitNodesAtLevel.length - 1 ? this.limitNodesAtLevel.length - 1 : level];
		// System.out.println("level=" + level + " m=" + m);
		if (nodes == null)
		{
			return null;
		}
		if (nodes.size() <= m)
		{
			return (List<INode>) nodes;
		}
		final List<INode> nodes2 = buildHierarchy1(nodes, imageIndex, image, level);
		return buildHierarchy(nodes2, imageIndex, image, level + 1);
	}

	/**
	 * Recursive build hierarchy
	 *
	 * @param nodes nodes at level
	 * @param level level
	 * @return list of tree nodes
	 */
	@Nullable
	public List<INode> buildHierarchy(final List<? extends INode> nodes, @SuppressWarnings("SameParameterValue") final int level)
	{
		return buildHierarchy(nodes, -1, null, level);
	}

	// H E L P E R S

	/**
	 * Label factory of non-leave nodes
	 *
	 * @param first first child node
	 * @param last  last child node
	 * @return makeRangeLabel of parent node
	 */
	@Nullable
	private String makeRangeLabel(@NonNull final INode first, @NonNull final INode last)
	{
		String label1 = first.getTarget();
		String label2 = last.getTarget();
		if (label1 == null || label2 == null)
		{
			return null;
		}
		label1 = label1.toLowerCase(Locale.getDefault());
		label2 = label2.toLowerCase(Locale.getDefault());
		final StringBuilder sb = new StringBuilder();
		sb.append(left(label1, this.truncateLabelAt)) //
				.append('-');
		boolean isItem = !DIR_TARGET.equals(last.getTarget());
		if (isItem)
		{
			sb.append(left(label2, this.truncateLabelAt));
		}
		else
		{
			sb.append(right(label2, this.truncateLabelAt));
		}
		return sb.toString();
	}

	/**
	 * Right truncation of a string
	 *
	 * @param str string
	 * @param n   n trailing characters
	 * @return right of string
	 */
	@Nullable
	private String right(@Nullable final String str, final int n)
	{
		if (str == null)
		{
			return null;
		}
		int l = str.length();
		if (l > n)
		{
			return str.substring(l - n).toLowerCase(Locale.getDefault());
		}
		return str.toLowerCase(Locale.getDefault());
	}

	/**
	 * Left truncation of a string
	 *
	 * @param str string
	 * @param n   n first characters
	 * @return left of string
	 */
	@Nullable
	private String left(@Nullable final String str, final int n)
	{
		if (str == null)
		{
			return null;
		}
		return str.length() > n ? str.substring(0, n).toLowerCase(Locale.getDefault()) : str.toLowerCase(Locale.getDefault());
	}
}
