package treebolic.view;

import treebolic.control.Controller;
import treebolic.control.EventListenerAdapter;
import treebolic.control.Finder;
import treebolic.core.AbstractLayerOut;
import treebolic.core.Transformer;
import treebolic.core.location.Complex;
import treebolic.core.transform.HyperRotation;
import treebolic.core.transform.HyperTransform;
import treebolic.glue.Animator;
import treebolic.glue.Graphics;
import treebolic.glue.GraphicsCache;
import treebolic.glue.Image;
import treebolic.glue.component.Surface;
import treebolic.model.INode;
import treebolic.model.Location;
import treebolic.model.Model;
import treebolic.model.Settings;

/**
 * View
 *
 * @author Bernard Bou
 */
@SuppressWarnings("ViewConstructor")
public class View extends Surface
{
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 715569943397018102L;

	// D A T A

	/**
	 * Data model
	 */
	private Model theModel;

	// A G E N T S

	/**
	 * Painter
	 */
	private final AbstractPainter thePainter;

	/**
	 * Transformer
	 */
	private final Transformer theTransformer;

	/**
	 * Animator
	 */
	private final Animator theAnimator;

	/**
	 * LayerOut
	 */
	private AbstractLayerOut theLayerOut;

	/**
	 * Event listener adapter
	 */
	private EventListenerAdapter theListener;

	// G R A P H I C S

	/**
	 * Full redraw flag
	 */
	private boolean invalidatePainterGraphics;

	/**
	 * View width
	 */
	private int theWidth;

	/**
	 * View height
	 */
	private int theHeight;

	/**
	 * Drawing cache
	 */
	private GraphicsCache theCache;

	// R E F E R E N C E . N O D E S

	/**
	 * Focus node
	 */
	private INode theFocusNode;

	// B E H A V I O U R

	/**
	 * Whether hovering on nodes triggers gaining focus
	 */
	private boolean focusOnHover;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param thisHandle Handle required for component creation (unused)
	 */
	public View(final Object thisHandle)
	{
		super(thisHandle);

		// painter
		this.thePainter = new Painter();
		this.thePainter.resetColors();

		// transformer
		this.theTransformer = new Transformer();

		// animator
		this.theAnimator = new Animator();

		// graphics
		this.theWidth = 0;
		this.theHeight = 0;
		this.theCache = null;
		this.invalidatePainterGraphics = true;

		// reference nodes
		this.theFocusNode = null;

		// behaviour flags
		this.focusOnHover = true;
	}

	// C O N N E C T

	/**
	 * Connect controller
	 *
	 * @param thisController controller
	 */
	public void connect(final Controller thisController)
	{
		// listener
		this.theListener = new EventListenerAdapter(thisController);
		addEventListener(this.theListener);
	}

	/**
	 * Set model
	 *
	 * @param thisModel data model
	 */
	public void connect(final Model thisModel)
	{
		this.theModel = thisModel;
	}

	/**
	 * Connect layout agent
	 *
	 * @param thisLayerOut layout agent
	 */
	public void connect(final AbstractLayerOut thisLayerOut)
	{
		this.theLayerOut = thisLayerOut;
	}

	// R E S E T

	/**
	 * Reset
	 */
	synchronized public void reset()
	{
		applyNullTransform();
		setXShift(0, false);
		setYShift(0, false);
		if (this.theListener != null)
		{
			this.theListener.reset();
		}
		repaint();
	}

	// P A I N T I N G

	@Override
	public void repaint()
	{
		if (this.theListener != null)
		{
			this.theListener.resetHotNode();
		}
		super.repaint();
	}

