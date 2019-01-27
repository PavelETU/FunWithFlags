package com.wordpress.lonelytripblog.funwithflags.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import kotlinx.android.synthetic.main.country_view.view.*

class StackAdapter(private val layoutInflater: LayoutInflater, private val countries: List<Country>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val country = countries[position]
        return convertView ?: layoutInflater.inflate(R.layout.country_view, parent, false).apply {
            country_name.text = country.name
            country_description.text = country.description
            country_image.setImageResource(country.resourceId)
        }
    }

    override fun getItem(position: Int): Any {
        return countries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return countries.size
    }
}