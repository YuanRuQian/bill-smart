package androidtechingdemo.billsmart.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidtechingdemo.billsmart.R
import androidtechingdemo.billsmart.ui.account.AccountFragment
import androidtechingdemo.billsmart.ui.activity.ActivityFragment
import androidtechingdemo.billsmart.ui.auth.LoginFragment
import androidtechingdemo.billsmart.ui.auth.RegisterFragment
import androidtechingdemo.billsmart.ui.expense.ExpenseFragment
import androidtechingdemo.billsmart.ui.friends.AddNewContactFragment
import androidtechingdemo.billsmart.ui.friends.FriendsFragment
import androidtechingdemo.billsmart.ui.groups.GroupsFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth
  private lateinit var authStateListener: FirebaseAuth.AuthStateListener
  private lateinit var bottomNavigationView: BottomNavigationView
  private lateinit var functions: FirebaseFunctions
  private lateinit var firestore: FirebaseFirestore
  private lateinit var navController: NavController

  private fun emulatorSettings() {
    // [START functions_emulator_connect]
    // 10.0.2.2 is the special IP address to connect to the 'localhost' of
    // the host computer from an Android emulator.
    functions = Firebase.functions
    functions.useEmulator("10.0.2.2", 5001)
    // [END functions_emulator_connect]

    // 10.0.2.2 is the special IP address to connect to the 'localhost' of
// the host computer from an Android emulator.
    firestore = Firebase.firestore
    firestore.useEmulator("10.0.2.2", 8080)

    firestore.firestoreSettings = firestoreSettings {
      isPersistenceEnabled = false
    }

    auth = Firebase.auth
    auth.useEmulator("10.0.2.2", 9099)
  }

  @SuppressLint("RestrictedApi")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // auth = FirebaseAuth.getInstance()

    emulatorSettings()

    // Retrieve the NavController.
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    navController = navHostFragment.navController

    navController.addOnDestinationChangedListener { controller, _, _ ->
      controller.currentBackStack.value.let { backStack ->
        val str = backStack.joinToString(
          separator = "\n",
          prefix = "backStack: [ ",
          postfix = " ]"
        ) { entry ->
          buildString {
            append(" route=")
            append(entry.destination.route)
            append(" displayName=")
            append(entry.destination.displayName)
          }
        }
        Log.d("Backstack", str)
      }
    }

    navController.graph = navController.createGraph(
      startDestination = ScreenSetting.FRIENDS.label
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

      fragment<AddNewContactFragment>(ScreenSetting.ADDNEWCONTACT.label) {
        label = ScreenSetting.ADDNEWCONTACT.label
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

    authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      val user = firebaseAuth.currentUser
      navController.popBackStack(navController.currentBackStackEntry!!.destination.route!!, true)
      if (user != null) {
        // User is signed in, clear all back stack
        changeBottomNavigationCheckedItem(ScreenSetting.FRIENDS.label)
        navController.navigate(ScreenSetting.FRIENDS.label) {
          popUpTo(ScreenSetting.LOGIN.label) {
            inclusive = true
          }
        }
        bottomNavigationView.visibility = View.VISIBLE
        navController.graph.setStartDestination(ScreenSetting.FRIENDS.label)
      } else {
        // User is signed out, clear all back stack
        navController.navigate(ScreenSetting.LOGIN.label) {
          popUpTo(ScreenSetting.LOGIN.label) {
            inclusive = true
          }
        }
        bottomNavigationView.visibility = View.GONE
      }
    }

    bottomNavigationView = findViewById(R.id.bottomNavigationView)

    bottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

    bottomNavigationView.setOnItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.friendsTab -> {
          navController.navigate(ScreenSetting.FRIENDS.label)
          true
        }

        R.id.groupsTab -> {
          navController.navigate(ScreenSetting.GROUPS.label)
          true
        }

        R.id.expenseTab -> {
          navController.navigate(ScreenSetting.EXPENSE.label)
          true
        }

        R.id.activityTab -> {
          navController.navigate(ScreenSetting.ACTIVITY.label)
          true
        }

        R.id.accountTab -> {
          navController.navigate(ScreenSetting.ACCOUNT.label)
          true
        }

        else -> {
          false
        }
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

  fun changeBottomNavigationCheckedItem(fragment: String) {
    when (fragment) {
      ScreenSetting.FRIENDS.label -> setBottomNavigationCheckedItem(R.id.friendsTab)
      ScreenSetting.GROUPS.label -> setBottomNavigationCheckedItem(R.id.groupsTab)
      ScreenSetting.EXPENSE.label -> setBottomNavigationCheckedItem(R.id.expenseTab)
      ScreenSetting.ACTIVITY.label -> setBottomNavigationCheckedItem(R.id.activityTab)
      ScreenSetting.ACCOUNT.label -> setBottomNavigationCheckedItem(R.id.accountTab)
      else -> {
        // Do nothing
      }
    }
  }

  private fun setBottomNavigationCheckedItem(tab: Int) {
    bottomNavigationView.menu.findItem(tab).isChecked = true
  }
}
