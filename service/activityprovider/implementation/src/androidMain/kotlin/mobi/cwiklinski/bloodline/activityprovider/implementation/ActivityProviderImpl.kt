package mobi.cwiklinski.bloodline.activityprovider.implementation

import android.app.Activity
import mobi.cwiklinski.bloodline.activityprovider.api.ActivityProvider
import mobi.cwiklinski.bloodline.activityprovider.api.ActivitySetter

class ActivityProviderImp : ActivitySetter, ActivityProvider {
    private var activity: Activity? = null

    override fun set(activity: Activity) {
        this.activity = activity
    }

    override fun clear() {
        activity = null
    }

    override fun get(): Activity {
        return activity ?: throw Throwable("Activity does not exists")
    }
}
