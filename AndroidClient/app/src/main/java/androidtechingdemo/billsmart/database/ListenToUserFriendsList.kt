package androidtechingdemo.billsmart.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

fun listenToUserFriendsList(handler: (List<FriendUserInfo>) -> Unit) {
  val uid = Firebase.auth.currentUser?.uid
  uid ?: return
  val docRef = Firebase.firestore.collection(CollectionNames.USERS.name).document(uid).collection(
    CollectionNames.FRIENDS.name,
  )
  docRef.addSnapshotListener { snapshot, e ->
    if (e != null) {
      Log.w("FriendsFragment", "Listen failed.", e)
      return@addSnapshotListener
    }

    if (snapshot != null && !snapshot.isEmpty) {
      Log.d("FriendsFragment", "Current data: ${snapshot.documents}")

      getFriendsList().addOnCompleteListener { task ->
        if (task.isSuccessful) {
          handler(task.result)
        } else {
          Log.d("FriendsFragment", "Error getting documents: ", task.exception)
        }
      }
    } else {
      Log.d("FriendsFragment", "Current data: null")
    }
  }
}
