package com.example.mr_framer_grocer.Helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class MySwipeHelper(context: Context, private val recyclerView: RecyclerView)
    :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    val BUTTON_WIDTH = 200
    private var buttons: MutableList<MyButton>?=null
    var gestureDetector: GestureDetector? = null
    var swipedPos = -1
    var swipeThreshold = 0.5f
    var buttonsBuffer:MutableMap<Int,MutableList<MyButton>>
    lateinit var recoverQueue: LinkedList<Int>

    abstract fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder,
                                        buffer:MutableList<MyButton>)

    private var gestureListener: SimpleOnGestureListener? = object : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            for (button in buttons!!) {
                if (button.onClick(e.x, e.y)) break
            }
            return true
        }
    }
    private val onTouchListener: View.OnTouchListener = object : View.OnTouchListener {

        override fun onTouch(view: View?, e: MotionEvent): Boolean {
            if (swipedPos < 0) return false
            val point = Point(e.rawX.toInt(), e.rawY.toInt())
            val swipedViewHolder: RecyclerView.ViewHolder? = recyclerView!!.findViewHolderForAdapterPosition(swipedPos)
            val swipedItem: View = swipedViewHolder!!.itemView
            val rect = Rect()
            swipedItem.getGlobalVisibleRect(rect)
            if (e.action == MotionEvent.ACTION_DOWN || e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y) gestureDetector!!.onTouchEvent(e) else {
                    recoverQueue.add(swipedPos)
                    swipedPos = -1
                    recoverSwipedItem()
                }
            }
            return false
        }
    }

    init {
        this.buttons = ArrayList()
        this.gestureDetector = GestureDetector(context, gestureListener)
        this.recyclerView.setOnTouchListener(onTouchListener)
        this.buttonsBuffer = HashMap()
        this.recoverQueue = IntLinkedList()

        attachSwipe()
    }

        class IntLinkedList : LinkedList<Int>() {
        override fun contains(element: Int): Boolean {
            return false
        }

        override fun lastIndexOf(element: Int): Int {
            return element
        }

        override fun remove(element: Int): Boolean {
            return false
        }

        override fun indexOf(element: Int): Int {
            return element
        }

        override fun add(element: Int): Boolean {
            return if(contains(element))
                false
            else super.add(element)
        }
    }

    fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipedPos !== pos) recoverQueue.add(swipedPos)
        swipedPos = pos
        if (buttonsBuffer.containsKey(swipedPos)) buttons = buttonsBuffer.get(swipedPos)!!
        else
            buttons!!.clear()

        buttonsBuffer.clear()
        swipeThreshold = 0.5f * buttons!!.size * BUTTON_WIDTH
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView: View = viewHolder.itemView
        if (pos < 0) {
            swipedPos = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: MutableList<MyButton> = ArrayList()
                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateMyButton(viewHolder, buffer);
                    buffer.let { buttonsBuffer.put(pos, it) }
                } else {
                    buffer = buttonsBuffer[pos]!!
                }
                translationX = dX * buffer.size * BUTTON_WIDTH / itemView.getWidth()
                drawButtons(c, itemView, buffer, pos, translationX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    @Synchronized
    open fun recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            val pos: Int? = recoverQueue.poll()
            if (pos != null) {
                if (pos > -1) {
                    recyclerView.getAdapter()?.notifyItemChanged(pos)
                }
            }
        }
    }

    open fun drawButtons(c: Canvas, itemView: View, buffer: MutableList<MyButton>?, pos: Int, dX: Float) {
        var right: Float = itemView.right.toFloat()
        val dButtonWidth = -1 * dX / buffer!!.size

        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(
                    c,
                    RectF(
                            left,
                            itemView.top.toFloat(),
                            right,
                            itemView.bottom.toFloat()
                    ),
                    pos
            )
            right = left
        }
    }





}