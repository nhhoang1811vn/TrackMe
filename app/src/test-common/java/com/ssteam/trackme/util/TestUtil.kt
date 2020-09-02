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

package com.ssteam.trackme.util

import com.ssteam.trackme.data.db.entities.LocationEntity
import com.ssteam.trackme.data.db.entities.ResultEntity
import com.ssteam.trackme.data.db.entities.ResultInfoEntity
import com.ssteam.trackme.domain.models.Result
import java.util.*

object TestUtil {
    fun createResult(distance: Double, avgSpeed: Double, duration: Long) = Result(
        locations = mutableListOf(),
        distance = distance,
        avgSpeed = avgSpeed,
        duration = duration,
        createdDate = Date()
    )
    fun createResults(count: Int) : List<Result>{
        val results = mutableListOf<Result>()
        for (i in 0 until count){
            results.add(createResult(i.toDouble(), i.toDouble(), i.toLong()))
        }
        return results
    }

    fun createResultEntity(distance: Double, avgSpeed: Double, duration: Long, count: Int) = ResultEntity().apply {
        resultInfo = createResultInfoEntity(distance,avgSpeed,duration)
        locations = createLocations(resultInfo.id, count)
    }
    private fun createResultInfoEntity(distance: Double, avgSpeed: Double, duration: Long) = ResultInfoEntity().apply {
        id = UUID.randomUUID().toString()
        this.distance = distance
        this.avgSpeed = avgSpeed
        this.duration = duration
        createdDate = null
    }
    private fun createLocations(resultId: String, count: Int) : List<LocationEntity>{
        val entities = mutableListOf<LocationEntity>()
        for (i in 0 until count){
            entities.add(createLocationEntity(i.toDouble(), i.toDouble(), resultId))
        }
        return entities
    }
    private fun createLocationEntity(lat: Double, lng: Double, resultId: String) = LocationEntity().apply {
        this.lat = lat
        this.lng = lng
        this.resultId = resultId
    }

    /*fun createRepos(count: Int, owner: String, name: String, description: String): List<Repo> {
        return (0 until count).map {
            createRepo(
                owner = owner + it,
                name = name + it,
                description = description + it
            )
        }
    }

    fun createRepo(owner: String, name: String, description: String) = createRepo(
        id = Repo.UNKNOWN_ID,
        owner = owner,
        name = name,
        description = description
    )

    fun createRepo(id: Int, owner: String, name: String, description: String) = Repo(
        id = id,
        name = name,
        fullName = "$owner/$name",
        description = description,
        owner = Repo.Owner(owner, null),
        stars = 3
    )

    fun createContributor(repo: Repo, login: String, contributions: Int) = Contributor(
        login = login,
        contributions = contributions,
        avatarUrl = null
    ).apply {
        repoName = repo.name
        repoOwner = repo.owner.login
    }*/
}
