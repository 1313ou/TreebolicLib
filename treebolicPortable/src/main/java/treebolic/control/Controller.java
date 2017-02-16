/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.control;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
import treebolic.view.View;

/**
 * Controller
 *
 * @author Bernard Bou
 */
public class Controller extends Commander
{
	/**
	 * Search Commands
	 */
	public static enum SearchCommand
	{
		SEARCH, CONTINUE, RESET
	}

	/**
	 * Match scope
	 */
	public static enum MatchScope
	{
		LABEL, CONTENT, LINK, ID;
	}

	/**
	 * Match mode
	 */
	public static enum MatchMode
	{
		EQUALS, STARTSWITH, INCLUDES
	}

	// connected

	/**
	 * Connected widget
	 */
	private Widget theWidget;

	/**
	 * Connected model
	 */
	private Model theModel;

	/**
	 * Connected view
	 */
	private View theView;

	/**
	 * Connected layout agent
	 */
	private AbstractLayerOut theLayerOut;

	// behaviour

	/**
	 * Label flag
	 */
	static public boolean LABEL_HAS_TAGS = true;

	/**
	 * Link status flag (use for debug purposes)
	 */
	static public boolean CONTENT_HAS_LINK = false;

	/**
	 * Mount status flag (use for debug purposes)
	 */
	static public boolean CONTENT_HAS_MOUNT = false;

	/**
	 * Verbose status flag (use for debug purposes)
	 */
	static public boolean CONTENT_VERBOSE = false; // weight...

	// action

	/**
	 * Event types
	 */
	static public enum Event
	{
		SELECT, HOVER, DRAG, LEAVEDRAG, MOVE, ROTATE, FOCUS, MOUNT, LINK, POPUP, ZOOM, SCALE
	}

	/**
	 * Match scopes
	 */
	static private final String[] theMatchScopeString = { IWidget.SEARCHSCOPELABEL, IWidget.SEARCHSCOPECONTENT, IWidget.SEARCHSCOPELINK, IWidget.SEARCHSCOPEID };

	/**
	 * Match modes
	 */
	static private final String[] theMatchModeString = { IWidget.SEARCHMODEEQUALS, IWidget.SEARCHMODESTARTSWITH, IWidget.SEARCHMODEINCLUDES };

	// C O N T R O L L E R

	/**
	 * Constructor
	 */
	public Controller()
	{
		super();
		this.theWidget = null;
		this.theModel = null;
		this.theView = null;
		this.theLayerOut = null;
	}

	// C O N N E C T

	/**
	 * Connect with widget
	 *
	 * @param thisWidget
	 *        widget
	 */
	public void connect(final Widget thisWidget)
	{
		this.theWidget = thisWidget;
	}

	/**
	 * Connect
	 *
	 * @param thisModel
	 *        model
	 */
	public void connect(final Model thisModel)
	{
		this.theModel = thisModel;
	}

	/**
	 * Connect with view
	 *
	 * @param thisView
	 *        view
	 */
	public void connect(final View thisView)
	{
		this.theView = thisView;
	}

	/**
	 * Connect with layout agent
	 *
	 * @param thisLayerOut
	 *        layerout
	 */
	public void connect(final AbstractLayerOut thisLayerOut)
	{
		this.theLayerOut = thisLayerOut;
	}

	// R E F E R E N C E . F O R . C O M M A N D E R

