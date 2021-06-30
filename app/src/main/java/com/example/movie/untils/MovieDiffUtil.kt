package com.example.movie.untils

import androidx.recyclerview.widget.DiffUtil
import com.example.movie.models.MoviesItem
import com.google.firebase.database.core.view.Change

class MovieDiffUtil(
    private val oldList: List<MoviesItem>,
    private val newList: List<MoviesItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]


    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return getChangePayload(
            oldItemPosition,
            newItemPosition
        )
    }


}