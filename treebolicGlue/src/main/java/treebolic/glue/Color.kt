/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue

/**
 * Color treebolic glue
 *
 * @author Bernard Bou
 */
object Color {

    /**
     * Make opaque, ensure that color is opaque
     *
     * @param color color
     * @return opaque color
     */
    @JvmStatic
    fun makeOpaque(color: Int): Int {
        return -0x1000000 or color
    }
}
