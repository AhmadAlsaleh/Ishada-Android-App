package com.crazy_iter.ishada.MainFragments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainFragmentsAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val mFragmentsList: MutableList<Fragment> = mutableListOf()
    private val mFragmentTitle: MutableList<String> = mutableListOf()

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentsList.add(fragment)
        mFragmentTitle.add(title)
    }

    override fun getItem(position: Int) = mFragmentsList[position]

    override fun getCount() = mFragmentsList.size

    override fun getPageTitle(position: Int) = mFragmentTitle[position]

}