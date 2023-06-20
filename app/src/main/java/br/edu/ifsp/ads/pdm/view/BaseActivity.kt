package br.edu.ifsp.ads.pdm.view

import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.ads.pdm.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class BaseActivity: AppCompatActivity() {
  protected val EXTRA_TASK = "Task"
  protected val EXTRA_VIEW_TASK = "ViewTask"

  protected val googleSignInOptions: GoogleSignInOptions by lazy {
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build()
  }

  protected val googleSignInClient: GoogleSignInClient by lazy {
    GoogleSignIn.getClient(this, googleSignInOptions)
  }
}
