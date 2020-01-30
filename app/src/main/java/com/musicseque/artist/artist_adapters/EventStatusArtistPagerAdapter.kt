package com.musicseque.artist.artist_adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.musicseque.artist.fragments.ArtistEventsListFragment
import com.musicseque.artist.fragments.PendingEventsListArtistFragment

class EventStatusArtistPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> {
                PendingEventsListArtistFragment()
            }
            1 -> {
                ArtistEventsListFragment(2)
            }

            else -> {
                return ArtistEventsListFragment(1)
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