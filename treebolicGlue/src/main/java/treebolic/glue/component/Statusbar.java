package treebolic.glue.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.treebolic.glue.R;

import treebolic.glue.ActionListener;
import treebolic.glue.Color;

/**
 * Status bar
 *
 * @author Bernard Bou
 */
@SuppressLint("ViewConstructor")
public class Statusbar extends FrameLayout implements treebolic.glue.iface.component.Statusbar<Color, ActionListener>
{
	/**
	 * Image index enum
	 */
	public enum ImageIndices
	{
		INFO, LINK, MOUNT, SEARCH
	}

	/**
	 * Base URL for webview
	 */
	static String base = "file:///android_asset/"; //$NON-NLS-1$

	/**
	 * Drawables
	 */
	static private final Drawable[] drawables = new Drawable[ImageIndices.values().length];

	/**
	 * Main status view
	 */
	private final TextView statusView;

	/**
	 * Content status view
	 */
	private final WebView contentView;

	/**
	 * Activity
	 */
	private final AppCompatActivity activity;

	/**
	 * Horizontal / vertical
	 */
	private final boolean isHorizontal;

	/**
	 * Style
	 */
	private String style;

	/**
	 * Background
	 */
	private final int background;

	/**
	 * Foreground
	 */
	private final int foreground;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param activity0
	 *            activity
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings("deprecation")
	protected Statusbar(final AppCompatActivity activity0)
	{
		super(activity0);
		this.activity = activity0;

		// determine orientation
		final WindowManager windowManager = (WindowManager) this.activity.getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		final boolean isHorizontalScreen = size.x >= size.y;
		this.isHorizontal = !isHorizontalScreen;

		// inflate
		final LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup wrappedView = (ViewGroup) inflater.inflate(isHorizontalScreen ? R.layout.status_h : R.layout.status_v, this);
		this.statusView = (TextView) wrappedView.findViewById(R.id.status);
		this.contentView = (WebView) wrappedView.findViewById(R.id.content);
		this.contentView.setFocusable(false);

		// colors
		final Resources resources = getResources();
		this.background = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? resources.getColor(R.color.background, null) : resources.getColor(R.color.background);
		this.foreground = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? resources.getColor(R.color.foreground, null) : resources.getColor(R.color.foreground);
		this.contentView.setBackgroundColor(this.background);
	}

	/**
	 * Constructor from handle
	 *
	 * @param handle
	 *            activity
	 */
	protected Statusbar(final Object handle)
	{
		this((AppCompatActivity) handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.component.Statusbar#init(treebolic.glue.Font, int)
	 */
	@Override
	public void init(final int image)
	{
		if (this.isHorizontal)
		{
			this.statusView.setCompoundDrawablesWithIntrinsicBounds(getDrawable(image), null, null, null);
		}
		else
		{
			this.statusView.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(image), null, null);
		}
	}

	@Override
	public void addListener(ActionListener arg0)
	{
		// does not fire events
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.component.Statusbar#setColors(treebolic.glue.Color, treebolic.glue.Color)
	 */
	@Override
	public void setColors(final Color backColor, final Color foreColor)
	{
		//
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.component.Statusbar#setStyle(java.lang.String)
	 */
	@Override
	public void setStyle(final String style0)
	{
		this.style = style0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.component.Statusbar#put(java.lang.String, java.lang.String, int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void put(final String label, final String content, final int image)
	{
		// System.out.println("put("+label+","+content+");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// icon
		if (this.isHorizontal)
		{
			this.statusView.setCompoundDrawablesWithIntrinsicBounds(getDrawable(image), null, null, null);
		}
		else
		{
			this.statusView.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(image), null, null);
		}

		// label
		this.statusView.setText(label == null ? "" : label); //$NON-NLS-1$

		// content
		if (content == null)
		{
			if (Build.VERSION.SDK_INT < 18)
			{
				this.contentView.clearView();
			}
			else
			{
				this.contentView.loadUrl("about:blank"); //$NON-NLS-1$
			}
		}
		else
		{
			final StringBuilder html = new StringBuilder();
			html.append("<html><head>"); //$NON-NLS-1$
			html.append("<style type='text/css'>"); //$NON-NLS-1$
			html.append(getDefaultBaseStyle());
			if (this.style != null && !this.style.isEmpty())
			{
				html.append(this.style);
			}
			html.append("</style>"); //$NON-NLS-1$
			html.append("</head><body>"); //$NON-NLS-1$
			html.append(content);
			html.append("</body>"); //$NON-NLS-1$
			this.contentView.loadDataWithBaseURL(Statusbar.base, html.toString(), "text/html; charset=UTF-8", "UTF-8", null); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.component.Statusbar#put(java.lang.String)
	 */
	@Override
	public void put(final String message)
	{
		this.statusView.setText(message);
	}

	/**
	 * Get drawable from index
	 *
	 * @param index
	 *            index
	 * @return drawable
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@SuppressWarnings("deprecation")
	private Drawable getDrawable(final int index)
	{
		if (Statusbar.drawables[index] == null)
		{
			int res = -1;
			switch (ImageIndices.values()[index])
			{
			case INFO:
				res = R.drawable.status_info;
				break;
			case LINK:
				res = R.drawable.status_link;
				break;
			case MOUNT:
				res = R.drawable.status_mount;
				break;
			case SEARCH:
				res = R.drawable.status_search;
				break;
			default:
				break;
			}
			if (res != -1)
			{
				Statusbar.drawables[index] = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP //
						? this.activity.getResources().getDrawable(res, null) : this.activity.getResources().getDrawable(res);
			}
		}
		return Statusbar.drawables[index];
	}

	/**
	 * Default style
	 *
	 * @return default style
	 */
	@SuppressWarnings("boxing")
	private String getDefaultBaseStyle()
	{
		return "body {" + //$NON-NLS-1$
				String.format("background-color: #%06X;", 0xFFFFFF & this.background) + //$NON-NLS-1$
				String.format("color: #%06X;", 0xFFFFFF & this.foreground) + //$NON-NLS-1$
				'}';
	}

	/**
	 * Set base for status bar
	 * 
	 * @param base0
	 *            base URL
	 */
	public static void setBase(String base0)
	{
		Statusbar.base = base0;
	}
}
