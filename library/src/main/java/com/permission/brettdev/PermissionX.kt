package com.permission.brettdev

import android.app.Activity
import androidx.fragment.app.FragmentActivity

object PermissionX {
    private const val TAG = "InvisibleFragment"

    fun request(activity: FragmentActivity,vararg permissions:String,callback:PermissionCallback){
        val fragmentManager = activity.supportFragmentManager
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if(existedFragment!=null){
            existedFragment as InvisibleFragment
        }else{
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment,TAG).commitNow()//不能使用commit方法，commit方法不会立即执行添加操作无法保证下一行代码执行时fragment被添加到activity
            invisibleFragment
        }
        //permissions参数实际上是一个数组。对于数组，我们可以遍历它，可以通过下标访问，
        // 但是不可以直接将它传递给另外一个接收可变长度参数的方法。因此，在permissions参数前面加个*表示将一个数组转换成可变长度参数传递过去
        fragment.requestNow(callback,*permissions)
    }
}