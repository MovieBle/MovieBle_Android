package com.example.movie.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.LocalActivityResultRegistryOwner.current
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapters.ChatAdapter
import com.example.movie.adapters.LikeViewAdapter
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.data.database.entities.MovieLikeEntity
import com.example.movie.databinding.FragmentExampleMovieBinding
import com.example.movie.databinding.MovieChatListRowLayoutBinding
import com.example.movie.models.ChatModel
import com.example.movie.ui.activity.SplashActivity
import com.example.movie.ui.activity.SplashActivity.Companion.userName
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.untils.MovieCase
import com.example.movie.viewmodels.DatabaseViewModel
import com.example.movie.viewmodels.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar.SHORT


@Suppress("CAST_NEVER_SUCCEEDS", "UNREACHABLE_CODE")
@InternalCoroutinesApi
@AndroidEntryPoint
class ExampleMovieFragment : Fragment() {


    // 저장 -> movieSave true
    // 삭제 -> movieSave false

    // liveData로 비동기처리하면서 movieSave 상태에 따라 snackBar에 뜨는 text변경
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentExampleMovieBinding? = null
    private val binding get() = _binding!!

    private val currentDateTime = Calendar.getInstance().time
    private val args by navArgs<ExampleMovieFragmentArgs>()
    private val auth = FirebaseAuth.getInstance()
    private val databaseViewModel: DatabaseViewModel by viewModels()
    private val viewModel: ViewModel = ViewModel()
    private lateinit var dateFormat: String

    private lateinit var contents: TextView
    private lateinit var examText: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseText: TextView
    var num = 0
    private var movieSave = false

    @InternalCoroutinesApi
    private val chatAdapter: ChatAdapter by lazy {
        ChatAdapter(


        )
    }
    private val chatRecycler: RecyclerView by lazy {

        binding.recyclerView
    }
    var movieId: Int = 0

    private val likeAdapter: LikeViewAdapter by lazy {
        LikeViewAdapter(
            requireContext(),

            )
    }


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExampleMovieBinding.inflate(inflater, container, false)
        postInfo()



        contents = binding.examContents
        examText = binding.examTitle
        rating = binding.movieRating
        releaseText = binding.releaseText

        args.result?.poster_path?.let { viewModel.imageLoad(binding.examImg, it) }

        rating.rating = args.result?.vote_average!!.toFloat() / 2

        contents.text = args.result?.overview
        examText.text = args.result?.title
        releaseText.text = args.result?.release_date

        binding.button3.setOnClickListener {

            textNullTest()
        }
        setAdapter()

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setAdapter() {

        chatRecycler.adapter = chatAdapter
        chatRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        chatRecycler.setHasFixedSize(false)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exam, menu)
        val menuItem = menu.findItem(R.id.menu_action_star)
        changeMenuItemColor(menuItem, R.color.white)
        checkSaveMovie(menuItem)
    }

    private fun textNullTest() {
        if (!TextUtils.isEmpty(binding.chatEditText.text.toString().trim { it <= ' ' }))
            postInfo()
        else {
            Toast.makeText(requireContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
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
        databaseViewModel.getAllLikeData.observe(this, { favoritesEntity ->
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
        val movieData = args.result.let {
            MovieLikeEntity(
                movieId++,
                args.result!!
            )
        }

        databaseViewModel.insertLikeMovie(movieData)

        viewModel.showSnackBar("포스트가 저장되었습니다.", requireView())

        movieSave = true


        likeAdapter.setLikeData(listOf(movieData))

        changeMenuItemColor(item, R.color.yellow)

        Log.d(
            TAG,
            "insertMovie: ${BASE_IMG_URL + args.result?.poster_path}, ${args.result?.title}"
        )
    }

    private fun deleteMovie(item: MenuItem) {


        val movieData = args.result?.let {
            MovieLikeEntity(
                movieId--,
                args.result!!
            )
        }


        movieData?.let {
            databaseViewModel.deleteLikeMovie(it)
            likeAdapter.setLikeData(listOf(movieData))
            viewModel.showSnackBar("포스트가 지워졌습니다.", requireView())


            movieSave = false

            changeMenuItemColor(item, R.color.white)

        }



        Log.d(TAG, "deleteMovie: $movieData")


    }


    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    //chat
    private fun postInfo() {


        val onlyDate: LocalDate = LocalDate.now()

        dateFormat =
            SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
        val day = onlyDate

        val chatModel = ChatModel(

            binding.chatEditText.text.toString(),
            day.toString(),
            userName.toString(),
            num
        )
        Log.d(TAG, "day: $day")



        db.collection("chat").document(auth.currentUser?.uid + dateFormat)
            .set(
                chatModel
            )
            .addOnSuccessListener {

                Log.d(TAG, "postInfo: 성공 $chatModel")
                binding.chatEditText.setText("")
            }
            .addOnFailureListener {
                Log.d(TAG, "postInfo: 실패  $it")
            }

    }


}


