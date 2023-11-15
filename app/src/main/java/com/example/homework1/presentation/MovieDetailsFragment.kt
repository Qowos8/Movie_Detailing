package com.example.homework1.presentation

import com.example.homework1.presentation.adapters.ActorAdapter
import com.example.homework1.presentation.adapters.DetailAdapter
import com.example.homework1.data.DetailDataBase
import com.example.homework1.data.` api`.ActorApi
import com.example.homework1.data.` api`.ActorRetrofitModule
import com.example.homework1.data.` api`.Api_movie
import com.example.homework1.data.` api`.DetailApi
import com.example.homework1.data.` api`.DetailRetrofitModule
import com.example.homework1.data.` api`.OnMovieClickListener
import com.example.homework1.presentation.factory.DetailsViewModelFactory
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework1.R
import com.example.homework1.databinding.ActorDefaultBinding
import com.example.homework1.databinding.DetailsDefaultBinding
import com.example.homework1.presentation.viewModels.DetailsViewModel
import kotlinx.coroutines.launch


class MovieDetailsFragment : Fragment(), OnMovieClickListener {
    private lateinit var detailRecyclerView: RecyclerView
    private lateinit var adapter: DetailAdapter
    private lateinit var viewModel: DetailsViewModel
    private lateinit var detailBinding: DetailsDefaultBinding
    private lateinit var locationsDb: DetailDataBase
    private lateinit var actorRecyclerVIew: RecyclerView
    private lateinit var actorBinding: ActorDefaultBinding
    private lateinit var actorAdapter: ActorAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        //locationsDb = DetailDataBase.newInstance(requireContext())
    }
    @SuppressLint("NotifyDataSetChanged", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.details_default,
            container,
            false
        )
        actorBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.actor_default,
            container,
            false
        )

        val apiDetailsService: DetailApi = DetailRetrofitModule.retrofit.create(DetailApi::class.java)
        val apiActorService: ActorApi = ActorRetrofitModule.retrofit.create(ActorApi::class.java)
        detailRecyclerView = detailBinding.root.findViewById(R.id.detail_recycler_view)
        actorRecyclerVIew = detailBinding.root.findViewById(R.id.actor_recycler_view)
        val layoutManager = LinearLayoutManager(requireContext())
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        detailRecyclerView.layoutManager = layoutManager
        actorRecyclerVIew.layoutManager = horizontalLayoutManager
        adapter = DetailAdapter(requireContext(), emptyList())
        actorAdapter = ActorAdapter(requireContext(), emptyList())// Пустой адаптер
        detailRecyclerView.adapter = adapter
        actorRecyclerVIew.adapter = actorAdapter
        var movie_index = arguments?.getInt(ARG_MOVIE_ID, -1) ?: -1
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        Log.d("movie_index", "$movie_index")

        viewModel = ViewModelProvider(requireActivity(), DetailsViewModelFactory(apiDetailsService, apiActorService))[DetailsViewModel::class.java]
        Log.d("movie_index", "$movie_index")
        viewModel.setMovieID(movie_index)
        viewModel.setActor(requireContext())
        Log.d("viewModel:", "viewModel = $viewModel")
        viewModel.setDetails(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
                viewModel.movieDetails.observe(viewLifecycleOwner){movieDetails ->
                    adapter = DetailAdapter(requireContext(), movieDetails!!)
                    detailRecyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    progressBar?.visibility = View.GONE

                }
            }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actorInfo.observe(viewLifecycleOwner){actorInfo ->
                actorAdapter = ActorAdapter(requireContext(), actorInfo!!)
                actorRecyclerVIew.adapter = actorAdapter
            }
        }
        //actorBinding.lifecycleOwner = viewLifecycleOwner
        detailBinding.viewModel = viewModel
        detailBinding.lifecycleOwner = viewLifecycleOwner
        Log.d("MovieDetailsFragment", "Received movieID in onCreateView: $movie_index")


        detailRecyclerView.layoutManager = layoutManager
        actorRecyclerVIew.layoutManager = horizontalLayoutManager
        /*sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
            //drawingViewId = R.id.
        }*/
        return detailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button = view.findViewById(R.id.back_btn)
        val movieID = arguments?.getInt(ARG_MOVIE_ID, -1) ?: -1
        button.setOnClickListener {
            val fragment = MovieListFragment()
            requireFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit()
            viewModel.navigateBack(movieID)

        }
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition()
        }
    }

    override fun onMovieClicked(movie: Api_movie, movieID: Int) {
        val fragment = newInstance(movieID)
        requireFragmentManager().beginTransaction()
            .replace(R.id.frame_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ARG_MOVIE_ID = "movie_id"
        const val NET_ID = "id123"

        fun newInstance(movieID: Int): MovieDetailsFragment {

            val args = Bundle()
            args.putInt(ARG_MOVIE_ID, movieID)
            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            Log.d("MovieDetailsFragment", "Created new instance with movieID: $movieID")
            return fragment
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        //locationsDb.close()
    }

}
