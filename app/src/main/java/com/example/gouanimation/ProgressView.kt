package com.example.gouanimation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @Description
 * 代码高手
 */
class ProgressView : View{
    //1.定义函数的类型
    //参数  -> 需要返回的数据的类型   返回值 ->是否需要对方给我结果
    var callBack:(()-> Unit)? = null


    //定义变成半圆的动画因子
    private var cornerRadius = 0f
    //定义中间靠拢的动画因子
    private var transX = 0f


    //定义进度的变化因子 0-1.0
     var progress = 0f


    set(value) {
        field  = value
       //刷新
        invalidate()
        //开启完成动画
        if(progress==1.0f){
            startFinishAnim()
        }
    }

    //矩形区域的画笔
    private val RoundRectPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    //定义变量保存下载结果
    var resort= UNKOOW

    //绘制勾勾或者叉叉的路径Path
    private var markPath = Path()
    //结果的size
    private var markSize = 0f
    //中心点坐标
    private var cx =0f
    private var cy =0f
    //勾勾裁剪动画的动画因子
    private var clipSize = 0f

    //勾勾叉叉的画笔
    private val markPaint = Paint().apply {
        color=Color.WHITE
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    //暴露接口给外部使用
    var bgColor = Color.BLUE


    //静态的变量 方法
    companion object{
        val SUCCESS = 0
        val FAILURE = 1
        val UNKOOW  = 2
    }
    constructor(context: Context):super(context){}
    constructor(context: Context,attrs: AttributeSet?):super(context,attrs){}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //勾勾或者叉叉的矩形边长
        markSize = height/3f
        //中心点坐标
        cx = width/2f
        cy = height/2f

    }
    override fun onDraw(canvas: Canvas?) {
        if (progress < 1.0f) {
            //绘制背景
            RoundRectPaint.color =bgColor
            canvas?.drawRoundRect(
                0f, 0f, width.toFloat(), height.toFloat(),
                0f, 0f, RoundRectPaint
            )
        }
            //前景 进度
            RoundRectPaint.color = Color.MAGENTA
            //绘制一个圆角矩形
            canvas?.drawRoundRect(
            transX, 0f, progress * width-transX, height.toFloat(),
            cornerRadius, cornerRadius, RoundRectPaint
        )

        //判断是否绘制勾勾
       if( transX ==( width - height)/2f){
           if(resort== SUCCESS||resort== UNKOOW){
                  //绘制勾勾
               markPath.apply {
                  moveTo(cx-markSize/2,cy)
                   lineTo(cx-markSize/8,cy+markSize/2)
                   lineTo(cx+markSize/2,cy-markSize/4)
               }

           }else{
                //绘制叉叉
                markPath.apply {
                    moveTo(cx-markSize/2,cy-markSize/2)
                    lineTo(cx+markSize/2,cy+markSize/2)
                    moveTo(cx-markSize/2,cy+markSize/2)
                    lineTo(cx+markSize/2,cy-markSize/2)
                }
           }
           canvas?.drawPath(markPath,markPaint)

           //绘制遮罩层
           canvas?.drawRect(cx-markSize/2-10+clipSize,cy-markSize/2-10,cx+markSize/2+10,cy+markSize/2+10,
               RoundRectPaint)
       }
    }


    private  fun startFinishAnim(){
        //变成半圆
      val changeIntoCircle=  ValueAnimator.ofFloat(0f,height/2f).apply {
            duration = 1000
            addUpdateListener {anim->
                cornerRadius=  anim.animatedValue as Float
                invalidate()
            }
        }
        //中间靠拢的动画
      val moveToCenterAnim=  ValueAnimator.ofFloat(0f,(width-height)/2f).apply {
            duration = 1000
            addUpdateListener {anim->
                transX=  anim.animatedValue as Float
                invalidate()
            }
        }
        //裁剪动画
        val clipAnim = ValueAnimator.ofFloat(0f,markSize+20).apply {
            duration = 1000
            addUpdateListener {anim->
                clipSize=  anim.animatedValue as Float
                invalidate()
            }
        }

        AnimatorSet().apply {
            playSequentially(changeIntoCircle,moveToCenterAnim,clipAnim)
            start()
        }
    }

    //点击一下才产生动画效果
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            //3.调用
            callBack?.let {back->
               back()
               }
        }
        return true
    }

    /*
    显示文本 ：绘制文本
    drawText

    外部可以做更多的设置
       设置背景颜色   进度条颜色  勾勾颜色  字体颜色
       1.代码中设置
            将需要设置的值作为一个属性变量进行接收
       2.xml中配置
            自定义属性
       3.自定义ViewGroup
            measure
            Layout
     */
}