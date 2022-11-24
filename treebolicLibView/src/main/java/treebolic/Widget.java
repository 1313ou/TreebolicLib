/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
import treebolic.component.Progress;
import treebolic.component.Statusbar;
import treebolic.component.Toolbar;
import treebolic.control.Commander;
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
import treebolic.glue.component.Dialog;
import treebolic.model.*;
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
public class Widget extends Container implements IWidget, IProviderContext
{
	// private static final long serialVersionUID = 3962167000082869632L;

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
	static public final String DEFAULT_PROVIDER = "treebolic.provider.xml.Provider";

	// V E R S I O N

	/**
	 * Version : 3.x
	 */
	private static final String version = "4.0.0";

	// C O N T E X T

	/**
	 * Context for operations (provides some treebolic.glue to the context, application)
	 *
	 * @see IContext
	 */
	private final IContext context;


	// A C T I O N   L I S T E N E R

	/**
	 * Action listener
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private final ActionListener linkActionListener;

	// M O D E L - V I E W - C O N T R O L L E R

	/**
	 * Model
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private Model model;

	/**
	 * View
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private View view;

	/**
	 * Controller
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final Controller controller;

	// D A T A . P R O V I D E R

	/**
	 * Provider
	 */
	@Nullable
	private IProvider provider;

	// A G E N T S

	/**
	 * Weigher
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final Weigher weigher;

	/**
	 * Provider
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final AbstractLayerOut layerOut;

	// C O M P O N E N T S

	/**
	 * Toolbar
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private Toolbar toolbar;

	/**
	 * Status bar
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	private Statusbar statusbar;

	/**
	 * Progress
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@NonNull
	private final Progress progress;

	// I M A G E . M A N A G E M E N T

	/**
	 * Images base Url
	 */
	@Nullable
	private URL imageBase;

	/**
	 * Name-to-image map, used to void loading twice the same images
	 */
	private Hashtable<String, Image> images;

	/**
	 * Default images
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private Image backgroundImage;

	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private Image defaultNodeImage;

	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private Image defaultTreeEdgeImage;

	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private Image defaultEdgeImage;

	/**
	 * Handle
	 */
	private final Object handle;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param context context
	 * @param handle  opaque handle
	 */
	public Widget(final IContext context, final Object handle)
	{
		super(handle);
		this.context = context;
		this.handle = handle;

		// first components
		this.progress = new Progress(handle);
		addComponent(this.progress, treebolic.glue.iface.component.Container.PANE);

		// controller
		this.controller = new Controller();
		this.controller.connect(this);

		// weigher
		this.weigher = new Weigher();

		// layer out
		this.layerOut = new LayerOut();
		this.controller.connect(this.layerOut);

		// action listener
		this.linkActionListener = new ActionListener()
		{
			@Override
			public boolean onAction(@NonNull final Object... params)
			{
				final String link = (String) params[0];
				final String target = params.length > 1 ? (String) params[1] : null;
				Widget.this.context.linkTo(link, target);
				return false;
			}
		};
	}

	// I N I T

	@Override
	public void init()
	{
		// source
		@Nullable final Properties parameters = this.context.getParameters();
		String source = parameters == null ? null : parameters.getProperty("source");
		if (source == null || source.isEmpty())
		{
			source = parameters == null ? null : parameters.getProperty("doc");
		}

		// provider
		final String providerName = parameters == null ? null : parameters.getProperty("provider");

		// init
		init(providerName, source);
	}

	@Override
	public void init(final String providerName0, final String source)
	{
		String providerName = providerName0;
		if (providerName == null)
		{
			providerName = Widget.DEFAULT_PROVIDER;
		}

		// log
		if (Widget.DEBUG)
		{
			this.context.status("provider=" + providerName);
		}

		// make provider
		@Nullable final IProvider provider = makeProvider(providerName);
		if (provider == null)
		{
			progress(Messages.getString("Widget.progress_err_provider_create") + ' ' + '<' + providerName + '>', true);
			return;
		}

		// init
		init(provider, source);
	}

