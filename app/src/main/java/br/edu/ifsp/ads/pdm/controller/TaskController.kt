package br.edu.ifsp.ads.pdm.controller

import br.edu.ifsp.ads.pdm.model.Task
import br.edu.ifsp.ads.pdm.model.TaskDAO
import br.edu.ifsp.ads.pdm.model.TaskDAODbFb
import br.edu.ifsp.ads.pdm.view.MainActivity

class TaskController(private val mainActivity: MainActivity) {
  private val taskDAOImpl: TaskDAO = TaskDAODbFb()

  fun saveTask(task: Task) {
    Thread { taskDAOImpl.insertTask(task) }.start()
  }

  fun editTask(task: Task) {
    Thread { taskDAOImpl.updateTask(task) }.start()
  }

  fun deleteTask(task: Task) {
    Thread { taskDAOImpl.deleteTask(task) }.start()
  }

  fun getAllTasks() {
    Thread {
      val tasks = taskDAOImpl.findAllTasks()
      mainActivity.runOnUiThread { mainActivity.updateTasks(tasks) }
    }.start()
  }

  fun countTaskByTitle(title: String)  = taskDAOImpl.findCountTaskByTitle(title)
}