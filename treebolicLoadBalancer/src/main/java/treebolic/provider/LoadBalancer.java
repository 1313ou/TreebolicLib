package treebolic.provider;

import java.util.ArrayList;
import java.util.List;

import treebolic.glue.Color;
import treebolic.glue.Image;
import treebolic.model.IEdge;
import treebolic.model.TreeMutableNode;

/**
 * Node load balancing (by creating intermediary node)
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class LoadBalancer
{
	// S E T T I N G S D A T A

	private static final String NL = "<br>";

	// P A R A M E T E R S

	/**
	 * Max children nodes at level 0, 1 .. n. Level is just above leaves. Last value i holds for level i to n.
	 */
	private final int[] limitNodesAtLevel;

	/**
	 * Truncation threshold
	 */
	private final int truncateLabelAt;

	/**
	 * Back color of intermediate node
	 */
	private final Color backColor;

	/**
	 * Fore color of intermediate node
	 */
	private final Color foreColor;

	/**
	 * Edge color of intermediate node
	 */
	private final Color edgeColor;

	/**
	 * Image index of intermediate node
	 */
	private final int imageIndex;

	/**
	 * Image of intermediate node
	 */
	private final Image image;

	// C O N S T R U C T O R

	/**
	 * @param limitNodesAtLevel0 limit number of nodes at given level
	 * @param truncateLabelAt0   truncate label threshhold
	 * @param backColor0         intermediate node back color
	 * @param foreColor0         intermediate node back color
	 * @param edgeColor0         intermediate node edge color
	 * @param imageIndex0        intermediate node image index
	 * @param image0             intermediate node image
	 */
	public LoadBalancer(final int[] limitNodesAtLevel0, final int truncateLabelAt0, final Color backColor0, final Color foreColor0, final Color edgeColor0, final int imageIndex0, final Image image0)
	{
		this.limitNodesAtLevel = limitNodesAtLevel0 != null ? limitNodesAtLevel0 : new int[]{10, 3};
		this.truncateLabelAt = truncateLabelAt0 > 0 ? truncateLabelAt0 : 3;
		this.backColor = backColor0;
		this.foreColor = foreColor0;
		this.edgeColor = edgeColor0;
		this.imageIndex = imageIndex0;
		this.image = image0;
	}

	// T R E E F A C T O R Y

	/**
	 * Attach to parent
	 *
	 * @param parent parent node
	 * @param nodes  children nodes
	 */
	static public void attachToParent(final TreeMutableNode parent, final List<TreeMutableNode> nodes)
	{
		if (nodes != null)
		{
			for (TreeMutableNode node : nodes)
			{
				node.addToParent(parent);
			}
		}
	}

	/**
	 * Build a list of tree parent nodes
	 *
	 * @param nodes children nodes
	 * @param level current level
	 * @return list of parent nodes
	 */
	private List<TreeMutableNode> buildHierarchy1(final List<TreeMutableNode> nodes, final int level)
	{
		final List<TreeMutableNode> roots = new ArrayList<>();

		int m = this.limitNodesAtLevel[level > this.limitNodesAtLevel.length - 1 ? this.limitNodesAtLevel.length - 1 : level];
		int z = nodes.size();

		// balance
		if (z % m == 1)
		{
			m--;
		}
		for (int i = 0; i < z; i = i + m)
		{
			final TreeMutableNode root = new TreeMutableNode(null, "root" + level + "-" + i);
			root.setLink("directory:"); //$NON-NLS-1$
			root.setBackColor(this.backColor);
			root.setForeColor(this.foreColor);
			root.setEdgeColor(this.edgeColor);
			root.setEdgeStyle(IEdge.SOLID | /* IEdge.FROMDEF | IEdge.FROMCIRCLE | */IEdge.TOTRIANGLE | IEdge.TOFILL | IEdge.TODEF);
			if (this.imageIndex > 0)
			{
				root.setImageIndex(this.imageIndex);
			}
			if (this.image != null)
			{
				root.setImage(this.image);
			}

			int b = Math.min(i + m, z);
			TreeMutableNode first = nodes.get(i);
			TreeMutableNode last = nodes.get(b - 1);

			final String startLabel = left(first.getTarget(), this.truncateLabelAt);
			root.setEdgeLabel(startLabel);
			final String rangeLabel = makeRangeLabel(first, last);
			root.setTarget(rangeLabel);
			// root.setLabel(rangeLabel);
			// root.setContent(/* "root" + level + "-" + i + NL + */ rangeLabel + NL + getNodeLabel(first) + NL + getNodeLabel(last));
			root.setContent(rangeLabel + /* NL + (b - i) + " elements" + */ NL + "<small>" + first.getTarget() + NL + last.getTarget() + "</small>");

			for (int k = i; k < b; k++)
			{
				final TreeMutableNode node = nodes.get(k);
				node.addToParent(root);
			}

			roots.add(root);
		}
		System.out.println("<" + level + " roots " + roots.size());
		return roots;
	}

	/**
	 * Recursive build hierarchy
	 *
	 * @param nodes nodes at level
	 * @param level level
	 * @return list of tree nodes
	 */
	public List<TreeMutableNode> buildHierarchy(final List<TreeMutableNode> nodes, final int level)
	{
		int m = this.limitNodesAtLevel[level > this.limitNodesAtLevel.length - 1 ? this.limitNodesAtLevel.length - 1 : level];
		System.out.println("level=" + level + "  m=" + m);
		if (nodes == null)
		{
			return null;
		}
		if (nodes.size() <= m)
		{
			return nodes;
		}

		List<TreeMutableNode> nodes2 = buildHierarchy1(nodes, level);
		return buildHierarchy(nodes2, level + 1);
	}

	// H E L P E R S

	/**
	 * Label factory of non-leave nodes
	 *
	 * @param first first child node
	 * @param last  lst child node
	 * @return makeRangeLabel of parent node
	 */
	private String makeRangeLabel(final TreeMutableNode first, final TreeMutableNode last)
	{
		String label1 = first.getTarget();
		String label2 = last.getTarget();
		if (label1 == null || label2 == null)
		{
			return null;
		}
		label1 = label1.toLowerCase();
		label2 = label2.toLowerCase();
		final StringBuilder sb = new StringBuilder();
		sb.append(left(label1, this.truncateLabelAt)) //
				.append('-');
		boolean isContact = !"directory:".equals(last.getLink());
		if (isContact)
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
	private String right(final String str, final int n)
	{
		if (str == null)
		{
			return null;
		}
		int l = str.length();
		if (l > n)
		{
			return str.substring(l - n).toLowerCase();
		}
		return str.toLowerCase();
	}

	/**
	 * Left truncation of a string
	 *
	 * @param str string
	 * @param n   n first characters
	 * @return left of string
	 */
	private String left(final String str, final int n)
	{
		if (str == null)
		{
			return null;
		}
		return str.length() > n ? str.substring(0, n).toLowerCase() : str.toLowerCase();
	}
}
