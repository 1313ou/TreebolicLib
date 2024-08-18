/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.res.use
import androidx.preference.PreferenceManager
import org.treebolic.glue.R

/**
 * A layout that splits the available space between two child views.
 *
 * An optionally movable bar exists between the children which allows the user to redistribute the space allocated to each view.
 *
 * @author com.mobidevelop
 * @author Bernard Bou (adapt)
 */
class SplitPaneLayout : ViewGroup {

    /**
     * Orientation
     */
    var orientation: Int = ORIENTATION_HORIZONTAL
        set(orientation) {
            if (field != orientation) {
                field = orientation
                if (childCount == 2) {
                    remeasure()
                }
            }
        }

    /**
     * Splitter position (== Integer.MIN_VALUE if splitterPositionPercent is used)
     */
    private var splitterPosition: Int = 0

    private fun positionSplitterAbsolute(position0: Int) {
            var position = position0
            if (position < 0) {
                position = 0
            }
            splitterPosition = position
            splitterPositionPercent = -1f
            if (childCount == 2) {
                remeasure()
            }
        }

    /**
     * Splitter relative position (== -1 if splitterPosition is used)
     */
    private var splitterPositionPercent: Float = 0F

    fun positionSplitterPercent(positionPercent0: Float) {
            var positionPercent = positionPercent0
            if (positionPercent < 0f) {
                positionPercent = 0f
            }
            if (positionPercent > 1f) {
                positionPercent = 1f
            }
            splitterPositionPercent = positionPercent
            splitterPosition = Int.MIN_VALUE
            if (childCount == 2) {
                remeasure()
            }
        }

    /**
     * Splitter size in pixels
     */
    private var splitterSize = 12
        set(splitterSize0) {
            field = splitterSize0
            if (childCount == 2) {
                remeasure()
            }
        }

    /**
     * Whether the splitter is movable by the user.
     */
    var isSplitterMovable: Boolean = true

    /**
     * Splitter rectangle
     */
    private val splitterRect = Rect()

    /**
     * Whether the splitter is dragging
     */
    private var isDragging = false

    /**
     * Splitter drawable
     */
    var splitterDrawable: Drawable? = null
        set(splitterDrawable0) {
            field = splitterDrawable0
            if (childCount == 2) {
                remeasure()
            }
        }

    /**
     * Dragging splitter drawable
     */
    var splitterDraggingDrawable: Drawable? = null
        set(splitterDraggingDrawable0) {
            field = splitterDraggingDrawable0
            if (isDragging) {
                invalidate()
            }
        }

    /**
     * Splitter rectangle
     */
    private val splitterRectangle = Rect()

    /**
     * Last touch X
     */
    private var lastTouchX = 0

    /**
     * Last touch Y
     */
    private var lastTouchY = 0

    // C O N S T R U C T O R S

    /**
     * Basic constructor
     *
     * @param context context
     */
    constructor(context: Context?) : super(context) {
        orientation = ORIENTATION_HORIZONTAL
        splitterPositionPercent = 0.5f
        splitterPosition = Int.MIN_VALUE
        splitterDrawable = PaintDrawable(SPLITTER_COLOR)
        splitterDraggingDrawable = PaintDrawable(SPLITTER_DRAG_COLOR)
    }

    /**
     * Constructor with attributes
     *
     * @param context context
     * @param attrs   attributes
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        orientation = ORIENTATION_HORIZONTAL
        splitterPositionPercent = 0.5f
        splitterPosition = Int.MIN_VALUE

        extractAttributes(context, attrs)
    }

    /**
     * Constructor with attributes and style
     *
     * @param context  context
     * @param attrs    attributes
     * @param defStyle style
     */
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        orientation = ORIENTATION_HORIZONTAL
        splitterPositionPercent = 0.5f
        splitterPosition = Int.MIN_VALUE

