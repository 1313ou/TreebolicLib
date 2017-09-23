package treebolic;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import treebolic.component.Progress;
import treebolic.component.Statusbar;
import treebolic.component.Toolbar;
import treebolic.control.Controller;
import treebolic.control.Finder;
import treebolic.core.AbstractLayerOut;
import treebolic.core.LayerOut;
import treebolic.core.Weigher;
import treebolic.core.location.Complex;
import treebolic.glue.ActionListener;
import treebolic.glue.Image;
import treebolic.glue.Worker;
import treebolic.glue.component.Container;
import treebolic.glue.component.WebDialog;
import treebolic.model.IEdge;
import treebolic.model.INode;
import treebolic.model.Location;
import treebolic.model.Model;
import treebolic.model.ModelReader;
import treebolic.model.MountPoint;
import treebolic.model.Mounter;
import treebolic.model.Settings;
import treebolic.model.Tree;
import treebolic.model.Types.MatchMode;
import treebolic.model.Types.MatchScope;
import treebolic.model.Types.SearchCommand;
import treebolic.provider.IProvider;
import treebolic.provider.IProviderContext;
import treebolic.view.View;

/**
 * Treebolic's main panel. Call either init() to feed the data
 *
 * @author Bernard Bou
 */
@SuppressWarnings("ViewConstructor")
public class Widget extends Container implements IWidget, IProviderContext
{
	private static final long serialVersionUID = 3962167000082869632L;

	// B E H A V I O U R

	/**
	 * Debug inputs to model factory
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean DEBUG = false;

	/**
	 * Warn image download fails
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean WARNIMAGEFAILS = false;

	/**
	 * Loading data on separate thread
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean THREADED = true;

	/**
	 * Animate on start flag
	 */
	@SuppressWarnings("WeakerAccess")
	static public final boolean ANIMATE_ON_START = true;

	/**
	 * Default provider
	 */
	@SuppressWarnings("WeakerAccess")
	static public final String DEFAULT_PROVIDER = "treebolic.provider.xml.dom.Provider";

	// V E R S I O N

	/**
	 * Version : 3.x
	 */
	private static final String theVersion = "3.7.0";

	// C O N T E X T

	/**
	 * Context for operations (provides some treebolic.glue to the context, application)
	 *
	 * @see IContext
	 */
	private final IContext theContext;

	// M O D E L - V I E W - C O N T R O L L E R

	/**
	 * Model
	 */
	private Model theModel;

	/**
	 * View
	 */
	private View theView;

	/**
	 * Controller
	 */
	private final Controller theController;

	// D A T A . P R O V I D E R

	/**
	 * Provider
	 */
	private IProvider theProvider;

	// A G E N T S

	/**
	 * Weigher
	 */
	private final Weigher theWeigher;

	/**
	 * Provider
	 */
	private final AbstractLayerOut theLayerOut;

	// C O M P O N E N T S

	/**
	 * Toolbar
	 */
	private Toolbar theToolbar;

	/**
	 * Status bar
	 */
	private Statusbar theStatusbar;

	/**
	 * Progress
	 */
	private final Progress theProgress;

	// I M A G E . M A N A G E M E N T

	/**
	 * Images base Url
	 */
	private URL theImageBase;

	/**
	 * Name-to-image map, used to void loading twice the same images
	 */
	private Hashtable<String, Image> theImages;

	/**
	 * Default images
	 */
	private Image theBackgroundImage;

	private Image theDefaultNodeImage;

	private Image theDefaultTreeEdgeImage;

	private Image theDefaultEdgeImage;

	/**
	 * Handle
	 */
	private final Object theHandle;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param thisContext context
	 * @param thisHandle  opaque handle
	 */
	public Widget(final IContext thisContext, final Object thisHandle)
	{
		super(thisHandle);
		this.theContext = thisContext;
		this.theHandle = thisHandle;

		// first components
		this.theProgress = new Progress(thisHandle);
		addComponent(this.theProgress, treebolic.glue.iface.component.Container.PANE);

		// controller
		this.theController = new Controller();
		this.theController.connect(this);

		// weigher
		this.theWeigher = new Weigher();

		// layer out
		this.theLayerOut = new LayerOut();
		this.theController.connect(this.theLayerOut);
	}

