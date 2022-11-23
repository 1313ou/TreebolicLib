/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.control;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import treebolic.IWidget;
import treebolic.Messages;
import treebolic.Widget;
import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
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
	static public final boolean CONTENT_VERBOSE = false;

	/**
	 * Tweak relative IMG urls to absolute
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean ABSOLUTE_IMG_URLS = true;

	// action

	/**
	 * Event types
	 */
	public enum Event
	{
		// @formatter:off
		/** Select event */
		SELECT,
		/**
		 * Hover event
		 */
		HOVER,
		/** Drag event */
		DRAG,
		/** Leave drag event */
		LEAVEDRAG,
		/** Move event */
		MOVE,
		/** Rotate event */
		ROTATE,
		/** Focus event */
		FOCUS,
		/** Mount event */
		MOUNT,
		/** Link event */
		LINK,
		/** Popup event */
		POPUP,
		/** Zoom event */
		ZOOM,
		/** Scale event */
		SCALE
		// @formatter:on
	}

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
				@Nullable final String link = node.getLink();

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
				@Nullable final MountPoint mountPoint = node.getMountPoint();
				if (mountPoint != null)
				{
					// mount/umount
					assert this.widget != null;
					if (mountPoint instanceof MountPoint.Mounted)
					{
						this.widget.umount(node);
					}
					else
					{
						@NonNull final MountPoint.Mounting mountingPoint = (MountPoint.Mounting) mountPoint;
						this.widget.mount(node, decode(mountingPoint.url));
					}
				}
				break;
			}

			case LINK:
			{
				final INode node = (INode) parameters[0];
				@Nullable final String link = node.getLink();
				@Nullable final String target = node.getTarget();
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
				popup(point.x(), point.y(), node);
				break;
			}

			default:
				System.err.println("Unhandled event: " + eventType);
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
				handle(Event.LINK, node);
				break;

			case MOUNT:
				handle(Event.MOUNT, node);
				break;

			// goto (expanded) link
			case GOTO:
				@Nullable final String gotoTarget = getGotoTarget(link, node);
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
				@Nullable final String searchTarget = getSearchTarget(matchTarget, node);
				if (searchTarget != null && matchScope != null && matchMode != null)
				{
					// status
					@NonNull final StringBuilder message = new StringBuilder();
					message.append(String.format(Messages.getString("Controller.status_search_scope_mode_target"), //
							Controller.matchScopeString[matchScope.ordinal()], //
							Controller.matchModeString[matchMode.ordinal()], //
							searchTarget)); //
					if (node.getLabel() != null)
					{
						message.append(' ') //
								.append(String.format(Messages.getString("Controller.status_search_origin"), node.getLabel())); //
					}
					assert this.widget != null;
					this.widget.putStatus(Statusbar.PutType.SEARCH, (s) -> makeHtml("searching", s), Messages.getString("Controller.status_searching"), message.toString());

					// search: scope, mode, target, [start]
					@Nullable final INode result = search(SearchCommand.SEARCH, matchScope, matchMode, searchTarget, node);

					if (result != null)
					{
						// status
						message.setLength(0);
						message.append(Messages.getString("Controller.status_result")) //
								.append(' ') //
								.append("ID") //
								.append(' ') //
								.append(result.getId());
						this.widget.putStatus(Statusbar.PutType.SEARCH, (s) -> makeHtml("searching", s), Messages.getString("Controller.status_found"), message.toString());
						putStatus(result);
					}
					else
					{
						this.widget.putStatus(Statusbar.PutType.SEARCH, null, Messages.getString("Controller.status_notfound"), message.toString());
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

	/**
	 * Get goto target
	 *
	 * @param link link
	 * @param node node
	 * @return target
	 */
	@Nullable
	public String getGotoTarget(@Nullable final String link, @NonNull final INode node)
	{
		@Nullable String expandedLink = null;
		if (link != null && !link.isEmpty())
		{
			assert this.widget != null;
			@Nullable final String edit = this.widget.getTarget();
			expandedLink = PopupMenu.expandMacro(link, edit, node);
			expandedLink = Controller.decode(expandedLink);
		}
		return expandedLink;
	}

	/**
	 * Get search target
	 *
	 * @param matchTarget match target
	 * @param node        node
	 * @return target
	 */
	@Nullable
	public String getSearchTarget(@Nullable final String matchTarget, @NonNull final INode node)
	{
		assert this.widget != null;
		@Nullable final String edit = this.widget.getTarget();
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
		@NonNull final String label = Controller.getLabel(node);
		@NonNull final String[] content = Controller.getContent(node);
		assert this.widget != null;
		this.widget.putStatus(Statusbar.PutType.INFO, (s) -> makeHtmlContent(s, Commander.TOOLTIPHTML), label, content);
	}

	/**
	 * Display node info
	 *
	 * @param node node
	 */
	private void putInfo(@NonNull final INode node)
	{
		@NonNull final String label = Controller.getLabel(node);
		@NonNull final String[] content = Controller.getContent(node);
		assert this.widget != null;
		this.widget.putInfo(label, content);
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
		@Nullable String label = node.getLabel();
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
		@NonNull final StringBuilder sb = new StringBuilder();
		sb.append(label);
		@Nullable final String link = node.getLink();
		if (link != null)
		{
			sb.append(' ');
			// sb.append('L');
			sb.append("🌐"); // \uD83C\uDF10 // &#x1f310;
		}
		@Nullable final MountPoint mountPoint = node.getMountPoint();
		if (mountPoint != null)
		{
			sb.append(' ');
			// sb.append('M');
			sb.append("🔗"); // \uD83D\uDD17 // &#x1f517;
		}

		return sb.toString();
	}

	private static final int IDX_NODE_CONTENT = 0;

	private static final int IDX_NODE_LINK = 1;

	private static final int IDX_NODE_MOUNTPOINT = 2;

	private static final int IDX_NODE_WEIGHT = 3;

	/**
	 * Get content string
	 *
	 * @param node node
	 * @return content string
	 */
	@NonNull
	static private String[] getContent(@NonNull final INode node)
	{
		@NonNull final String[] contents = new String[4];
		contents[IDX_NODE_CONTENT] = node.getContent();

		// link
		if (Controller.CONTENT_HAS_LINK)
		{
			@Nullable final String link = node.getLink();
			if (link != null && !link.isEmpty())
			{
				contents[IDX_NODE_LINK] = '[' + Controller.decode(link) + ']'
				// + 'L'
				// + "&#x1F310;"
				// + "🌐"
				;
			}
		}

		// mountpoint
		if (Controller.CONTENT_HAS_MOUNT)
		{
			@Nullable final MountPoint mountPoint = node.getMountPoint();
			if (mountPoint instanceof MountPoint.Mounting)
			{
				@NonNull final MountPoint.Mounting mountingPoint = (MountPoint.Mounting) mountPoint;
				contents[IDX_NODE_MOUNTPOINT] = '[' + Controller.decode(mountingPoint.url) + ']'
				// + 'M'
				// + "&#x1F517;"
				// + "🔗"
				;
			}
		}

		// weight
		if (Controller.CONTENT_VERBOSE)
		{
			contents[IDX_NODE_WEIGHT] = "[weight=" + node.getWeight() + ']';
		}
		return contents;
	}

	/**
	 * Get content string
	 *
	 * @param contents strings
	 * @param div      embed in a div tag
	 * @return html content string
	 */
	@NonNull
	public String makeHtmlContent(@NonNull final CharSequence[] contents, boolean div)
	{
		@NonNull final StringBuilder sb = new StringBuilder();
		if (contents[IDX_NODE_CONTENT] != null)
		{
			sb.append(div ? makeHtml("content", contents[IDX_NODE_CONTENT]) : contents[IDX_NODE_CONTENT]);
		}

		// link
		if (contents[IDX_NODE_LINK] != null)
		{
			sb.append(div ? makeHtml("link", contents[IDX_NODE_LINK]) : contents[IDX_NODE_LINK]);
		}

		// mountpoint
		if (contents[IDX_NODE_MOUNTPOINT] != null)
		{
			sb.append(div ? makeHtml("mount", contents[IDX_NODE_MOUNTPOINT]) : contents[IDX_NODE_MOUNTPOINT]);
		}

		// weight
		if (contents[IDX_NODE_WEIGHT] != null)
		{
			sb.append(div ? makeHtml("weight", contents[IDX_NODE_WEIGHT]) : contents[IDX_NODE_WEIGHT]);
		}
		return sb.toString();
	}

	/**
	 * Make html
	 *
	 * @param divStyle div tag style
	 * @param contents contents
	 * @return content as HTML
	 */
	@NonNull
	public String makeHtml(String divStyle, @NonNull final CharSequence... contents)
	{
		@NonNull final StringBuilder sb = new StringBuilder();
		for (@Nullable CharSequence content : contents)
		{
			if (content != null && content.length() > 0)
			{
				if (ABSOLUTE_IMG_URLS)
				{
					content = absoluteImageSrc(content.toString());
				}

				sb.append("<div class='");
				sb.append(divStyle);
				sb.append("'>");
				sb.append(content);
				sb.append("</div>");
			}
		}
		return sb.toString();
	}

	private static final Pattern SCR_QUOTE1_PATTERN = Pattern.compile("(?<=<img[^>]{1,20})src='([^']+)'", Pattern.CASE_INSENSITIVE);

	private static final Pattern SRC_QUOTE2_PATTERN = Pattern.compile("(?<=<img[^>]{1,20})src=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);

	/**
	 * Replace src='rel_url' or src="rel_url" with absolute URLs in IMG tags
	 *
	 * @param content content
	 * @return changed content
	 */
	private String absoluteImageSrc(@NonNull String content)
	{
		// "<IMG src='image'>"
		// "<img src='http://somesite/path/image'>"
		// "<img src='file:///path/image'>"
		// "<img src='file://image'>"
		// "<IMG src=\"jar:file:///proj/parser/jar/parser.jar!/test.xml\""
		assert widget != null;
		@Nullable URL imageBase = widget.getIContext().getImagesBase();
		if (imageBase != null)
		{
			@NonNull Matcher m = SCR_QUOTE1_PATTERN.matcher(content);
			while (m.find())
			{
				String f1 = m.group(1); // first group = src content
				@Nullable URL url2 = makeUrlAbsolute(imageBase, f1);
				if (url2 != null)
				{
					content = m.replaceFirst("src='" + url2 + "'");
				}
			}

			@NonNull Matcher m2 = SRC_QUOTE2_PATTERN.matcher(content);
			while (m2.find())
			{
				String f1 = m2.group(1); // first group = src content
				@Nullable URL url2 = makeUrlAbsolute(imageBase, f1);
				if (url2 != null)
				{
					content = m2.replaceFirst("src='" + url2 + "'");
				}
			}
		}
		return content;
	}

	/**
	 * Make url absolute
	 *
	 * @param base    base url
	 * @param urlSpec url spec
	 * @return new url with base, null if unchanged and already absolute
	 */
	@Nullable
	private URL makeUrlAbsolute(URL base, @NonNull String urlSpec)
	{
		try
		{
			new URL(urlSpec);
			return null;
		}
		catch (MalformedURLException e)
		{
			try
			{
				return new URL(base, urlSpec);
			}
			catch (MalformedURLException ignored)
			{
			}
		}
		return null;
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

		@Nullable String label = node.getLabel();
		@Nullable String content = node.getContent();
		if (label == null && (!Commander.tooltipDisplaysContent || content == null))
		{
			return;
		}

		@NonNull final StringBuilder sb = new StringBuilder();
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
					@NonNull final String[] lines = content.split("\n");
					for (@NonNull final String line : lines)
					{
						@NonNull final StringBuilder lineSb = new StringBuilder(line);

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
					if (ABSOLUTE_IMG_URLS)
					{
						content = absoluteImageSrc(content);
					}
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

		@NonNull String tip = sb.toString();
		assert this.view != null;
		this.view.setToolTipText(tip);
	}

	// P O P U P

	/**
	 * Pop up
	 *
	 * @param x    x
	 * @param y    y
	 * @param node node
	 */
	@SuppressWarnings("WeakerAccess")
	public void popup(final int x, final int y, @NonNull final INode node)
	{
		assert this.view != null;
		assert this.widget != null;
		assert this.model != null;
		@NonNull final PopupMenu menu = PopupMenu.makePopup(view, this, this.widget.getTarget(), node, this.model.settings);
		menu.popup(this.view, x, y);
	}

	// S E A R C H

	/**
	 * Search
	 *
	 * @param command    command
	 * @param parameters parameters (scope, mode, target, [start])
	 * @return found node or null
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
					@Nullable final INode result = startNode == null ? match(target, scope, mode) : match(target, scope, mode, startNode);
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
				@Nullable final INode result = reMatch();
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
	@Nullable
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
	@Nullable
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
		@NonNull final Complex euclideanLocation = this.view.viewToUnitCircle(vx, vy);
		return Finder.findNodeAt(this.model.tree.getRoot(), euclideanLocation, this.view.getFinderDistanceEpsilonFactor());
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
		@Nullable final INode node = nodeId == null || nodeId.isEmpty() ? this.model.tree.getRoot() : Finder.findNodeById(this.model.tree.getRoot(), nodeId);
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
	@Nullable
	private static String decode(@Nullable final String str)
	{
		if (str == null)
		{
			return null;
		}
		try
		{
			return URLDecoder.decode(str, "UTF8");
		}
		catch (@NonNull final UnsupportedEncodingException ignored)
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
			@NonNull final String bookmark = href.substring(1);
			@Nullable final INode focus = Finder.findNodeById(this.model.tree.getRoot(), bookmark);
			if (focus != null)
			{
				assert this.view != null;
				this.view.animateToCenter(focus, false);
				return;
			}
		}

		// link
		@Nullable final String decodedLink = Controller.decode(href);

		// status
		assert this.widget != null;
		this.widget.putStatus(Statusbar.PutType.LINK, (s) -> makeHtml("linking", s), Messages.getString("Controller.status_linkto"), decodedLink);
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
		return this.view.viewToUnitCircle(point.x(), point.y());
	}
}
