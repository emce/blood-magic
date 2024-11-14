package mobi.cwiklinski.bloodline.activityprovider.api

import android.app.Activity

interface ActivityProvider {
    fun get(): Activity
}