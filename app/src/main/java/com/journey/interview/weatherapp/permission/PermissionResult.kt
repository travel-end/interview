package com.journey.interview.weatherapp.permission

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @CreateDate: 2020/6/21 16:28
 */
sealed class PermissionResult {
    object Grant : PermissionResult()
    class Deny(val permissions: Array<String>) : PermissionResult()
    class Rationale(val permissions: Array<String>) : PermissionResult()
}