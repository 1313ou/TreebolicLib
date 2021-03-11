/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.control.Controller;
import treebolic.glue.ActionListener;
import treebolic.model.INode;
import treebolic.model.MenuItem;
import treebolic.model.MenuItem.Action;
import treebolic.model.MountPoint;
import treebolic.model.Settings;
import treebolic.view.View;

/**
 * Popup context menu
 *
 * @author Bernard Bou
 */
public class PopupMenu extends treebolic.glue.component.PopupMenu
{
	// private static final long serialVersionUID = 6316113839021843464L;

	/**
	 * Indexes to labels
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_CANCEL = 0;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_INFO = 1;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_FOCUS = 2;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_LINKTO = 3;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_MOUNT = 4;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_UNMOUNT = 5;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_GOTO = 6;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_SEARCH = 7;

	// static String[] labels defined in glue for localization

	/**
	 * Constructor
	 */
	@SuppressWarnings("WeakerAccess")
	public PopupMenu(@NonNull final View view)
	{
		super(view);
	}

	// public void show(final Component parent, final int x, final int y)

	/**
	 * Make popup menu
	 *
	 * @param view       view
	 * @param controller controller
	 * @param value      input value
	 * @param node       target node
	 * @param settings   settings
	 * @return popup menu
	 */
	@NonNull
	static public PopupMenu makePopup(@NonNull final View view, @NonNull final Controller controller, final String value, @NonNull final INode node, @NonNull final Settings settings)
	{
		final PopupMenu popupMenu = new PopupMenu(view);

		// info
		assert labels != null;
		popupMenu.addItem(labels[LABEL_INFO], ImageIndices.IMAGE_INFO.ordinal(), new ActionListener()
		{
			@SuppressWarnings("SameReturnValue")
			@Override
			public boolean onAction(final Object... params)
			{
				controller.dispatch(Action.INFO, null, null, null, null, null, node);
				return true;
			}
		});

		// focus
		popupMenu.addItem(labels[LABEL_FOCUS], ImageIndices.IMAGE_FOCUS.ordinal(), new ActionListener()
		{
			@SuppressWarnings("SameReturnValue")
			@Override
			public boolean onAction(final Object... params)
			{
				controller.dispatch(Action.FOCUS, null, null, null, null, null, node);
				return true;
			}
		});

		// mount
		final MountPoint mountPoint = node.getMountPoint();
		if (mountPoint != null)
		{
			@SuppressWarnings("InstanceofConcreteClass") final boolean isMounted = mountPoint instanceof MountPoint.Mounted;
			popupMenu.addItem(labels[isMounted ? LABEL_UNMOUNT : LABEL_MOUNT], ImageIndices.IMAGE_MOUNT.ordinal(), new ActionListener()
			{
				@SuppressWarnings("SameReturnValue")
				@Override
				public boolean onAction(final Object... params)
				{
					controller.dispatch(Action.MOUNT, null, null, null, null, null, node);
					return true;
				}
			});
		}

		// link
		if (PopupMenu.isURL(node.getLink()))
		{
			popupMenu.addItem(labels[LABEL_LINKTO], ImageIndices.IMAGE_LINK.ordinal(), new ActionListener()
			{
				@SuppressWarnings("SameReturnValue")
				@Override
				public boolean onAction(final Object... params)
				{
					controller.dispatch(Action.LINK, null, null, null, null, null, node);
					return true;
				}
			});
		}

		// custom
		if (settings.menu != null)
		{
			for (final MenuItem menuItem : settings.menu)
			{
				String menuLabel = null;
				boolean prepend = menuItem.label != null && (menuItem.label.length() == 0 || Character.isLowerCase(menuItem.label.charAt(0)));
				assert menuItem.action != null;
				switch (menuItem.action)
				{
					case GOTO:
						// check
						if (controller.getGotoTarget(menuItem.link, node) == null)
						{
							// illegal combination
							continue;
						}
						menuLabel = prepend ? labels[LABEL_GOTO] + ' ' + menuItem.label : menuItem.label;
						break;
					case SEARCH:
						if (controller.getSearchTarget(menuItem.target, node) == null)
						{
							// illegal combination
							continue;
						}
						menuLabel = prepend ? labels[LABEL_SEARCH] + ' ' + menuItem.label : menuItem.label;
						break;
					default:
						break;
				}

				try
				{
					menuLabel = PopupMenu.expandMacro(menuLabel, value, node);

					// no item without label
					if (menuLabel == null)
					{
						continue;
					}

					// assume ImageIndices.ordinal() = control.Action.ordinal()
					popupMenu.addItem(menuLabel, menuItem.action.ordinal(), new ActionListener()
					{
						@SuppressWarnings("SameReturnValue")
						@Override
						public boolean onAction(final Object... params)
						{
							controller.dispatch(menuItem.action, menuItem.link, menuItem.target, menuItem.matchTarget, menuItem.matchScope, menuItem.matchMode, node);
							return true;
						}
					});
				}
				catch (@NonNull final ArrayIndexOutOfBoundsException ignored)
				{
					// do nothing
				}
			}
		}

		// cancel
		popupMenu.addItem(labels[LABEL_CANCEL], ImageIndices.IMAGE_CANCEL.ordinal(), new ActionListener()
		{
			@SuppressWarnings("SameReturnValue")
			@Override
			public boolean onAction(final Object... params)
			{
				return false;
			}
		});

		return popupMenu;
	}

