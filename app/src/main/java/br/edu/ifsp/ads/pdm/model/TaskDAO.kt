package br.edu.ifsp.ads.pdm.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDAO {
  @Insert
  fun insertTask(task: Task)
  @Update
  fun updateTask(task: Task): Int
  @Delete
  fun deleteTask(task: Task): Int

  @Query("SELECT * FROM Task")
  fun findAllTasks(): MutableList<Task>
  @Query("SELECT * FROM Task WHERE id = :id")
  fun findTaskByID(id: Int): Task?
  @Query("SELECT COUNT(*) FROM Task WHERE title = :title")
  fun findCountTaskByTitle(title: String): Int
}