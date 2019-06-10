package treebolic.control;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.IWidget;
import treebolic.Messages;
import treebolic.Widget;
import treebolic.component.PopupMenu;
import treebolic.component.Statusbar;
import treebolic.control.Traverser.NoCaseMatcher;
import treebolic.core.AbstractLayerOut;
import treebolic.core.location.Complex;
import treebolic.glue.Point;
import treebolic.model.INode;
import treebolic.model.MenuItem.Action;
import treebolic.model.Model;
import treebolic.model.MountPoint;
import treebolic.model.Types.MatchMode;
import treebolic.model.Types.MatchScope;
import treebolic.model.Types.SearchCommand;
import treebolic.view.View;

/**
 * Controller
 *
 * @author Bernard Bou
 */
public class Controller extends Commander
{
	// connected

	/**
	 * Connected widget
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private Widget widget;

	/**
	 * Connected model
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private Model model;

	/**
	 * Connected view
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private View view;

	/**
	 * Connected layout agent
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private AbstractLayerOut layerOut;

	// behaviour

	/**
	 * Label flag
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean LABEL_HAS_TAGS = true;

	/**
	 * Link status flag (use for debug purposes)
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean CONTENT_HAS_LINK = false;

	/**
	 * Mount status flag (use for debug purposes)
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean CONTENT_HAS_MOUNT = false;

	/**
	 * Verbose status flag (use for debug purposes)
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean CONTENT_VERBOSE = false; // weight...

	// action

	/**
	 * Event types
	 */
	public enum Event
	{SELECT, HOVER, DRAG, LEAVEDRAG, MOVE, ROTATE, FOCUS, MOUNT, LINK, POPUP, ZOOM, SCALE}

	/**
	 * Match scopes
	 */
	static private final String[] matchScopeString = {IWidget.SEARCHSCOPELABEL, IWidget.SEARCHSCOPECONTENT, IWidget.SEARCHSCOPELINK, IWidget.SEARCHSCOPEID};

	/**
	 * Match modes
	 */
	static private final String[] matchModeString = {IWidget.SEARCHMODEEQUALS, IWidget.SEARCHMODESTARTSWITH, IWidget.SEARCHMODEINCLUDES};

	// C O N T R O L L E R

	/**
	 * Constructor
	 */
	public Controller()
	{
		super();
		this.widget = null;
		this.model = null;
		this.view = null;
		this.layerOut = null;
	}

	// C O N N E C T

	/**
	 * Connect with widget
	 *
	 * @param widget widget
	 */
	public void connect(final Widget widget)
	{
		this.widget = widget;
	}

	/**
	 * Connect
	 *
	 * @param model model
	 */
	public void connect(final Model model)
	{
		this.model = model;
	}

	/**
	 * Connect with view
	 *
	 * @param view view
	 */
	public void connect(final View view)
	{
		this.view = view;
	}

	/**
	 * Connect with layout agent
	 *
	 * @param layerOut layerout
	 */
	public void connect(final AbstractLayerOut layerOut)
	{
		this.layerOut = layerOut;
	}

	// R E F E R E N C E . F O R . C O M M A N D E R

	@Nullable
	@Override
	protected Model getModel()
	{
		return this.model;
	}

	@SuppressWarnings("WeakerAccess")
	@Nullable
	@Override
	protected View getView()
	{
		return this.view;
	}

	@Nullable
	@Override
	protected AbstractLayerOut getLayerOut()
	{
		return this.layerOut;
	}

	// E V E N T S

