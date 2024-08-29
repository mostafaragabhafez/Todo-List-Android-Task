package com.example.dev

data class TODO(
    val id:Int = generateId(),
    val date:String,
    val task:String
){
    companion object {
        private var currentId: Int = 0

        private fun generateId(): Int {
            return ++currentId
        }
    }
}