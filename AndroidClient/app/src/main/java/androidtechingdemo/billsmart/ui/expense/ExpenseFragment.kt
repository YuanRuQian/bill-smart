package androidtechingdemo.billsmart.ui.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.database.FriendsListAdapter
import androidtechingdemo.billsmart.database.listenToUserFriendsList
import androidtechingdemo.billsmart.databinding.FragmentExpenseBinding
import androidtechingdemo.billsmart.utils.BaseFragmentWithBackNavigation
import androidtechingdemo.billsmart.viewmodel.SearchFriendsViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpenseFragment : BaseFragmentWithBackNavigation() {
  private lateinit var binding: FragmentExpenseBinding

  @Inject
  lateinit var friendsListAdapter: FriendsListAdapter
  private lateinit var layoutManager: LinearLayoutManager

  private val searchFriendsViewModel: SearchFriendsViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentExpenseBinding.inflate(inflater, container, false)
    layoutManager = LinearLayoutManager(context)
    with(binding) {
      friendsRecyclerView.layoutManager = layoutManager
      friendsRecyclerView.adapter = friendsListAdapter
      expenseFriendSearchView.editText.setOnEditorActionListener { v, _, _ ->
        expenseFriendSearchBar.setText(v.getText())
        searchFriendsViewModel.updateSearchQuery(v.getText().toString())
        expenseFriendSearchView.hide()
        false
      }
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
        launch {
          listenToUserFriendsList { searchFriendsViewModel.updateFriendInfo(it) }
        }

        launch {
          searchFriendsViewModel.searchQuery.collect {
            searchFriendsViewModel.searchFriends()
          }
        }

        launch {
          searchFriendsViewModel.searchResults.collect {
            friendsListAdapter.updateData(it)
          }
        }
      }
    }

    return binding.root
  }
}
