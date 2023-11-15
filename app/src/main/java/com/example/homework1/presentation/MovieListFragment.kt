package com.example.homework1.presentation

import com.example.homework1.presentation.adapters.MovieAdapter
import com.example.homework1.data.ListDatabase
import com.example.homework1.data.MovieEntity
import com.example.homework1.data.` api`.Api_movie
import com.example.homework1.data.` api`.OnMovieClickListener
import com.example.homework1.presentation.factory.ListFactory
import com.example.homework1.presentation.viewModels.ListViewModel
import WorkCache.Schedule
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.homework1.R
import com.example.homework1.presentation.viewModels.MovieDetailsFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.launch

class MovieListFragment : Fragment(), OnMovieClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: ListViewModel
    private lateinit var locationsDb: ListDatabase
    private val movieList = MutableLiveData<List<MovieEntity>>()
    val schedule = Schedule()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationsDb = ListDatabase.newInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WorkManager.getInstance(requireContext()).enqueue(schedule.constrainedRequest)
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(schedule.constrainedRequest.id)
            .observe(viewLifecycleOwner) { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    Log.d("MyWorker", "Success")
                }
            }

        recyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        viewModel = ViewModelProvider(this, ListFactory(requireContext()))[ListViewModel::class.java]
        viewModel.loadMovies(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.observe(viewLifecycleOwner) { movies ->
                adapter = MovieAdapter(requireContext(), movies!!)
                recyclerView.adapter = adapter
                if(viewModel.status == 1)
                adapter.setOnClickListener(this@MovieListFragment)
                else{
                    showNoInternetSnackbar(view)
                    Toast.makeText(requireContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show()
                    Log.d("list", "offline")
                }
                }
            }
        viewModel.navigateToMovie.observe(viewLifecycleOwner) { movieId ->
            if (movieId != -1) {
                val movieDetailsFragment = MovieDetailsFragment.newInstance(movieId)
                requireFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, movieDetailsFragment)
                    .addToBackStack(null)
                    .commit()
                Log.d("MovieListFragment", "Navigate to movie details: $movieId")
            }
        }


    }
    override fun onMovieClicked(movie: Api_movie, movieIndex: Int) {
        val id: Int = movie.id
        val fragment = MovieDetailsFragment.newInstance(id)
        requireFragmentManager().beginTransaction()
            .add(R.id.frame_container, fragment)
            //.addtobackstack(null)
            .commit()
        Log.d("MovieListFragment", "Clicked on movie: ${movie.original_title}, id: $id")
        /*exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        val transitionName = getString(R.string.transtion_name)
        val extras = FragmentNavigatorExtras(cardView to transitionName)
        //val directions = LocalLayoutDirection.current
        //findNavController().navigate(directions, extras)
        *//*postponeEnterTransition()
        view?.doOnPreDraw { startPostponedEnterTransition() }*/
    }
    override fun onDestroy() {
        super.onDestroy()
        locationsDb.close()
    }

    private fun showNoInternetSnackbar(View: View) {
        val coordinatorLayout = view?.findViewById<CoordinatorLayout>(R.id.coord)
        if (coordinatorLayout != null) {
            val snackbar = Snackbar.make(
                View,
                "No internet connection",
                Snackbar.LENGTH_LONG,
            ).show()
        }
    }
}