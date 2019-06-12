/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
public class View extends Surface
{
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 715569943397018102L;

	// D A T A

	/**
	 * Data model
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private Model model;

	// A G E N T S

	/**
	 * Painter
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final AbstractPainter painter;

	/**
	 * Transformer
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final Transformer transformer;

	/**
	 * Animator
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final Animator animator;

	/**
	 * LayerOut
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private AbstractLayerOut layerOut;

	/**
	 * Event listener adapter
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private EventListenerAdapter listenerAdapter;

	// G R A P H I C S

	/**
	 * Full redraw flag
	 */
	private boolean invalidatePainterGraphics;

	/**
	 * View width
	 */
	private int width;

	/**
	 * View height
	 */
	private int height;

	/**
	 * Drawing cache
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private GraphicsCache cache;

	// R E F E R E N C E . N O D E S

	/**
	 * Focus node
	 */
	@Nullable
	private INode focusNode;

	// B E H A V I O U R

	/**
	 * Whether hovering on nodes triggers gaining focus
	 */
	private boolean focusOnHover;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param handle handle required for component creation (unused)
	 */
	public View(final Object handle)
	{
		super(handle);

		// painter
		this.painter = new Painter();
		this.painter.resetColors();

		// transformer
		this.transformer = new Transformer();

		// animator
		this.animator = new Animator();

		// graphics
		this.width = 0;
		this.height = 0;
		this.cache = null;
		this.invalidatePainterGraphics = true;

		// reference nodes
		this.focusNode = null;

		// behaviour flags
		this.focusOnHover = true;
	}

	// C O N N E C T

	/**
	 * Connect controller
	 *
	 * @param controller controller
	 */
	public void connect(final Controller controller)
	{
		// listener
		this.listenerAdapter = new EventListenerAdapter(controller);
		addEventListener(this.listenerAdapter);
	}

	/**
	 * Set model
	 *
	 * @param model data model
	 */
	public void connect(final Model model)
	{
		this.model = model;
	}

	/**
	 * Connect layout agent
	 *
	 * @param layerOut layout agent
	 */
	public void connect(final AbstractLayerOut layerOut)
	{
		this.layerOut = layerOut;
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
		if (this.listenerAdapter != null)
		{
			this.listenerAdapter.reset();
		}
		repaint();
	}

	// P A I N T I N G

	@Override
	public void repaint()
	{
		if (this.listenerAdapter != null)
		{
			this.listenerAdapter.resetHotNode();
		}
		super.repaint();
	}

	@Override
	public void paint(@NonNull final Graphics screenGraphics)
	{
		// drag
		if (this.listenerAdapter.drag())
		{
			this.invalidatePainterGraphics = true;
		}

		// create a cached image the first time or when size changes
		final int width = getWidth();
		final int height = getHeight();
		if (this.cache == null || width != this.width || height != this.height)
		{
			// invalidate old cache
			this.invalidatePainterGraphics = true;

			// size
			this.width = width;
			this.height = height;

			// cache
			this.cache = new GraphicsCache(this, screenGraphics, this.width, this.height);
		}

		// setup painter cached graphics and size
		if (this.invalidatePainterGraphics)
		{
			// cached graphics context
			final Graphics cacheGraphics = this.cache.getGraphics();

			// tell painter
			this.painter.setup(cacheGraphics, this.width, this.height);
		}

		// paint background
		this.painter.paintBackground();

		// paint tree
		this.painter.paint(this.model.tree.getRoot(), this.model.tree.getEdges());

		// transfer cache on screen
		this.cache.put(screenGraphics);
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
		this.painter.enterDrag();
		this.invalidatePainterGraphics = true;
		repaint();
	}

	/**
	 * Leave drag notification hook
	 */
	public void leaveDrag()
	{
		this.painter.leaveDrag();
		this.invalidatePainterGraphics = true;
		repaint();
	}

	// M O U N T . N O T I F I C A T I O N

