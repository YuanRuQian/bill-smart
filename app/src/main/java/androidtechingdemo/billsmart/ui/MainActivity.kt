package androidtechingdemo.billsmart.ui

import android.os.Bundle
import androidtechingdemo.billsmart.R
import androidtechingdemo.billsmart.ui.auth.LoginFragment
import androidtechingdemo.billsmart.ui.auth.RegisterFragment
import androidtechingdemo.billsmart.ui.home.HomeFragment
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth
  private lateinit var authStateListener: FirebaseAuth.AuthStateListener

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_main)

    auth = FirebaseAuth.getInstance()

    authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      val user = firebaseAuth.currentUser
      if (user != null) {
        // User is signed in, navigate to HomeFragment
        navigateToHomeFragment()
      } else {
        // User is signed out, navigate to LoginFragment
        navigateToLoginFragment()
      }
    }

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    // Retrieve the NavController.
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController

    navController.graph = navController.createGraph(
      startDestination = ScreenSetting.LOGIN.label,
    ) {
      fragment<LoginFragment>(ScreenSetting.LOGIN.label) {
        label = ScreenSetting.LOGIN.label
      }

      fragment<RegisterFragment>(ScreenSetting.REGISTER.label) {
        label = ScreenSetting.REGISTER.label
      }

      fragment<HomeFragment>(ScreenSetting.HOME.label) {
        label = ScreenSetting.HOME.label
      }
    }
  }

  override fun onStart() {
    super.onStart()
    // Add AuthStateListener when the activity starts
    auth.addAuthStateListener(authStateListener)
  }

  override fun onStop() {
    super.onStop()
    // Remove AuthStateListener when the activity stops
    auth.removeAuthStateListener(authStateListener)
  }

  private fun navigateToLoginFragment() {
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController
    navController.navigate(ScreenSetting.LOGIN.label) // Assuming the ID of your LoginFragment is loginFragment
  }

  private fun navigateToHomeFragment() {
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController
    navController.navigate(ScreenSetting.HOME.label) // Assuming the ID of your HomeFragment is homeFragment
  }
}
