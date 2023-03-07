/*
 * Copyright (C) 2022 The Android Open Source Project
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

package com.example.apptemplatebase.data

import android.util.Log
import com.example.apptemplatebase.data.remote.network.NetworkService
import com.example.apptemplatebase.data.remote.network.Yelp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.wrondon.baset.data.local.database.DataItemType
import com.wrondon.baset.data.local.database.DataItemTypeDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface DataItemTypeRepository {
    val dataItemTypes: Flow<List<String>>

    suspend fun add(name: String)

    suspend fun getYelpSuggestionsFromWeb(term: String, location: String) : Flow<Yelp>
}

class DefaultDataItemTypeRepository @Inject constructor(
    private val dataItemTypeDao: DataItemTypeDao,
    private val networkService: NetworkService
) : DataItemTypeRepository {

    override val dataItemTypes: Flow<List<String>> =
        dataItemTypeDao.getDataItemTypes().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        dataItemTypeDao.insertDataItemType(DataItemType(name = name))
    }

    override suspend fun getYelpSuggestionsFromWeb(term: String, location: String)  : Flow<Yelp> {
        return flow {
            Log.i("test-02","at repo , asking to search web at network serv")
            val yelpSugg = networkService.getYelpSuggestionsFromWeb(term=term, location = location)
            emit(yelpSugg)
        }
    }
}

/*
suspend fun getComment(id: Int): Flow<CommentApiState<CommentModel>> {
        return flow {

            // get the comment Data from the api
            val comment=apiService.getComments(id)

            // Emit this data wrapped in
            // the helper class [CommentApiState]
            emit(CommentApiState.success(comment))
        }.flowOn(Dispatchers.IO)
    }
 */