	@Override
	public void paint(final Graphics thisScreenGraphics)
	{
		// drag
		if (this.theListener.drag())
		{
			this.invalidatePainterGraphics = true;
		}

		// create a cached image the first time or when size changes
		final int thisWidth = getWidth();
		final int thisHeight = getHeight();
		if (this.theCache == null || thisWidth != this.theWidth || thisHeight != this.theHeight)
		{
			// invalidate old cache
			this.invalidatePainterGraphics = true;

			// size
			this.theWidth = thisWidth;
			this.theHeight = thisHeight;

			// cache
			this.theCache = new GraphicsCache(this, thisScreenGraphics, this.theWidth, this.theHeight);
		}

		// setup painter cached graphics and size
		if (this.invalidatePainterGraphics)
		{
			// cached graphics context
			final Graphics thisCacheGraphics = this.theCache.getGraphics();

			// tell painter
			this.thePainter.setup(thisCacheGraphics, this.theWidth, this.theHeight);
		}

		// paint background
		this.thePainter.paintBackground();

		// paint tree
		this.thePainter.paint(this.theModel.theTree.getRoot(), this.theModel.theTree.getEdges());

		// transfer cache on screen
		this.theCache.put(thisScreenGraphics);
	}

	/**
	 * Set default cursor
	 *
	 * @param linkCursor whether to use 'link' cursor
	 */
	public void setHoverCursor(final boolean linkCursor)
	{
		if (linkCursor)
		{
			setCursor(treebolic.glue.iface.component.Surface.HOTCURSOR);
		}
		else
		{
			setCursor(treebolic.glue.iface.component.Surface.DEFAULTCURSOR);
		}
	}

	// D R A G . N O T I F I C A T I O N

	/**
	 * Drag notification hook
	 */
	public void enterDrag()
	{
		this.thePainter.enterDrag();
		this.invalidatePainterGraphics = true;
		repaint();
	}

	/**
	 * Leave drag notification hook
	 */
	public void leaveDrag()
	{
		this.thePainter.leaveDrag();
		this.invalidatePainterGraphics = true;
		repaint();
	}

	// M O U N T . N O T I F I C A T I O N

	/**
	 * Mount notification hook
	 *
	 * @param thisMountedRoot mounted node
	 */
	public void mount(final INode thisMountedRoot)
	{
		this.theTransformer.transform(thisMountedRoot);
		repaint();
	}

	/**
	 * Unmount notification hook
	 */
	public void umount(final INode thisMountingRoot)
	{
		this.theTransformer.transform(thisMountingRoot);
		repaint();
	}

	// F O C U S

	/**
	 * Control whether hovering on node triggers gaining focus
	 *
	 * @param thisFlag whether hovering on node triggers gaining focus (null toggles value)
	 */
	@SuppressWarnings("boxing")
	public void setFocusOnHover(final Boolean thisFlag)
	{
		this.focusOnHover = thisFlag != null ? thisFlag : !this.focusOnHover;

		// implement
		setFireHover(this.focusOnHover);
	}

	// O R I E N T A T I O N

	/**
	 * Preserve orientation
	 *
	 * @param thisFlag whether transformations preserve orientation (null toggles value)
	 */
	@SuppressWarnings({"boxing", "WeakerAccess"})
	public void setPreserveOrientation(final Boolean thisFlag)
	{
		this.theTransformer.setPreserveOrientation(thisFlag != null ? thisFlag : !this.theTransformer.getPreserveOrientation());
	}

	// S H I F T

	/**
	 * Set x shift
	 *
	 * @param cx0  x shift
	 * @param xinc whether to add to current x shift
	 */
	synchronized public void setXShift(final float cx0, final boolean xinc)
	{
		float cx = cx0;
		if (xinc)
		{
			cx += this.thePainter.getXShift();
		}
		this.thePainter.setXShift(cx);
	}

	/**
	 * Set y shift
	 *
	 * @param cy0  y shift
	 * @param yinc whether to add to current y shift
	 */
	synchronized public void setYShift(final float cy0, final boolean yinc)
	{
		float cy = cy0;
		if (yinc)
		{
			cy += this.thePainter.getYShift();
		}
		this.thePainter.setYShift(cy);
	}