	@SuppressWarnings("WeakerAccess")
	@Override
	public void init(final IProvider provider, final String source)
	{
		this.provider = provider;

		// pass context reference to provider, so that it can access some services
		this.provider.setLocator(this.context);
		this.provider.setContext(this);
		this.provider.setHandle(this.handle);

		// init
		initProcess(source);
	}

	/**
	 * Init from serialized model
	 *
	 * @param serFile zipped serialized model file
	 */
	@Override
	public void initSerialized(final String serFile)
	{
		@NonNull final ModelReader deSerializer = new ModelReader(serFile);
		try
		{
			@NonNull final Model model = deSerializer.deserialize();
			init(model);
		}
		catch (@NonNull final IOException | ClassNotFoundException exception)
		{
			progress(Messages.getString("Widget.progress_err_serialized_create") + ' ' + '<' + serFile + '>' + ' ' + exception, true);
			exception.printStackTrace();
		}
	}

	@Override
	public void reinit(final String source)
	{
		if (this.provider == null)
		{
			progress(Messages.getString("Widget.progress_err_reinit_provider_null") + ' ' + this.provider, true);
			return;
		}

		// init
		initProcess(source);
	}

	/**
	 * Static worker class to avoid leaks
	 */
	static private class InitWorker extends Worker
	{
		private final Runnable job;
		private final Runnable done;

		@SuppressWarnings("WeakerAccess")
		public InitWorker(final Runnable job, final Runnable done)
		{
			this.job = job;
			this.done = done;
		}

		@Override
		public void job()
		{
			this.job.run();
		}

		@Override
		public void onDone()
		{
			this.done.run();
		}
	}

	/**
	 * Init (typically called by embedding applet's init()). Data source and data provider have not yet been determined.
	 *
	 * @param source source (anything the provider will make sense of)
	 */
	@SuppressWarnings("WeakerAccess")
	protected void initProcess(final String source)
	{
		// further (possibly lengthy) loading is done on separate thread
		if (!Widget.THREADED)
		{
			assert Widget.this.provider != null;
			initModel(this.provider, source);
			initDisplay();
		}
		else
		{
			@NonNull final treebolic.glue.iface.Worker worker = new InitWorker( //
					() -> {
						try
						{
							assert Widget.this.provider != null;
							initModel(Widget.this.provider, source);
						}
						catch (Throwable e)
						{
							Widget.this.context.warn(Messages.getString("Widget.warn_err_model_create") + ':' + e);
							e.printStackTrace();
						}
					}, //
					this::initDisplay);
			worker.execute();
		}
	}

	/**
	 * Init from provider and source
	 *
	 * @param provider data provider
	 * @param source   data source
	 */
	@SuppressWarnings("WeakerAccess")
	protected void initModel(@NonNull final IProvider provider, @Nullable final String source)
	{
		if (Widget.DEBUG)
		{
			this.context.status("source=" + source);
			this.context.status("base=" + this.context.getBase());
			this.context.status("imagebase=" + this.context.getImagesBase());
			this.context.status("parameters=" + this.context.getParameters());
		}

		@NonNull String message = Messages.getString("Widget.progress_loading");
		if (source != null)
		{
			message += ' ' + source;
		}
		progress(message, false);
		@Nullable final Model model = provider.makeModel(source, this.context.getBase(), this.context.getParameters());
		/*
		if (model == null)
		{
			progress(String.format(Messages.getString("Widget.progress_err_model_null_provider_source"), provider.getClass().getCanonicalName(), source), true);
			return;
		}
		*/
		progress(Messages.getString("Widget.progress_loaded") + ' ' + source, false);

		// load model
		initModel(model);
	}

	@SuppressWarnings("WeakerAccess")
	@Override
	public void init(final Model model)
	{
		initModel(model);
		initDisplay();
	}

