package com.example.movie.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.movie.untils.Constants.Companion.API_KEY
import com.example.movie.untils.Constants.Companion.QUERY_API_KEY
import com.example.movie.untils.Constants.Companion.language
import com.example.movie.untils.Constants.Companion.page
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.HashMap
import javax.inject.Inject


//HiltViewModel 어노테이션 추가안해도 되는 이유는 ?
class QueryViewModel(

    application: Application
) : AndroidViewModel(application) {

    fun getQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_API_KEY] = API_KEY
        queries[language] = "ko-KR"

        return queries
    }
}