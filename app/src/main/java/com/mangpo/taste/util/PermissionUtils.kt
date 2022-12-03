package com.mangpo.taste.util

import androidx.lifecycle.LifecycleCoroutineScope
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

fun checkPermission(lifecycleScope: LifecycleCoroutineScope, permission: String, deniedMsg: String, callback: (Boolean) -> Unit) {
    lifecycleScope.launch {
        val permissionResult = lifecycleScope.async {
            TedPermission.create()
                .setPermissions(permission)
                .setDeniedMessage(deniedMsg)
                .check()
        }

        callback.invoke(permissionResult.await().isGranted)
    }
}