	/**
	 * Handle events
	 *
	 * @param eventType  event type
	 * @param parameters event-specific objects
	 */
	public void handle(@NonNull final Event eventType, final Object... parameters)
	{
		// System.out.println(eventType);
		assert this.view != null;
		switch (eventType)
		{
			case MOVE:
			{
				// translate as per vector(start,end)
				this.view.composeTranslate((Complex) parameters[0], (Complex) parameters[1]);
				break;
			}

			case ROTATE:
			{
				// rotate as per vector(start,end)
				this.view.composeRotate((Complex) parameters[0], (Complex) parameters[1]);
				break;
			}

			case HOVER:
			{
				final INode node = (INode) parameters[0];
				final String link = node.getLink();

				// cursor
				this.view.setHoverCursor(link != null && !link.isEmpty());

				// tooltip
				putTip(node);

				// status
				putStatus(node);
				break;
			}

			case DRAG:
			{
				this.view.enterDrag();
				break;
			}

			case LEAVEDRAG:
			{
				this.view.leaveDrag();
				break;
			}

			case SELECT:
			{
				final INode node = (INode) parameters[0];
				putStatus(node);
				break;
			}

			case ZOOM:
			{
				// this.view.applyNullTransform();
				// this.view.setShift(0F, 0F, false, false);

				final float zoomFactor = (Float) parameters[0];
				final float zoomPivotX = (Float) parameters[1];
				final float zoomPivotY = (Float) parameters[2];
				this.view.setZoomFactor(zoomFactor, zoomPivotX, zoomPivotY);
				break;
			}

			case SCALE:
			{
				// this.view.applyNullTransform();
				// this.view.setShift(0F, 0F, false, false);

				final float mapScale = (Float) parameters[0];
				final float fontScale = (Float) parameters[1];
				final float imageScale = (Float) parameters[2];
				this.view.setScaleFactors(mapScale, fontScale, imageScale);
				break;
			}

			case FOCUS:
			{
				final INode node = (INode) parameters[0];
				if (!this.view.isAnimating())
				{
					this.view.animateToCenter(node, false);
				}
				break;
			}

			case MOUNT:
			{
				final INode node = (INode) parameters[0];
				final MountPoint mountPoint = node.getMountPoint();
				if (mountPoint != null)
				{
					// mount/umount
					assert this.widget != null;
					//noinspection InstanceofConcreteClass
					if (mountPoint instanceof MountPoint.Mounted)
					{
						this.widget.umount(node);
					}
					else
					{
						final MountPoint.Mounting mountingPoint = (MountPoint.Mounting) mountPoint;
						this.widget.mount(node, decode(mountingPoint.url));
					}
				}
				break;
			}

			case LINK:
			{
				final INode node = (INode) parameters[0];
				final String link = node.getLink();
				final String target = node.getTarget();
				if (link != null)
				{
					linkTo(link, target);
				}
				break;
			}

			case POPUP:
			{
				final Point point = (Point) parameters[0];
				final INode node = (INode) parameters[1];
				popup(point.x, point.y, node);
				break;
			}

			default:
				System.err.println("Unhandled event: " + eventType.toString());
		}
	}

	// D I S P A T C H

