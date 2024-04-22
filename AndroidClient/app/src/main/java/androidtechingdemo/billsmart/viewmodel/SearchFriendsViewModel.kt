package androidtechingdemo.billsmart.viewmodel

import android.util.Log
import androidtechingdemo.billsmart.database.FriendUserInfo
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class SearchFriendsViewModel @Inject constructor() : ViewModel() {
  private var _friendList: List<FriendUserInfo> = emptyList()

  private val _searchResults = MutableStateFlow<List<FriendUserInfo>>(emptyList())
  val searchResults get() = _searchResults

  private val _searchQuery = MutableStateFlow("")
  val searchQuery get() = _searchQuery

  fun updateSearchQuery(query: String) {
    Log.d("SearchFriendsViewModel", "updateSearchQuery: $query")
    _searchQuery.value = query
  }

  fun updateFriendInfo(friendList: List<FriendUserInfo>) {
    _friendList = friendList
  }

  fun searchFriends() {
    val friendName = _searchQuery.value
    Log.d("SearchFriendsViewModel", "searchFriends: $friendName")
    if (friendName.isEmpty()) {
      _searchResults.value = _friendList
      return
    }
    val results = _friendList.filter {
      it.displayName.contains(friendName, ignoreCase = true)
    }
    _searchResults.value = results
  }
}
