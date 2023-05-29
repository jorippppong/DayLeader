package com.example.dayleader.recycler

data class TodoInfo(
    var complete:Boolean,
    var date:String,
    var imageUrl:String,
    var task:String,
    var modifyClicked:Boolean
)
