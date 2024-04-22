package androidtechingdemo.billsmart.database

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.functions.functions

fun getFriendsList(): Task<List<FriendUserInfo>> {
  return Firebase.functions
    .getHttpsCallable("getFriendsList")
    .call()
    .continueWith { task ->
      val result = task.result?.data as List<HashMap<String, String>>
      result.map {
        FriendUserInfo(
          uid = it["uid"]!!,
          displayName = it["displayName"]!!,
          email = it["email"]!!,
          photoURL = it["photoURL"]!!
        )
      }
    }
}
