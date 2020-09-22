package com.journey.interview.recyclerview.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.journey.interview.R
import com.journey.interview.customizeview.popupwindow.CommonPopupWindow
import com.journey.interview.recyclerview.bean.Image
import com.journey.interview.recyclerview.bean.Music
import com.journey.interview.recyclerview.bean.SectionHeader
import com.journey.interview.recyclerview.bean.User
import com.journey.interview.recyclerview.core.*
import kotlinx.android.synthetic.main.activity_list.*
import java.util.ArrayList

/**
 * @By Journey 2020/8/21
 * @Description
 */
class ListActivity:AppCompatActivity() {
    private val data: MutableList<Any> = ArrayList()
    var adapter: EfficientAdapter<Any>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initData()

//        impl1()
//        impl2()
        impl3()
    }

    /**
     * 第一种实现方式
     */
    private fun impl1() {
        val gridLayoutManager = GridLayoutManager(this@ListActivity, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter?.getItem(position) is Image) 1 else 3
            }
        }
        recycle_view.layoutManager = gridLayoutManager
        adapter = EfficientAdapter<Any>()
            .register(object : ViewHolderCreator<Any>() {
                override fun isForViewType(data: Any?, position: Int) = data is SectionHeader
                override fun getLayoutResourceId() = R.layout.item_setion_header

                override fun onBindViewHolder(
                    data: Any?,
                    items: MutableList<Any>?, position: Int,
                    holder: ViewHolderCreator<Any>
                ) {
                    val header = data as SectionHeader
                    setText(R.id.section_title, header.title)
                }
            }).register(object : ViewHolderCreator<Any>() {
                override fun isForViewType(data: Any?, position: Int) = data is User
                override fun getLayoutResourceId() = R.layout.item_user

                override fun onBindViewHolder(
                    data: Any?,
                    items: MutableList<Any>?, position: Int,
                    holder: ViewHolderCreator<Any>
                ) {
                    val user = data as User
                    setText(R.id.name, user.name)
                    setImageResource(R.id.avatar, user.avatarRes)

                    //如果你的控件找不到方便赋值的方法，可以通过 findViewById 去查找
                    val phone = findViewById<TextView>(R.id.phone)
                    phone.text = user.phone
                }
            }).register(object : ViewHolderCreator<Any>() {
                override fun isForViewType(data: Any?, position: Int) = data is Image

                override fun getLayoutResourceId() = R.layout.item_image

                override fun onBindViewHolder(
                    data: Any?,
                    items: MutableList<Any>?, position: Int,
                    holder: ViewHolderCreator<Any>
                ) {
                    val image = data as Image
                    setImageResource(R.id.imageView, image.res)
                }
            }).register(object : ViewHolderCreator<Any>() {
                override fun isForViewType(data: Any?, position: Int) = data is Music
                override fun getLayoutResourceId() = R.layout.item_music

                override fun onBindViewHolder(
                    data: Any?,
                    items: MutableList<Any>?, position: Int,
                    holder: ViewHolderCreator<Any>
                ) {
                    val music = data as Music?
                    setText(R.id.name, music!!.name)
                    setImageResource(R.id.cover, music.coverRes)
                }
            }).attach(recycle_view)
        adapter?.submitList(data)
    }

    /**
     * 第二种实现方式
     */
    private fun impl2() {
        val gridLayoutManager = GridLayoutManager(this@ListActivity, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter?.getItem(position) is Image) 1 else 3
            }
        }
        recycle_view.layoutManager = gridLayoutManager
        adapter = efficientAdapter<Any> {
            addItem(R.layout.item_setion_header) {
                isForViewType { data, _ -> data is SectionHeader }
                bindViewHolder { data, _, _ ->
                    val header = data as SectionHeader
                    setText(R.id.section_title, header.title)
                }
            }
            addItem(R.layout.item_user) {
                isForViewType { data, _ -> data is User }
                bindViewHolder { data, _, _ ->
                    val user = data as User
                    setText(R.id.name, user.name)
                    setImageResource(R.id.avatar, user.avatarRes)
                    //如果你的控件找不到方便赋值的方法，可以通过 findViewById 去查找
                    val phone = findViewById<TextView>(R.id.phone)
                    phone.text = user.phone
                }
            }
            addItem(R.layout.item_image) {
                isForViewType { data, _ -> data is Image }
                bindViewHolder { data, _, _ ->
                    val image = data as Image
                    setImageResource(R.id.imageView, image.res)
                }
            }
            addItem(R.layout.item_music) {
                isForViewType { data, _ -> data is Music }
                bindViewHolder { data, _, _ ->
                    val music = data as Music
                    setText(R.id.name, music.name)
                    setImageResource(R.id.cover, music.coverRes)
                }
            }
        }.attach(recycle_view)
        adapter?.submitList(data)
    }

    /**
     * 第三种实现方式
     */
    private fun impl3() {
        recycle_view.setup<Any> {
            withLayoutManager {
                val gridLayoutManager = GridLayoutManager(this@ListActivity, 3)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItem(position) is Image) 1 else 3
                    }
                }
                return@withLayoutManager gridLayoutManager
            }
            adapter {
                addItem(R.layout.item_setion_header) {
                    isForViewType { data, _ -> data is SectionHeader }
                    bindViewHolder { data, _, _ ->
                        val header = data as SectionHeader
                        setText(R.id.section_title, header.title)
                    }
                }
                addItem(R.layout.item_user) {
                    isForViewType { data, _ -> data is User }
                    bindViewHolder { data, pos, _ ->
                        val user = data as User
                        setText(R.id.name, user.name)
                        setImageResource(R.id.avatar, user.avatarRes)
                        //如果你的控件找不到方便赋值的方法，可以通过 findViewById 去查找
                        val phone = findViewById<TextView>(R.id.phone)
                        phone.text = user.phone
                        itemClicked(View.OnClickListener {
                            Log.i("JG",user.name)
                        })
                    }
                }
                addItem(R.layout.item_image) {
                    isForViewType { data, _ -> data is Image }
                    bindViewHolder { data, pos, _ ->
                        val image = data as Image
                        setImageResource(R.id.imageView, image.res)
                        itemClicked(View.OnClickListener {
                            Log.i("JG","pos=$pos")
                        })
                    }
                }
                addItem(R.layout.item_music) {
                    isForViewType { data, _ -> data is Music }
                    bindViewHolder { data, _, _ ->
                        val music = data as Music?
                        setText(R.id.name, music!!.name)
                        setImageResource(R.id.cover, music.coverRes)
                        itemClicked(View.OnClickListener {
                            Log.i("JG", music.name)
                        })
                    }
                }
            }
        }
        recycle_view.submitList(data)
    }

    private fun initData() {
        data.add(SectionHeader("My Friends"))
        data.add(User("Jack", 21, R.drawable.icon1, "123456789XX"))
        data.add(User("Marry", 17, R.drawable.icon2, "123456789XX"))
        data.add(SectionHeader("My Images"))
        data.add(Image(R.drawable.cover1))
        data.add(Image(R.drawable.cover2))
        data.add(Image(R.drawable.cover3))
        data.add(Image(R.drawable.cover4))
        data.add(Image(R.drawable.cover5))
        data.add(Image(R.drawable.cover6))
        data.add(Image(R.drawable.cover7))
        data.add(Image(R.drawable.cover8))
        data.add(Image(R.drawable.cover9))
        data.add(Image(R.drawable.cover10))
        data.add(Image(R.drawable.cover11))
        data.add(SectionHeader("My Musics"))
        data.add(Music("Love story", R.drawable.icon3))
        data.add(Music("Nothing's gonna change my love for u", R.drawable.icon4))
        data.add(Music("Just one last dance", R.drawable.icon5))
    }
}