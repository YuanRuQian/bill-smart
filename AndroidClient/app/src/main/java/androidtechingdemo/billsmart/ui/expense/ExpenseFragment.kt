package androidtechingdemo.billsmart.ui.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidtechingdemo.billsmart.R
import androidtechingdemo.billsmart.utils.BaseFragmentWithBackNavigation

class ExpenseFragment : BaseFragmentWithBackNavigation() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_expense, container, false)
  }
}
