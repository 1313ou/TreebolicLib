package treebolic.control;

import treebolic.core.location.Complex;
import treebolic.core.math.Distance;
import treebolic.glue.EventListener;
import treebolic.glue.Point;
import treebolic.model.INode;

/**
 * Event listener adapter
 *
 * @author Bernard Bou
 */
public class EventListenerAdapter extends EventListener
{
	// D A T A

	/**
	 * Controller
	 */
	private final Controller theController;

	// D R A G A N D D R O P

	/**
	 * Drag mode enum
	 */
	private enum DragMode
	{
		TRANSLATE, ROTATE
	}

	/**
	 * Drag mode
	 */
	private DragMode theDragMode = DragMode.TRANSLATE;

	/**
	 * Whether pointer was dragged
	 */
	private boolean wasDragged = false;

	/**
	 * Whether pointer was moved
	 */
	private boolean wasMoved = false;

	// popup behaviour

	/**
	 * Whether it has popup
	 */
	public boolean hasPopUp = true;

	// translation
	/**
	 * Drag starting point
	 */
	private final Complex theDragStart = new Complex();

	/**
	 * Drag end point
	 */
	private Complex theDragEnd = new Complex();

	// selection

	/**
	 * Hot node
	 */
	private INode theHotNode = null;

	/**
	 * Linger node
	 */
	private INode theHoverNode = null;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param thisController controller
	 */
	public EventListenerAdapter(final Controller thisController)
	{
		this.theController = thisController;
	}

	// R E S E T

	/**
	 * Reset
	 */
	public void reset()
	{
		this.theHotNode = null;
		this.theHoverNode = null;
		this.wasDragged = false;
		this.wasMoved = false;
		this.theDragStart.reset();
		this.theDragEnd.reset();
	}

	// L I S T E N E R

	@Override
	public boolean onFocus(final int x, final int y)
	{
		final INode thisNode = this.theController.findNode(x, y);
		if (thisNode != null)
		{
			this.theController.handle(Controller.Event.FOCUS, thisNode);
			return true;
		}
		return false;
	}

	@Override
	public boolean onMenu(final int x, final int y)
	{
		final INode thisNode = this.theController.findNode(x, y);
		if (thisNode != null)
		{
			if (this.hasPopUp)
			{
				this.theController.handle(Controller.Event.POPUP, new Point(x, y), thisNode);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMount(final int x, final int y)
	{
		final INode thisNode = this.theController.findNode(x, y);
		if (thisNode != null)
		{
			this.theController.handle(Controller.Event.MOUNT, thisNode);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLink(final int x, final int y)
	{
		final INode thisNode = this.theController.findNode(x, y);
		if (thisNode != null)
		{
			this.theController.handle(Controller.Event.LINK, thisNode);
			return true;
		}
		return false;
	}

	@Override
	public boolean onDown(final int x, final int y, final boolean rotate)
	{
		final Complex thisDragStart = this.theController.viewToUnitCircle(new Point(x, y));
		this.theDragMode = rotate ? DragMode.ROTATE : DragMode.TRANSLATE;
		this.theDragStart.set(thisDragStart);
		this.theDragEnd.set(this.theDragStart);
		return true;
	}

	@Override
	public boolean onUp(final int x, final int y)
	{
		// drag
		if (this.wasDragged)
		{
			this.wasDragged = false;
			this.theController.handle(Controller.Event.LEAVEDRAG);
		}
		else
		{
			// selection
			final INode thisNode = this.theController.findNode(x, y);
			if (thisNode != null)
			{
				this.theController.handle(Controller.Event.SELECT, thisNode);
			}
		}
		return true;
	}

	@Override
	public boolean onDragged(final int x, final int y)
	{
		final double theMaxShiftSpan = .5;

		// avoid wide pointer shift which will lead to cross-circle xlations
		Complex thisDragEnd = this.theController.viewToUnitCircle(new Point(x, y));
		if (Distance.getEuclideanDistance(this.theDragStart, thisDragEnd) > theMaxShiftSpan)
		{
			// keep shift direction but limit span
			thisDragEnd = Complex.makeFromArgAbs(thisDragEnd.sub(this.theDragStart).arg(), theMaxShiftSpan).add(this.theDragStart);
		}
		this.theDragEnd = thisDragEnd;
		this.wasMoved = true;
		this.wasDragged = true;
		this.theController.handle(Controller.Event.DRAG);
		return true;
	}

	@Override
	public boolean onSelect(final int x, final int y)
	{
		// selection
		final INode thisNode = this.theController.findNode(x, y);
		if (thisNode != null)
		{
			this.theController.handle(Controller.Event.SELECT, thisNode);
		}
		return true;
	}

	// D R A G M O V E

	/**
	 * Rotate
	 */
	private synchronized void rotate()
	{
		this.theController.handle(Controller.Event.ROTATE, this.theDragStart, this.theDragEnd);
		this.theDragStart.set(this.theDragEnd); // eat
	}

	/**
	 * Move
	 */
	private synchronized void move()
	{
		this.theController.handle(Controller.Event.MOVE, this.theDragStart, this.theDragEnd);
		this.theDragStart.set(this.theDragEnd); // eat
	}

	/**
	 * Drag
	 *
	 * @return true if successful
	 */
	public boolean drag()
	{
		if (this.wasMoved)
		{
			// move it now
			switch (this.theDragMode)
			{
				case TRANSLATE:
					move();
					break;

				case ROTATE:
					rotate();
					break;

				default:
					break;
			}
			this.wasMoved = false;
			return true;
		}
		return false;
	}

	// H O V E R

	@Override
	public boolean onHover(final int x, final int y)
	{
		final INode thisNode = this.theController.findNode(x, y);
		final boolean again = this.theHotNode == thisNode;
		this.theHotNode = thisNode;
		if (thisNode != null && !again)
		{
			this.theController.handle(Controller.Event.HOVER, thisNode);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLongHover()
	{
		final boolean again = this.theHotNode == this.theHoverNode;
		this.theHoverNode = this.theHotNode;
		if (!again || this.theHoverNode == null || this.theHoverNode.getLocation().hyper.center.equals(Complex.ZERO))
		{
			return false;
		}
		this.theController.handle(Controller.Event.FOCUS, this.theHoverNode);
		return true;
	}

	/**
	 * Reset hot node
	 */
	public void resetHotNode()
	{
		this.theHotNode = null;
	}

	/**
	 * Get hot node
	 *
	 * @return hot node
	 */
	public INode getHotNode()
	{
		return this.theHotNode;
	}

	@SuppressWarnings("boxing")
	@Override
	public void onZoom(final float thisZoomFactor, final float thisZoomPivotX, final float thisZoomPivotY)
	{
		this.theController.handle(Controller.Event.ZOOM, thisZoomFactor, thisZoomPivotX, thisZoomPivotY);
	}

	@SuppressWarnings("boxing")
	@Override
	public void onScale(final float thisMapScaleFactor, final float thisFontScaleFactor, final float thisImageScaleFactor)
	{
		this.theController.handle(Controller.Event.SCALE, thisMapScaleFactor, thisFontScaleFactor, thisImageScaleFactor);
	}
}
