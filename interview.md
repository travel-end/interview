### kotlin
####also函数，返回调用者本身，如：
fun makeDir(path:String) = path.let{File(it)}.also{it.mkdirs()}