package androidtechingdemo.billsmart.database

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.functions.functions

fun addNewFriendByEmail(email: String): Task<String> {
  val data = hashMapOf(
    "email" to email
  )
  return Firebase.functions
    .getHttpsCallable("addNewFriendByEmail")
    .call(data)
    .continueWith { task ->
      val result = task.result?.data as String
      result
    }
}
