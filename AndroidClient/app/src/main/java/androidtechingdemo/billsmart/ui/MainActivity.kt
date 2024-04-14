package androidtechingdemo.billsmart.ui

import android.os.Bundle
import android.view.View
import androidtechingdemo.billsmart.R
import androidtechingdemo.billsmart.ui.account.AccountFragment
import androidtechingdemo.billsmart.ui.activity.ActivityFragment
import androidtechingdemo.billsmart.ui.auth.LoginFragment
import androidtechingdemo.billsmart.ui.auth.RegisterFragment
import androidtechingdemo.billsmart.ui.expense.ExpenseFragment
import androidtechingdemo.billsmart.ui.friends.FriendsFragment
import androidtechingdemo.billsmart.ui.groups.GroupsFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth
  private lateinit var authStateListener: FirebaseAuth.AuthStateListener
  private lateinit var bottomNavigationView: BottomNavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    auth = FirebaseAuth.getInstance()

    bottomNavigationView = findViewById(R.id.bottomNavigationView)

    bottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

    bottomNavigationView.setOnItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.friendsTab -> {
          setCurrentFragment(ScreenSetting.FRIENDS)
          true
        }

        R.id.groupsTab -> {
          setCurrentFragment(ScreenSetting.GROUPS)
          true
        }

        R.id.expenseTab -> {
          setCurrentFragment(ScreenSetting.EXPENSE)
          true
        }

        R.id.activityTab -> {
          setCurrentFragment(ScreenSetting.ACTIVITY)
          true
        }

        R.id.accountTab -> {
          setCurrentFragment(ScreenSetting.ACCOUNT)
          true
        }

        else -> {
          false
        }
      }
    }

    authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      val user = firebaseAuth.currentUser
      if (user != null) {
        // User is signed in, clear all back stack
        setCurrentFragment(ScreenSetting.FRIENDS)
        bottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.menu.findItem(R.id.friendsTab).isChecked = true
      } else {
        // User is signed out, clear all back stack
        setCurrentFragment(ScreenSetting.LOGIN)
        bottomNavigationView.visibility = View.GONE
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

      fragment<FriendsFragment>(ScreenSetting.FRIENDS.label) {
        label = ScreenSetting.FRIENDS.label
      }

      fragment<GroupsFragment>(ScreenSetting.GROUPS.label) {
        label = ScreenSetting.GROUPS.label
      }

      fragment<ExpenseFragment>(ScreenSetting.EXPENSE.label) {
        label = ScreenSetting.EXPENSE.label
      }

      fragment<ActivityFragment>(ScreenSetting.ACTIVITY.label) {
        label = ScreenSetting.ACTIVITY.label
      }

      fragment<AccountFragment>(ScreenSetting.ACCOUNT.label) {
        label = ScreenSetting.ACCOUNT.label
      }
    }
  }

  override fun onStart() {
    super.onStart()
    auth.addAuthStateListener(authStateListener)
  }

  override fun onStop() {
    super.onStop()
    auth.removeAuthStateListener(authStateListener)
  }

  private fun setCurrentFragment(fragment: ScreenSetting) {
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController
    navController.navigate(fragment.label)
  }
}
