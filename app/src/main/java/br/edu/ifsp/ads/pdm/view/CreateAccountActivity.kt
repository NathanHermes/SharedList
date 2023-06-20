package br.edu.ifsp.ads.pdm.view

import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.ads.pdm.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : BaseActivity() {
  private val acab: ActivityCreateAccountBinding by lazy {
    ActivityCreateAccountBinding.inflate(layoutInflater)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(acab.root)

    with(acab) {
      createAccountSaveBt.setOnClickListener {
        val email = createAccountEmailEt.text.toString()
        val pass = createAccountPassEt.text.toString()
        val repeatPass = createAccountRepeatPassEt.text.toString()

        if (pass != repeatPass) {
          Toast.makeText(this@CreateAccountActivity, "Senhas não são iguais", Toast.LENGTH_LONG)
            .show()
          return@setOnClickListener
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnSuccessListener {
          Toast.makeText(this@CreateAccountActivity, "Usuário criado com sucesso", Toast.LENGTH_SHORT)
            .show()
          finish()
        }.addOnFailureListener {
          Toast.makeText(this@CreateAccountActivity, "Falha ao criar um usuário com o email: $email", Toast.LENGTH_SHORT)
            .show()
        }
      }
    }
  }
}