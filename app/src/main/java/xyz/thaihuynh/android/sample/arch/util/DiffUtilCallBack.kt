package xyz.thaihuynh.android.sample.arch.util

import androidx.recyclerview.widget.DiffUtil
import xyz.thaihuynh.android.sample.arch.model.Note

class DiffUtilCallBack(private var newList: List<Note>, private var oldList: List<Note>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].id === oldList[oldItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.data == newItem.data && oldItem.title == newItem.title
    }
}