	// S C A L I N G . A N D . Z O O M I N G

	/**
	 * Set zoom factor
	 *
	 * @param thisZoomFactor zoom factor
	 * @param thisZoomPivotX zoom pivot X
	 * @param thisZoomPivotY zoom pivot Y
	 */
	public void setZoomFactor(final float thisZoomFactor, final float thisZoomPivotX, final float thisZoomPivotY)
	{
		this.thePainter.setZoomFactor(thisZoomFactor, thisZoomPivotX, thisZoomPivotY);
		repaint();
	}

	/**
	 * Set scale factors
	 *
	 * @param thisMapScaleFactor   map scale factor (how unit circle is mapped to view)
	 * @param thisFontScaleFactor  font scale factor
	 * @param thisImageScaleFactor image scale factor
	 */
	public void setScaleFactors(final float thisMapScaleFactor, final float thisFontScaleFactor, final float thisImageScaleFactor)
	{
		this.thePainter.setScaleFactors(thisMapScaleFactor, thisFontScaleFactor, thisImageScaleFactor);
		repaint();
	}

	// R E N D E R I N G

	/**
	 * Set linear/arc rendering of edges
	 *
	 * @param thisFlag whether edges are rendered as arcs (null toggles value)
	 */
	@SuppressWarnings("boxing")
	public void setArcEdges(@SuppressWarnings("SameParameterValue") final Boolean thisFlag)
	{
		this.thePainter.setArcEdges(thisFlag != null ? thisFlag : !this.thePainter.getArcEdges());
	}

	/**
	 * Set linear/arc rendering of edges
	 *
	 * @param thisFlag whether label texts are ellipsized (null toggles value)
	 */
	@SuppressWarnings("boxing")
	public void setEllipsize(final Boolean thisFlag)
	{
		this.thePainter.setEllipsize(thisFlag != null ? thisFlag : !this.thePainter.getEllipsize());
	}

	// P O P U P

	/**
	 * Control whether view has popup menu
	 *
	 * @param thisFlag whether view has popup menu (null toggles value)
	 */
	@SuppressWarnings({"boxing", "WeakerAccess"})
	public void setPopUpMenu(final Boolean thisFlag)
	{
		this.theListener.hasPopUp = thisFlag != null ? thisFlag : !this.theListener.hasPopUp;
	}

	// A P P L Y . S E T T I N G S

	/**
	 * Set view behaviour
	 *
	 * @param theseSettings settings
	 */
	@SuppressWarnings("boxing")
	public void apply(final Settings theseSettings)
	{
		// view settings
		if (theseSettings.theHasPopUpMenuFlag != null)
		{
			setPopUpMenu(theseSettings.theHasPopUpMenuFlag);
		}
		if (theseSettings.theFocusOnHoverFlag != null)
		{
			setFocusOnHover(theseSettings.theFocusOnHoverFlag);
		}
		if (theseSettings.thePreserveOrientationFlag != null)
		{
			setPreserveOrientation(theseSettings.thePreserveOrientationFlag);
		}
		if (theseSettings.theXShift != null)
		{
			setXShift(theseSettings.theXShift, false);
		}
		if (theseSettings.theYShift != null)
		{
			setYShift(theseSettings.theYShift, false);
		}

		// painter
		this.thePainter.setColors(theseSettings.theBackColor, theseSettings.theForeColor, theseSettings.theNodeBackColor, theseSettings.theNodeForeColor, theseSettings.theTreeEdgeColor, theseSettings.theEdgeColor);
		this.thePainter.setImageScaling(theseSettings.theDownscaleImagesFlag, theseSettings.theImageDownscaler);
		this.thePainter.setFont(theseSettings.theFontFace, theseSettings.theFontSize, theseSettings.theFontSizeFactor, theseSettings.theDownscaleFontsFlag, theseSettings.theFontDownscaler);
		this.thePainter.setBorder(theseSettings.theBorderFlag);
		this.thePainter.setEllipsize(theseSettings.theEllipsizeFlag);
		this.thePainter.setLabelMaxLines(theseSettings.theLabelMaxLines);
		this.thePainter.setLabelExtraLineFactor(theseSettings.theLabelExtraLineFactor);
		this.thePainter.setArcEdges(theseSettings.theEdgesAsArcsFlag);
		this.thePainter.setEdgeStyles(theseSettings.theTreeEdgeStyle, theseSettings.theEdgeStyle);
	}