	/**
	 * Init model, weigh and lay out
	 *
	 * @param model model
	 */
	private void initModel(@Nullable final Model model)
	{
		if (model == null)
		{
			return;
		}

		// model/view/controller
		this.model = model;

		// initiate image loading
		progress(Messages.getString("Widget.progress_images_loading"), false);
		loadImages();
		progress(Messages.getString("Widget.progress_images_loaded"), false);

		// weigh model
		progress(Messages.getString("Widget.progress_weighing"), false);
		this.weigher.weigh(model.tree.getRoot());

		// enforce settings
		this.layerOut.apply(this.model.settings);

		// lay out model
		progress(Messages.getString("Widget.progress_layingout"), false);
		this.layerOut.layout(model.tree.getRoot());
	}

	/**
	 * Init display
	 */
	@SuppressWarnings({"WeakerAccess"})
	public void initDisplay()
	{
		if (this.model == null)
		{
			progress(Messages.getString("Widget.progress_err_model_null"), true);
			return;
		}

		// view
		this.view = new View(this.handle);

		// connect view and controller to each other
		this.controller.connect(this.view);
		this.view.connect(this.controller);

		// connect both controller and view to model
		this.view.connect(this.model);
		this.controller.connect(this.model);

		// connect view to layerout
		this.view.connect(this.layerOut);

		// settings
		this.controller.apply(this.model.settings);
		this.view.apply(this.model.settings);

		// images
		this.view.setImages(this.backgroundImage, this.defaultNodeImage, this.defaultTreeEdgeImage, this.defaultEdgeImage);

		// remove all previous components (possibly progress or previous view and tools)
		removeAll();

		// toolbar
		if (this.model.settings.hasToolbarFlag != null && this.model.settings.hasToolbarFlag)
		{
			final boolean hasTooltip = this.model.settings.hasToolTipFlag != null ? this.model.settings.hasToolTipFlag : true;
			final boolean toolTipDisplaysContent = this.model.settings.toolTipDisplaysContentFlag != null ? this.model.settings.toolTipDisplaysContentFlag : true;
			final boolean edgesAsArc = this.model.settings.edgesAsArcsFlag != null ? this.model.settings.edgesAsArcsFlag : true;
			final boolean focusOnHover = this.model.settings.focusOnHoverFlag != null ? this.model.settings.focusOnHoverFlag : false;
			this.toolbar = new Toolbar(this.controller, hasTooltip, toolTipDisplaysContent, edgesAsArc, focusOnHover, this.handle);
			addComponent(this.toolbar, treebolic.glue.iface.component.Container.TOOLBAR);
		}

		// show view
		addComponent(this.view, treebolic.glue.iface.component.Container.VIEW);

		// status bar
		if (this.model.settings.hasStatusbarFlag != null && this.model.settings.hasStatusbarFlag)
		{
			this.statusbar = new Statusbar(this.handle);
			this.statusbar.setListener(this.linkActionListener);
			//noinspection SameReturnValue,SameReturnValue
			this.statusbar.addListener(new ActionListener()
			{
				@SuppressWarnings("SameReturnValue")
				@Override
				public boolean onAction(final Object... params)
				{
					@NonNull final SearchCommand command = SearchCommand.valueOf((String) params[0]);
					if (command == SearchCommand.SEARCH)
					{
						@NonNull final MatchScope scope = MatchScope.valueOf((String) params[1]);
						@NonNull final MatchMode mode = MatchMode.valueOf((String) params[2]);
						final String target = (String) params[3];
						// scope, mode, target, [start]
						Widget.this.controller.search(SearchCommand.SEARCH, scope, mode, target);
					}
					else
					{
						Widget.this.controller.search(command);
					}
					return true;
				}
			});
			this.statusbar.setStyle(this.context.getStyle());
			addComponent(this.statusbar, treebolic.glue.iface.component.Container.STATUSBAR);
		}

		// validate
		validate();

		// animate
		if (!Widget.ANIMATE_ON_START)
		{
			// initial transform
			this.view.applyNullTransform();
		}
		else
		{
			this.view.applyInitialTransform();

			// animation to focus
			@Nullable final INode focus = getFocusNode();
			if (focus != null)
			{
				if (this.model.settings.xMoveTo != null && this.model.settings.xMoveTo < 1. || this.model.settings.yMoveTo != null && this.model.settings.yMoveTo < 1.)
				{
					// move required
					@NonNull final Complex to = new Complex(this.model.settings.xMoveTo == null ? 0. : this.model.settings.xMoveTo, this.model.settings.yMoveTo == null ? 0. : this.model.settings.yMoveTo);
					if (to.abs2() > 1.)
					{
						to.normalize().multiply(.9);
					}
					this.view.animateTo(focus, to, false);
				}
				else
				{
					this.view.animateToCenter(focus, false);
				}
			}
		}
	}

