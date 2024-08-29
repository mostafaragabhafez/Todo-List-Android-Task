package com.example.dev

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dev.databinding.FragmentTodoBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TodoFragment : Fragment() {
    private lateinit var binding: FragmentTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments?.getInt("id") == null){
            binding.floatingRemoveButton.visibility = View.GONE
        }

        val rootLayout = binding.rootLayout
        val fabs = binding.floatingActionButtons

        rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootLayout.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootLayout.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15) {
                fabs.animate().translationY(-keypadHeight.toFloat()).setDuration(300).start()
            } else {
                fabs.animate().translationY(0f).setDuration(300).start()
            }
        }
        val fab = binding.floatingActionButton
        fab.setOnClickListener {
            val myList: MutableList<TODO> = getFromSharedPreferences("TodoList")
            val id = arguments?.getInt("id")

            if (id != null) {
                myList.remove(
                    TODO(
                        id,
                        arguments?.getString("date").toString(),
                        arguments?.getString("task").toString()
                    )
                )
                myList.add(
                    TODO(
                        id = id,
                        date = binding.editTextDate.text.toString(),
                        task = binding.Todo.text.toString()
                    )
                )
            } else {
                myList.add(
                    TODO(
                        date = binding.editTextDate.text.toString(),
                        task = binding.Todo.text.toString()
                    )
                )
            }
            saveToSharedPreferences("TodoList", myList)
            findNavController().navigateUp()
        }

        val id = arguments?.getInt("id")
        if (id != null) {
            binding.todo = TODO(
                id,
                arguments?.getString("date").toString(),
                arguments?.getString("task").toString()
            )
        }

        binding.floatingRemoveButton.setOnClickListener(){
            var myList: MutableList<TODO> = getFromSharedPreferences("TodoList")
            myList.remove(TODO(
                arguments?.getInt("id")!!,
                arguments?.getString("date").toString(),
                arguments?.getString("task").toString()
            )
            )
            saveToSharedPreferences("TodoList", myList)
            findNavController().navigateUp()
        }


    }

    private fun saveToSharedPreferences(key: String, list: MutableList<TODO>) {
        val gson = Gson()
        val jsonString = gson.toJson(list)

        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, jsonString)
        editor.apply()
    }

    private fun getFromSharedPreferences(key: String): MutableList<TODO> {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(key, null)

        return if (jsonString != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<TODO>>() {}.type
            gson.fromJson(jsonString, type)
        } else {
            mutableListOf()
        }
    }
}
