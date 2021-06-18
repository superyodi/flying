package com.foo.pomodoro.ui



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foo.pomodoro.R
import com.foo.pomodoro.adapters.POMODORO_PAGE_INDEX
import com.foo.pomodoro.adapters.REPORT_PAGE_INDEX
import com.foo.pomodoro.adapters.SectionsPagerAdapter
import com.foo.pomodoro.databinding.FragmentViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = SectionsPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

//        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun getTabIcon(position: Int): Int {
        return when (position) {
            POMODORO_PAGE_INDEX -> R.drawable.ic_baseline_access_alarm_24
            REPORT_PAGE_INDEX -> R.drawable.ic_baseline_event_note_24
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            POMODORO_PAGE_INDEX -> getString(R.string.tab_text_pomo)
            REPORT_PAGE_INDEX -> getString(R.string.tab_text_report)
            else -> null
        }
    }
}
