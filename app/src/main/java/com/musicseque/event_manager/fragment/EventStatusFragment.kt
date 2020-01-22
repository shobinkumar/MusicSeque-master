package com.musicseque.event_manager.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.musicseque.R
import com.musicseque.event_manager.adapter.EventStatusPagerAdapter
import com.musicseque.utilities.KotlinBaseFragment
import kotlinx.android.synthetic.main.fragment_event_status.*

class EventStatusFragment:KotlinBaseFragment() {
    lateinit var v:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.fragment_event_status,null)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ButterKnife.bind(this,v)
        initViews()
    }

    private fun initViews() {

        val myViewPageStateAdapter = EventStatusPagerAdapter(fragmentManager!!)
        myViewPageStateAdapter.addFragment(PendingEventsFragment(),"Pending")
        myViewPageStateAdapter.addFragment(UpcomingEventsFragment(),"Upcoming")
        myViewPageStateAdapter.addFragment(PastEventsFragment(),"Past")
        viewPager.adapter=myViewPageStateAdapter
        tab_layout.setupWithViewPager(viewPager,true)




        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                val fm = activity!!.supportFragmentManager
                val ft = fm.beginTransaction()
                val count = fm.backStackEntryCount
                if (count >= 1) {
                    fm.popBackStack()
                }
                ft.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // setAdapter();


            }

            override fun onTabReselected(tab: TabLayout.Tab) {

                //   viewPager.notifyAll();
            }
        })
    }


}