	// I N I T

	@Override
	public void init()
	{
		// source
		String thisSource = this.theContext.getParameters().getProperty("source");
		if (thisSource == null || thisSource.isEmpty())
		{
			thisSource = this.theContext.getParameters().getProperty("doc");
		}

		// provider
		final String thisProviderName = this.theContext.getParameters().getProperty("provider");

		// init
		init(thisProviderName, thisSource);
	}

	@Override
	public void init(final String thatProviderName, final String thisSource)
	{
		String thisProviderName = thatProviderName;
		if (thisProviderName == null)
		{
			thisProviderName = Widget.DEFAULT_PROVIDER;
		}

		// log
		if (Widget.DEBUG)
		{
			this.theContext.status("provider=" + thisProviderName);
		}

		// make provider
		final IProvider thisProvider = makeProvider(thisProviderName);
		if (thisProvider == null)
		{
			progress(Messages.getString("Widget.progress_err_provider_create") + ' ' + '<' + thisProviderName + '>', true);
			return;
		}

		// init
		init(thisProvider, thisSource);
	}

	@Override
	public void init(final IProvider thisProvider, final String thisSource)
	{
		this.theProvider = thisProvider;

		// pass context reference to provider, so that it can access some services
		this.theProvider.setLocator(this.theContext);
		this.theProvider.setContext(this);
		this.theProvider.setHandle(this.theHandle);

		// init
		initProcess(thisSource);
	}

	/**
	 * Init from serialized model
	 *
	 * @param thisSerFile zipped serialized model file
	 */
	@Override
	public void initSerialized(final String thisSerFile)
	{
		final ModelReader thisDeSerializer = new ModelReader(thisSerFile);
		//noinspection TryWithIdenticalCatches
		try
		{
			final Model thisModel = thisDeSerializer.deserialize();
			init(thisModel);
		}
		catch (final IOException thisException)
		{
			progress(Messages.getString("Widget.progress_err_serialized_create") + ' ' + '<' + thisSerFile + '>' + ' ' + thisException, true);
			thisException.printStackTrace();
		}
		catch (final ClassNotFoundException thisException)
		{
			progress(Messages.getString("Widget.progress_err_serialized_create") + ' ' + '<' + thisSerFile + '>' + ' ' + thisException, true);
			thisException.printStackTrace();
		}
	}

	@Override
	public void reinit(final String thisSource)
	{
		if (this.theProvider == null)
		{
			progress(Messages.getString("Widget.progress_err_reinit_provider_null") + ' ' + this.theProvider, true);
			return;
		}

		// init
		initProcess(thisSource);
	}

	/**
	 * Init (typically called by embedding applet's init()). Data source and data provider have not yet been determined.
	 *
	 * @param thisSource source (anything the provider will make sense of)
	 */
	@SuppressWarnings("WeakerAccess")
	protected void initProcess(final String thisSource)
	{
		// further (possibly lengthy) loading is done on separate thread
		if (!Widget.THREADED)
		{
			initModel(this.theProvider, thisSource);
			initDisplay();
		}
		else
		{
			final Worker thisWorker = new Worker()
			{
				@SuppressWarnings("synthetic-access")
				@Override
				public void job()
				{
					try
					{
						initModel(Widget.this.theProvider, thisSource);
					}
					catch (final Throwable e)
					{
						Widget.this.theContext.warn(Messages.getString("Widget.warn_err_model_create") + ':' + e.toString());
						e.printStackTrace();
					}
				}

				@Override
				public void onDone()
				{
					initDisplay();
				}
			};
			thisWorker.execute();
		}
	}