	/**
	 * Set images
	 *
	 * @param thisBackgroundImage      background image
	 * @param thisDefaultNodeImage     default node image
	 * @param thisDefaultTreeEdgeImage default tree edge image
	 * @param thisDefaultEdgeImage     default edge image
	 */
	public void setImages(final Image thisBackgroundImage, final Image thisDefaultNodeImage, final Image thisDefaultTreeEdgeImage, final Image thisDefaultEdgeImage)
	{
		this.thePainter.setImages(thisBackgroundImage, thisDefaultNodeImage, thisDefaultTreeEdgeImage, thisDefaultEdgeImage);
	}

	// S E T . T R A N S F O R M

	/**
	 * Reset transform
	 */
	public void resetTransform()
	{
		this.theTransformer.setTransform(HyperTransform.NULLTRANSFORM);
	}

	// A P P L Y . T R A N S F O R M

	/**
	 * Compose this transform with current and apply
	 *
	 * @param thisTransform transform to compose with current transform
	 */
	@SuppressWarnings("WeakerAccess")
	public void applyComposedTransform(final HyperTransform thisTransform)
	{
		this.theTransformer.composeTransform(thisTransform);
		this.theTransformer.transform(this.theModel.theTree.getRoot());
	}

	/**
	 * Apply transform
	 *
	 * @param thisTransform transform
	 */
	public void applyTransform(final HyperTransform thisTransform)
	{
		this.theTransformer.setTransform(thisTransform);
		this.theTransformer.transform(this.theModel.theTree.getRoot());
	}

	/**
	 * Apply null transform
	 */
	public void applyNullTransform()
	{
		this.theTransformer.reset(this.theModel.theTree.getRoot());
	}

	/**
	 * Apply initial transform
	 */
	public void applyInitialTransform()
	{
		final Complex thisStart = new Complex(-1, -1).normalize().multiply(0.9);
		final HyperTransform thisTransform = this.theTransformer.makeTransform(thisStart, Complex.ZERO, this.theLayerOut.getOrientation());
		applyTransform(thisTransform);
	}

	// D E L T A . M O V E

	/**
	 * Compose delta translation
	 *
	 * @param thisStart unit circle delta vector start
	 * @param thisEnd   unit circle delta vector start
	 */
	public void composeTranslate(final Complex thisStart, final Complex thisEnd)
	{
		final HyperTransform thisTransform = this.theTransformer.makeTransform(thisStart, thisEnd, this.theLayerOut.getOrientation());
		applyComposedTransform(thisTransform);
	}

	/**
	 * Compose delta rotation
	 *
	 * @param thisStart unit circle delta vector start
	 * @param thisEnd   unit circle delta vector end
	 */
	public void composeRotate(final Complex thisStart, final Complex thisEnd)
	{
		final HyperRotation thisRotation = new HyperRotation(Complex.ONE);
		thisRotation.div(thisEnd, thisStart);
		thisRotation.normalize();
		applyComposedTransform(new HyperTransform(thisRotation));
	}

	// M O V E

	/**
	 * Translate tree according to vector
	 *
	 * @param thisStart unit circle vector start
	 * @param thisEnd   unit circle vector end
	 */
	private void translate(final Complex thisStart, final Complex thisEnd)
	{
		final HyperTransform thisTransform = this.theTransformer.makeTransform(thisStart, thisEnd, this.theLayerOut.getOrientation());
		applyTransform(thisTransform);
	}

