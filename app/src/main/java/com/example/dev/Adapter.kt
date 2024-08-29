package com.example.dev

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dev.databinding.TodoOutLayoutBinding


class MyAdapter(
    val onItemClick: (TODO) -> Unit, val onItemRemoved: (TODO) -> Unit
) : RecyclerView.Adapter<MyAdapter.TODOHolder>() {

    private val todoList: MutableList<TODO> = mutableListOf()

    override fun getItemCount() = todoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TODOHolder {
        val binding = TodoOutLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TODOHolder(binding)
    }

    inner class TODOHolder(val binding: TodoOutLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(todo: TODO) {
            binding.todo = todo
            binding.root.setOnClickListener { onItemClick(todo) }
            binding.edit.setOnClickListener { onItemClick(todo) }
            binding.remove.setOnClickListener { onItemRemoved(todo) }
        }
    }

    override fun onBindViewHolder(holder: TODOHolder, position: Int) {
        holder.bindData(todoList[position])
    }

    fun setTODOList(list: List<TODO>) {
        val diffUtil = DiffU(todoList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        todoList.clear()
        todoList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}