	/**
	 * Init from provider and source
	 *
	 * @param thisProvider data provider
	 * @param thisSource   data source
	 */
	@SuppressWarnings("WeakerAccess")
	protected void initModel(final IProvider thisProvider, final String thisSource)
	{
		if (Widget.DEBUG)
		{
			this.theContext.status("source=" + thisSource);
			this.theContext.status("base=" + this.theContext.getBase());
			this.theContext.status("imagebase=" + this.theContext.getImagesBase());
			this.theContext.status("parameters=" + this.theContext.getParameters());
		}

		progress(Messages.getString("Widget.progress_loading") + '\n' + ' ' + thisSource, false);
		final Model thisModel = thisProvider.makeModel(thisSource, this.theContext.getBase(), this.theContext.getParameters());
		if (thisModel == null)
		{
			progress(String.format(Messages.getString("Widget.progress_err_model_null_provider_source"), thisProvider.getClass().getCanonicalName(), thisSource), true);
			return;
		}
		progress(Messages.getString("Widget.progress_loaded") + '\n' + ' ' + thisSource, false);

		// load model
		initModel(thisModel);
	}

	@Override
	public void init(final Model thisModel)
	{
		initModel(thisModel);
		initDisplay();
	}

	/**
	 * Init model, weigh and lay out
	 *
	 * @param thisModel model
	 */
	private void initModel(final Model thisModel)
	{
		if (thisModel == null)
		{
			return;
		}

		// model/view/controller
		this.theModel = thisModel;

		// initiate image loading
		progress(Messages.getString("Widget.progress_images_loading"), false);
		loadImages();
		progress(Messages.getString("Widget.progress_images_loaded"), false);

		// weigh model
		progress(Messages.getString("Widget.progress_weighing"), false);
		this.theWeigher.weigh(thisModel.theTree.getRoot());

		// enforce settings
		this.theLayerOut.apply(this.theModel.theSettings);

		// lay out model
		progress(Messages.getString("Widget.progress_layingout"), false);
		this.theLayerOut.layout(thisModel.theTree.getRoot());
	}