	/**
	 * Dispatch action
	 *
	 * @param action      action
	 * @param link        url
	 * @param linkTarget  url link target
	 * @param matchTarget match target
	 * @param matchScope  match scope
	 * @param matchMode   match mode
	 * @param node        node
	 */
	public void dispatch(@NonNull final Action action, final String link, final String linkTarget, final String matchTarget, @Nullable final MatchScope matchScope, @Nullable final MatchMode matchMode, @NonNull final INode node)
	{
		switch (action)
		{
			case LINK:
				handle(Controller.Event.LINK, node);
				break;

			case MOUNT:
				handle(Controller.Event.MOUNT, node);
				break;

			// goto (expanded) link
			case GOTO:
				final String gotoTarget = getGotoTarget(link, node);
				if (gotoTarget != null)
				{
					linkTo(gotoTarget, linkTarget);
				}
				break;

			// search (expanded) match target
			case SEARCH:
				// reset pending searches
				search(SearchCommand.RESET);

				// start new search
				final String searchTarget = getSearchTarget(matchTarget, node);
				if (searchTarget != null && matchScope != null && matchMode != null)
				{
					// status
					final StringBuilder message = new StringBuilder();
					message.append("<div class='searching'>") //
							.append(String.format(Messages.getString("Controller.status_search_scope_mode_target"), Controller.matchScopeString[matchScope.ordinal()], //
									Controller.matchModeString[matchMode.ordinal()], //
									searchTarget)); //
					if (node.getLabel() != null)
					{
						message.append(' ') //
								.append(String.format(Messages.getString("Controller.status_search_origin"), node.getLabel())); //
					}
					message.append("</div>").append('\n');
					assert this.widget != null;
					this.widget.putStatus(Messages.getString("Controller.status_searching"), message.toString(), Statusbar.PutType.SEARCH);

					// search: scope, mode, target, [start]
					final INode result = search(SearchCommand.SEARCH, matchScope, matchMode, searchTarget, node);

					if (result != null)
					{
						// status
						message.setLength(0);
						message.append("<div class='searching'>") //
								// .append(searchTarget) //
								// .append(' ') //
								.append(Messages.getString("Controller.status_result")) //
								.append(' ') //
								.append("ID") //
								.append(' ') //
								.append(result.getId()) //
								.append("</div>");
						this.widget.putStatus(Messages.getString("Controller.status_found"), message.toString(), Statusbar.PutType.SEARCH);
						putStatus(result);
					}
					else
					{
						this.widget.putStatus(Messages.getString("Controller.status_notfound"), message.toString(), Statusbar.PutType.SEARCH);
					}
				}
				break;

			case FOCUS:
				assert this.view != null;
				this.view.animateToCenter(node, false);
				break;

			case INFO:
				putInfo(node);
				break;

			default:
				System.err.println("Unsupported dispatch action=" + action + " link=" + link + " context=" + node);
		}
	}

	@Nullable
	public String getGotoTarget(@Nullable final String link, @NonNull final INode node)
	{
		String expandedLink = null;
		if (link != null && !link.isEmpty())
		{
			assert this.widget != null;
			final String edit = this.widget.getTarget();
			expandedLink = PopupMenu.expandMacro(link, edit, node);
			expandedLink = Controller.decode(expandedLink);
		}
		return expandedLink;
	}

	@Nullable
	public String getSearchTarget(@Nullable final String matchTarget, @NonNull final INode node)
	{
		assert this.widget != null;
		final String edit = this.widget.getTarget();
		if (matchTarget == null || matchTarget.isEmpty())
		{
			return edit == null || edit.isEmpty() ? null : edit;
		}
		return PopupMenu.expandMacro(matchTarget, edit, node);
	}

	// D I S P L A Y

	/**
	 * Display node in status
	 *
	 * @param node node
	 */
	private void putStatus(@NonNull final INode node)
	{
		final String label = Controller.getLabel(node);
		final String content = Controller.getContent(node);
		assert this.widget != null;
		this.widget.putStatus(label, content, Statusbar.PutType.INFO);
	}

	/**
	 * Display node info
	 *
	 * @param node node
	 */
	private void putInfo(@NonNull final INode node)
	{
		final String label = Controller.getLabel(node);
		final StringBuffer sb = new StringBuffer();
		sb.append(Controller.getContent(node));
		sb.append(Commander.TOOLTIPHTML ? "<br/>" : "\n");
		addLink(sb, node);
		addMountPoint(sb, node);
		assert this.widget != null;
		this.widget.putInfo(label, sb.toString());
	}

	/**
	 * Get label string
	 *
	 * @param node node
	 * @return label string
	 */
	@NonNull
	private static String getLabel(@NonNull final INode node)
	{
		// guard against null
		String label = node.getLabel();
		if (label == null)
		{
			label = "";
		}

		// no tags
		if (!Controller.LABEL_HAS_TAGS)
		{
			return label;
		}

		// tags
		final StringBuilder sb = new StringBuilder();
		sb.append(label);
		final String link = node.getLink();
		if (link != null)
		{
			sb.append(' ');
			// sb.append('L');
			sb.append("🌐"); // \uD83C\uDF10 // &#x1f310;
		}
		final MountPoint mountPoint = node.getMountPoint();
		if (mountPoint != null)
		{
			sb.append(' ');
			// sb.append('M');
			sb.append("🔗"); // \uD83D\uDD17 // &#x1f517;
		}

		return sb.toString();
	}

