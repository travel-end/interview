package com.journey.interview.customizeview.swipecaptcha.core

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

object GlideUtil {

    /**
     * Glide加载网络图片
     * 包含一个加载过程的回调
     */
    fun loadImg(
        context: Context,
        url: String,
        imageView: ImageView,
        error:Int=0,
        onGlideLoadReady: OnGlideLoadReady?
    ) {
        val options = RequestOptions()
            .error(error)
            .priority(Priority.HIGH)
        Glide.with(context)
            .load(url)
            .apply(options)
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {

//                    val bd = resource as BitmapDrawable

//                    imageView.setImageBitmap(bd.bitmap)
                    imageView.setImageDrawable(resource)
                    onGlideLoadReady?.ready(true)

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    onGlideLoadReady?.ready(false)
                }
            })
    }

    fun loadNetImg(context: Context, imgUrl: String?, imageView: ImageView) {
        imgUrl?.let {
            Glide.with(context)
                .load(imgUrl)
                .into(imageView)
        }
    }


    interface OnGlideLoadReady {
        fun ready(isOk: Boolean)
    }
}