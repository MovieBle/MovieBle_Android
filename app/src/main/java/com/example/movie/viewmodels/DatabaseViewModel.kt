package com.example.movie.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.movie.data.Repository
import com.example.movie.models.Movie
import com.example.movie.data.database.entities.movie.*
import com.example.movie.untils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/***
코루틴을 사용하는이유

네트워크 호출이나 디스크 작업과 같은 장기 실행 작업을 관리하면서 앱의 응답성을 유지하는 깔끔하고 간소화된 비동기 처리를 할 수 있다.

 */

// 비동기 처리로 viesModelScope 에 넣음 (데이터 추가)
@InternalCoroutinesApi
@HiltViewModel
class DatabaseViewModel @Inject constructor(
    application: Application,
    private val repository: Repository

) :
    AndroidViewModel(application) {


    val emptyDatabase=MutableLiveData<Boolean>()
    private val _getMovie: LiveData<List<MovieEntity>> =
        repository.local.getAllMovie().asLiveData()
    val getMovie: LiveData<List<MovieEntity>> = _getMovie

    private val _getLikeMovie: LiveData<List<MovieLikeEntity>> =
        repository.local.getAllLikeMovie().asLiveData()
    val getLikeMovie: LiveData<List<MovieLikeEntity>> = _getLikeMovie


    private val _lsLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _lsLoading


    private val _getRandomMovie = MutableLiveData<NetworkResult<Movie>>()
    val getRandomMovie: MutableLiveData<NetworkResult<Movie>> get() = _getRandomMovie


    // 데이터를 추가한다.
    @InternalCoroutinesApi
    fun insertMovie(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMovie(movie)
        }
    }

    // 데이터를 추가한다.
    @InternalCoroutinesApi
    fun insertLikeMovie(movie: MovieLikeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertLikeMovie(movie)
        }
    }

    fun deleteLikeMovie(movie: MovieLikeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteLikeMovie(movie)
        }
    }


    // 비동기 처리로 서버에서 받아온 data 를 뿌려준다.
    @InternalCoroutinesApi
    fun getAllMovie(id: Int,page: Int) = viewModelScope.launch {
        getDiscoverMoviesSafeCall(id,page)

    }


    @InternalCoroutinesApi
    private fun offlineCashMovie(id:Int,movie: Movie) {
        val movieEntity = MovieEntity(id,movie[0])
        insertMovie(movieEntity)
    }

    @InternalCoroutinesApi
    private suspend fun getDiscoverMoviesSafeCall(id:Int,page: Int) {

        getRandomMovie.value = NetworkResult.Loading()
        //인터넷이 연결되었을 때
        if (hasInternetConnection()) {

            try {

                val response = repository.remote.getMovie(page)
                getRandomMovie.value = handleMovieResponse(response)


                val movieData = getRandomMovie.value!!.data
                if (movieData != null) {

                    offlineCashMovie(id,movieData)
                }

            } catch (e: Exception) {

                getRandomMovie.value = NetworkResult.Error("영화를 찾을수 없음")
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
    fun checkIfDatabaseEmpty(movieData: List<MovieLikeEntity>) {
        emptyDatabase.value = movieData.isEmpty() //데이터 있음
    }

}