	/**
	 * Get content string
	 *
	 * @param node node
	 * @return content string
	 */
	static private String getContent(@NonNull final INode node)
	{
		final StringBuffer sb = new StringBuffer();

		final String content = node.getContent();
		if (content != null)
		{
			if (Commander.TOOLTIPHTML)
			{
				sb.append("<div class='content'>");
			}
			sb.append(content);
			if (Commander.TOOLTIPHTML)
			{
				sb.append("</div>");
			}
		}

		// link
		if (Controller.CONTENT_HAS_LINK)
		{
			addLink(sb, node);
		}

		// mountpoint
		if (Controller.CONTENT_HAS_MOUNT)
		{
			addMountPoint(sb, node);
		}

		if (Controller.CONTENT_VERBOSE)
		{
			if (Commander.TOOLTIPHTML)
			{
				sb.append("<div class='weight'>");
			}
			sb.append(" [weight=");
			sb.append(node.getWeight());
			sb.append(']');
			if (Commander.TOOLTIPHTML)
			{
				sb.append("</div>");
			}
		}
		return sb.toString();
	}

	/**
	 * Add link to string buffer
	 *
	 * @param sb   string buffer
	 * @param node node
	 */
	static private void addLink(@NonNull final StringBuffer sb, @NonNull final INode node)
	{
		final String link = node.getLink();
		if (link != null && !link.isEmpty())
		{
			if (Commander.TOOLTIPHTML)
			{
				sb.append("<div class='link'>");
			}
			sb.append('[');
			sb.append(Controller.decode(link));
			sb.append(']');
			// {
			// // sb.append('L');
			// sb.append(Commander.tooltipHtml ? "&#x1F310;" : "🌐");
			// }
			if (Commander.TOOLTIPHTML)
			{
				sb.append("</div>");
			}
		}
	}

	/**
	 * Add mountpoint to string buffer
	 *
	 * @param sb   string buffer
	 * @param node node
	 */
	static private void addMountPoint(@NonNull final StringBuffer sb, @NonNull final INode node)
	{
		final MountPoint mountPoint = node.getMountPoint();
		//noinspection InstanceofConcreteClass
		if (mountPoint instanceof MountPoint.Mounting)
		{
			if (Commander.TOOLTIPHTML)
			{
				sb.append("<div class='mount'>");
			}
			final MountPoint.Mounting mountingPoint = (MountPoint.Mounting) mountPoint;
			sb.append('[');
			sb.append(Controller.decode(mountingPoint.url));
			sb.append(']');
			// {
			// // sb.append('M');
			// sb.append(Commander.tooltipHtml ? "&#x1F517;" : "🔗");
			// }
			if (Commander.TOOLTIPHTML)
			{
				sb.append("</div>");
			}
		}
	}

	@Override
	public void setHasTooltip(final Boolean flag)
	{
		super.setHasTooltip(flag);
		assert this.view != null;
		this.view.setToolTipText(null);
	}