	/**
	 * Mount notification hook
	 *
	 * @param mountedRoot mounted node
	 */
	public void mount(@NonNull final INode mountedRoot)
	{
		this.transformer.transform(mountedRoot);
		repaint();
	}

	/**
	 * Unmount notification hook
	 */
	public void umount(@NonNull final INode mountingRoot)
	{
		this.transformer.transform(mountingRoot);
		repaint();
	}

	// F O C U S

	/**
	 * Control whether hovering on node triggers gaining focus
	 *
	 * @param flag whether hovering on node triggers gaining focus (null toggles value)
	 */
	public void setFocusOnHover(@Nullable final Boolean flag)
	{
		this.focusOnHover = flag != null ? flag : !this.focusOnHover;

		// implement
		setFireHover(this.focusOnHover);
	}

	// O R I E N T A T I O N

	/**
	 * Preserve orientation
	 *
	 * @param flag whether transformations preserve orientation (null toggles value)
	 */
	@SuppressWarnings({"WeakerAccess"})
	public void setPreserveOrientation(@Nullable final Boolean flag)
	{
		this.transformer.setPreserveOrientation(flag != null ? flag : !this.transformer.getPreserveOrientation());
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
			cx += this.painter.getXShift();
		}
		this.painter.setXShift(cx);
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
			cy += this.painter.getYShift();
		}
		this.painter.setYShift(cy);
	}

	// S C A L I N G . A N D . Z O O M I N G

	/**
	 * Set zoom factor
	 *
	 * @param zoomFactor zoom factor
	 * @param zoomPivotX zoom pivot X
	 * @param zoomPivotY zoom pivot Y
	 */
	public void setZoomFactor(final float zoomFactor, final float zoomPivotX, final float zoomPivotY)
	{
		this.painter.setZoomFactor(zoomFactor, zoomPivotX, zoomPivotY);
		repaint();
	}

	/**
	 * Set scale factors
	 *
	 * @param mapScaleFactor   map scale factor (how unit circle is mapped to view)
	 * @param fontScaleFactor  font scale factor
	 * @param imageScaleFactor image scale factor
	 */
	public void setScaleFactors(final float mapScaleFactor, final float fontScaleFactor, final float imageScaleFactor)
	{
		this.painter.setScaleFactors(mapScaleFactor, fontScaleFactor, imageScaleFactor);
		repaint();
	}

	// R E N D E R I N G

	/**
	 * Set linear/arc rendering of edges
	 *
	 * @param flag whether edges are rendered as arcs (null toggles value)
	 */
	@SuppressWarnings("boxing")
	public void setArcEdges(@Nullable @SuppressWarnings("SameParameterValue") final Boolean flag)
	{
		this.painter.setArcEdges(flag != null ? flag : !this.painter.getArcEdges());
	}

	/**
	 * Set linear/arc rendering of edges
	 *
	 * @param flag whether label texts are ellipsized (null toggles value)
	 */
	@SuppressWarnings("boxing")
	public void setEllipsize(@Nullable final Boolean flag)
	{
		this.painter.setEllipsize(flag != null ? flag : !this.painter.getEllipsize());
	}

	// P O P U P

	/**
	 * Control whether view has popup menu
	 *
	 * @param flag whether view has popup menu (null toggles value)
	 */
	@SuppressWarnings({"WeakerAccess"})
	public void setPopUpMenu(@Nullable final Boolean flag)
	{
		this.listenerAdapter.hasPopUp = flag != null ? flag : !this.listenerAdapter.hasPopUp;
	}

	// A P P L Y . S E T T I N G S

	/**
	 * Set view behaviour
	 *
	 * @param settings settings
	 */
	public void apply(@NonNull final Settings settings)
	{
		// view settings
		if (settings.hasPopUpMenuFlag != null)
		{
			setPopUpMenu(settings.hasPopUpMenuFlag);
		}
		if (settings.focusOnHoverFlag != null)
		{
			setFocusOnHover(settings.focusOnHoverFlag);
		}
		if (settings.preserveOrientationFlag != null)
		{
			setPreserveOrientation(settings.preserveOrientationFlag);
		}
		if (settings.xShift != null)
		{
			setXShift(settings.xShift, false);
		}
		if (settings.yShift != null)
		{
			setYShift(settings.yShift, false);
		}

		// painter
		this.painter.setColors(settings.backColor, settings.foreColor, settings.nodeBackColor, settings.nodeForeColor, settings.treeEdgeColor, settings.edgeColor);
		this.painter.setImageScaling(settings.downscaleImagesFlag, settings.imageDownscaler);
		this.painter.setFont(settings.fontFace, settings.fontSize, settings.fontSizeFactor, settings.downscaleFontsFlag, settings.fontDownscaler);
		this.painter.setBorder(settings.borderFlag);
		this.painter.setEllipsize(settings.ellipsizeFlag);
		this.painter.setLabelMaxLines(settings.labelMaxLines);
		this.painter.setLabelExtraLineFactor(settings.labelExtraLineFactor);
		this.painter.setArcEdges(settings.edgesAsArcsFlag);
		this.painter.setEdgeStyles(settings.treeEdgeStyle, settings.edgeStyle);
	}

	/**
	 * Set images
	 *
	 * @param backgroundImage      background image
	 * @param defaultNodeImage     default node image
	 * @param defaultTreeEdgeImage default tree edge image
	 * @param defaultEdgeImage     default edge image
	 */
	public void setImages(final Image backgroundImage, final Image defaultNodeImage, final Image defaultTreeEdgeImage, final Image defaultEdgeImage)
	{
		this.painter.setImages(backgroundImage, defaultNodeImage, defaultTreeEdgeImage, defaultEdgeImage);
	}

	// S E T . T R A N S F O R M

	/**
	 * Reset transform
	 */
	public void resetTransform()
	{
		this.transformer.setTransform(HyperTransform.NULLTRANSFORM);
	}

	// A P P L Y . T R A N S F O R M

	/**
	 * Compose this transform with current and apply
	 *
	 * @param transform transform to compose with current transform
	 */
	@SuppressWarnings("WeakerAccess")
	public void applyComposedTransform(@NonNull final HyperTransform transform)
	{
		this.transformer.composeTransform(transform);
		this.transformer.transform(this.model.tree.getRoot());
	}

	/**
	 * Apply transform
	 *
	 * @param transform transform
	 */
	public void applyTransform(final HyperTransform transform)
	{
		this.transformer.setTransform(transform);
		this.transformer.transform(this.model.tree.getRoot());
	}

	/**
	 * Apply null transform
	 */
	public void applyNullTransform()
	{
		this.transformer.reset(this.model.tree.getRoot());
	}

	/**
	 * Apply initial transform
	 */
	public void applyInitialTransform()
	{
		final Complex start = new Complex(-1, -1).normalize().multiply(0.9);
		final HyperTransform transform = this.transformer.makeTransform(start, Complex.ZERO, this.layerOut.getOrientation());
		applyTransform(transform);
	}

	// D E L T A . M O V E

	/**
	 * Compose delta translation
	 *
	 * @param start unit circle delta vector start
	 * @param end   unit circle delta vector start
	 */
	public void composeTranslate(@NonNull final Complex start, @NonNull final Complex end)
	{
		final HyperTransform transform = this.transformer.makeTransform(start, end, this.layerOut.getOrientation());
		applyComposedTransform(transform);
	}

	/**
	 * Compose delta rotation
	 *
	 * @param start unit circle delta vector start
	 * @param end   unit circle delta vector end
	 */
	public void composeRotate(@NonNull final Complex start, @NonNull final Complex end)
	{
		final HyperRotation rotation = new HyperRotation(Complex.ONE);
		rotation.div(end, start);
		rotation.normalize();
		applyComposedTransform(new HyperTransform(rotation));
	}

	// M O V E

	/**
	 * Translate tree according to vector
	 *
	 * @param start unit circle vector start
	 * @param end   unit circle vector end
	 */
	private void translate(@NonNull final Complex start, @NonNull final Complex end)
	{
		final HyperTransform transform = this.transformer.makeTransform(start, end, this.layerOut.getOrientation());
		applyTransform(transform);
	}

	/**
	 * Translate node to point (unused, may be called by javascript)
	 *
	 * @param node        node
	 * @param destination unit circle destination location
	 */
	public void moveTo(@NonNull final INode node, @NonNull final Complex destination)
	{
		final Location location = node.getLocation();
		translate(location.hyper.center0, destination);
		this.focusNode = node;
		repaint();
	}

	/**
	 * Translate node to unit circle center (unused, may be called by javascript)
	 *
	 * @param node node
	 */
	public void moveToCenter(@NonNull final INode node)
	{
		final Location location = node.getLocation();
		translate(location.hyper.center0, Complex.ZERO);
		this.focusNode = node;
		repaint();
	}

	// A N I M A T E

	/**
	 * Animate to unit circle center
	 *
	 * @param node node
	 * @param now  whether to start now
	 */
	public void animateToCenter(@NonNull final INode node, @SuppressWarnings("SameParameterValue") final boolean now)
	{
		final Location location = node.getLocation();
		final Complex from = location.hyper.center;
		animate(from, Complex.ZERO, now);
		this.focusNode = node;
	}

	/**
	 * Animate node to destination location
	 *
	 * @param node        node
	 * @param destination unit circle destination location
	 * @param now         whether to start now
	 */
	public void animateTo(@NonNull final INode node, @NonNull final Complex destination, @SuppressWarnings("SameParameterValue") final boolean now)
	{
		final Location location = node.getLocation();
		final Complex from = location.hyper.center;
		animate(from, destination, now);
		this.focusNode = node;
	}

	/**
	 * Animate tree. Translate as per vector(start,end)
	 *
	 * @param from unit circle vector start
	 * @param to   unit circle vector end
	 * @param now  whether to start now
	 */
	private synchronized void animate(@NonNull final Complex from, @NonNull final Complex to, final boolean now)
	{
		final AnimationTransforms transforms = AnimationTransforms.make(from, to, this.transformer, this.layerOut.getOrientation(), 0);
		if (transforms.transforms == null)
		{
			return;
		}
		final Animation animation = new Animation(transforms, this);
		this.animator.run(animation, animation.getSteps(), now ? 0 : Animation.ANIMATION_START_DELAY);
	}

	/**
	 * Whether animation is running
	 *
	 * @return whether animation is running
	 */
	public boolean isAnimating()
	{
		return this.animator.isRunning();
	}

	// S E A R C H I N G

	/**
	 * Find node given view location
	 *
	 * @param vx view x location
	 * @param vy view y location
	 * @return node
	 */
	@Nullable
	public INode findNode(final int vx, final int vy)
	{
		final Complex euclideanLocation = this.painter.viewToUnitCircle(vx, vy, this.width, this.height);
		return Finder.findNodeAt(this.model.tree.getRoot(), euclideanLocation);
	}

	/**
	 * Get focused node
	 *
	 * @return focus noe (at center of unit circle)
	 */
	@Nullable
	public INode getFocusNode()
	{
		return this.focusNode;
	}

	// S P A C E . C O N V E R S I O N

	/**
	 * Convert from view coordinates to unit circle euclidean coordinates
	 *
	 * @param vx view x-coordinate
	 * @param vy view x-coordinate
	 * @return unit circle euclidean coordinate
	 */
	@NonNull
	public Complex viewToUnitCircle(final int vx, final int vy)
	{
		return this.painter.viewToUnitCircle(vx, vy, getWidth(), getHeight());
	}
}
