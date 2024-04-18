package androidtechingdemo.billsmart.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.R
import androidtechingdemo.billsmart.utils.BaseFragment
import androidx.fragment.app.Fragment

class ActivityFragment : BaseFragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_activity, container, false)
  }
}