	/*
	 * (non-Javadoc)
	 * @see treebolic.control.Commander#getModel()
	 */
	@Override
	protected Model getModel()
	{
		return this.theModel;
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.control.Commander#getView()
	 */
	@Override
	protected View getView()
	{
		return this.theView;
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.control.Commander#getLayerOut()
	 */
	@Override
	protected AbstractLayerOut getLayerOut()
	{
		return this.theLayerOut;
	}

	// E V E N T S

	/**
	 * Handle events
	 *
	 * @param thisEventType
	 *        event type
	 * @param theseParameters
	 *        event-specific objects
	 */
	@SuppressWarnings("boxing")
	public void handle(final Event thisEventType, final Object... theseParameters)
	{
		// System.out.println(thisEventType);
		switch (thisEventType)
		{
		case MOVE:
		{
			// translate as per vector(start,end)
			this.theView.composeTranslate((Complex) theseParameters[0], (Complex) theseParameters[1]);
			break;
		}

		case ROTATE:
		{
			// rotate as per vector(start,end)
			this.theView.composeRotate((Complex) theseParameters[0], (Complex) theseParameters[1]);
			break;
		}

		case HOVER:
		{
			final INode thisNode = (INode) theseParameters[0];
			final String thisLink = thisNode.getLink();

			// cursor
			this.theView.setHoverCursor(thisLink != null && !thisLink.isEmpty());

			// tooltip
			putTip(thisNode);

			// status
			putStatus(thisNode);
			break;
		}

		case DRAG:
		{
			this.theView.enterDrag();
			break;
		}

		case LEAVEDRAG:
		{
			this.theView.leaveDrag();
			break;
		}

		case SELECT:
		{
			final INode thisNode = (INode) theseParameters[0];
			putStatus(thisNode);
			break;
		}

		case ZOOM:
		{
			// this.theView.applyNullTransform();
			// this.theView.setShift(0F, 0F, false, false);

			final float thisZoomFactor = (Float) theseParameters[0];
			final float thisZoomPivotX = (Float) theseParameters[1];
			final float thisZoomPivotY = (Float) theseParameters[2];
			this.theView.setZoomFactor(thisZoomFactor, thisZoomPivotX, thisZoomPivotY);
			break;
		}

		case SCALE:
		{
			// this.theView.applyNullTransform();
			// this.theView.setShift(0F, 0F, false, false);

			final float thisMapScale = (Float) theseParameters[0];
			final float thisFontScale = (Float) theseParameters[1];
			final float thisImageScale = (Float) theseParameters[2];
			this.theView.setScaleFactors(thisMapScale, thisFontScale, thisImageScale);
			break;
		}

		case FOCUS:
		{
			final INode thisNode = (INode) theseParameters[0];
			if (!this.theView.isAnimating())
			{
				this.theView.animateToCenter(thisNode, false);
			}
			break;
		}

		case MOUNT:
		{
			final INode thisNode = (INode) theseParameters[0];
			final MountPoint thisMountPoint = thisNode.getMountPoint();
			if (thisMountPoint != null)
			{
				// mount/umount
				if (thisMountPoint instanceof MountPoint.Mounted)
				{
					this.theWidget.umount(thisNode);
				}
				else
				{
					final MountPoint.Mounting thisMointingPoint = (MountPoint.Mounting) thisMountPoint;
					this.theWidget.mount(thisNode, decode(thisMointingPoint.theURL));
				}
			}
			break;
		}

		case LINK:
		{
			final INode thisNode = (INode) theseParameters[0];
			final String thisLink = thisNode.getLink();
			final String thisTarget = thisNode.getTarget();
			if (thisLink != null)
			{
				linkTo(thisLink, thisTarget);
			}
			break;
		}

		case POPUP:
		{
			final Point thisPoint = (Point) theseParameters[0];
			final INode thisNode = (INode) theseParameters[1];
			popup(thisPoint.x, thisPoint.y, thisNode);
			break;
		}

		default:
			System.err.println("Unhandled event: " + thisEventType.toString()); //$NON-NLS-1$
		}
	}

	// D I S P A T C H

	/**
	 * Dispatch action
	 *
	 * @param thisAction
	 *        action
	 * @param thisLink
	 *        url
	 * @param thisLinkTarget
	 *        url link target
	 * @param thisMatchTarget
	 *        match target
	 * @param thisMatchScope
	 *        match scope
	 * @param thisMatchMode
	 *        match mode
	 * @param thisNode
	 *        node
	 */
	public void dispatch(final Action thisAction, final String thisLink, final String thisLinkTarget, final String thisMatchTarget, final MatchScope thisMatchScope, final MatchMode thisMatchMode, final INode thisNode)
	{
		switch (thisAction)
		{
		case LINK:
			handle(Controller.Event.LINK, thisNode);
			break;

		case MOUNT:
			handle(Controller.Event.MOUNT, thisNode);
			break;

		// goto (expanded) link
		case GOTO:
			final String thisGotoTarget = getGotoTarget(thisLink, thisNode);
			if (thisGotoTarget != null)
			{
				linkTo(thisGotoTarget, thisLinkTarget);
			}
			break;

		// search (expanded) match target
		case SEARCH:
			// reset pending searches
			search(SearchCommand.RESET);

			// start new search
			final String thisSearchTarget = getSearchTarget(thisMatchTarget, thisNode);
			if (thisSearchTarget != null && thisMatchScope != null && thisMatchMode != null)
			{
				// status
				final StringBuffer thisMessage = new StringBuffer();
				thisMessage.append("<div class='searching'>") // //$NON-NLS-1$
						.append(String.format(Messages.getString("Controller.status_search_scope_mode_target"), //$NON-NLS-1$
								Controller.theMatchScopeString[thisMatchScope.ordinal()], //
								Controller.theMatchModeString[thisMatchMode.ordinal()], //
								thisSearchTarget)); //
				if (thisNode.getLabel() != null)
				{
					thisMessage.append(' ') //
							.append(String.format(Messages.getString("Controller.status_search_origin"), thisNode.getLabel())); // //$NON-NLS-1$
				}
				thisMessage.append("</div>").append('\n'); //$NON-NLS-1$
				this.theWidget.putStatus(Messages.getString("Controller.status_searching"), thisMessage.toString(), Statusbar.PutType.SEARCH); //$NON-NLS-1$

				// search: scope, mode, target, [start]
				final INode thisResult = search(SearchCommand.SEARCH, thisMatchScope, thisMatchMode, thisSearchTarget, thisNode);

				if (thisResult != null)
				{
					// status
					thisMessage.setLength(0);
					thisMessage.append("<div class='searching'>") // //$NON-NLS-1$
							// .append(thisSearchTarget) //
							// .append(' ') //
							.append(Messages.getString("Controller.status_result")) // //$NON-NLS-1$
							.append(' ') //
							.append("ID") // //$NON-NLS-1$
							.append(' ') //
							.append(thisResult.getId()) //
							.append("</div>"); //$NON-NLS-1$
					this.theWidget.putStatus(Messages.getString("Controller.status_found"), thisMessage.toString(), Statusbar.PutType.SEARCH); //$NON-NLS-1$
					putStatus(thisResult);
				}
				else
				{
					this.theWidget.putStatus(Messages.getString("Controller.status_notfound"), thisMessage.toString(), Statusbar.PutType.SEARCH); //$NON-NLS-1$
				}
			}
			break;

		case FOCUS:
			this.theView.animateToCenter(thisNode, false);
			break;

		case INFO:
			putInfo(thisNode);
			break;

		default:
			System.err.println("Unsupported dispatch action=" + thisAction + " link=" + thisLink + " context=" + thisNode); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	public String getGotoTarget(final String thisLink, final INode thisNode)
	{
		String thisExpandedLink = null;
		if (thisLink != null && !thisLink.isEmpty())
		{
			final String thisEdit = this.theWidget.getTarget();
			thisExpandedLink = PopupMenu.expandMacro(thisLink, thisEdit, thisNode);
			thisExpandedLink = Controller.decode(thisExpandedLink);
		}
		return thisExpandedLink;
	}

	public String getSearchTarget(final String thisMatchTarget, final INode thisNode)
	{
		final String thisEdit = this.theWidget.getTarget();
		if (thisMatchTarget == null || thisMatchTarget.isEmpty())
			return thisEdit == null || thisEdit.isEmpty() ? null : thisEdit;
		return PopupMenu.expandMacro(thisMatchTarget, thisEdit, thisNode);
	}

	// D I S P L A Y

	/**
	 * Display node in status
	 *
	 * @param thisNode
	 *        node
	 */
	private void putStatus(final INode thisNode)
	{
		final String thisLabel = Controller.getLabel(thisNode);
		final String thisContent = Controller.getContent(thisNode);
		this.theWidget.putStatus(thisLabel, thisContent, Statusbar.PutType.INFO);
	}

	/**
	 * Display node info
	 *
	 * @param thisNode
	 *        node
	 */
	private void putInfo(final INode thisNode)
	{
		final String thisLabel = Controller.getLabel(thisNode);
		final StringBuffer thisBuffer = new StringBuffer();
		thisBuffer.append(Controller.getContent(thisNode));
		thisBuffer.append(Commander.TOOLTIPHTML ? "<br/>" : "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		addLink(thisBuffer, thisNode);
		addMountPoint(thisBuffer, thisNode);
		this.theWidget.putInfo(thisLabel, thisBuffer.toString());
	}

	/**
	 * Get label string
	 *
	 * @param thisNode
	 *        node
	 * @return label string
	 */
	private static String getLabel(final INode thisNode)
	{
		// guard against null
		String thisLabel = thisNode.getLabel();
		if (thisLabel == null)
		{
			thisLabel = ""; //$NON-NLS-1$
		}

		// no tags
		if (!Controller.LABEL_HAS_TAGS)
		{
			return thisLabel;
		}

		// tags
		final StringBuffer thisBuffer = new StringBuffer();
		thisBuffer.append(thisLabel);
		final String thisLink = thisNode.getLink();
		if (thisLink != null)
		{
			thisBuffer.append(' ');
			// thisBuffer.append('L');
			thisBuffer.append("🌐"); //$NON-NLS-1$
		}
		final MountPoint thisMountPoint = thisNode.getMountPoint();
		if (thisMountPoint != null)
		{
			thisBuffer.append(' ');
			// thisBuffer.append('M');
			thisBuffer.append("🔗"); //$NON-NLS-1$
		}

		return thisBuffer.toString();
	}

	/**
	 * Get content string
	 *
	 * @param thisNode
	 *        node
	 * @return content string
	 */
	static private String getContent(final INode thisNode)
	{
		final StringBuffer thisBuffer = new StringBuffer();

		final String thisContent = thisNode.getContent();
		if (thisContent != null)
		{
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("<div class='content'>"); //$NON-NLS-1$
			}
			thisBuffer.append(thisContent);
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("</div>"); //$NON-NLS-1$
			}
		}

		// link
		if (Controller.CONTENT_HAS_LINK)
			addLink(thisBuffer, thisNode);

		// mountpoint
		if (Controller.CONTENT_HAS_MOUNT)
			addMountPoint(thisBuffer, thisNode);

		if (Controller.CONTENT_VERBOSE)
		{
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("<div='weight'>"); //$NON-NLS-1$
			}
			thisBuffer.append(" [weight="); //$NON-NLS-1$
			thisBuffer.append(thisNode.getWeight());
			thisBuffer.append(']');
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("</div>"); //$NON-NLS-1$
			}
		}
		return thisBuffer.toString();
	}

	/**
	 * Add link to string buffer
	 * 
	 * @param thisBuffer
	 *        string buffer
	 * @param thisNode
	 *        node
	 */
	static private void addLink(final StringBuffer thisBuffer, final INode thisNode)
	{
		final String thisLink = thisNode.getLink();
		if (thisLink != null && !thisLink.isEmpty())
		{
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("<div class='link'>"); //$NON-NLS-1$
			}
			thisBuffer.append('[');
			thisBuffer.append(Controller.decode(thisLink));
			thisBuffer.append(']');
			// {
			// // thisBuffer.append('L');
			// thisBuffer.append(Commander.tooltipHtml ? "&#x1F310;" : "🌐"); //$NON-NLS-1$ //$NON-NLS-2$
			// }
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("</div>"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Add mountpoint to string buffer
	 * 
	 * @param thisBuffer
	 *        string buffer
	 * @param thisNode
	 *        node
	 */
	static private void addMountPoint(final StringBuffer thisBuffer, final INode thisNode)
	{
		//
		final MountPoint thisMountPoint = thisNode.getMountPoint();
		if (thisMountPoint != null && thisMountPoint instanceof MountPoint.Mounting)
		{
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("<div class='mount'>"); //$NON-NLS-1$
			}
			final MountPoint.Mounting thisMountingPoint = (MountPoint.Mounting) thisMountPoint;
			thisBuffer.append('[');
			thisBuffer.append(Controller.decode(thisMountingPoint.theURL));
			thisBuffer.append(']');
			// {
			// // thisBuffer.append('M');
			// thisBuffer.append(Commander.tooltipHtml ? "&#x1F517;" : "🔗"); //$NON-NLS-1$ //$NON-NLS-2$
			// }
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("</div>"); //$NON-NLS-1$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.control.Commander#setHasTooltip(java.lang.Boolean)
	 */
	@Override
	public void setHasTooltip(final Boolean thisFlag)
	{
		super.setHasTooltip(thisFlag);
		this.theView.setToolTipText(null);
	}

	/**
	 * Display node in tooltip
	 *
	 * @param thisNode
	 *        node
	 */
	private void putTip(final INode thisNode)
	{
		if (!Commander.hasTooltip)
			return;

		final String thisLabel = thisNode.getLabel();
		final String thisContent = thisNode.getContent();
		if (thisLabel == null && (Commander.tooltipDisplaysContent ? thisContent == null : true))
			return;

		final StringBuffer thisBuffer = new StringBuffer();
		if (Commander.TOOLTIPHTML)
		{
			thisBuffer.append("<html>"); //$NON-NLS-1$
		}

		// label
		if (thisLabel != null && !thisLabel.isEmpty())
		{
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("<strong>"); //$NON-NLS-1$
			}
			thisBuffer.append(thisLabel);
			if (Commander.TOOLTIPHTML)
			{
				thisBuffer.append("</strong><br/>"); //$NON-NLS-1$
			}
		}

		// content
		if (Commander.tooltipDisplaysContent)
		{
			if (thisContent != null && !thisContent.isEmpty())
			{
				if (!Commander.TOOLTIPHTML)
				{
					final String[] theseLines = thisContent.split("\n"); //$NON-NLS-1$
					for (final String thisLine : theseLines)
					{
						final StringBuffer thisLineBuffer = new StringBuffer(thisLine);

						// force break after x characters
						for (int offset = Commander.TOOLTIPLINESPAN; offset < thisLineBuffer.length(); offset += Commander.TOOLTIPLINESPAN)
						{
							thisLineBuffer.insert(offset, "\n"); //$NON-NLS-1$
						}

						// append processed line with break
						thisBuffer.append(thisLineBuffer);
						thisBuffer.append('\n');
					}
				}
				else
				{
					thisBuffer.append(thisContent.length() <= Commander.TOOLTIPLINESPAN ? "<div>" : "<div width='" + Commander.TOOLTIPLINESPAN * 7 + "'>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					thisBuffer.append(thisContent);
					thisBuffer.append("</div>"); //$NON-NLS-1$
				}
			}
		}
		if (Commander.TOOLTIPHTML)
		{
			thisBuffer.append("</html>"); //$NON-NLS-1$
		}
		this.theView.setToolTipText(thisBuffer.toString());
	}

	// P O P U P

	public void popup(final int x, final int y, final INode thisNode)
	{
		final PopupMenu thisMenu = PopupMenu.makePopup(getView(), this, this.theWidget.getTarget(), thisNode, this.theModel.theSettings);
		thisMenu.popup(this.theView, x, y);
	}

	// S E A R C H

	/**
	 * Search
	 * 
	 * @param thisCommand
	 *        command
	 * @param theseParameters
	 *        parameters (scope, mode, target, [start])
	 */
	public INode search(final SearchCommand thisCommand, final Object... theseParameters)
	{
		System.out.print("Search: " + thisCommand); //$NON-NLS-1$
		for (Object thisParameter : theseParameters)
			System.out.print(" " + thisParameter); //$NON-NLS-1$
		System.out.println();

		switch (thisCommand)
		{
		case SEARCH:
		{
			if (theseParameters.length != 0)
			{
				final MatchScope thisScope = (MatchScope) theseParameters[0];
				final MatchMode thisMode = (MatchMode) theseParameters[1];
				final String thisTarget = (String) theseParameters[2];
				final INode thisStartNode = theseParameters.length > 3 ? (INode) theseParameters[3] : null;
				final INode thisResult = thisStartNode == null ? match(thisTarget, thisScope, thisMode) : match(thisTarget, thisScope, thisMode, thisStartNode);
				if (thisResult != null)
				{
					focus(thisResult);
					putStatus(thisResult);
					return thisResult;
				}
			}
		}
			break;

		case CONTINUE:
		{
			final INode thisResult = reMatch();
			if (thisResult != null)
			{
				focus(thisResult);
				putStatus(thisResult);
				return thisResult;
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

	private Traverser theTraverser = null;

	private Iterator<INode> theTraversedNodes = null;

	/**
	 * Match node against string
	 *
	 * @param thisTarget
	 *        string to match
	 * @param thisScope
	 *        scope
	 * @param thisMode
	 *        mode
	 * @param thisNode
	 *        start node
	 * @return next found node
	 */
	private INode match(final String thisTarget, final MatchScope thisScope, final MatchMode thisMode, final INode thisNode)
	{
		if (this.theTraversedNodes == null)
		{
			this.theTraverser = new Traverser(new NoCaseMatcher(thisTarget, thisScope, thisMode), thisNode);
			this.theTraversedNodes = this.theTraverser.iterator();
		}

		try
		{
			return this.theTraversedNodes.next();
		}
		catch (NoSuchElementException e)
		{
			return null;
		}
	}

	/**
	 * Match node against string
	 *
	 * @param thisTarget
	 *        string to match
	 * @param thisScope
	 *        scope (LABEL, CONTENT, LINK, ID)
	 * @param thisMode
	 *        mode (EQUALS, STARTSWITH, INCLUDES)
	 * @return next found node
	 */
	public INode match(final String thisTarget, final MatchScope thisScope, final MatchMode thisMode)
	{
		if (this.theModel == null || this.theModel.theTree == null)
			return null;
		return match(thisTarget, thisScope, thisMode, this.theModel.theTree.getRoot());
	}

	/**
	 * Continue matching
	 * 
	 * @return next found node
	 */
	private INode reMatch()
	{
		if (this.theTraversedNodes == null)
			return null;
		try
		{
			return this.theTraversedNodes.next();
		}
		catch (NoSuchElementException e)
		{
			return null;
		}
	}

	/**
	 * Clear matcher
	 */
	synchronized private void resetMatch()
	{
		if (this.theTraversedNodes == null)
			return;
		try
		{
			this.theTraverser.terminate();
		}
		catch (InterruptedException thisException)
		{
			//
		}
		this.theTraversedNodes = null;
		this.theTraverser = null;
	}

	// S E A R C H . F O R . N O D E . F R O M . L O C A T I O N

	/**
	 * Find node
	 *
	 * @param vx
	 *        at view x position
	 * @param vy
	 *        at view y position
	 * @return found node or null
	 */
	public INode findNode(final int vx, final int vy)
	{
		final Complex thisEuclideanLocation = this.theView.viewToUnitCircle(vx, vy);
		return Finder.findNodeAt(this.theModel.theTree.getRoot(), thisEuclideanLocation);
	}

	// F O C U S

	/**
	 * Focus node
	 *
	 * @param thisNodeId
	 *        node id to get focus
	 */
	public void focus(final String thisNodeId)
	{
		if (this.theModel == null || this.theModel.theTree == null)
			return;
		final INode thisNode = thisNodeId == null || thisNodeId.isEmpty() ? this.theModel.theTree.getRoot() : Finder.findNodeById(this.theModel.theTree.getRoot(), thisNodeId);
		focus(thisNode);
	}

	/**
	 * Focus node
	 *
	 * @param thisNode
	 *        node to get focus
	 */
	public void focus(final INode thisNode)
	{
		if (thisNode != null)
		{
			this.theView.animateToCenter(thisNode, false);
		}
	}

	// N A V I G A T I O N

	/**
	 * Decode encoded URL (for display)
	 *
	 * @param thisString
	 *        encode URL string
	 * @return decoded URL string
	 */
	private static String decode(final String thisString)
	{
		try
		{
			return URLDecoder.decode(thisString, "UTF8"); //$NON-NLS-1$
		}
		catch (final Exception e)
		{
			// System.err.println("Can't decode " + thisString + " - " + e);
		}
		return thisString;
	}

	/**
	 * Follow hypertext link
	 *
	 * @param thisHref
	 *        url string
	 * @param thisTarget
	 *        target frame
	 */
	public void linkTo(final String thisHref, final String thisTarget)
	{
		if (thisHref == null)
			return;

		// reference hook : find node with identifier
		if (thisHref.startsWith("#")) //$NON-NLS-1$
		{
			final String thisBookmark = thisHref.substring(1);
			final INode thisFocus = Finder.findNodeById(this.theModel.theTree.getRoot(), thisBookmark);
			if (thisFocus != null)
			{
				this.theView.animateToCenter(thisFocus, false);
				return;
			}
		}

		// link
		final String thisDecodedLink = Controller.decode(thisHref);

		// status
		this.theWidget.putStatus(Messages.getString("Controller.status_linkto"), "<div class='linking'>" + thisDecodedLink + "</div>", Statusbar.PutType.LINK); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		this.theWidget.getIContext().status(Messages.getString("Controller.status_linkto") + ' ' + thisDecodedLink); //$NON-NLS-1$

		// jump link: try system link first
		if (!this.theWidget.getIContext().linkTo(thisDecodedLink, thisTarget))
		{
			// fall back on reinit
			this.theWidget.reinit(thisDecodedLink);
		}
	}

	// S P A C E . C O N V E R S I O N

	/**
	 * Convert view space to unit circle
	 *
	 * @param thisPoint
	 *        view space coordinate
	 * @return unit circle coordinate
	 */
	public Complex viewToUnitCircle(final Point thisPoint)
	{
		return this.theView.viewToUnitCircle(thisPoint.x, thisPoint.y);
	}
}
