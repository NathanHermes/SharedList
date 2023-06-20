package br.edu.ifsp.ads.pdm.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class Task(
  @PrimaryKey(autoGenerate = true) val id: Int? = -1,
  @NonNull var title: String = "",
  @NonNull var description: String = "",
  @NonNull var createdAt: String = "",
  @NonNull var previousToFinish: String = "",
  @NonNull var createdBy: String = "",
  @NonNull var completed: Boolean = false,
  var completedBy: String = ""
  ): Parcelable