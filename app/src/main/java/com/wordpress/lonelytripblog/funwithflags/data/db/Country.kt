package com.wordpress.lonelytripblog.funwithflags.data.db

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["id"])
data class Country(val id: Int, val name: String, val resourceId: Int,
                   val description: String, val flagIsLearnt: Int = 0)