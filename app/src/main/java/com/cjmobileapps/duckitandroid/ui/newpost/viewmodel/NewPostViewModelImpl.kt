package com.cjmobileapps.duckitandroid.ui.newpost.viewmodel

import androidx.lifecycle.ViewModel
import com.cjmobileapps.duckitandroid.util.coroutine.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPostViewModelImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel(), NewPostViewModel {



}