	// N A V I G A T I O N

	/**
	 * Mount a source at node
	 *
	 * @param mountingNode mounting node
	 * @param source       mounted source
	 */
	public synchronized void mount(@NonNull final INode mountingNode, final String source)
	{
		putStatus(Statusbar.PutType.MOUNT, (Function<CharSequence[], String>) null, Messages.getString("Widget.status_mount"), source);

		if (this.provider == null)
		{
			@NonNull final Function<CharSequence[], String> toHtml = (s) -> this.controller.makeHtml("mount", s);

			putStatus(Statusbar.PutType.MOUNT, toHtml, Messages.getString("Widget.status_mount"), Messages.getString("Widget.status_mount_err_provider_null"));

			// get provider name
			@Nullable final Properties parameters = this.context.getParameters();
			final String providerName = parameters == null ? null : parameters.getProperty("provider");
			if (providerName == null || providerName.isEmpty())
			{
				putStatus(Statusbar.PutType.MOUNT, toHtml, Messages.getString("Widget.status_mount"), Messages.getString("Widget.status_mount_err_providername_null"));
				return;
			}

			// make provider
			this.provider = makeProvider(providerName);
			if (this.provider == null)
			{
				putStatus(Statusbar.PutType.MOUNT, toHtml, Messages.getString("Widget.status_mount"), Messages.getString("Widget.status_mount_err_provider_create"));
				return;
			}

			// pass context reference to provider, so that it can access some services
			this.provider.setContext(this);

			// pass context reference to provider, so that it can access some services
			this.provider.setHandle(this.handle);
		}

		// make model
		@Nullable final Tree tree = this.provider.makeTree(source, this.context.getBase(), this.context.getParameters(), false);
		/*
		if (tree == null)
		{
			putStatus(Statusbar.PutType.MOUNT, Messages.getString("Widget.status_mount"), Controller.makeHtml("mount", Messages.getString("Widget.status_mount_err_model_null") + source);
			return;
		}
        */

		// extract subroot + edges
		assert tree != null;
		final INode mountedRoot = tree.getRoot();
		final List<IEdge> mountedEdges = tree.getEdges();

		// images
		loadImages(mountedRoot);
		loadImages(mountedEdges);

		// ensure edge list is non null
		assert this.model != null;
		if (this.model.tree.getEdges() == null)
		{
			this.model.tree.setEdges(new ArrayList<>());
		}

		// graft nodes
		if (!Mounter.graft(mountingNode, mountedRoot, this.model.tree.getEdges(), mountedEdges))
		{
			putStatus(Statusbar.PutType.MOUNT, Messages.getString("Widget.status_mount"), Messages.getString("Widget.status_mount_err"));
			return;
		}

		// weigh
		this.weigher.weigh(mountedRoot);
		mountingNode.setChildrenWeight(mountedRoot.getChildrenWeight());
		mountingNode.setMinWeight(mountedRoot.getMinWeight());

		// compute locations : layout
		@Nullable final MountPoint.Mounting mountingPoint = (MountPoint.Mounting) mountingNode.getMountPoint();
		assert mountingPoint != null;
		this.layerOut.layout(mountedRoot, mountingNode.getLocation().hyper.center0, mountingPoint.halfWedge, mountingPoint.orientation);

		// notify view
		this.view.mount(mountedRoot);
	}