	/**
	 * Translate node to point (unused, may be called by javascript)
	 *
	 * @param thisNode        node
	 * @param thisDestination unit circle destination location
	 */
	public void moveTo(final INode thisNode, final Complex thisDestination)
	{
		final Location thisLocation = thisNode.getLocation();
		translate(thisLocation.hyper.center0, thisDestination);
		this.theFocusNode = thisNode;
		repaint();
	}

	/**
	 * Translate node to unit circle center (unused, may be called by javascript)
	 *
	 * @param thisNode node
	 */
	public void moveToCenter(final INode thisNode)
	{
		final Location thisLocation = thisNode.getLocation();
		translate(thisLocation.hyper.center0, Complex.ZERO);
		this.theFocusNode = thisNode;
		repaint();
	}

	// A N I M A T E

	/**
	 * Animate to unit circle center
	 *
	 * @param thisNode node
	 * @param now      whether to start now
	 */
	public void animateToCenter(final INode thisNode, @SuppressWarnings("SameParameterValue") final boolean now)
	{
		final Location thisLocation = thisNode.getLocation();
		final Complex thisFrom = thisLocation.hyper.center;
		animate(thisFrom, Complex.ZERO, now);
		this.theFocusNode = thisNode;
	}

	/**
	 * Animate node to destination location
	 *
	 * @param thisNode        node
	 * @param thisDestination unit circle destination location
	 * @param now             whether to start now
	 */
	public void animateTo(final INode thisNode, final Complex thisDestination, @SuppressWarnings("SameParameterValue") final boolean now)
	{
		final Location thisLocation = thisNode.getLocation();
		final Complex thisFrom = thisLocation.hyper.center;
		animate(thisFrom, thisDestination, now);
		this.theFocusNode = thisNode;
	}

	/**
	 * Animate tree. Translate as per vector(start,end)
	 *
	 * @param thisFrom unit circle vector start
	 * @param thisTo   unit circle vector end
	 * @param now      whether to start now
	 */
	private synchronized void animate(final Complex thisFrom, final Complex thisTo, final boolean now)
	{
		final AnimationTransforms theseTransforms = AnimationTransforms.make(thisFrom, thisTo, this.theTransformer, this.theLayerOut.getOrientation(), 0);
		if (theseTransforms.theTransforms == null)
		{
			return;
		}
		final Animation thisAnimation = new Animation(theseTransforms, this);
		this.theAnimator.run(thisAnimation, thisAnimation.getSteps(), now ? 0 : Animation.ANIMATION_START_DELAY);
	}

	/**
	 * Whether animation is running
	 *
	 * @return whether animation is running
	 */
	public boolean isAnimating()
	{
		return this.theAnimator.isRunning();
	}

	// S E A R C H I N G

	/**
	 * Find node given view location
	 *
	 * @param vx view x location
	 * @param vy view y location
	 * @return node
	 */
	public INode findNode(final int vx, final int vy)
	{
		final Complex thisEuclideanLocation = this.thePainter.viewToUnitCircle(vx, vy, this.theWidth, this.theHeight);
		return Finder.findNodeAt(this.theModel.theTree.getRoot(), thisEuclideanLocation);
	}

	/**
	 * Get focused node
	 *
	 * @return focus noe (at center of unit circle)
	 */
	public INode getFocusNode()
	{
		return this.theFocusNode;
	}

	// S P A C E . C O N V E R S I O N

	/**
	 * Convert from view coordinates to unit circle euclidean coordinates
	 *
	 * @param vx view x-coordinate
	 * @param vy view x-coordinate
	 * @return unit circle euclidean coordinate
	 */
	public Complex viewToUnitCircle(final int vx, final int vy)
	{
		return this.thePainter.viewToUnitCircle(vx, vy, getWidth(), getHeight());
	}
}
