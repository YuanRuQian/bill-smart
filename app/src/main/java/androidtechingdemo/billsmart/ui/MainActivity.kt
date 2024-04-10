package androidtechingdemo.billsmart.ui

import android.os.Bundle
import androidtechingdemo.billsmart.R
import androidtechingdemo.billsmart.ui.auth.LoginFragment
import androidtechingdemo.billsmart.ui.auth.RegisterFragment
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_main)
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
    }
  }
}