	/**
	 * Unmount at mountpoint node
	 *
	 * @param mountedNode mounted node
	 */
	public synchronized void umount(@NonNull final INode mountedNode)
	{
		putStatus(Statusbar.PutType.MOUNT, Messages.getString("Widget.status_unmount"), "");

		// model
		assert this.model != null;
		@Nullable final INode mountingNode = Mounter.prune(mountedNode, this.model.tree.getEdges());
		if (mountingNode == null)
		{
			putStatus(Statusbar.PutType.MOUNT, (s) -> this.controller.makeHtml("mount", s), Messages.getString("Widget.status_unmount"), Messages.getString("Widget.status_unmount_err"));
			return;
		}

		// compute locations : copy
		final Location mountedNodeLocation = mountedNode.getLocation();
		final Location mountingNodeLocation = mountingNode.getLocation();
		mountingNodeLocation.hyper.clone(mountedNodeLocation.hyper);

		// notify view
		this.view.umount(mountingNode);
	}

	// A C C E S S

	/**
	 * Get version
	 *
	 * @return version
	 */
	@NonNull
	@Override
	public String getVersion()
	{
		return Widget.version;
	}

	/**
	 * Get model
	 *
	 * @return model
	 */
	@Nullable
	public Model getModel()
	{
		return this.model;
	}

	/**
	 * Get view
	 *
	 * @return view
	 */
	public View getView()
	{
		return this.view;
	}

	/**
	 * Get statusbar
	 *
	 * @return statusbar
	 */
	public Statusbar getStatusbar()
	{
		return this.statusbar;
	}

	/**
	 * Get toolbar
	 *
	 * @return toolbar
	 */
	public Toolbar getToolbar()
	{
		return this.toolbar;
	}

	/**
	 * Get context
	 *
	 * @return context
	 */
	public IContext getIContext()
	{
		return this.context;
	}

	// P R O V I D E R - F A C T O R Y

	/**
	 * Make provider
	 *
	 * @param providerName provider name
	 * @return provider
	 */
	@Nullable
	private IProvider makeProvider(@NonNull final String providerName)
	{
		try
		{
			@NonNull final Class<?> clazz = Class.forName(providerName);
			@NonNull final Class<?>[] argsClass = new Class[]{};
			@NonNull final Object[] args = new Object[]{};

			@NonNull final Constructor<?> constructor = clazz.getConstructor(argsClass);
			@NonNull final Object instance = constructor.newInstance(args);
			return (IProvider) instance;
		}
		catch (@NonNull final ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException e)
		{
			this.context.warn(Messages.getString("Widget.warn_err_provider_create") + e);
		}
		return null;
	}

	// U R L . F A C T O R Y

	/**
	 * Get image Url
	 *
	 * @param imageSource image source
	 * @return image url
	 */
	@Nullable
	private URL makeImageURL(@NonNull final String imageSource)
	{
		try
		{
			return new URL(this.imageBase, imageSource);
		}
		catch (@NonNull final MalformedURLException ignored)
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
		this.images = new Hashtable<>();
		this.imageBase = this.context.getImagesBase();
		assert this.model != null;
		loadImages(this.model.tree.getRoot());
		loadImages(this.model.tree.getEdges());
		loadTopImages(this.model.settings);
	}

	/**
	 * Load node and node's children's images
	 *
	 * @param node starting node
	 */
	private void loadImages(@Nullable final INode node)
	{
		if (node == null)
		{
			return;
		}
		if (node.getImage() == null)
		{
			// node image from file
			@Nullable String source = node.getImageFile();
			if (source != null)
			{
				// cache lookup
				@Nullable Image image = this.images.get(source);
				if (image == null)
				{
					// cache miss
					image = loadImage(source);
				}

				// store image in the node
				node.setImage(image);
			}

			// edge image
			source = node.getEdgeImageFile();
			if (source != null)
			{
				// cache lookup
				@Nullable Image image = this.images.get(source);
				if (image == null)
				{
					// cache miss
					image = loadImage(source);
				}
				node.setEdgeImage(image);
			}

			assert this.model != null;

			// set node image from index
			final int imageIndex = node.getImageIndex();
			if (imageIndex != -1 && this.model.images != null)
			{
				node.setImage(this.model.images[imageIndex]);
			}

			// set edge image from index
			final int edgeImageIndex = node.getEdgeImageIndex();
			if (edgeImageIndex != -1 && this.model.images != null)
			{
				node.setEdgeImage(this.model.images[edgeImageIndex]);
			}
		}

		// recurse on mounting node obfuscated by mounted node)
		@Nullable final MountPoint mountPoint = node.getMountPoint();
		if (mountPoint instanceof MountPoint.Mounted)
		{
			@NonNull final MountPoint.Mounted mountedPoint = (MountPoint.Mounted) mountPoint;
			@Nullable final INode mountingNode = mountedPoint.mountingNode;
			loadImages(mountingNode);
		}

		// recurse children
		@Nullable final List<INode> children = node.getChildren();
		if (children != null)
		{
			for (final INode child : node.getChildren())
			{
				loadImages(child);
			}
		}
	}

