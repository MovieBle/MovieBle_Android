package com.example.movie.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.movie.data.Repository
import com.example.movie.models.Movie
import com.example.movie.data.database.entities.MovieLikeEntity
import com.example.movie.data.database.entities.UserEntity
import com.example.movie.data.database.entities.movie.*
import com.example.movie.untils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class DatabaseViewModel @Inject constructor(
    application: Application,
    private val repository: Repository

) :
    AndroidViewModel(application) {

    // 데이터가 있지 여부
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)


    val getAllData: LiveData<List<MovieEntity>> = repository.local.getAllMovie().asLiveData()

    val getAllMovie: MutableLiveData<NetworkResult<Movie>> = MutableLiveData()


    companion object {
        const val TAG = "DataBase"
    }

    @InternalCoroutinesApi
    fun insertMovie(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMovie(movie)
        }
    }


    @InternalCoroutinesApi
    fun getAllMovie(page: Int) = viewModelScope.launch {


        getDiscoverMoviesSafeCall(page)

    }

    @InternalCoroutinesApi
    private fun offlineCashMovie(movie: Movie) {

        val movieEntity = MovieEntity(movie)
        insertMovie(movieEntity)
    }


    @InternalCoroutinesApi
    private suspend fun getDiscoverMoviesSafeCall(page: Int) {

        getAllMovie.value = NetworkResult.Loading()
        //인터넷이 연결되었을 때
        if (hasInternetConnection()) {


            val response = repository.remote.getMovie(page)
            getAllMovie.value = handleMovieResponse(response)


            val movieData = getAllMovie.value!!.data
            Log.d(TAG, "getDiscoverMoviesSafeCall: $movieData")

            if (movieData != null) {

                offlineCashMovie(movieData)
            }


        }
    }


    private fun handleMovieResponse(response: Response<Movie>): NetworkResult<Movie> {

        when {
            //너무 오래 걸릴 때
            response.message().toString().contains("Timeout") -> {

                return NetworkResult.Error("Timeout")
            }

            // 402가 뜰 때
            response.code() == 402 -> {

                return NetworkResult.Error("API Key Limited.")
            }

            //데이터가 없을 때
            response.body()!!.isNullOrEmpty() -> {
                return NetworkResult.Error("API 연동 실패")
            }

            //성공했을 때 data 를 가져옴
            response.isSuccessful -> {

                val movieData = response.body()
                return NetworkResult.Success(movieData!!)
            }
            else -> {

                return NetworkResult.Error(response.message())
            }

        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        //연결 관리자
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }




}

