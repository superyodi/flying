package com.foo.pomodoro.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.foo.pomodoro.ReportFragment
import com.foo.pomodoro.PomodoroFragment
import com.foo.pomodoro.R

private val TAB_TITLES = arrayOf(
        R.string.tab_text_pomo,
        R.string.tab_text_report
)

const val POMODORO_PAGE_INDEX = 0
const val REPORT_PAGE_INDEX = 1

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        POMODORO_PAGE_INDEX to { PomodoroFragment() },
        REPORT_PAGE_INDEX to { ReportFragment() }
    )


    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}