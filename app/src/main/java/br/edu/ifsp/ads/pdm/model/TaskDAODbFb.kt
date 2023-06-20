package br.edu.ifsp.ads.pdm.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class TaskDAODbFb : TaskDAO {
  private val TASK_LIST_ROOT_NODE = "tasks"
  private val taskRtDbFbReference = FirebaseDatabase.getInstance().getReference(TASK_LIST_ROOT_NODE)

  private val tasks: MutableList<Task> = mutableListOf()

  init {
    taskRtDbFbReference.addChildEventListener(object : ChildEventListener {
      override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val task: Task? = snapshot.getValue<Task>()
        task?.let { _task ->
          if (!tasks.any { _task.title == it.title }) tasks.add(_task)
        }
      }

      override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        val task: Task? = snapshot.getValue<Task>()
        task?.let { _task ->
          val indexOfTask = tasks.indexOfFirst { _task.title == it.title }
          tasks[indexOfTask] = _task
        }
      }

      override fun onChildRemoved(snapshot: DataSnapshot) {
        val task: Task? = snapshot.getValue<Task>()
        task?.let { _task -> tasks.remove(_task) }
      }

      override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        TODO("Not yet implemented")
      }

      override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
      }
    })

    taskRtDbFbReference.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val taskHashMap = snapshot.getValue<HashMap<String, Task>>()
        tasks.clear()
        taskHashMap?.values?.forEach { _task -> tasks.add(_task) }
      }

      override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
      }
    })
  }

  override fun insertTask(task: Task) {
    taskRtDbFbReference.child(task.title).setValue(task)
  }

  override fun updateTask(task: Task): Int {
    taskRtDbFbReference.child(task.title).setValue(task)
    return 1
  }

  override fun deleteTask(task: Task): Int {
    taskRtDbFbReference.child(task.title).removeValue()
    return 1
  }

  override fun findAllTasks(): MutableList<Task> {
    return tasks
  }

  override fun findTaskByID(id: Int): Task? {
    TODO("Not yet implemented")
  }

  override fun findCountTaskByTitle(title: String): Int {
    return tasks.count { _task -> _task.title == title }
  }
}