	@SuppressWarnings({"boxing", "WeakerAccess"})
	public void initDisplay()
	{
		if (this.theModel == null)
		{
			progress(Messages.getString("Widget.progress_err_model_null"), true);
			return;
		}

		// view
		this.theView = new View(this.theHandle);

		// connect view and controller to each other
		this.theController.connect(this.theView);
		this.theView.connect(this.theController);

		// connect both controller and view to model
		this.theView.connect(this.theModel);
		this.theController.connect(this.theModel);

		// connect view to layerout
		this.theView.connect(this.theLayerOut);

		// settings
		this.theController.apply(this.theModel.theSettings);
		this.theView.apply(this.theModel.theSettings);

		// images
		this.theView.setImages(this.theBackgroundImage, this.theDefaultNodeImage, this.theDefaultTreeEdgeImage, this.theDefaultEdgeImage);

		// remove all previous components (possibly progress or previous view and tools)
		removeAll();

		// toolbar
		if (this.theModel.theSettings.theHasToolbarFlag != null && this.theModel.theSettings.theHasToolbarFlag)
		{
			final boolean hasTooltip = this.theModel.theSettings.theHasToolTipFlag != null ? this.theModel.theSettings.theHasToolTipFlag : true;
			final boolean toolTipDisplaysContent = this.theModel.theSettings.theToolTipDisplaysContentFlag != null ? this.theModel.theSettings.theToolTipDisplaysContentFlag : true;
			final boolean edgesAsArc = this.theModel.theSettings.theEdgesAsArcsFlag != null ? this.theModel.theSettings.theEdgesAsArcsFlag : true;
			final boolean focusOnHover = this.theModel.theSettings.theFocusOnHoverFlag != null ? this.theModel.theSettings.theFocusOnHoverFlag : false;
			this.theToolbar = new Toolbar(this.theController, hasTooltip, toolTipDisplaysContent, edgesAsArc, focusOnHover, this.theHandle);
			addComponent(this.theToolbar, treebolic.glue.iface.component.Container.TOOLBAR);
		}

		// show view
		addComponent(this.theView, treebolic.glue.iface.component.Container.VIEW);

		// status bar
		if (this.theModel.theSettings.theHasStatusbarFlag != null && this.theModel.theSettings.theHasStatusbarFlag)
		{
			this.theStatusbar = new Statusbar(this.theHandle);
			this.theStatusbar.addListener(new ActionListener()
			{
				@SuppressWarnings("synthetic-access")
				@Override
				public boolean onAction(final Object... theseParams)
				{
					final SearchCommand thisCommand = SearchCommand.valueOf((String) theseParams[0]);
					switch (thisCommand)
					{
						case SEARCH:
							final MatchScope thisScope = MatchScope.valueOf((String) theseParams[1]);
							final MatchMode thisMode = MatchMode.valueOf((String) theseParams[2]);
							final String thisTarget = (String) theseParams[3];
							// scope, mode, target, [start]
							Widget.this.theController.search(SearchCommand.SEARCH, thisScope, thisMode, thisTarget);
							break;
						default:
							Widget.this.theController.search(thisCommand);
							break;
					}
					return true;
				}
			});
			this.theStatusbar.setStyle(this.theContext.getStyle());
			addComponent(this.theStatusbar, treebolic.glue.iface.component.Container.STATUSBAR);
		}

		// validate
		validate();

		// animate
		final INode thisFocus = getFocusNode();
		if (!Widget.ANIMATE_ON_START)
		{
			// initial transform
			this.theView.applyNullTransform();
		}
		else
		{
			this.theView.applyInitialTransform();

			// animation to focus
			if (this.theModel.theSettings.theXMoveTo != null && this.theModel.theSettings.theXMoveTo < 1. || this.theModel.theSettings.theYMoveTo != null && this.theModel.theSettings.theYMoveTo < 1.)
			{
				// move required
				final Complex thisTo = new Complex(this.theModel.theSettings.theXMoveTo == null ? 0. : this.theModel.theSettings.theXMoveTo, this.theModel.theSettings.theYMoveTo == null ? 0. : this.theModel.theSettings.theYMoveTo);
				if (thisTo.abs2() > 1.)
				{
					thisTo.normalize().multiply(.9);
				}
				this.theView.animateTo(thisFocus, thisTo, false);
			}
			else
			{
				this.theView.animateToCenter(thisFocus, false);
			}
		}
	}

	// N A V I G A T I O N

