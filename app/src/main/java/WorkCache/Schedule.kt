package WorkCache

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit
class Schedule {
    /*val simpleRequest = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
    val delayedRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
        .setInitialDelay(10L, TimeUnit.SECONDS)

        .build()*/
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(true)
        .build()
    val constrainedRequest = PeriodicWorkRequest
        .Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()
    /*val constrainedRequest = OneTimeWorkRequest
        .Builder(MyWorker::class.java)
        .setConstraints(constraints)
        .build()*/
    /*val myWorkerRequest: WorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
        .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
        .build()*/

}
