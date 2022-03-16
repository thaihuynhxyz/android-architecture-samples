package xyz.thaihuynh.android.sample.arch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: String,
    val title: String? = null,
    val data: String? = null,
) : Parcelable
