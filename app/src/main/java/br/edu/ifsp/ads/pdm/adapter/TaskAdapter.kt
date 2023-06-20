package br.edu.ifsp.ads.pdm.adapter

import android.graphics.Paint
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.ads.pdm.databinding.TileTaskBinding
import br.edu.ifsp.ads.pdm.model.Task

class TaskAdapter(
  private val tasks: MutableList<Task>,
  private val onTaskClickListener: OnTaskClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
  inner class TaskViewHolder(tileTaskBinding: TileTaskBinding) :
    RecyclerView.ViewHolder(tileTaskBinding.root), View.OnCreateContextMenuListener {
    val taskTitleTv: TextView = tileTaskBinding.tileTaskTitleTv
    val taskDateTv: TextView = tileTaskBinding.tileTaskDateTv
    var taskPosition = -1;

    init {
      tileTaskBinding.root.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
      menu: ContextMenu?,
      view: View?,
      menuInfo: ContextMenu.ContextMenuInfo?
    ) {
      menu?.add(Menu.NONE, 0, 0, "Detail")?.setOnMenuItemClickListener {
        if (taskPosition != -1) onTaskClickListener.onDetailMenuItemClick(taskPosition)
        true
      }
      menu?.add(Menu.NONE, 0, 0, "Edit")?.setOnMenuItemClickListener {
        if (taskPosition != -1) onTaskClickListener.onEditMenuItemClick(taskPosition)
        true
      }
      menu?.add(Menu.NONE, 0, 0, "Remove")?.setOnMenuItemClickListener {
        if (taskPosition != -1) onTaskClickListener.onDeleteMenuItemClick(taskPosition)
        true
      }
      menu?.add(Menu.NONE, 0, 0, "Complete")?.setOnMenuItemClickListener {
        if (taskPosition != -1) onTaskClickListener.onCompletedMenuItemClick(taskPosition)
        true
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
    val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))
    return TaskViewHolder(tileTaskBinding)
  }

  override fun getItemCount(): Int = tasks.size

  override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
    val task = tasks[position]

    if (task.completed) {
      holder.taskTitleTv.paintFlags = holder.taskTitleTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
      holder.taskTitleTv.paintFlags =
        holder.taskTitleTv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    holder.taskTitleTv.text = task.title
    holder.taskDateTv.text = task.previousToFinish
    holder.taskPosition = position

    holder.itemView.setOnClickListener { onTaskClickListener.onTileTaskClick(position) }
  }
}