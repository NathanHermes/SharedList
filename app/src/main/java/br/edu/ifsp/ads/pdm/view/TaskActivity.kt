package br.edu.ifsp.ads.pdm.view

import android.os.Build
import android.os.Bundle
import android.view.View
import br.edu.ifsp.ads.pdm.databinding.ActivityTaskBinding
import br.edu.ifsp.ads.pdm.model.Task
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date

class TaskActivity : BaseActivity() {
  private val atb: ActivityTaskBinding by lazy {
    ActivityTaskBinding.inflate(layoutInflater)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(atb.root)

    val receivedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
    } else {
      intent.getParcelableExtra(EXTRA_TASK)
    }

    with(atb) {
      receivedTask?.let { _receivedTask ->
        with(_receivedTask) {
          taskTitleEt.setText(title)
          taskDescriptionEt.setText(description)
          taskCreatedAtEt.setText(createdAt)
          taskPreviousToFinishEt.setText(previousToFinish)
          taskCreatedAtEt.setText(createdAt)
          taskCompletedByEt.setText(completedBy)
        }
      }

      val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
      taskTitleEt.isEnabled = !viewTask
      taskDescriptionEt.isEnabled = !viewTask
      taskCreatedAtTv.visibility = if (viewTask) View.VISIBLE else View.GONE
      taskCreatedAtEt.visibility = if (viewTask) View.VISIBLE else View.GONE
      taskCreatedAtEt.isEnabled = !viewTask
      taskPreviousToFinishEt.isEnabled = !viewTask
      taskUserTv.visibility = if (viewTask) View.VISIBLE else View.GONE
      taskUserEt.visibility = if (viewTask) View.VISIBLE else View.GONE
      taskUserEt.isEnabled = !viewTask
      taskSaveBt.visibility = if (viewTask) View.GONE else View.VISIBLE
      if (receivedTask?.completed == true) {
        taskCompletedByTv.visibility = if (viewTask) View.VISIBLE else View.GONE
        taskCompletedByEt.visibility = if (viewTask) View.VISIBLE else View.GONE
        taskCompletedByEt.isEnabled = !viewTask
      }

      taskSaveBt.setOnClickListener {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val date = Date()
        val dateCreate = sdf.format(date)
        val task = Task(
          id = receivedTask?.id,
          title = taskTitleEt.text.toString(),
          description = taskDescriptionEt.text.toString(),
          createdAt = dateCreate,
          previousToFinish = taskPreviousToFinishEt.text.toString(),
          createdBy = FirebaseAuth.getInstance().currentUser?.email.toString()
        )

        val resultIntent = intent
        resultIntent.putExtra(EXTRA_TASK, task)
        setResult(RESULT_OK, resultIntent)
        finish()
      }
    }
  }
}