package ru.tech.imageresizershrinker.coredata.image.filters

import android.content.Context
import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBilateralBlurFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import ru.tech.imageresizershrinker.coredomain.image.filters.Filter

class BilaterialBlurFilter(
    private val context: Context,
    override val value: Float = -8f,
) : GPUFilterTransformation(context), Filter.BilaterialBlur<Bitmap> {
    override val cacheKey: String
        get() = (value to context).hashCode().toString()

    override fun createFilter(): GPUImageFilter = GPUImageBilateralBlurFilter(-value)
}