        extractAttributes(context, attrs)
    }

    // A T T R I B U T E S

    private fun extractAttributes(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.SplitPaneLayout).use {

                // misc
                orientation = it.getInt(R.styleable.SplitPaneLayout_orientation, 0)
                splitterSize = it.getDimensionPixelSize(R.styleable.SplitPaneLayout_splitterSize, context.resources.getDimensionPixelSize(R.dimen.spl_default_splitter_size))
                isSplitterMovable = it.getBoolean(R.styleable.SplitPaneLayout_splitterMovable, true)

                // position
                var value = it.peekValue(R.styleable.SplitPaneLayout_splitterPosition)
                if (value != null) {
                    if (value.type == TypedValue.TYPE_DIMENSION) {
                        splitterPosition = it.getDimensionPixelSize(R.styleable.SplitPaneLayout_splitterPosition, Int.MIN_VALUE)
                        splitterPositionPercent = -1f
                    } else if (value.type == TypedValue.TYPE_FRACTION) {
                        splitterPositionPercent = it.getFraction(R.styleable.SplitPaneLayout_splitterPosition, 100, 100, 50f) * 0.01f
                        splitterPosition = Int.MIN_VALUE
                    }
                } else {
                    splitterPosition = Int.MIN_VALUE
                    splitterPositionPercent = 0.5f
                }

                // backgrounds
                value = it.peekValue(R.styleable.SplitPaneLayout_splitterBackground)
                if (value != null) {
                    if (value.type == TypedValue.TYPE_REFERENCE || value.type == TypedValue.TYPE_STRING) {
                        splitterDrawable = it.getDrawable(R.styleable.SplitPaneLayout_splitterBackground)
                    } else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 || value.type == TypedValue.TYPE_INT_COLOR_ARGB4 || value.type == TypedValue.TYPE_INT_COLOR_RGB8 || value.type == TypedValue.TYPE_INT_COLOR_RGB4) {
                        splitterDrawable = PaintDrawable(it.getColor(R.styleable.SplitPaneLayout_splitterBackground, -0x1000000))
                    }
                }
                value = it.peekValue(R.styleable.SplitPaneLayout_splitterDraggingBackground)
                if (value != null) {
                    if (value.type == TypedValue.TYPE_REFERENCE || value.type == TypedValue.TYPE_STRING) {
                        splitterDraggingDrawable = it.getDrawable(R.styleable.SplitPaneLayout_splitterDraggingBackground)
                    } else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 || value.type == TypedValue.TYPE_INT_COLOR_ARGB4 || value.type == TypedValue.TYPE_INT_COLOR_RGB8 || value.type == TypedValue.TYPE_INT_COLOR_RGB4) {
                        splitterDraggingDrawable = PaintDrawable(it.getColor(R.styleable.SplitPaneLayout_splitterDraggingBackground, SPLITTER_DRAG_COLOR))
                    }
                } else {
                    splitterDraggingDrawable = PaintDrawable(SPLITTER_DRAG_COLOR)
                }
            }
        }
    }

    // O V E R R I D E S

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        check()

        if (widthSize > 0 && heightSize > 0) {
            when (orientation) {
                ORIENTATION_HORIZONTAL -> {
                    // neither specified
                    // middle: p=# %=-1
                    if (splitterPosition == Int.MIN_VALUE && splitterPositionPercent < 0) {
                        splitterPosition = widthSize / 2
                    } else if (splitterPosition != Int.MIN_VALUE && splitterPositionPercent < 0) {
                        splitterPositionPercent = splitterPosition.toFloat() / widthSize.toFloat()
                    } else if ( /* splitterPosition == Integer.MIN_VALUE && */splitterPositionPercent >= 0) {
                        splitterPosition = (widthSize * splitterPositionPercent).toInt()
                    }
                    getChildAt(0).measure(MeasureSpec.makeMeasureSpec(splitterPosition - splitterSize / 2, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY))
                    getChildAt(1).measure(MeasureSpec.makeMeasureSpec(widthSize - splitterSize / 2 - splitterPosition, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY))
                }

                ORIENTATION_VERTICAL -> {
                    if (splitterPosition == Int.MIN_VALUE && splitterPositionPercent < 0) {
                        splitterPosition = heightSize / 2
                    } else if (splitterPosition != Int.MIN_VALUE && splitterPositionPercent < 0) {
                        splitterPositionPercent = splitterPosition.toFloat() / heightSize.toFloat()
                    } else if ( /* splitterPosition == Integer.MIN_VALUE && */splitterPositionPercent >= 0) {
                        splitterPosition = (heightSize * splitterPositionPercent).toInt()
                    }
                    getChildAt(0).measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(splitterPosition - splitterSize / 2, MeasureSpec.EXACTLY))
                    getChildAt(1).measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - splitterSize / 2 - splitterPosition, MeasureSpec.EXACTLY))
                }

                else -> {}
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val w = r - l
        val h = b - t
        when (orientation) {
            ORIENTATION_HORIZONTAL -> {
                getChildAt(0).layout(0, 0, splitterPosition - splitterSize / 2, h)
                splitterRect[splitterPosition - splitterSize / 2, 0, splitterPosition + splitterSize / 2] = h
                getChildAt(1).layout(splitterPosition + splitterSize / 2, 0, r, h)
            }

            ORIENTATION_VERTICAL -> {
                getChildAt(0).layout(0, 0, w, splitterPosition - splitterSize / 2)
                splitterRect[0, splitterPosition - splitterSize / 2, w] = splitterPosition + splitterSize / 2
                getChildAt(1).layout(0, splitterPosition + splitterSize / 2, w, h)
            }

            else -> {}
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (splitterDrawable != null) {
            splitterDrawable!!.bounds = splitterRect
            splitterDrawable!!.draw(canvas)
        }
        if (isDragging) {
            splitterDraggingDrawable!!.bounds = splitterRectangle
            splitterDraggingDrawable!!.draw(canvas)
        }
    }

    // T O U C H L I S T E N E R

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isSplitterMovable) {
            val x = event.x.toInt()
            val y = event.y.toInt()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (splitterRect.contains(x, y)) {
                        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        isDragging = true
                        splitterRectangle.set(splitterRect)
                        invalidate()
                        lastTouchX = x
                        lastTouchY = y
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) {
                        when (orientation) {
                            ORIENTATION_HORIZONTAL -> {
                                splitterRectangle.offset(x - lastTouchX, 0)
                            }

                            ORIENTATION_VERTICAL -> {
                                splitterRectangle.offset(0, y - lastTouchY)
                            }

                            else -> {}
                        }
                        lastTouchX = x
                        lastTouchY = y
                        invalidate()
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (isDragging) {
                        isDragging = false
                        when (orientation) {
                            ORIENTATION_HORIZONTAL -> {
                                positionSplitterAbsolute(x)
                            }

                            ORIENTATION_VERTICAL -> {
                                positionSplitterAbsolute(y)
                            }

                            else -> {}
                        }
                        splitterPositionPercent = -1f
                        remeasure()
                        requestLayout()
                    }
                }

                else -> {}
            }
            return true
        }
        return false
    }

    // H E L P E R S

    /**
     * Convenience for calling own measure method.
     */
    private fun remeasure() {
        measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }

    /**
     * Checks that we have exactly two children.
     */
    private fun check() {
        if (childCount != 2) {
            throw RuntimeException("SplitPaneLayout must have exactly two child views.")
        }
    }

    // S A V E   S T A T E

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        restorePref()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        savePref()
    }

    private fun savePref() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val value = splitterPositionPercent
        if (value == -1f) {
            sharedPrefs.edit().remove(SPLITTER_POSITION_PERCENT).apply()
        } else {
            sharedPrefs.edit().putFloat(SPLITTER_POSITION_PERCENT, value).apply()
        }
    }

    private fun restorePref() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val value = sharedPrefs.getFloat(SPLITTER_POSITION_PERCENT, -1f)
        if (value != -1f) {
            splitterPositionPercent = value
        }
    }

    public override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.splitterPositionPercent = splitterPositionPercent
        return savedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        splitterPositionPercent = state.splitterPositionPercent
    }

    /**
     * Holds important values when we need to save instance state.
     */
    class SavedState : BaseSavedState {

        /**
         * Position percent
         */
        var splitterPositionPercent: Float = 0f

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            splitterPositionPercent = `in`.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(splitterPositionPercent)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Creator<SavedState> {

            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    companion object {

        /**
         * Preference key to splitter relative position
         */
        private const val SPLITTER_POSITION_PERCENT = "splitter_position_percent"

        /**
         * Orientation horizontal values
         */
        const val ORIENTATION_HORIZONTAL: Int = 0

        /**
         * Orientation vertical value
         */
        const val ORIENTATION_VERTICAL: Int = 1

        /**
         * Splitter color
         */
        private const val SPLITTER_COLOR = -0x7f000001

        /**
         * Splitter color when dragging
         */
        private const val SPLITTER_DRAG_COLOR = -0x3f000001
    }
}