	/**
	 * Mount a source at node
	 *
	 * @param thisMountingNode mounting node
	 * @param thisSource       mounted source
	 */
	public synchronized void mount(final INode thisMountingNode, final String thisSource)
	{
		putStatus(Messages.getString("Widget.status_mount"), thisSource, Statusbar.PutType.MOUNT);

		if (this.theProvider == null)
		{
			putStatus(Messages.getString("Widget.status_mount"), "<div class='mount'>" + Messages.getString("Widget.status_mount_err_provider_null") + "</div>", Statusbar.PutType.MOUNT);

			// get provider name
			final String thisProviderName = this.theContext.getParameters().getProperty("provider");
			if (thisProviderName == null || thisProviderName.isEmpty())
			{
				putStatus(Messages.getString("Widget.status_mount"), "<div class='mount'>" + Messages.getString("Widget.status_mount_err_providername_null") + "</div>", Statusbar.PutType.MOUNT);
				return;
			}

			// make provider
			this.theProvider = makeProvider(thisProviderName);
			if (this.theProvider == null)
			{
				putStatus(Messages.getString("Widget.status_mount"), "<div class='mount'>" + Messages.getString("Widget.status_mount_err_provider_create") + thisProviderName + "</div>", Statusbar.PutType.MOUNT);
				return;
			}

			// pass context reference to provider, so that it can access some services
			this.theProvider.setContext(this);

			// pass context reference to provider, so that it can access some services
			this.theProvider.setHandle(this.theHandle);
		}

		// make model
		final Tree thisTree = this.theProvider.makeTree(thisSource, this.theContext.getBase(), this.theContext.getParameters(), false);
		if (thisTree == null)
		{
			putStatus(Messages.getString("Widget.status_mount"), "<div class='mount'>" + Messages.getString("Widget.status_mount_err_model_null") + thisSource + "</div>", Statusbar.PutType.MOUNT);
			return;
		}

		// extract subroot + edges
		final INode thisMountedRoot = thisTree.getRoot();
		final List<IEdge> theseMountedEdges = thisTree.getEdges();

		// images
		loadImages(thisMountedRoot);
		loadImages(theseMountedEdges);

		// ensure edge list is non null
		if (this.theModel.theTree.getEdges() == null)
		{
			this.theModel.theTree.setEdges(new ArrayList<IEdge>());
		}

		// graft nodes
		if (!Mounter.graft(thisMountingNode, thisMountedRoot, this.theModel.theTree.getEdges(), theseMountedEdges))
		{
			putStatus(Messages.getString("Widget.status_mount"), Messages.getString("Widget.status_mount_err"), Statusbar.PutType.MOUNT);
			return;
		}

		// weigh
		this.theWeigher.weigh(thisMountedRoot);
		thisMountingNode.setChildrenWeight(thisMountedRoot.getChildrenWeight());
		thisMountingNode.setMinWeight(thisMountedRoot.getMinWeight());

		// compute locations : layout
		final MountPoint.Mounting thisMountingPoint = (MountPoint.Mounting) thisMountingNode.getMountPoint();
		this.theLayerOut.layout(thisMountedRoot, thisMountingNode.getLocation().hyper.center0, thisMountingPoint.theHalfWedge, thisMountingPoint.theOrientation);

		// notify view
		this.theView.mount(thisMountedRoot);
	}

	/**
	 * Unmount at mountpoint node
	 *
	 * @param thisMountedNode mounted node
	 */
	public synchronized void umount(final INode thisMountedNode)
	{
		putStatus(Messages.getString("Widget.status_unmount"), "", Statusbar.PutType.MOUNT);

		// model
		final INode thisMountingNode = Mounter.prune(thisMountedNode, this.theModel.theTree.getEdges());
		if (thisMountingNode == null)
		{
			putStatus(Messages.getString("Widget.status_unmount"), "<div class='mount'>" + Messages.getString("Widget.status_unmount_err") + "</div>", Statusbar.PutType.MOUNT);
			return;
		}

		// compute locations : copy
		final Location thisMountedNodeLocation = thisMountedNode.getLocation();
		final Location thisMountingNodeLocation = thisMountingNode.getLocation();
		thisMountingNodeLocation.hyper.clone(thisMountedNodeLocation.hyper);

		// notify view
		this.theView.umount(thisMountingNode);
	}

	// A C C E S S

	/**
	 * Get version
	 *
	 * @return version
	 */
	@Override
	public String getVersion()
	{
		return Widget.theVersion;
	}

	/**
	 * Get model
	 *
	 * @return model
	 */
	public Model getModel()
	{
		return this.theModel;
	}

	/**
	 * Get view
	 *
	 * @return view
	 */
	public View getView()
	{
		return this.theView;
	}

	/**
	 * Get statusbar
	 *
	 * @return statusbar
	 */
	public Statusbar getStatusbar()
	{
		return this.theStatusbar;
	}

	/**
	 * Get toolbar
	 *
	 * @return toolbar
	 */
	public Toolbar getToolbar()
	{
		return this.theToolbar;
	}

	/**
	 * Get context
	 *
	 * @return context
	 */
	public IContext getIContext()
	{
		return this.theContext;
	}

	// P R O V I D E R - F A C T O R Y

