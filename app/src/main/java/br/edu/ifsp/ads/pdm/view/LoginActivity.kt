package br.edu.ifsp.ads.pdm.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.ads.pdm.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : BaseActivity() {
  private val alb: ActivityLoginBinding by lazy {
    ActivityLoginBinding.inflate(layoutInflater)
  }

  private lateinit var gsarl: ActivityResultLauncher<Intent>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(alb.root)

    gsarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == RESULT_OK) {
        val googleAccount: GoogleSignInAccount =
          GoogleSignIn.getSignedInAccountFromIntent(result.data).result
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
          Toast.makeText(
            this@LoginActivity,
            "Bem vindo, ${googleAccount.email}",
            Toast.LENGTH_SHORT
          ).show()
          openMainActivity()
        }.addOnFailureListener {
          Toast.makeText(this, "Falha ao acessar", Toast.LENGTH_SHORT).show()
        }
      }
    }

    with(alb) {
      loginLoginBt.setOnClickListener {
        signWithEmailAndPass()
      }

      loginPasswordResetBt.setOnClickListener {
        startActivity(Intent(this@LoginActivity, ResetPassActivity::class.java))
      }

      loginCreateAccountBt.setOnClickListener {
        startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java))
      }

      loginGoogleBt.setOnClickListener {
        signWithGoogle()
      }
    }
  }

  override fun onStart() {
    super.onStart()
    if (FirebaseAuth.getInstance().currentUser != null) openMainActivity()
  }

  private fun signWithEmailAndPass() {
    val email = alb.loginEmailEt.text.toString()
    val pass = alb.loginPassEt.text.toString()

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnSuccessListener {
      Toast.makeText(this@LoginActivity, "Bem vindo, $email", Toast.LENGTH_SHORT).show()
      openMainActivity()
    }.addOnFailureListener {
      Toast.makeText(this@LoginActivity, "Falha ao logar com o email: $email", Toast.LENGTH_SHORT)
        .show()
    }
  }

  private fun signWithGoogle() {
    val googleAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
    if (googleAccount == null) gsarl.launch(googleSignInClient.signInIntent)
    else openMainActivity()
  }

  private fun openMainActivity() {
    startActivity(Intent(this, MainActivity::class.java))
    finish()
  }
}