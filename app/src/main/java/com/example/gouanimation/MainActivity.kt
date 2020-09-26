package com.example.gouanimation

import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //2.设置回调函数的具体实现
        mProgress.resort = ProgressView.SUCCESS
        mProgress.bgColor = Color.GREEN
        mProgress.callBack ={
            //做下载任务
            downLoadAnim()
        }

    }
    private fun downLoadAnim(){
        ValueAnimator.ofFloat(0f,1.0f).apply {
            duration = 2000
            addUpdateListener {
                ( it.animatedValue as Float ).also {value->
                    mProgress.progress = value
                }
            }
            start()
        }
    }
}