	/**
	 * Make provider
	 *
	 * @param thisProviderName provider name
	 * @return provider
	 */
	private IProvider makeProvider(final String thisProviderName)
	{
		//noinspection TryWithIdenticalCatches
		try
		{
			final Class<?> thisClass = Class.forName(thisProviderName);
			final Class<?>[] theseArgsClass = new Class[]{};
			final Object[] theseArgs = new Object[]{};

			final Constructor<?> thisConstructor = thisClass.getConstructor(theseArgsClass);
			final Object thisInstance = thisConstructor.newInstance(theseArgs);
			return (IProvider) thisInstance;
		}
		catch (final ClassNotFoundException e)
		{
			this.theContext.warn(Messages.getString("Widget.warn_err_provider_create") + e.toString());
		}
		catch (final NoSuchMethodException e)
		{
			this.theContext.warn(Messages.getString("Widget.warn_err_provider_create") + e.toString());
		}
		catch (final IllegalAccessException e)
		{
			this.theContext.warn(Messages.getString("Widget.warn_err_provider_create") + e.toString());
		}
		catch (final InstantiationException e)
		{
			this.theContext.warn(Messages.getString("Widget.warn_err_provider_create") + e.toString());
		}
		catch (final IllegalArgumentException e)
		{
			this.theContext.warn(Messages.getString("Widget.warn_err_provider_create") + e.toString());
		}
		catch (final InvocationTargetException e)
		{
			this.theContext.warn(Messages.getString("Widget.warn_err_provider_create") + e.toString());
		}
		return null;
	}

	// U R L . F A C T O R Y

	/**
	 * Get image Url
	 *
	 * @param thisImageSource image source
	 * @return image url
	 */
	private URL makeImageURL(final String thisImageSource)
	{
		try
		{
			return new URL(this.theImageBase, thisImageSource);
		}
		catch (final MalformedURLException thisException)
		{
			// do nothing
		}
		return null;
	}

	// I M A G E . L O A D E R

	/**
	 * Load all images
	 */
	private void loadImages()
	{
		this.theImages = new Hashtable<>();
		this.theImageBase = this.theContext.getImagesBase();
		loadImages(this.theModel.theTree.getRoot());
		loadImages(this.theModel.theTree.getEdges());
		loadTopImages(this.theModel.theSettings);
	}

	/**
	 * Load node and node's children's images
	 *
	 * @param thisNode starting node
	 */
	private void loadImages(final INode thisNode)
	{
		if (thisNode == null)
		{
			return;
		}
		if (thisNode.getImage() == null)
		{
			// node image from file
			String thisSource = thisNode.getImageFile();
			if (thisSource != null)
			{
				// cache lookup
				Image thisImage = this.theImages.get(thisSource);
				if (thisImage == null)
				{
					// cache miss
					thisImage = loadImage(thisSource);
				}

				// store image in the node
				thisNode.setImage(thisImage);
			}

			// set node image from index
			final int thisImageIndex = thisNode.getImageIndex();
			if (thisImageIndex != -1 && this.theModel.theImages != null)
			{
				thisNode.setImage(this.theModel.theImages[thisImageIndex]);
			}

			// edge image
			thisSource = thisNode.getEdgeImageFile();
			if (thisSource != null)
			{
				// cache lookup
				Image thisImage = this.theImages.get(thisSource);
				if (thisImage == null)
				{
					// cache miss
					thisImage = loadImage(thisSource);
				}
				thisNode.setEdgeImage(thisImage);
			}

			// set node image from index
			final int thisEdgeImageIndex = thisNode.getEdgeImageIndex();
			if (thisEdgeImageIndex != -1 && this.theModel.theImages != null)
			{
				thisNode.setEdgeImage(this.theModel.theImages[thisEdgeImageIndex]);
			}
		}

		// recurse on mounting node obfuscated by mounted node)
		final MountPoint thisMountPoint = thisNode.getMountPoint();
		if (thisMountPoint != null && thisMountPoint instanceof MountPoint.Mounted)
		{
			final MountPoint.Mounted thisMountedPoint = (MountPoint.Mounted) thisMountPoint;
			final INode theMountingNode = thisMountedPoint.theMountingNode;
			loadImages(theMountingNode);
		}

		// recurse children
		final List<INode> theseChildren = thisNode.getChildren();
		if (theseChildren != null)
		{
			for (final INode thisChild : thisNode.getChildren())
			{
				loadImages(thisChild);
			}
		}
	}