	/**
	 * Display node in tooltip
	 *
	 * @param node node
	 */
	private void putTip(@NonNull final INode node)
	{
		if (!Commander.hasTooltip)
		{
			return;
		}

		String label = node.getLabel();
		String content = node.getContent();
		if (label == null && (!Commander.tooltipDisplaysContent || content == null))
		{
			return;
		}

		final StringBuilder sb = new StringBuilder();
		if (Commander.TOOLTIPHTML)
		{
			sb.append("<html>");
		}

		// label
		if (label != null && !label.isEmpty())
		{
			if (Commander.TOOLTIPHTML)
			{
				label = label.replaceAll("\n", "<br>");
				sb.append("<strong>");
			}
			sb.append(label);
			if (Commander.TOOLTIPHTML)
			{
				sb.append("</strong><br/>");
			}
		}

		// content
		if (Commander.tooltipDisplaysContent)
		{
			if (content != null && !content.isEmpty())
			{
				if (!Commander.TOOLTIPHTML)
				{
					final String[] lines = content.split("\n");
					for (final String line : lines)
					{
						final StringBuilder lineSb = new StringBuilder(line);

						// force break after x characters
						for (int offset = Commander.TOOLTIPLINESPAN; offset < lineSb.length(); offset += Commander.TOOLTIPLINESPAN)
						{
							lineSb.insert(offset, "\n");
						}

						// append processed line with break
						sb.append(lineSb);
						sb.append('\n');
					}
				}
				else
				{
					content = content.replaceAll("\n", "<br>");
					sb.append(content.length() <= Commander.TOOLTIPLINESPAN ? "<div>" : "<div width='" + Commander.TOOLTIPLINESPAN * 7 + "'>");
					sb.append(content);
					sb.append("</div>");
				}
			}
		}
		if (Commander.TOOLTIPHTML)
		{
			sb.append("</html>");
		}
		assert this.view != null;
		this.view.setToolTipText(sb.toString());
	}

	// P O P U P

	@SuppressWarnings("WeakerAccess")
	public void popup(final int x, final int y, @NonNull final INode node)
	{
		final View view = getView();
		assert view != null;
		assert this.widget != null;
		assert this.model != null;
		final PopupMenu menu = PopupMenu.makePopup(view, this, this.widget.getTarget(), node, this.model.settings);
		menu.popup(this.view, x, y);
	}

	// S E A R C H

	/**
	 * Search
	 *
	 * @param command    command
	 * @param parameters parameters (scope, mode, target, [start])
	 */
	@Nullable
	public INode search(@NonNull final SearchCommand command, @NonNull final Object... parameters)
	{
		System.out.print("Search: " + command);
		for (Object parameter : parameters)
		{
			System.out.print(" " + parameter);
		}
		System.out.println();

		switch (command)
		{
			case SEARCH:
			{
				if (parameters.length != 0)
				{
					final MatchScope scope = (MatchScope) parameters[0];
					final MatchMode mode = (MatchMode) parameters[1];
					final String target = (String) parameters[2];
					final INode startNode = parameters.length > 3 ? (INode) parameters[3] : null;
					final INode result = startNode == null ? match(target, scope, mode) : match(target, scope, mode, startNode);
					if (result != null)
					{
						focus(result);
						putStatus(result);
						return result;
					}
				}
			}
			break;

			case CONTINUE:
			{
				final INode result = reMatch();
				if (result != null)
				{
					focus(result);
					putStatus(result);
					return result;
				}
			}
			break;

			case RESET:
				resetMatch();
				break;

			default:
				break;
		}
		return null;
	}

	// M A T C H . N O D E

	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private Traverser traverser = null;

	@Nullable
	private Iterator<INode> traversedNodes = null;

	/**
	 * Match node against string
	 *
	 * @param target string to match
	 * @param scope  scope
	 * @param mode   mode
	 * @param node   start node
	 * @return next found node
	 */
	private INode match(@NonNull final String target, final MatchScope scope, final MatchMode mode, final INode node)
	{
		if (this.traversedNodes == null)
		{
			this.traverser = new Traverser(new NoCaseMatcher(target, scope, mode), node);
			this.traversedNodes = this.traverser.iterator();
		}

		try
		{
			return this.traversedNodes.next();
		}
		catch (NoSuchElementException ignored)
		{
			return null;
		}
	}

	/**
	 * Match node against string
	 *
	 * @param target string to match
	 * @param scope  scope (LABEL, CONTENT, LINK, ID)
	 * @param mode   mode (EQUALS, STARTSWITH, INCLUDES)
	 * @return next found node
	 */
	@Nullable
	public INode match(@NonNull final String target, final MatchScope scope, final MatchMode mode)
	{
		if (this.model == null || this.model.tree == null)
		{
			return null;
		}
		return match(target, scope, mode, this.model.tree.getRoot());
	}

