package androidtechingdemo.billsmart.ui.friends

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidtechingdemo.billsmart.R
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.squareup.picasso.Picasso

data class FriendUserInfo(
  val uid: String,
  val displayName: String,
  val email: String,
  val photoURL: String,
)

fun getFriendInfoByUID(uid: String): Task<FriendUserInfo> {
  val data = hashMapOf(
    "uid" to uid,
  )
  return FirebaseFunctions.getInstance()
    .getHttpsCallable("getFriendInfoByUID")
    .call(data)
    .continueWith { task ->
      val ret = task.result?.data as HashMap<String, String>
      FriendUserInfo(
        uid = ret["uid"]!!,
        displayName = ret["displayName"]!!,
        email = ret["email"]!!,
        photoURL = ret["photoURL"]!!,
      )
    }
}

class FriendsListAdapter(private val dataSet: MutableList<String>) :
  RecyclerView.Adapter<FriendsListAdapter.ViewHolder>() {

  /**
   * Provide a reference to the type of views that you are using
   * (custom ViewHolder)
   */
  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView
    val imageView: ImageView

    init {
      // Define click listener for the ViewHolder's View
      textView = view.findViewById(R.id.friendName)
      imageView = view.findViewById(R.id.friendProfileImage)
    }
  }

  // Create new views (invoked by the layout manager)
  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
    // Create a new view, which defines the UI of the list item
    val view = LayoutInflater.from(viewGroup.context)
      .inflate(R.layout.friend_row_item, viewGroup, false)

    return ViewHolder(view)
  }

  // Replace the contents of a view (invoked by the layout manager)
  override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    getFriendInfoByUID(dataSet[position]).addOnCompleteListener {
      if (it.isSuccessful) {
        val friendInfo = it.result
        viewHolder.textView.text = friendInfo.displayName
        if (friendInfo.photoURL.isEmpty()) {
          viewHolder.imageView.setImageResource(R.drawable.default_profile_image)
        } else {
          Picasso.get().load(friendInfo.photoURL).into(viewHolder.imageView)
        }
      } else {
        Log.e("FriendsListAdapter", "Failed to get friend info", it.exception)
      }
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  override fun getItemCount() = dataSet.size

  fun updateData(newData: List<String>) {
    Log.d("FriendsListAdapter", "updateData: $newData")
    dataSet.clear()
    dataSet.addAll(newData)
    notifyDataSetChanged()
  }
}
