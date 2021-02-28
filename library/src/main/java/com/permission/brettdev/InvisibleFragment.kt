package com.permission.brettdev

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

// typealias可以给任意类型指定一个别名
typealias PermissionCallback = (Boolean,List<String>) ->Unit

//想要对运行时权限的api进行封装并不是一件容易的事情，因为这个操作有特定的上下文依赖的，
// 一般需要在activity中接收onRequestPermissionsResult方法的回调才行，所以不能简单地将整个操作封装到一个独立的类中
//事实上，如果涉及到对上下文的封装，可以使用一个看不见的fragment进行封装。
//fragment不像activity那样必须有界面，我们完全可以向activity中添加一个隐藏的fragment(不要重写oncreateview方法),然后
//在这个fragment中国对运行时权限的api进行封装。这是一种非常轻量级的做法，不用担心fragment会对activity的性能造成什么影响。
class InvisibleFragment:Fragment() {
    private var callback :PermissionCallback? = null

    fun requestNow(cb:PermissionCallback,vararg permission:String){ // vararg接收了一个可变长度的permissions参数列表
        callback = cb
        requestPermissions(permission,1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode ==1){
            val deniedList = mutableListOf<String>()
            for ((index,result) in grantResults.withIndex()){
                if(result!= PackageManager.PERMISSION_GRANTED){
                    deniedList.add(permissions[index])
                }
            }
            val allGranted = deniedList.isEmpty()
            callback?.let { it(allGranted,deniedList) }
        }
    }
}