package androidtechingdemo.billsmart.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.R
import androidtechingdemo.billsmart.utils.BaseFragment
import androidx.fragment.app.Fragment

class GroupsFragment : BaseFragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_groups, container, false)
  }
}