	/**
	 * Continue matching
	 *
	 * @return next found node
	 */
	private INode reMatch()
	{
		if (this.traversedNodes == null)
		{
			return null;
		}
		try
		{
			return this.traversedNodes.next();
		}
		catch (NoSuchElementException ignored)
		{
			return null;
		}
	}

	/**
	 * Clear matcher
	 */
	synchronized private void resetMatch()
	{
		if (this.traversedNodes == null)
		{
			return;
		}
		try
		{
			assert this.traverser != null;
			this.traverser.terminate();
		}
		catch (InterruptedException ignored)
		{
			//
		}
		this.traversedNodes = null;
		this.traverser = null;
	}

	// S E A R C H . F O R . N O D E . F R O M . L O C A T I O N

	/**
	 * Find node
	 *
	 * @param vx at view x position
	 * @param vy at view y position
	 * @return found node or null
	 */
	@Nullable
	public INode findNode(final int vx, final int vy)
	{
		assert this.view != null;
		assert this.model != null;
		final Complex euclideanLocation = this.view.viewToUnitCircle(vx, vy);
		return Finder.findNodeAt(this.model.tree.getRoot(), euclideanLocation);
	}

	// F O C U S

	/**
	 * Focus node
	 *
	 * @param nodeId node id to get focus
	 */
	public void focus(@Nullable final String nodeId)
	{
		if (this.model == null || this.model.tree == null)
		{
			return;
		}
		final INode node = nodeId == null || nodeId.isEmpty() ? this.model.tree.getRoot() : Finder.findNodeById(this.model.tree.getRoot(), nodeId);
		focus(node);
	}

	/**
	 * Focus node
	 *
	 * @param node node to get focus
	 */
	@SuppressWarnings("WeakerAccess")
	public void focus(@Nullable final INode node)
	{
		if (node != null)
		{
			assert this.view != null;
			this.view.animateToCenter(node, false);
		}
	}

	// N A V I G A T I O N

	/**
	 * Decode encoded URL (for display)
	 *
	 * @param str encode URL string
	 * @return decoded URL string
	 */
	private static String decode(final String str)
	{
		try
		{
			return URLDecoder.decode(str, "UTF8");
		}
		catch (@NonNull final Exception ignored)
		{
			// System.err.println("Can't decode " + str + " - " + e);
		}
		return str;
	}

	/**
	 * Follow hypertext link
	 *
	 * @param href   url string
	 * @param target target frame
	 */
	public void linkTo(@Nullable final String href, final String target)
	{
		if (href == null)
		{
			return;
		}

		// reference hook : find node with identifier
		if (href.startsWith("#"))
		{
			assert this.model != null;
			final String bookmark = href.substring(1);
			final INode focus = Finder.findNodeById(this.model.tree.getRoot(), bookmark);
			if (focus != null)
			{
				assert this.view != null;
				this.view.animateToCenter(focus, false);
				return;
			}
		}

		// link
		final String decodedLink = Controller.decode(href);

		// status
		assert this.widget != null;
		this.widget.putStatus(Messages.getString("Controller.status_linkto"), "<div class='linking'>" + decodedLink + "</div>", Statusbar.PutType.LINK);
		this.widget.getIContext().status(Messages.getString("Controller.status_linkto") + ' ' + decodedLink);

		// jump link: try system link first
		if (!this.widget.getIContext().linkTo(decodedLink, target))
		{
			// fall back on reinit
			this.widget.reinit(decodedLink);
		}
	}

	// S P A C E . C O N V E R S I O N

	/**
	 * Convert view space to unit circle
	 *
	 * @param point view space coordinate
	 * @return unit circle coordinate
	 */
	@NonNull
	public Complex viewToUnitCircle(@NonNull final Point point)
	{
		assert this.view != null;
		return this.view.viewToUnitCircle(point.x, point.y);
	}
}
