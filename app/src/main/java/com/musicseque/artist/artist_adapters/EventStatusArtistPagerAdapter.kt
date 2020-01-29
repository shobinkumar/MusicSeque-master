package com.musicseque.artist.artist_adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import com.musicseque.fragments.ArtistEventsListFragment

class EventStatusArtistPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                   ArtistEventsListFragment(2)
            }

            else -> {
                return ArtistEventsListFragment(1)
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {

            0 -> "Upcoming"
            else -> {
                return "Past"
            }
        }
    }
}