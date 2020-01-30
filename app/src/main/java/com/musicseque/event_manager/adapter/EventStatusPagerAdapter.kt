package com.musicseque.event_manager.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.musicseque.event_manager.fragment.PastEventsFragment
import com.musicseque.event_manager.fragment.PendingEventsFragment
import com.musicseque.event_manager.fragment.UpcomingEventsFragment


class EventStatusPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PendingEventsFragment()
            }
            1 -> UpcomingEventsFragment()
            else -> {
                return PastEventsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Pending"
            1 -> "Upcoming"
            else -> {
                return "Past"
            }
        }
    }
}