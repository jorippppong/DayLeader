package com.example.dayleader.recycler

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.example.dayleader.R
import com.example.dayleader.TodoActivity
import com.example.dayleader.databinding.ItemTodoListBinding


class TodoAdapter:RecyclerView.Adapter<TodoAdapter.Holder>() {
    var listData = mutableListOf<TodoInfo>()

    //몇 개의 목록을 만들지 반환
    override fun getItemCount(): Int {
        return listData.size
    }

    //어떤 레이 아웃을 생성 할 것인가
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    //생성된 뷰에 무슨 데이터 를 넣을 것인가
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val member = listData[position]
        holder.setData(member, position)
    }

    //뷰 홀더
    //각 목록에 필요한 기능 들을 구현 하는 공간
    class Holder(val binding: ItemTodoListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val todoActivity = TodoActivity.getInstance()
        var mMember: TodoInfo? = null
        private var mPosition: Int? = null
        init {
            binding.btnCheck.setOnClickListener {
                //Check 표시 변경 가능 할 수 있는 기능 : 초기 설정
                if (!(mMember!!.complete)) {
                    binding.btnCheck.setImageResource(R.drawable.iv_daily_todo_checked)
                    val putDailyTodo = PutDailyTodoChanged("true", mMember!!.private_todo_id.toString())
                    putModifyCheck(token, putDailyTodo)
                    mMember!!.complete = true
                } else {
                    binding.btnCheck.setImageResource(R.drawable.btn_todo_disabled)
                    val putDailyTodo = PutDailyTodoChanged("false", mMember!!.private_todo_id.toString())
                    putModifyCheck(token, putDailyTodo)
                    mMember!!.complete = false
                }
            }

            binding.ivPhoto.setOnClickListener {
                todoActivity?.showImageBottomSheetDialog(mMember!!.imageUrl, mMember!!.task, mMember!!.date)
            }

            binding.btnThreeDot.setOnClickListener {
                todoActivity?.showDailyBottomSheetDialog(mMember!!, mPosition!!)
            }

        }

        fun setData(member: TodoInfo, position: Int) {
            this.mMember = member
            this.mPosition = position

            //text 설정
            binding.etTodo.setText(member.task)

            //complete = true, false
            if (mMember!!.complete) {
                binding.btnCheck.setImageResource(R.drawable.iv_daily_todo_checked)
            } else {
                binding.btnCheck.setImageResource(R.drawable.btn_todo_disabled)
            }

            //to-do 수정 하는 구간 (내용이 없으면 삭제될 수도 있음)
            if (mMember!!.modifyClicked) {
                binding.btnThreeDot.visibility = View.INVISIBLE
                binding.ivPhoto.visibility = View.INVISIBLE
                binding.btnCheck.isEnabled = false
                binding.etTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#6B9AC4"))
                binding.etTodo.isEnabled = true
                binding.etTodo.setSelection(member.task.length)
                binding.etTodo.requestFocus()

                //todo
                //todoActivity?.keyboardDown(binding.etTodo)

                binding.etTodo.setOnEditorActionListener { _, actionId, event ->
                    Log.d("Yuri", "키보드 접근")
                    Log.d("Yuri", "pressed key : $actionId")
                    if ((actionId == EditorInfo.IME_ACTION_DONE) || (event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                        member.modifyClicked = false
                        binding.etTodo.isEnabled = false
                        binding.btnThreeDot.visibility = View.VISIBLE
                        binding.ivPhoto.visibility = View.VISIBLE
                        binding.btnCheck.isEnabled = true
                        binding.etTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00000000"))

                        if (binding.etTodo.text.toString().isEmpty()) {
                            //투두에 값이 없는 경우
                            if (member.task.isEmpty()) {
                                //신규 투두 -> nothing to change
                            } else {
                                //있는 투두 -> 삭제 api
                                //todoActivity?.deleteDailyTodo(token, member.private_todo_id)
                            }
                            //todoActivity?.deleteTodo(member)
                        } else {
                            //투두에 값이 있는 경우 (신규 -> 추가 or 있는 -> 수정)
                            if (member.task.isEmpty()) {
                                //to-do (api - 성공) : 신규 투두 -> 새로 추가 api
                                val postDailyTodo = PostDailyNewTodo(date, binding.etTodo.text.toString())
                                postDailyNewTodo(token, postDailyTodo)
                            } else {
                                //to-do (api - 성공) : 있는 투두 -> 수정 api
                                val putDailyTodoTaskModify = PutDailyTodoTaskModify(
                                    member.private_todo_id.toString(),
                                    binding.etTodo.text.toString()
                                )
                                putModifyTask(token, putDailyTodoTaskModify)
                            }
                            member.task = binding.etTodo.text.toString() //task 갱신
                        }
                        true
                    } else {
                        false
                    }
                }
            }

            //TODO : 이미지 초기 설정
            if(mMember!!.imageUrl == "EMPTY"){
                //없으면 투명 하고, 터치 못하게 설정
                binding.ivPhoto.isEnabled = false
                binding.ivPhoto.setImageResource(R.drawable.iv_invisible_box)
            }else{
                binding.ivPhoto.isEnabled = true
                //val path = "https://images.unsplash.com/photo-1661956602868-6ae368943878?ixlib=rb-4.0.3&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwxfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=1200&q=60"
                val path = default_Address + mMember!!.imageUrl

                //todo : 이미지 를 불러 온다...
                if(member.imageUrl != "EMPTY"){
                    Log.d("Yuri", "link : ${member.imageUrl}")
                    Glide.with(binding.ivPhoto.context)
                        .load(path)
                        .listener(object:RequestListener<Drawable>{
                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("Yuri", "fail : ${e.toString()}")
                                return false
                            }
                        })
                        .error(R.drawable.ic_leaf)
                        .transform(CenterCrop(), RoundedCorners(5))
                        .into(binding.ivPhoto)
                }
            }
        }
}