	// U R L . V A L I D A T I O N

	static private boolean isURL(@Nullable final String link)
	{
		if (link != null && !link.isEmpty())
		{
			// well-formed URL
			try
			{
				/* URL url = */
				new URL(link);
				return true;
			}
			catch (@NonNull final MalformedURLException ignored)
			{
				// well-formed URI
				try
				{
					final URI uri = new URI(link);

					// relative form not including scheme
					if (link.equals(uri.getPath()))
					{
						return true;
					}

					// fragment
					final String fragment = '#' + uri.getFragment();
					//noinspection SimplifiableIfStatement
					if (link.equals(fragment))
					{
						return true;
					}

					// desperate attempt
					return uri.getScheme().matches("[a-z]*");
				}
				catch (@NonNull final URISyntaxException ignored2)
				{
					//
				}
			}
		}
		return false;
	}

	// S T R I N G . E X P A N D

	/**
	 * Expand string
	 *
	 * @param str   string to expand
	 * @param value interactively supplied value
	 * @param node  node node
	 * @return expanded string
	 */
	@Nullable
	static public String expandMacro(@Nullable final CharSequence str, @Nullable final String value, @NonNull final INode node)
	{
		if (str == null)
		{
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		final int n = str.length();
		for (int i = 0; i < n; i++)
		{
			final char c = str.charAt(i);
			if (c != '$')
			{
				sb.append(c);
			}
			else
			{
				if (i < n - 1)
				{
					// c is not last : peek at next
					final char c2 = str.charAt(++i);
					switch (c2)
					{

						// label
						case 'l':
							final String label = node.getLabel();
							if (label != null)
							{
								sb.append(label.toCharArray());
							}
							break;

						// content
						case 'c':
							final String content = node.getContent();
							if (content != null)
							{
								sb.append(content.toCharArray());
							}
							break;

						// link url
						case 'u':
							final String link = node.getLink();
							if (link != null)
							{
								sb.append(link.toCharArray());
							}
							break;

						// id
						case 'i':
							final String id = node.getId();
							if (id != null)
							{
								sb.append(id.toCharArray());
							}
							break;

						// parent
						case 'p':
							final INode parent = node.getParent();
							if (parent != null)
							{
								final String parentId = parent.getId();
								if (parentId != null)
								{
									sb.append(parentId.toCharArray());
								}
							}
							break;

						// element value
						case 'e':
							if (value != null)
							{
								sb.append(value.toCharArray());
							}
							break;

						// escaped $
						case '$':
							sb.append(c2);
							break;

						// unrecognized
						default:
							sb.append(c);
							sb.append(c2);
					}
				}
				else
				{
					// is last : copy
					sb.append(c);
				}
			}
		}
		final String result = sb.toString();
		if (result.isEmpty())
		{
			return null;
		}
		return result;
	}
}
