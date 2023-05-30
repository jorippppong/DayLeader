package com.example.dayleader

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dayleader.databinding.ActivityTodoBinding
import com.example.dayleader.recycler.TodoAdapter
import com.example.dayleader.recycler.TodoInfo
import com.google.android.material.bottomsheet.BottomSheetDialog


class TodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var filePath = ""
    private var publicPosition = 0
    private var whichTodo = 0

    //recyclerView 가 불러올 목록
    private var todoAdapter: TodoAdapter? = null
    private var todoData:MutableList<TodoInfo> = mutableListOf()

    init{
        instance = this
    }

    companion object{
        private var instance:TodoActivity? = null
        fun getInstance():TodoActivity?{
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

        //init()

        //투두 1. 날짜 변환해서 View에 보여주기 (0)
        val intent = intent
        val year = intent.getIntExtra("year", 0).toString()
        val month = intent.getIntExtra("month", 0)
        val day = intent.getIntExtra("day", 0).toString()
        Log.d("yuri", "$year-$month-$day")
        binding.year.text = year
        binding.month.text = convertMonthToString(month)
        binding.day.text = day
        val date = year+month.toString()+day

        //to-do 4 : to-do 추가
        binding.btnTodo.setOnClickListener {
            todoData.add(TodoInfo(false, date, "EMPTY", "", true))
            todoAdapter?.notifyItemInserted(todoData.size -1)
        }
    }

    //bottom sheet 함수
    fun showTodoBottomSheetDialog(member:TodoInfo, position:Int){
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
        if(todoData[position].imageUrl == "EMPTY"){
            image?.setText(R.string.upload_photo)
        } else{
            image?.setText(R.string.delete_photo)
        }

        //이미지 추가 삭제
        //
        image?.setOnClickListener {
            bottomSheetDialog.dismiss()
            publicPosition = position
            if(todoData[position].imageUrl == "EMPTY"){
                //사진 추가
                whichTodo = 2
                checkPermission()
                todoAdapter?.notifyItemChanged(position)
            }else{
                //사진 삭제
                todoData[position].imageUrl = "EMPTY"
                todoAdapter?.notifyItemChanged(position)
            }
        }
        bottomSheetDialog.show()
    }

    fun deleteTodo(member:TodoInfo){
        todoData.remove(member)
        todoAdapter?.notifyDataSetChanged()
    }

    //TODO 여기 구현 필요 : 이미지 크게 보여주는 기능
    fun showImageBottomSheetDialog(){

    }

    //UserPermission 1
    private fun checkPermission(){
        when {
            //1. 처음 부터 허용 권한 있었음
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                navigatePhoto()
            }
            //2. 교육용 팝업 확인 후 권한 팝업을 띄우는 기능
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


    //User Permission 2
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("Need Permission")
            .setMessage("Greendar requires permission to select photos.")
            .setPositiveButton("Agree", {_, _ ->
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            })
            .setNegativeButton("Deny", {_, _->})
            .create()
            .show()
    }

    //UserPermission 3
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //권한이 허용 된것
                    //허용 클릭시
                    Toast.makeText(this, "move to Album", Toast.LENGTH_SHORT).show()
                    navigatePhoto()
                }
                else{
                    //거부 클릭시
                    Toast.makeText(this, "Album access denied", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                //do nothing
            }
        }
    }

    //갤러리 에서 사진 가져 오기
    private fun navigatePhoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/jpeg"
        launcher.launch(intent)
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