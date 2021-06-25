package com.example.movie

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


// bindingAdapter
object MovieBindingAdapter {

    @BindingAdapter("movieImage")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: String) {

        if (url.isEmpty())
        else
            //placeholder() : Glide 로 이미지 로딩을 시작하기 전에 보여줄 이미지를 설정한다.
        //error() : 리소스를 불러오다가 에러가 발생했을 때 보여줄 이미지를 설정한다.
        //fallback() : load할 url이 null인 경우 등 비어있을 때 보여줄 이미지를 설정한다
            Glide.with(imageView.context).load(url).error(R.drawable.test_post).fallback(R.drawable.test_post).placeholder(R.drawable.test_post).into(imageView)
    }

    @BindingAdapter("movieRating")
    @JvmStatic
    fun setRating(view: RatingBar, rating: Double) {
        if (view.rating != rating.toFloat()) {
            view.rating = rating.toFloat() / 2
        }
    }

    @BindingAdapter("movieVote")
    @JvmStatic
    fun setVote(textView: TextView, vote: Int) {
        textView.text = vote.toString()
    }

    @BindingAdapter("movieRunTime")
    @JvmStatic
    fun setRunTime(textView: TextView, time: Int) {
        textView.text = time.toString()
    }

}




