### kotlin
####also函数，返回调用者本身，如：
fun makeDir(path:String) = path.let{File(it)}.also{it.mkdirs()}

#### kotlin语法糖  

#### DSL


###关于kotlin中的内部类和嵌套类
在Kotlin中的内部类也是指在一个类的内部声明另一个类，但是和Java中的规则有些差异。
在Kotlin中的内部类和Java中的内部类相似，都会持有一个外部类的引用，但是在Kotlin中
内部类的声明方式变化了，必须要使用 `inner`修饰符。
在Kotlin中，没有静态内部类一说，Java中的静态内部类在Kotlin中称为`嵌套类`。而且默认的
就是嵌套类，也就是内部类不写任何修饰符就是`嵌套类`。
同样的，`内部类`会持有一个外部类的引用，`嵌套类`不持有外部类的引用。
下面通过代码来说明：
```
class OutClass {
    var count = 0
    fun add() {
        count++
    }
    // 内部类
    inner class InnerClass {
        fun doSomething():Int {
               // 默认持有外部类的引用，直接访问外部类的方法属性
            add()
            this@OutClass.add()
            return this@OutClass.count
        }
    }
    // 嵌套类
    class NestedClass {
        fun getValue() :Int {
            // 嵌套类不持有外部类的引用，必须通过外部类的对象访问
            val outClass = OutClass()
            outClass.add()
            return outClass.count
        }
    }
}
            
```
下面通过表格来对比一下Java和Kotlin的内部类
    类A在另一个类B中声明             在Java中              在Kotlin中
    嵌套类（不储存外部类的引用）      static class A          class A
    内部类（存储外部类的引用）        class A                 inner class A
    
    