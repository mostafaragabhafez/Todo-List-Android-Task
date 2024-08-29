package com.example.dev

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dev.databinding.FragmentToDoListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ToDoListFragment : Fragment() {
    private lateinit var binding: FragmentToDoListBinding
    var myList: MutableList<TODO> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentToDoListBinding.inflate(inflater, container, false)
        myList = getFromSharedPreferences("TodoList")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_todoFragment)
        }

        binding.MyList.layoutManager = LinearLayoutManager(requireContext())
        lateinit var todoAdapter: MyAdapter

        todoAdapter = MyAdapter(onItemClick = {
            findNavController().navigate(
                R.id.action_toDoListFragment_to_todoFragment, bundleOf(
                    "id" to it.id, "date" to it.date, "task" to it.task
                )
            )
        }, onItemRemoved = {
            myList.remove(it)
            todoAdapter.setTODOList(myList)
            saveToSharedPreferences("TodoList", myList)
        })
        todoAdapter.setTODOList(myList)
        binding.MyList.adapter = todoAdapter
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
            gson.fromJson<MutableList<TODO>>(jsonString, type)
        } else {
            mutableListOf()
        }
    }
}