	/**
	 * Load edges' images
	 *
	 * @param edgeList edge list
	 */
	private void loadImages(@Nullable final Iterable<IEdge> edgeList)
	{
		if (edgeList != null)
		{
			for (final IEdge edge : edgeList)
			{
				loadImage(edge);
			}
		}
	}

	/**
	 * Load edge image
	 *
	 * @param edge edge
	 */
	private void loadImage(@Nullable final IEdge edge)
	{
		if (edge == null)
		{
			return;
		}

		// edge image
		@Nullable final String source = edge.getImageFile();
		if (source != null)
		{
			// cache lookup
			@Nullable Image image = this.images.get(source);
			if (image == null)
			{
				// cache miss
				image = loadImage(source);
			}
			edge.setImage(image);
		}

		assert this.model != null;

		// set node image from index
		final int imageIndex = edge.getImageIndex();
		if (imageIndex != -1 && this.model.images != null)
		{
			edge.setImage(this.model.images[imageIndex]);
		}
	}

	/**
	 * Load top images
	 *
	 * @param settings settings
	 */
	private void loadTopImages(@NonNull final Settings settings)
	{
		this.backgroundImage = loadImage(settings.backgroundImageFile);
		this.defaultNodeImage = loadImage(settings.defaultNodeImage);
		this.defaultTreeEdgeImage = loadImage(settings.defaultTreeEdgeImage);
		this.defaultEdgeImage = loadImage(settings.defaultEdgeImage);
		assert this.model != null;
		if (this.model.images != null)
		{
			if (settings.backgroundImageIndex != -1)
			{
				this.backgroundImage = this.model.images[settings.backgroundImageIndex];
			}
			if (settings.defaultNodeImageIndex != -1)
			{
				this.defaultNodeImage = this.model.images[settings.defaultNodeImageIndex];
			}
			if (settings.defaultTreeEdgeImageIndex != -1)
			{
				this.defaultTreeEdgeImage = this.model.images[settings.defaultTreeEdgeImageIndex];
			}
			if (settings.defaultEdgeImageIndex != -1)
			{
				this.defaultEdgeImage = this.model.images[settings.defaultEdgeImageIndex];
			}
		}
	}

	/**
	 * Load image given its source
	 *
	 * @param source source
	 * @return image
	 */
	@Nullable
	private Image loadImage(@Nullable final String source)
	{
		if (source == null)
		{
			return null;
		}

		@Nullable final URL url = makeImageURL(source);
		if (url != null)
		{
			if (Widget.DEBUG)
			{
				this.context.status("image=" + url);
			}

			// image loading
			Image image = new Image(url);

			// cache image
			this.images.put(source, image);
			return image;
		}
		return null;
	}

	// F O C U S . N O D E

	/**
	 * Get node to initially receive focus
	 *
	 * @return node
	 */
	@Nullable
	private INode getFocusNode()
	{
		assert this.model != null;

		// focus node
		@Nullable String focusNodeId = null;
		if (this.model.settings.focus != null)
		{
			focusNodeId = this.model.settings.focus;
		}

		// animate
		@Nullable INode focusNode;
		if (focusNodeId == null)
		{
			focusNode = this.model.tree.getRoot();
		}
		else
		{
			focusNode = Finder.findNodeById(this.model.tree.getRoot(), focusNodeId);

			// default to root if not found
			if (focusNode == null)
			{
				focusNode = this.model.tree.getRoot();
			}
		}
		return focusNode;
	}

