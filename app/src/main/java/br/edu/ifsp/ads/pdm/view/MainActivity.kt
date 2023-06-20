package br.edu.ifsp.ads.pdm.view

import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.ads.pdm.R
import br.edu.ifsp.ads.pdm.adapter.OnTaskClickListener
import br.edu.ifsp.ads.pdm.adapter.TaskAdapter
import br.edu.ifsp.ads.pdm.controller.TaskController
import br.edu.ifsp.ads.pdm.databinding.ActivityMainBinding
import br.edu.ifsp.ads.pdm.model.Task
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), OnTaskClickListener {
  private val tasks: MutableList<Task> = mutableListOf()
  private lateinit var tarl: ActivityResultLauncher<Intent>
  lateinit var updateViewsHandler: Handler

  private val amb: ActivityMainBinding by lazy {
    ActivityMainBinding.inflate(layoutInflater)
  }
  private val taskAdapter: TaskAdapter by lazy {
    TaskAdapter(tasks, this)
  }
  private val taskController: TaskController by lazy {
    TaskController(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(amb.root)

    taskController.getAllTasks()
    with(amb) {
      mainTasksRv.layoutManager = LinearLayoutManager(this@MainActivity)
      mainTasksRv.adapter = taskAdapter

      tarl = bindTaskActivityResultLauncher()

      updateViewsHandler = Handler(Looper.myLooper()!!) {
        taskController.getAllTasks()
        true
      }

      updateViewsHandler.sendMessageDelayed(Message(), 3000)
    }
  }

  private fun bindTaskActivityResultLauncher(): ActivityResultLauncher<Intent> {
    return registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ) { result ->
      if (result.resultCode == RESULT_OK) {
        val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
          result.data?.getParcelableExtra(EXTRA_TASK)
        }

        task?.let { _task ->
          val position = tasks.indexOfFirst { it.id == task.id }

          if (position != -1) {
            tasks[position] = _task
            taskController.editTask(_task)

            Toast.makeText(this, "Task editada com sucesso", Toast.LENGTH_LONG).show()
            taskAdapter.notifyItemChanged(position)
          } else {
            if (taskController.countTaskByTitle(_task.title) == 0) {
              taskController.saveTask(_task)

              Toast.makeText(this, "Task cadastrada", Toast.LENGTH_LONG).show()
            } else {
              Toast.makeText(this, "Task já cadastrada", Toast.LENGTH_LONG).show()
            }
          }
          taskController.getAllTasks()
          taskAdapter.notifyDataSetChanged()
        }
      }
    }
  }

  fun updateTasks(_tasks: MutableList<Task>) {
    tasks.clear()
    tasks.addAll(_tasks)
    taskAdapter.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId) {
      R.id.addTaskMi -> {
        tarl.launch(Intent(this, TaskActivity::class.java))
        true
      }
      R.id.signOutMi -> {
        FirebaseAuth.getInstance().signOut()
        googleSignInClient.signOut()
        finish()
        true
      }
      else -> false
    }
  }

  override fun onTileTaskClick(position: Int) {
    val task = tasks[position]
    val taskIntent = Intent(this, TaskActivity::class.java)
    taskIntent.putExtra(EXTRA_TASK, task)
    taskIntent.putExtra(EXTRA_VIEW_TASK, true)
    tarl.launch(taskIntent)
  }

  override fun onDetailMenuItemClick(position: Int) {
    val task = tasks[position]
    val taskIntent = Intent(this, TaskActivity::class.java)
    taskIntent.putExtra(EXTRA_TASK, task)
    taskIntent.putExtra(EXTRA_VIEW_TASK, true)
    tarl.launch(taskIntent)
  }

  override fun onEditMenuItemClick(position: Int) {
    val task = tasks[position]
    if(task.completed) {
      Toast.makeText(this, "Essa task já foi conluída,\nportanto não pode ser editada", Toast.LENGTH_SHORT).show()
      return
    }
    val taskIntent = Intent(this, TaskActivity::class.java)
    taskIntent.putExtra(EXTRA_TASK, task)
    tarl.launch(taskIntent)
  }

  override fun onDeleteMenuItemClick(position: Int) {
    val task = tasks[position]
    if(task.completed) {
      Toast.makeText(this, "Essa task já foi conluída,\nportanto não pode ser excluida", Toast.LENGTH_SHORT).show()
      return
    }
    tasks.removeAt(position)
    taskController.deleteTask(task)
    taskAdapter.notifyDataSetChanged()
    Toast.makeText(this, "Task excluida", Toast.LENGTH_SHORT).show()
  }

  override fun onCompletedMenuItemClick(position: Int) {
    val task = tasks[position]
    task.completed = true
    task.completedBy = FirebaseAuth.getInstance().currentUser?.email.toString()
    taskController.editTask(task)

    val viewHolder = amb.mainTasksRv.findViewHolderForAdapterPosition(position)
    if (viewHolder is TaskAdapter.TaskViewHolder) {
      viewHolder.taskTitleTv.paintFlags = viewHolder.taskTitleTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
    taskAdapter.notifyItemChanged(position)
    Toast.makeText(this, "Task concluida", Toast.LENGTH_SHORT).show()
  }

  override fun onStart() {
    super.onStart()
    if (FirebaseAuth.getInstance().currentUser != null) {
      val email = FirebaseAuth.getInstance().currentUser?.email
    }
    else {
      Toast.makeText(this, "Faça login passar acessar a lista de tasks", Toast.LENGTH_SHORT).show()
      finish()
    }
  }
}