package androidtechingdemo.billsmart.database

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.functions.functions

fun updateUserDisplayName(displayName: String): Task<Boolean> {
  val data = hashMapOf(
    "displayName" to displayName,
  )
  return Firebase.functions
    .getHttpsCallable("updateUserDisplayName")
    .call(data)
    .continueWith { task ->
      val result = task.result?.data as Boolean
      result
    }
}
