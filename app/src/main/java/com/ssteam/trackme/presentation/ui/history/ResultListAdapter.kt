/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ssteam.trackme.presentation.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.ssteam.trackme.R
import com.ssteam.trackme.databinding.ResultItemBinding
import com.ssteam.trackme.domain.AppExecutors
import com.ssteam.trackme.domain.models.Result
import com.ssteam.trackme.presentation.ui.common.DataBoundListAdapter
import com.ssteam.trackme.presentation.utils.Utils


class ResultListAdapter(appExecutors: AppExecutors) : DataBoundListAdapter<Result, ResultItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.avgSpeed == newItem.avgSpeed
                    && oldItem.distance == newItem.distance
                    && oldItem.duration == newItem.duration
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ResultItemBinding {
        val binding = DataBindingUtil.inflate<ResultItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.result_item,
            parent,
            false
        )
        binding.mapImageView.onCreate(null)
        binding.mapImageView.onResume()
        return binding
    }

    override fun bind(binding: ResultItemBinding, item: Result) {
        binding.resultView.update(item.distance, item.avgSpeed,Utils.getDurationText(item.duration))
        binding.mapImageView.getMapAsync {
            val validLocations = item.locations.filterNotNull()
            if (validLocations.isEmpty()){
                ///TODO do something ?
            }else{
                val startLocation = validLocations.first()
                Utils.drawStartLocation(it, startLocation)

                Utils.drawRoute(it, validLocations)

                val endLocation = validLocations.last()
                Utils.drawEndLocation(it, endLocation)

                val startLatLng = LatLng(startLocation.lat, startLocation.lng)
                val endLatLng = LatLng(endLocation.lat, endLocation.lng)

                val builder = LatLngBounds.Builder()
                builder.include(startLatLng).include(endLatLng)
                it.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20))
            }
        }
    }

}
