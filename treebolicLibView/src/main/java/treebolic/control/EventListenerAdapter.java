package treebolic.control;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private final Controller controller;

	// D R A G A N D D R O P

	/**
	 * Drag mode enum
	 */
	private enum DragMode
	{TRANSLATE, ROTATE}

	/**
	 * Drag mode
	 */
	@NonNull
	private DragMode dragMode = DragMode.TRANSLATE;

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
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private final Complex dragStart = new Complex();

	/**
	 * Drag end point
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private Complex dragEnd = new Complex();

	// selection

	/**
	 * Hot node
	 */
	@Nullable
	private INode hotNode = null;

	/**
	 * Linger node
	 */
	@Nullable
	private INode hoverNode = null;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param controller controller
	 */
	public EventListenerAdapter(final Controller controller)
	{
		this.controller = controller;
	}

	// R E S E T

	/**
	 * Reset
	 */
	public void reset()
	{
		this.hotNode = null;
		this.hoverNode = null;
		this.wasDragged = false;
		this.wasMoved = false;
		this.dragStart.reset();
		this.dragEnd.reset();
	}

	// L I S T E N E R

	@Override
	public boolean onFocus(final int x, final int y)
	{
		final INode node = this.controller.findNode(x, y);
		if (node != null)
		{
			this.controller.handle(Controller.Event.FOCUS, node);
			return true;
		}
		return false;
	}

	@Override
	public boolean onMenu(final int x, final int y)
	{
		final INode node = this.controller.findNode(x, y);
		if (node != null)
		{
			if (this.hasPopUp)
			{
				this.controller.handle(Controller.Event.POPUP, new Point(x, y), node);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMount(final int x, final int y)
	{
		final INode node = this.controller.findNode(x, y);
		if (node != null)
		{
			this.controller.handle(Controller.Event.MOUNT, node);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLink(final int x, final int y)
	{
		final INode node = this.controller.findNode(x, y);
		if (node != null)
		{
			this.controller.handle(Controller.Event.LINK, node);
			return true;
		}
		return false;
	}

	@SuppressWarnings("SameReturnValue")
	@Override
	public boolean onDown(final int x, final int y, final boolean rotate)
	{
		final Complex dragStart = this.controller.viewToUnitCircle(new Point(x, y));
		this.dragMode = rotate ? DragMode.ROTATE : DragMode.TRANSLATE;
		this.dragStart.set(dragStart);
		this.dragEnd.set(this.dragStart);
		return true;
	}

	@SuppressWarnings("SameReturnValue")
	@Override
	public boolean onUp(final int x, final int y)
	{
		// drag
		if (this.wasDragged)
		{
			this.wasDragged = false;
			this.controller.handle(Controller.Event.LEAVEDRAG);
		}
		else
		{
			// selection
			final INode node = this.controller.findNode(x, y);
			if (node != null)
			{
				this.controller.handle(Controller.Event.SELECT, node);
			}
		}
		return true;
	}

	@SuppressWarnings("SameReturnValue")
	@Override
	public boolean onDragged(final int x, final int y)
	{
		final double maxShiftSpan = .5;

		// avoid wide pointer shift which will lead to cross-circle xlations
		Complex dragEnd = this.controller.viewToUnitCircle(new Point(x, y));
		if (Distance.getEuclideanDistance(this.dragStart, dragEnd) > maxShiftSpan)
		{
			// keep shift direction but limit span
			dragEnd = Complex.makeFromArgAbs(dragEnd.sub(this.dragStart).arg(), maxShiftSpan).add(this.dragStart);
		}
		this.dragEnd = dragEnd;
		this.wasMoved = true;
		this.wasDragged = true;
		this.controller.handle(Controller.Event.DRAG);
		return true;
	}

	@SuppressWarnings("SameReturnValue")
	@Override
	public boolean onSelect(final int x, final int y)
	{
		// selection
		final INode node = this.controller.findNode(x, y);
		if (node != null)
		{
			this.controller.handle(Controller.Event.SELECT, node);
		}
		return true;
	}

	// D R A G M O V E

	/**
	 * Rotate
	 */
	private synchronized void rotate()
	{
		this.controller.handle(Controller.Event.ROTATE, this.dragStart, this.dragEnd);
		this.dragStart.set(this.dragEnd); // eat
	}

	/**
	 * Move
	 */
	private synchronized void move()
	{
		this.controller.handle(Controller.Event.MOVE, this.dragStart, this.dragEnd);
		this.dragStart.set(this.dragEnd); // eat
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
			switch (this.dragMode)
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
		final INode node = this.controller.findNode(x, y);
		final boolean again = this.hotNode == node;
		this.hotNode = node;
		if (node != null && !again)
		{
			this.controller.handle(Controller.Event.HOVER, node);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLongHover()
	{
		final boolean again = this.hotNode == this.hoverNode;
		this.hoverNode = this.hotNode;
		if (!again || this.hoverNode == null || this.hoverNode.getLocation().hyper.center.equals(Complex.ZERO))
		{
			return false;
		}
		this.controller.handle(Controller.Event.FOCUS, this.hoverNode);
		return true;
	}

	/**
	 * Reset hot node
	 */
	public void resetHotNode()
	{
		this.hotNode = null;
	}

	/**
	 * Get hot node
	 *
	 * @return hot node
	 */
	@Nullable
	public INode getHotNode()
	{
		return this.hotNode;
	}

	@SuppressWarnings("boxing")
	@Override
	public void onZoom(final float zoomFactor, final float zoomPivotX, final float zoomPivotY)
	{
		this.controller.handle(Controller.Event.ZOOM, zoomFactor, zoomPivotX, zoomPivotY);
	}

	@SuppressWarnings("boxing")
	@Override
	public void onScale(final float mapScaleFactor, final float fontScaleFactor, final float imageScaleFactor)
	{
		this.controller.handle(Controller.Event.SCALE, mapScaleFactor, fontScaleFactor, imageScaleFactor);
	}
}
