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
import com.bumptech.glide.Glide
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
        val binding =
            ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    //생성된 뷰에 무슨 데이터 를 넣을 것인가
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val member = listData[position]
        holder.setData(member, position)
    }

    //뷰 홀더
    //각 목록에 필요한 기능 들을 구현 하는 공간
    class Holder(private val binding: ItemTodoListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val todoActivity = TodoActivity.getInstance()
        var mMember: TodoInfo? = null
        private var mPosition: Int? = null

        init {
            binding.btnCheck.setOnClickListener {
                //Check 표시 변경 가능 할 수 있는 기능 : 초기 설정
                if (!(mMember!!.complete)) {
                    binding.btnCheck.setImageResource(R.drawable.iv_daily_todo_checked)
                    mMember!!.complete = true
                } else {
                    binding.btnCheck.setImageResource(R.drawable.btn_todo_disabled)
                    mMember!!.complete = false
                }
            }

            //이미지 클릭하면 보여주는 기능
            binding.ivPhoto.setOnClickListener {
                todoActivity?.showImageBottomSheetDialog(mMember!!.imageUrl, mMember!!.task)
            }

            binding.btnThreeDot.setOnClickListener {
                todoActivity?.showTodoBottomSheetDialog(mMember!!, mPosition!!)
            }

        }

        fun setData(member: TodoInfo, position: Int) {
            this.mMember = member
            this.mPosition = position

            //todo
            //todoActivity?.commercialVisibility(member)

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
                todoActivity?.commercialVisibility(false)   //안보이게
                binding.btnThreeDot.visibility = View.INVISIBLE
                binding.ivPhoto.visibility = View.INVISIBLE
                binding.btnCheck.isEnabled = false
                binding.etTodo.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#6B9AC4"))
                binding.etTodo.isEnabled = true
                binding.etTodo.setSelection(member.task.length)
                binding.etTodo.requestFocus()

                binding.etTodo.setOnEditorActionListener { _, actionId, event ->
                    Log.d("Yuri", "키보드 접근")
                    Log.d("Yuri", "pressed key : $actionId")
                    if ((actionId == EditorInfo.IME_ACTION_DONE) || (event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                        member.modifyClicked = false
                        binding.etTodo.isEnabled = false
                        binding.btnThreeDot.visibility = View.VISIBLE
                        binding.ivPhoto.visibility = View.VISIBLE
                        binding.btnCheck.isEnabled = true
                        binding.etTodo.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#00000000"))

                        if (binding.etTodo.text.toString().isEmpty()) {
                            //투두에 값이 없는 경우
                            todoActivity?.deleteTodo(member)
                        } else {
                            //투두에 값이 있는 경우 (신규 -> 추가 or 있는 -> 수정)
                            member.task = binding.etTodo.text.toString() //task 갱신
                        }
                        //보이게
                        todoActivity?.commercialVisibility(true) //보이게
                        true
                    } else {
                        false
                    }
                }
            }

            //이미지 보이게, 안 보이게 설정
            if(mMember!!.imageUrl == "EMPTY"){
                //없으면 투명 하고, 터치 못하게 설정
                binding.ivPhoto.isEnabled = false
                binding.ivPhoto.setImageResource(R.drawable.iv_invisible_box)
            }else{
                binding.ivPhoto.isEnabled = true
                mMember!!.imageUrl.let {
                    Glide.with(binding.ivPhoto.context)
                        .load(mMember!!.imageUrl)
                        .error(R.drawable.ic_add_circle)
                        .into(binding.ivPhoto)
                }
            }
        }
    }
}
