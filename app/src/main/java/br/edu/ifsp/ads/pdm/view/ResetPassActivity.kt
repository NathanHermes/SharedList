package br.edu.ifsp.ads.pdm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.ads.pdm.R
import br.edu.ifsp.ads.pdm.databinding.ActivityResetPassBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPassActivity : BaseActivity() {
  private val arpb: ActivityResetPassBinding by lazy {
    ActivityResetPassBinding.inflate(layoutInflater)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(arpb.root)

    with(arpb) {
      resetPassSendBt.setOnClickListener {
        val email = resetPassEmailEt.text.toString()

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { result ->
          if (result.isSuccessful) {
            Toast.makeText(
              this@ResetPassActivity,
              "Email de recuperação enviado",
              Toast.LENGTH_SHORT
            ).show()
            finish()
          } else {
            Toast.makeText(
              this@ResetPassActivity,
              "Falha ao enviar email de recuperação para o email: $email",
              Toast.LENGTH_SHORT
            )
              .show()
          }
        }
      }
    }
  }
}