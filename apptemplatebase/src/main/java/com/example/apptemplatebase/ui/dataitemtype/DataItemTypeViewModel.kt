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

package com.example.apptemplatebase.ui.dataitemtype

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplatebase.data.DataItemTypeRepository
import com.example.apptemplatebase.data.remote.network.Yelp
import com.example.apptemplatebase.ui.dataitemtype.DataItemTypeUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class DataItemTypeViewModel @Inject constructor(
    private val dataItemTypeRepository: DataItemTypeRepository
) : ViewModel() {

    val uiState: StateFlow<DataItemTypeUiState> = dataItemTypeRepository
        .dataItemTypes.map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DataItemTypeUiState.Loading)

    fun addDataItemType(name: String) {
        viewModelScope.launch {
            dataItemTypeRepository.add(name)
        }
    }

    val uiState2API: MutableStateFlow<Yelp> = MutableStateFlow(Yelp())

    fun getYelpSuggestionsFromWeb(term: String, location: String) {
        Log.i("test-02","at view model , asking to search web at repo")
        viewModelScope.launch(Dispatchers.IO){
            dataItemTypeRepository.getYelpSuggestionsFromWeb(term=term, location = location).collect(){
                uiState2API.value = it
            }
        }
    }

}

sealed interface DataItemTypeUiState {
    object Loading : DataItemTypeUiState
    data class Error(val throwable: Throwable) : DataItemTypeUiState
    data class Success(val data: List<String>) : DataItemTypeUiState
}
