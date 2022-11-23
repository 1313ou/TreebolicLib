/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface.component;

/**
 * Glue interface for PopupMenu
 *
 * @param <C> component type
 * @param <L> platform action listener
 * @author Bernard Bou
 */
public interface PopupMenu<C, L>
{
	/**
	 * Indexes to labels
	 */
	enum LabelIndices
	{
		/**
		 * Cancel label
		 */
		LABEL_CANCEL,
		/**
		 * INfo label
		 */
		LABEL_INFO,
		/**
		 * Focus label
		 */
		LABEL_FOCUS,
		/**
		 * Link to label
		 */
		LABEL_LINKTO,
		/**
		 * Mount label
		 */
		LABEL_MOUNT,
		/**
		 * Unmount label
		 */
		LABEL_UNMOUNT,
		/**
		 * Goto label
		 */
		LABEL_GOTO,
		/**
		 * Search label
		 */
		LABEL_SEARCH,
		/**
		 * Label count
		 */
		LABEL_COUNT
	}

	/**
	 * Indexes to images
	 */
	enum ImageIndices
	{
		/**
		 * Cancel image
		 */
		IMAGE_CANCEL,
		/**
		 * Info image
		 */
		IMAGE_INFO,
		/**
		 * Focus image
		 */
		IMAGE_FOCUS,
		/**
		 * Link image
		 */
		IMAGE_LINK,
		/**
		 * Mount image
		 */
		IMAGE_MOUNT,
		/**
		 * Goto image
		 */
		IMAGE_GOTO,
		/**
		 * Search image
		 */
		IMAGE_SEARCH,
		/**
		 * Image count
		 */
		IMAGE_COUNT
	}

	/**
	 * Add item
	 *
	 * @param label      label String
	 * @param imageIndex image index (as per ImageIndices ordinals)
	 * @param listener   listener
	 */
	void addItem(final String label, final int imageIndex, final L listener);

	/**
	 * Add item
	 *
	 * @param labelIndex label index (as per LabelIndices ordinals)
	 * @param imageIndex image index (as per ImageIndices ordinals)
	 * @param listener   listener
	 */
	void addItem(final int labelIndex, final int imageIndex, final L listener);

	/**
	 * Popup component at position
	 *
	 * @param component component to popup
	 * @param x         x-position
	 * @param y         y-position
	 */
	void popup(C component, int x, int y);
}
