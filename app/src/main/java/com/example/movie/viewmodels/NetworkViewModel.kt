package com.example.movie.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.movie.data.Movie
import com.example.movie.repositorys.NetworkRepository
import com.example.movie.untils.Constants.Companion.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(

    application: Application,
    private val networkRepository: NetworkRepository
) : AndroidViewModel(application) {


    // LiveData 가 아닌 MutableLiveData 를 쓴 이유
    // LiveData 는 읽기전용으로 값의 변경을 못하지만 MutableLiveData는 get/set이 가능하다. 그래서 데이터 변경이 잦는
    // 네트워크는 MutableLiveData를 쓰는게 합리적이다.
    private val result: LiveData<Movie>
        get() = networkRepository.getMovie

    private val resultPopular: LiveData<Movie>
        get() = networkRepository.getPopular

    private val resultTop: LiveData<Movie>
        get() = networkRepository.getTop

    private val resultDiscover: LiveData<Movie>
        get() = networkRepository.getDiscover

    private val resultRecent: LiveData<Movie>
        get() = networkRepository.getRecent


    fun getNowPlayingMovies(page: Int) {

        Log.d(TAG, "getNowPlayingMovies: ")
        networkRepository.getNowPlayingMovies(page)

    }

    fun getTopRatedMovies(page: Int) {

        Log.d(TAG, "getTopRatedMovies: ")
        networkRepository.getTopRatedMovies(page)

    }


    fun getDiscoverMovies(page: Int) {

        Log.d(TAG, "getDiscoverMovies: ")
        networkRepository.getDiscoverMovies(page)

    }

//    fun getMovieList(page: Int) {
//
//        Log.d(TAG, "getMovieList: ")
//        baeminRepository.getMovieList(page)
//
//    }

    fun getRecentMovies(page: Int) {

        Log.d(TAG, "getMovie: ")

        networkRepository.getRecentMovie(page)

    }

    @InternalCoroutinesApi
    fun getPopularMovies(page: Int) {

        Log.d(TAG, "getPopularMovies: ")
        networkRepository.getPopularMovies(page)

    }

    fun getAll(): LiveData<Movie> {
        return result
    }

    fun getAllPopular(): LiveData<Movie> {
        return resultPopular
    }

    fun getAllTop(): LiveData<Movie> {
        return resultTop
    }

    fun grtAllDiscover(): LiveData<Movie> {
        return resultDiscover
    }

    fun getAllRecent(): LiveData<Movie> {
        return resultRecent
    }


}

