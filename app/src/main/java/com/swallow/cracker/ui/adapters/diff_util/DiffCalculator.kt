package com.swallow.cracker.ui.adapters.diff_util

import androidx.recyclerview.widget.DiffUtil

abstract class DiffCalculator<T : Any>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return contentsAreSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return null
    }

    fun calculateDiff(): DiffUtil.DiffResult = DiffUtil.calculateDiff(this)

    abstract fun areSame(first: T, second: T): Boolean

    abstract fun contentsAreSame(first: T, second: T): Boolean
}
