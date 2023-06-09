package com.example.dayleader

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dayleader.databinding.ActivityTodoBinding
import com.example.dayleader.recycler.TodoAdapter
import com.example.dayleader.recycler.TodoInfo
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog


class TodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var publicPosition = 0

    //recyclerView 가 불러올 목록
    private var todoAdapter: TodoAdapter? = null
    private var todoData: MutableList<TodoInfo> = mutableListOf()

    init {
        instance = this
    }

    companion object {
        private var instance: TodoActivity? = null
        fun getInstance(): TodoActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //titleBar
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        //activity 에 recycler 연결
        todoAdapter = TodoAdapter()
        todoAdapter!!.listData = todoData
        binding.recyclerViewDailyTodo.adapter = todoAdapter
        binding.recyclerViewDailyTodo.layoutManager = LinearLayoutManager(this)

        init()

        //투두 1. 날짜 변환해서 View에 보여주기 (0)
        val intent = intent
        val year = intent.getIntExtra("year", 0).toString()
        val month = intent.getIntExtra("month", 0)
        val day = intent.getIntExtra("day", 0).toString()
        Log.d("yuri", "$year-$month-$day")
        binding.year.text = year
        binding.month.text = convertMonthToString(month)
        binding.day.text = day
        val date = year + month.toString() + day

        //to-do 4 : to-do 추가
        binding.btnTodo.setOnClickListener {
            todoData.add(TodoInfo(false, date, "EMPTY", "", true))
            todoAdapter?.notifyItemInserted(todoData.size - 1)
        }

        //링크 이동
        binding.ivCommercial.setOnClickListener {
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse("https://www.hufs.ac.kr/")
            startActivity(openUrl)
        }
    }

    //bottom sheet 함수
    fun showTodoBottomSheetDialog(member: TodoInfo, position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_todo)


        //바텀 시트 텍스트 연결
        val todoText = bottomSheetDialog.findViewById<TextView>(R.id.tv_todo_text)
        todoText!!.text = todoData[position].task

        //투두 수정 버튼 구현
        val modify = bottomSheetDialog.findViewById<Button>(R.id.btn_modify_todo)
        modify?.setOnClickListener {
            bottomSheetDialog.dismiss()

            todoData[position].modifyClicked = true
            todoAdapter?.notifyItemChanged(position)

        }

        //투두 삭제 버튼 구현
        val delete = bottomSheetDialog.findViewById<Button>(R.id.btn_delete_todo)
        delete?.setOnClickListener {
            bottomSheetDialog.dismiss()
            deleteTodo(member)
        }

        //이미지 추가, 삭제의 버튼 text 설정
        val image = bottomSheetDialog.findViewById<Button>(R.id.btn_delete_photo)
        if (todoData[position].imageUrl == "EMPTY") {
            image?.setText(R.string.upload_photo)
        } else {
            image?.setText(R.string.delete_photo)
        }

        //이미지 추가, 삭제
        image?.setOnClickListener {
            bottomSheetDialog.dismiss()
            publicPosition = position
            if (todoData[position].imageUrl == "EMPTY") {
                //사진 추가
                checkingPermission()
                todoAdapter?.notifyItemChanged(position)
            } else {
                //사진 삭제
                todoData[position].imageUrl = "EMPTY"
                todoAdapter?.notifyItemChanged(position)
            }
        }
        bottomSheetDialog.show()
    }

    //수정 클릭했을 때 광고 안보이게
    fun commercialVisibility(visible:Boolean){
        if(visible){
            binding.ivCommercial.visibility = View.VISIBLE
        }
        else{
            binding.ivCommercial.visibility = View.INVISIBLE
        }
    }


    fun deleteTodo(member: TodoInfo) {
        todoData.remove(member)
        todoAdapter?.notifyDataSetChanged()
    }

    //이미지 크게 보여 주는 기능
    fun showImageBottomSheetDialog(imageUrl: String, task: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_show_image)
        bottomSheetDialog.behavior.state = STATE_EXPANDED

        val image = bottomSheetDialog.findViewById<ImageView>(R.id.iv_image)
        val tvTask = bottomSheetDialog.findViewById<TextView>(R.id.tv_task)
        val exit = bottomSheetDialog.findViewById<ImageButton>(R.id.btn_exit)
        val popTop = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.pop_top)
        val popBottom = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.pop_bottom)

        tvTask?.text = task

        Glide.with(image!!)
            .load(imageUrl)
            .error(R.drawable.iv_daily_todo_checked)
            .into(image)

        image.setOnClickListener {
            if ((popTop?.visibility == View.VISIBLE) and (popBottom?.visibility == View.VISIBLE)) {
                exit?.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }
                popTop?.visibility = View.INVISIBLE
                popBottom?.visibility = View.INVISIBLE
            } else if ((popTop?.visibility == View.INVISIBLE) and (popBottom?.visibility == View.INVISIBLE)) {
                popTop?.visibility = View.VISIBLE
                popBottom?.visibility = View.VISIBLE
            }
        }
        bottomSheetDialog.show()
    }

    //앨범 접근 권한
    fun checkingPermission() {
        when {
            //1. 처음 부터 허용 권한 있었음
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "move to Album", Toast.LENGTH_SHORT).show()
                navigatePhoto()
            }
            //2.
            //권한을 명시적 으로 거부한 경우 true
            //처음 보거나, 다시 묻지 않음을 선택한 경우 false
            //교육용 팝업 확인 후 권한 팝업을 띄우는 기능
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionContextPopup()
            }
            //3. 처음 으로 앱을 실행 하고 앨범 접근할 때 실행 되는 코드
            else -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }
        }
    }

    fun init() {
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = checkNotNull(result.data)
                    val imageUri = intent.data

                    Log.d("yuri", "imageUri : $imageUri")
                    uploadPhoto(publicPosition, imageUri)
                }
            }
    }

    //사진 업로드
    fun uploadPhoto(publicPosition: Int, imageUri: Uri?) {
        Log.d("yuri", "image설정 성공$imageUri")
        todoData[publicPosition].imageUrl = imageUri.toString()
        todoAdapter?.notifyItemChanged(publicPosition)
    }

    //갤러리 에서 사진 가져 오기
    fun navigatePhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/jpeg"
        launcher.launch(intent)
    }


    //여기 입니다....(2)
    fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("Need Permission")
            .setMessage("DayLeader requires permission to select photos.")
            .setPositiveButton("Agree") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }
            .setNegativeButton("Deny") { _, _ -> }
            .create()
            .show()
    }

    //여기 입니다....(3)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //권한이 허용 된것
                    //허용 클릭시
                    Toast.makeText(this, "move to Album", Toast.LENGTH_SHORT).show()
                    navigatePhoto()
                } else {
                    //거부 클릭시
                    Toast.makeText(this, "Album access denied", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                //do nothing
            }
        }
    }

    //투두 2 : appBar goBack
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //뒤로 가기 버튼 눌렀을 때
                //return 은 boolean 으로
                finish()
                return true
            }

            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}