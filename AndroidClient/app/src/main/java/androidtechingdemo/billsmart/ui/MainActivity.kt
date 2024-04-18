package androidtechingdemo.billsmart.ui

import android.os.Bundle
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.google.android.gms.tasks.Task
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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // auth = FirebaseAuth.getInstance()

    emulatorSettings()

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

  fun addNewFriendByEmail(email: String): Task<String> {
    val data = hashMapOf(
      "email" to email,
    )
    return functions
      .getHttpsCallable("addNewFriendByEmail")
      .call(data)
      .continueWith { task ->
        val result = task.result?.data as String
        result
      }
  }

  fun updateUserDisplayName(displayName: String): Task<Boolean> {
    val data = hashMapOf(
      "displayName" to displayName,
    )
    return functions
      .getHttpsCallable("updateUserDisplayName")
      .call(data)
      .continueWith { task ->
        val result = task.result?.data as Boolean
        result
      }
  }
}
