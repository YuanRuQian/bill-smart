package androidtechingdemo.billsmart.database

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidtechingdemo.billsmart.R
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import javax.inject.Inject

class FriendsListAdapter @Inject constructor() :
  RecyclerView.Adapter<FriendsListAdapter.ViewHolder>() {

  private val dataSet: MutableList<FriendUserInfo> = mutableListOf()

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
    val friendInfo = dataSet[position]
    viewHolder.textView.text = friendInfo.displayName
    if (friendInfo.photoURL.isEmpty()) {
      viewHolder.imageView.setImageResource(R.drawable.default_profile_image)
    } else {
      Picasso.get().load(friendInfo.photoURL).into(viewHolder.imageView)
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  override fun getItemCount() = dataSet.size

  fun updateData(newData: List<FriendUserInfo>) {
    Log.d("FriendsListAdapter", "updateData: $newData")
    dataSet.clear()
    dataSet.addAll(newData)
    notifyDataSetChanged()
  }
}