	/**
	 * Load edges' images
	 *
	 * @param thisEdgeList edge list
	 */
	private void loadImages(final List<IEdge> thisEdgeList)
	{
		if (thisEdgeList != null)
		{
			for (final IEdge thisEdge : thisEdgeList)
			{
				loadImage(thisEdge);
			}
		}
	}

	/**
	 * Load edge image
	 *
	 * @param thisEdge edge
	 */
	private void loadImage(final IEdge thisEdge)
	{
		if (thisEdge == null)
		{
			return;
		}

		// edge image
		final String thisSource = thisEdge.getImageFile();
		if (thisSource != null)
		{
			// cache lookup
			Image thisImage = this.theImages.get(thisSource);
			if (thisImage == null)
			{
				// cache miss
				thisImage = loadImage(thisSource);
			}
			thisEdge.setImage(thisImage);
		}

		// set node image from index
		final int thisImageIndex = thisEdge.getImageIndex();
		if (thisImageIndex != -1 && this.theModel.theImages != null)
		{
			thisEdge.setImage(this.theModel.theImages[thisImageIndex]);
		}
	}

	/**
	 * Load top images
	 *
	 * @param theseSettings settings
	 */
	private void loadTopImages(final Settings theseSettings)
	{
		this.theBackgroundImage = loadImage(theseSettings.theBackgroundImageFile);
		this.theDefaultNodeImage = loadImage(theseSettings.theDefaultNodeImage);
		this.theDefaultTreeEdgeImage = loadImage(theseSettings.theDefaultTreeEdgeImage);
		this.theDefaultEdgeImage = loadImage(theseSettings.theDefaultEdgeImage);
		if (this.theModel.theImages != null)
		{
			if (theseSettings.theBackgroundImageIndex != -1)
			{
				this.theBackgroundImage = this.theModel.theImages[theseSettings.theBackgroundImageIndex];
			}
			if (theseSettings.theDefaultNodeImageIndex != -1)
			{
				this.theDefaultNodeImage = this.theModel.theImages[theseSettings.theDefaultNodeImageIndex];
			}
			if (theseSettings.theDefaultTreeEdgeImageIndex != -1)
			{
				this.theDefaultTreeEdgeImage = this.theModel.theImages[theseSettings.theDefaultTreeEdgeImageIndex];
			}
			if (theseSettings.theDefaultEdgeImageIndex != -1)
			{
				this.theDefaultEdgeImage = this.theModel.theImages[theseSettings.theDefaultEdgeImageIndex];
			}
		}
	}

	/**
	 * Load image given its source
	 *
	 * @param thisSource source
	 * @return image
	 */
	private Image loadImage(final String thisSource)
	{
		if (thisSource == null)
		{
			return null;
		}

		final URL thisUrl = makeImageURL(thisSource);
		if (thisUrl != null)
		{
			if (Widget.DEBUG)
			{
				this.theContext.status("image=" + thisUrl);
			}

			// image loading
			Image thisImage;
			try
			{
				thisImage = Image.make(thisUrl);

				// cache image
				this.theImages.put(thisSource, thisImage);
				return thisImage;
			}
			catch (final IOException e)
			{
				if (Widget.WARNIMAGEFAILS)
				{
					this.theContext.warn(Messages.getString("Widget.warn_err_image_load") + thisUrl + ' ' + e);
				}
			}
		}
		return null;
	}

	// F O C U S . N O D E

