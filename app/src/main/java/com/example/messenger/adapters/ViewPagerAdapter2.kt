package com.example.messenger.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.messenger.fragments.ChanelsFragment
import com.example.messenger.fragments.LatestMessageFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> LatestMessageFragment()
            1 -> ChanelsFragment()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }

}