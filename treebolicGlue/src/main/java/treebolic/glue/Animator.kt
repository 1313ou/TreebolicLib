/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.animation.LinearInterpolator
import treebolic.glue.iface.Animator

/**
 * Animator
 *
 * @author Bernard Bou
 */
class Animator : Animator<ActionListener?>, AnimatorUpdateListener, android.animation.Animator.AnimatorListener {

    /**
     * Animation listener
     */
    private lateinit var listener: ActionListener

    /**
     * Android animator
     */
    private lateinit var animator: ValueAnimator

    /**
     * Android last step
     */
    private var lastStep = 0

    /**
     * Constructor
     */
    init {
        ValueAnimator.setFrameDelay(ANIMATIONTIMESLICE.toLong())
    }

    override fun run(listener0: ActionListener?, steps: Int, startDelay: Int): Boolean {
        lastStep = steps - 1
        listener = listener0!!
        animator = ValueAnimator.ofInt(0, lastStep)
        animator.repeatCount = 0
        animator.setDuration(1000)
        animator.setStartDelay(startDelay.toLong())
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener(this)
        animator.addListener(this)
        animator.start()
        return true
    }

    override fun isRunning(): Boolean {
        return animator.isRunning
    }

    override fun onAnimationUpdate(animator: ValueAnimator) {
        val step = animator.animatedValue as Int
        listener.onAction(step)
    }

    override fun onAnimationStart(animator: android.animation.Animator) {
    }

    override fun onAnimationEnd(animator: android.animation.Animator) {
        listener.onAction(lastStep)
    }

    override fun onAnimationCancel(animator: android.animation.Animator) {
        listener.onAction(lastStep)
    }

    override fun onAnimationRepeat(animator: android.animation.Animator) {
    }

    companion object {

        /**
         * Animation time slice
         */
        private const val ANIMATIONTIMESLICE = 250
    }
}
