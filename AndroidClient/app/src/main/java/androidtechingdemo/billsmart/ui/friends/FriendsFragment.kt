package androidtechingdemo.billsmart.ui.friends

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.databinding.FragmentFriendsBinding
import androidtechingdemo.billsmart.ui.CollectionNames
import androidtechingdemo.billsmart.ui.ScreenSetting
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FriendsFragment : Fragment() {
  private lateinit var binding: FragmentFriendsBinding
  private lateinit var firestore: FirebaseFirestore

  @Inject
  lateinit var friendsListAdapter: FriendsListAdapter
  private lateinit var layoutManager: LinearLayoutManager
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentFriendsBinding.inflate(inflater, container, false)
    firestore = Firebase.firestore
    layoutManager = LinearLayoutManager(context)
    with(binding) {
      friendsRecyclerView.layoutManager = layoutManager
      friendsRecyclerView.adapter = friendsListAdapter
    }
    listenToUserFriendsInfo()
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      addFriendButton.setOnClickListener {
        val navController = findNavController()
        navController.navigate(ScreenSetting.ADDNEWCONTACT.label)
      }
    }
  }

  private fun listenToUserFriendsInfo() {
    val uid = Firebase.auth.currentUser?.uid
    val docRef = firestore.collection(CollectionNames.USERS.name).document(uid!!).collection(CollectionNames.FRIENDS.name)
    docRef.addSnapshotListener { snapshot, e ->
      if (e != null) {
        Log.w("FriendsFragment", "Listen failed.", e)
        return@addSnapshotListener
      }

      if (snapshot != null && !snapshot.isEmpty) {
        Log.d("FriendsFragment", "Current data: ${snapshot.documents}")
        val newFriendsUidList = snapshot.documents.map { it.id }
        friendsListAdapter.updateData(newFriendsUidList)
      } else {
        Log.d("FriendsFragment", "Current data: null")
      }
    }
  }
}
