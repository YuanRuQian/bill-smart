package androidtechingdemo.billsmart.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.database.FriendsListAdapter
import androidtechingdemo.billsmart.database.listenToUserFriendsList
import androidtechingdemo.billsmart.databinding.FragmentFriendsBinding
import androidtechingdemo.billsmart.ui.ScreenSetting
import androidtechingdemo.billsmart.utils.BaseFragmentWithBackNavigation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsFragment : BaseFragmentWithBackNavigation() {
  private lateinit var binding: FragmentFriendsBinding
  private lateinit var firestore: FirebaseFirestore

  @Inject
  lateinit var friendsListAdapter: FriendsListAdapter
  private lateinit var layoutManager: LinearLayoutManager
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentFriendsBinding.inflate(inflater, container, false)
    firestore = Firebase.firestore
    layoutManager = LinearLayoutManager(context)
    with(binding) {
      friendsRecyclerView.layoutManager = layoutManager
      friendsRecyclerView.adapter = friendsListAdapter
    }
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        listenToUserFriendsList { friendsListAdapter.updateData(it) }
      }
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      addFriendButton.setOnClickListener {
        val navController = findNavController()
        navController.navigate(ScreenSetting.ADDNEWCONTACT.label) {
          popUpTo(ScreenSetting.FRIENDS.label) {
            inclusive = true
          }
        }
      }
    }
  }
}
