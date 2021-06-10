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
import com.example.movie.data.database.entities.movie.*
import com.example.movie.untils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class DatabaseViewModel @Inject constructor(
    application: Application,
     val repository: Repository

) :
    AndroidViewModel(application) {

    // 데이터가 있지 여부
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    val getAllLikeData: LiveData<List<MovieLikeEntity>> =
        repository.local.getLikeAllMovie().asLiveData()
    val getAllData: LiveData<List<MovieEntity>> = repository.local.getAllMovie().asLiveData()
    val getAllPopularData: LiveData<List<MoviePopularEntity>> =
        repository.local.getAllPopularMovie().asLiveData()
    val getAllDiscoverData: LiveData<List<MovieDiscoverEntity>> =
        repository.local.getAllDiscoverMovie().asLiveData()
    val getAllRecentData: LiveData<List<MovieRecentEntity>> =
        repository.local.getAllRecentMovie().asLiveData()


    val getAllMovieListData: LiveData<List<MovieListEntity>> =
        repository.local.getAllMovieListMovie().asLiveData()

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

    private fun insertPopularMovie(movie: MoviePopularEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertPopularMovie(movie)
        }
    }

    private fun insertRecentMovie(movie: MovieRecentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecentMovie(movie)
        }
    }

    private fun insertDiscoverMovie(movie: MovieDiscoverEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertDiscoverMovie(movie)
        }
    }


    private fun insertMovieListMovie(movie: MovieListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMovieListMovie(movie)
        }
    }

    @InternalCoroutinesApi
    fun deleteLikeMovie(movie: MovieLikeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteLikeMovie(movie)
        }
    }

    fun insertLikeMovie(movie: MovieLikeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertLikeMovie(movie)
        }
    }

    @InternalCoroutinesApi
    fun getDiscoverMovie(queries: Map<String, String>, page: Int) = viewModelScope.launch {


        getDiscoverMoviesSafeCall(queries, page)

    }

    @InternalCoroutinesApi
    fun getRecentMovie(queries: Map<String, String>, page: Int) = viewModelScope.launch {
        getRecentMovieSafeCall(queries, page)

    }

    @InternalCoroutinesApi
    fun getTopMovie(queries: Map<String, String>, page: Int) = viewModelScope.launch {
        getTopMovieSafeCall(queries, page)

    }

    @InternalCoroutinesApi
    fun getPopularMovie(queries: Map<String, String>, page: Int) = viewModelScope.launch {
        getPopularMovieSafeCall(queries, page)

    }

    @InternalCoroutinesApi
     fun getMovieList(queries: Map<String, String>, page: Int) = viewModelScope.launch {
        getMovieListSafeCall(queries, page)

    }

    @InternalCoroutinesApi
    private fun offlineCashMovie(movie: Movie) {

        val movieEntity = MovieEntity(movie)
        insertMovie(movieEntity)
    }

    @InternalCoroutinesApi
    private fun offlineCashPopularMovie(movie: Movie) {

        val movieEntity = MoviePopularEntity(movie)
        insertPopularMovie(movieEntity)
    }

    @InternalCoroutinesApi
    private fun offlineCashRecentMovie(movie: Movie) {

        val movieEntity = MovieRecentEntity(movie)
        insertRecentMovie(movieEntity)
    }

    @InternalCoroutinesApi
    private fun offlineCashDiscoverMovie(movie: Movie) {

        val movieEntity = MovieDiscoverEntity(movie)
        insertDiscoverMovie(movieEntity)
    }

    @InternalCoroutinesApi
    private fun offlineCashMovieListMovie(movie: Movie) {

        val movieEntity = MovieListEntity(movie)
        insertMovieListMovie(movieEntity)
    }


    @InternalCoroutinesApi
    private suspend fun getDiscoverMoviesSafeCall(queries: Map<String, String>, page: Int) {

        getAllMovie.value = NetworkResult.Loading()
        //인터넷이 연결되었을 때
        if (hasInternetConnection()) {


            try {


                val response = repository.remote.getDiscoverMovie(queries, page)
                getAllMovie.value = handleMovieResponse(response)


                val movieData = getAllMovie.value!!.data
                Log.d(TAG, "getDiscoverMoviesSafeCall: $movieData")

                if (movieData != null) {

                    offlineCashDiscoverMovie(movieData)
                }


            } catch (e: Exception) {

                getAllMovie.value = NetworkResult.Error("영화를 찾을수 없음")
            }
        } else {
            // 인터넷 연결 실패
            getAllMovie.value = NetworkResult.Error("No Internet Connection.")
        }

    }


    private suspend fun getRecentMovieSafeCall(queries: Map<String, String>, page: Int) {


        getAllMovie.value = NetworkResult.Loading()

        //인터넷이 연결되었을 때
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecentMovie(queries, page)

                getAllMovie.value = handleMovieResponse1(response)

                val movieData = getAllMovie.value!!.data
                Log.d(TAG, "getRecentMovieSafeCall: $movieData}")
                if (movieData != null) {

                    offlineCashRecentMovie(movieData)
                }
            } catch (e: Exception) {
                getAllMovie.value = NetworkResult.Error("영화를 찾을수 없음")

            }
        } else {
            // 인터넷 연결 실패
            getAllMovie.value = NetworkResult.Error("No Internet Connection.")


        }

    }


    private suspend fun getPopularMovieSafeCall(queries: Map<String, String>, page: Int) {

        getAllMovie.value = NetworkResult.Loading()
        //인터넷이 연결되었을 때
        if (hasInternetConnection()) {

            try {
                val response = repository.remote.getPopularMovie(queries, page)
                getAllMovie.value = handleMovieResponse2(response)
                val movieData = getAllMovie.value!!.data

                Log.d(TAG, "getPopularMovieSafeCall: $movieData")
                if (movieData != null) {

                    offlineCashPopularMovie(movieData)
                }


            } catch (e: Exception) {
                getAllMovie.value = NetworkResult.Error("영화를 찾을수 없음")

            }
        } else {
            // 인터넷 연결 실패
            getAllMovie.value = NetworkResult.Error("No Internet Connection.")

        }
    }


    private suspend fun getTopMovieSafeCall(queries: Map<String, String>, page: Int) {

        getAllMovie.value = NetworkResult.Loading()

        //인터넷이 연결되었을 때
        if (hasInternetConnection()) {

            try {
                val response = repository.remote.getTopMovie(queries, page)
                getAllMovie.value = handleMovieResponse3(response)
                val movieData = getAllMovie.value!!.data
                if (movieData != null) {

                    offlineCashMovie(movieData)
                }

            } catch (e: Exception) {
                getAllMovie.value = NetworkResult.Error("영화를 찾을수 없음")

            }
        } else {
            // 인터넷 연결 실패
            getAllMovie.value = NetworkResult.Error("No Internet Connection.")


        }

    }


    private suspend fun getMovieListSafeCall(queries: Map<String, String>, page: Int) {

        getAllMovie.value = NetworkResult.Loading()

        //인터넷이 연결되었을 때
        if (hasInternetConnection()) {

            try {
                val response = repository.remote.getMovieList(queries, page)
                getAllMovie.value = handleMovieResponse3(response)

                val movieData = getAllMovie.value!!.data
                if (movieData != null) {

                    offlineCashMovieListMovie(movieData)
                }

            } catch (e: Exception) {
                getAllMovie.value = NetworkResult.Error("영화를 찾을수 없음")

            }
        } else {
            // 인터넷 연결 실패
            getAllMovie.value = NetworkResult.Error("No Internet Connection.")


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
            response.body()!!.results.isNullOrEmpty() -> {

                return NetworkResult.Error("API 연동 실패")
            }

            //성공했을 때 data 를 가져옴
            response.isSuccessful -> {

                val movieData = response.body()
                Log.d(TAG, "handleMovieResponse Discover: $movieData")
                return NetworkResult.Success(movieData!!)
            }
            else -> {

                return NetworkResult.Error(response.message())
            }

        }
    }

    private fun handleMovieResponse1(response: Response<Movie>): NetworkResult<Movie> {

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
            response.body()!!.results.isNullOrEmpty() -> {

                return NetworkResult.Error("API 연동 실패")
            }

            //성공했을 때 data 를 가져옴
            response.isSuccessful -> {

                val movieData = response.body()
                Log.d(TAG, "handleMovieResponse Recent: $movieData")
                return NetworkResult.Success(movieData!!)
            }
            else -> {

                return NetworkResult.Error(response.message())
            }

        }
    }

    private fun handleMovieResponse2(response: Response<Movie>): NetworkResult<Movie> {

        when {
            //너무 오래 걸릴 때
            response.toString().contains("Timeout") -> {

                return NetworkResult.Error("Timeout")
            }

            // 402가 뜰 때
            response.code() == 402 -> {

                return NetworkResult.Error("API Key Limited.")
            }

            //데이터가 없을 때
            response.body()?.results.isNullOrEmpty() -> {

                return NetworkResult.Error("API 연동 실패")
            }

            //성공했을 때 data 를 가져옴
            response.isSuccessful -> {

                val movieData = response.body()
                Log.d(TAG, "handleMovieResponse Popular: $movieData")
                return NetworkResult.Success(movieData!!)
            }
            else -> {

                return NetworkResult.Error(response?.message())
            }

        }
    }

    private fun handleMovieResponse3(response: Response<Movie>): NetworkResult<Movie> {

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
            response.body()!!.results.isNullOrEmpty() -> {

                return NetworkResult.Error("API 연동 실패")
            }

            //성공했을 때 data 를 가져옴
            response.isSuccessful -> {

                val movieData = response.body()
                Log.d(TAG, "handleMovieResponse Top: $movieData")
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