	/**
	 * Get node to initially receive focus
	 *
	 * @return node
	 */
	private INode getFocusNode()
	{
		// focus node
		String thisFocusNodeId = null;
		if (this.theModel.theSettings.theFocus != null)
		{
			thisFocusNodeId = this.theModel.theSettings.theFocus;
		}

		// animate
		INode thisFocus;
		if (thisFocusNodeId == null)
		{
			thisFocus = this.theModel.theTree.getRoot();
		}
		else
		{
			thisFocus = Finder.findNodeById(this.theModel.theTree.getRoot(), thisFocusNodeId);

			// default to root if not found
			if (thisFocus == null)
			{
				thisFocus = this.theModel.theTree.getRoot();
			}
		}
		return thisFocus;
	}

	// I N T E R A C T I O N

	@Override
	public void progress(final String thisString, final boolean fail)
	{
		this.theProgress.put(thisString, fail);
	}

	@Override
	public void message(final String thisString)
	{
		this.theContext.status(thisString);
	}

	@Override
	public void warn(final String thisMessage)
	{
		this.theContext.warn(thisMessage);
	}

	// I N P U T

	/**
	 * Get target
	 *
	 * @return target
	 */
	public String getTarget()
	{
		return this.theContext.getInput();
	}

	// S T A T U S

	/**
	 * Put information
	 *
	 * @param thisHeader  header
	 * @param thisMessage message
	 * @param thisType    type of message
	 */
	public void putStatus(final String thisHeader, final String thisMessage, final Statusbar.PutType thisType)
	{
		if (this.theStatusbar == null)
		{
			return;
		}
		this.theStatusbar.put(thisHeader, thisMessage, thisType);
	}

	// I N F O

	/**
	 * Put information
	 *
	 * @param thisHeader  header
	 * @param thisContent content
	 */
	public void putInfo(final String thisHeader, final String thisContent)
	{
		final WebDialog thisWebDialog = new WebDialog();
		thisWebDialog.setHandle(this.theHandle);
		thisWebDialog.setListener(new ActionListener()
		{
			@SuppressWarnings("synthetic-access")
			@Override
			public boolean onAction(final Object... theseParams)
			{
				final String thisLink = (String) theseParams[0];
				Widget.this.theContext.linkTo(thisLink, null);
				return false;
			}
		});
		final String thisStyle = this.theContext.getStyle();
		thisWebDialog.setStyle(thisStyle);
		thisWebDialog.set(thisHeader, thisContent);
		thisWebDialog.display();
	}

	// J A V A S C R I P T

	@Override
	public void focus(final String thisNodeId)
	{
		if (this.theModel == null)
		{
			return;
		}

		this.theController.focus(thisNodeId);
	}

	@Override
	public String match(final String thisTargetString, final String thisScopeString, final String thisModeString)
	{
		if (this.theModel == null)
		{
			return null;
		}

		MatchScope thisScope = MatchScope.LABEL;
		if (thisScopeString != null)
		{
			thisScope = MatchScope.valueOf(thisScopeString.toUpperCase());
		}

		MatchMode thisMode = MatchMode.EQUALS;
		if (thisModeString != null)
		{
			thisMode = MatchMode.valueOf(thisModeString.toUpperCase());
		}

		// search
		final INode thisFound = this.theController.match(thisTargetString, thisScope, thisMode);
		if (thisFound != null)
		{
			return thisFound.getId();
		}
		return null;
	}

	@Override
	public void link(final String thisUrlString, final String thisUrlTarget)
	{
		this.theController.linkTo(thisUrlString, thisUrlTarget);
	}

	@Override
	public void search(final String thisCommandString, final String... theseParams)
	{
		final SearchCommand thisCommand = SearchCommand.valueOf(thisCommandString.toUpperCase());
		switch (thisCommand)
		{
			case SEARCH:
				final MatchScope thisScope = MatchScope.valueOf(theseParams[0].toUpperCase());
				final MatchMode thisMode = MatchMode.valueOf(theseParams[1].toUpperCase());
				final String thisTarget = theseParams[2];
				// scope, mode, target, [start]
				this.theController.search(thisCommand, thisScope, thisMode, thisTarget);
				break;
			default:
				this.theController.search(thisCommand);
				break;
		}
	}
}
