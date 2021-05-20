package com.example.movie.ui.fragment

import android.annotation.SuppressLint
import android.media.Rating
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.adapters.LikeViewAdapter
import com.example.movie.data.Viedo.GetVideoResponse
import com.example.movie.data.Viedo.Video
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.FragmentExampleMovieBinding
import com.example.movie.network.ApiInterface
import com.example.movie.untils.App
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.BASE_URL
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.viewmodels.DatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("CAST_NEVER_SUCCEEDS")
@InternalCoroutinesApi

class ExampleMovieFragment : Fragment() {


    // 저장 -> movieSave true
    // 삭제 -> movieSave false

    // liveData로 비동기처리하면서 movieSave 상태에 따라 snackBar에 뜨는 text변경

    private var _binding: FragmentExampleMovieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ExampleMovieFragmentArgs>()

    private val databaseViewModel: DatabaseViewModel by viewModels()
    private lateinit var contents: TextView
    private lateinit var examText: TextView
    private lateinit var rating: RatingBar

    private lateinit var constantsData: TextView
    private lateinit var examTextData: TextView
    private lateinit var ratingData: RatingBar
    private lateinit var img: String


    private var movieSave = false


    private var movieId: Int = 0


    private val likeAdapter: LikeViewAdapter by lazy {
        LikeViewAdapter(
            requireContext(),
            ExampleMovieFragment()

        )


    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExampleMovieBinding.inflate(inflater, container, false)


        val bundle = Bundle()


        val constantsDatas = bundle.getString("content")
        val examTextDatas = bundle.getString("title")
        val ratingDatas = bundle.getDouble("rating")
        img = bundle.getString("poster").toString()

        Log.d(TAG, "contentData : $constantsDatas, examTextDatas : $examTextDatas ratingDatas : $ratingDatas  ")

        imgLoad()

        contents = binding.examContents
        examText = binding.examTitle
        rating = binding.movieRating

        contents.text = args.result?.overview
        examText.text = args.result?.title


        // rating.rating = args.result?.vote_average !!. toFloat() / 2


        constantsData = binding.examContents
        examTextData = binding.examTitle
        ratingData = binding.movieRating




        imgLikeLoad()
        constantsData.text = constantsDatas
        examTextData.text = examTextDatas
        ratingData.rating = ratingDatas.toFloat() / 2



        setHasOptionsMenu(true)

        return binding.root
    }

    private fun imgLikeLoad() {
        Glide.with(App.instance)
            .load(BASE_IMG_URL + img)
            .centerCrop()
            .placeholder(R.drawable.test_post)
            .into(binding.examImg)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exam, menu)
        val menuItem = menu.findItem(R.id.menu_action_star)
        changeMenuItemColor(menuItem, R.color.white)
        checkSaveMovie(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_action_star && !movieSave) {
            insertMovie(item)

        } else if (item.itemId == R.id.menu_action_star && movieSave) {
            deleteMovie(item)
        } else {
            return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSaveMovie(menuItem: MenuItem) {

        // ROOM 과 jSON 객체로 받아온 id 값을 비교
        databaseViewModel.getAllData.observe(this, { favoritesEntity ->
            try {
                for (savedMovie in favoritesEntity) {
                    if (savedMovie.result.id == args.result?.id) {

                        changeMenuItemColor(menuItem, R.color.yellow)
                        movieId = savedMovie.id
                        movieSave = true
                    } else {
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    //데이터 저장
    private fun insertMovie(item: MenuItem) {
        val movieData = args.result?.let {
            MovieEntity(

                it
            )
        }


        movieData?.let { databaseViewModel.insertData(it) }

        movieSave = true

        likeAdapter.notifyDataSetChanged()

        changeMenuItemColor(item, R.color.yellow)


        showSnackBar("포스트가 저장되었습니다.")
        Log.d(
            TAG,
            "insertMovie: ${BASE_IMG_URL + args.result?.poster_path}, ${args.result?.title}"
        )
    }

    private fun deleteMovie(item: MenuItem) {
        val movieData = args.result?.let {
            MovieEntity(

                it
            )
        }



        movieData?.let { databaseViewModel.deleteData(it) }


        likeAdapter.notifyDataSetChanged()
        movieSave = false

        changeMenuItemColor(item, R.color.white)
        showSnackBar("포스트가 지워졌습니다.")
        Log.d(TAG, "deleteMovie: $movieData")
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.exampleLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()

    }

    private fun imgLoad() {
        Glide.with(App.instance)
            .load(BASE_IMG_URL + args.result?.poster_path)
            .centerCrop()
            .placeholder(R.drawable.test_post)
            .into(binding.examImg)
    }


    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    fun getVideo(
        movie_id: Long,
        onSuccess: (videos: List<Video>) -> Unit,
        onError: () -> Unit
    ) {
        val movieID = movie_id
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiInterface::class.java)
        val call = service.getVideoTrailer(movieID = movieID)

        call.enqueue(object : Callback<GetVideoResponse> {
            override fun onResponse(
                call: Call<GetVideoResponse>,
                response: Response<GetVideoResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("response", responseBody.toString())
                    if (responseBody!!.videos.isNotEmpty() || responseBody.videos.isNotEmpty()) {
                        onSuccess.invoke(responseBody.videos)
                    } else {
                        onError.invoke()
                    }
                } else {
                    Log.d("Result", "응답이 없습니다.")
                }
            }

            override fun onFailure(call: Call<GetVideoResponse>, t: Throwable) {
                Log.d("Error", "오류가 발생했습니다.")
            }
        })
    }

}