	// I N T E R A C T I O N

	@SuppressWarnings("WeakerAccess")
	@Override
	public void progress(final String message, final boolean fail)
	{
		this.progress.put(message, fail);
	}

	@Override
	public void message(final String message)
	{
		this.context.status(message);
	}

	@Override
	public void warn(final String message)
	{
		this.context.warn(message);
	}

	// I N P U T

	/**
	 * Get target
	 *
	 * @return target
	 */
	@Nullable
	public String getTarget()
	{
		return this.context.getInput();
	}

	// S T A T U S

	/**
	 * Put information
	 *
	 * @param header  header
	 * @param message message
	 * @param type    type of message
	 */
	private void putStatus(@SuppressWarnings("SameParameterValue") @NonNull final Statusbar.PutType type, final String header, final String... message)
	{
		if (this.statusbar == null)
		{
			return;
		}
		this.statusbar.put(type, null, header, message);
	}

	/**
	 * Put information
	 *
	 * @param type      type of message
	 * @param converter converter
	 * @param header    header
	 * @param message   message
	 */
	public void putStatus(@NonNull final Statusbar.PutType type, @Nullable final Function<CharSequence[], String> converter, final String header, final String... message)
	{
		if (this.statusbar == null)
		{
			return;
		}
		this.statusbar.put(type, converter, header, message);
	}

	// I N F O

	/**
	 * Put information
	 *
	 * @param header  header
	 * @param content content
	 */
	public void putInfo(final CharSequence header, final String[] content)
	{
		@NonNull @SuppressWarnings("TypeMayBeWeakened") final Dialog dialog = new Dialog();
		dialog.setHandle(this.handle);
		dialog.setListener(this.linkActionListener);
		@Nullable final String style = this.context.getStyle();
		dialog.setStyle(style);
		dialog.setConverter((s) -> this.controller.makeHtmlContent(s, Commander.TOOLTIPHTML));
		dialog.set(header, content);
		dialog.display();
	}

	// J A V A S C R I P T

	@Override
	public void focus(final String nodeId)
	{
		if (this.model == null)
		{
			return;
		}

		this.controller.focus(nodeId);
	}

	@Nullable
	@Override
	public String match(@NonNull final String targetString, @Nullable final String scopeString, @Nullable final String modeString)
	{
		if (this.model == null)
		{
			return null;
		}

		@NonNull MatchScope scope = MatchScope.LABEL;
		if (scopeString != null)
		{
			scope = MatchScope.valueOf(scopeString.toUpperCase(Locale.ROOT));
		}

		@NonNull MatchMode mode = MatchMode.EQUALS;
		if (modeString != null)
		{
			mode = MatchMode.valueOf(modeString.toUpperCase(Locale.ROOT));
		}

		// search
		@Nullable final INode foundNode = this.controller.match(targetString, scope, mode);
		if (foundNode != null)
		{
			return foundNode.getId();
		}
		return null;
	}

	@Override
	public void link(final String urlString, final String urlTarget)
	{
		this.controller.linkTo(urlString, urlTarget);
	}

	@Override
	public void search(@NonNull final String commandString, final String... params)
	{
		@NonNull final SearchCommand command = SearchCommand.valueOf(commandString.toUpperCase(Locale.ROOT));
		if (command == SearchCommand.SEARCH)
		{
			@NonNull final MatchScope scope = MatchScope.valueOf(params[0].toUpperCase(Locale.ROOT));
			@NonNull final MatchMode mode = MatchMode.valueOf(params[1].toUpperCase(Locale.ROOT));
			final String target = params[2];
			// scope, mode, target, [start]
			this.controller.search(command, scope, mode, target);
		}
		else
		{
			this.controller.search(command);
		}
	}
}
