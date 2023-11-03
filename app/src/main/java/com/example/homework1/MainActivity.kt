package com.example.homework1


import MVVM.MainViewModel
import WorkCache.MyWorker
import WorkCache.Schedule
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.homework1.databinding.ActivityMainBinding
import androidx.databinding.DataBindingUtil
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration

@HiltAndroidApp
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var animationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appContext = applicationContext
        val schedule = Schedule()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()
        val requestik = PeriodicWorkRequestBuilder<MyWorker>(Duration.ZERO).apply {
            setConstraints(constraints)
            addTag("TEST_BACK")
        }.build()
        with (WorkManager.getInstance(appContext)){
            enqueueUniquePeriodicWork("TEST_BACK", ExistingPeriodicWorkPolicy.REPLACE, requestik)
        }
        animationView = findViewById<LottieAnimationView>(R.id.animation)
        //animationView.setAnimation(R.raw.movie_animate)
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.onButtonStartClicked.observe(this) {
            val fragment = MovieListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_container, fragment)
                .commit()
        }

        val buttonStart: Button = binding.buttonStart
        buttonStart.setOnClickListener {
            viewModel.buttonStart()
        }
    }

}