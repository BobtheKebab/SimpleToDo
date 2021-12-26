package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove item from the list
                listOfTasks.removeAt(position)
                // Notify adapter that data has changed
                adapter.notifyDataSetChanged()
                // Save tasks to data file
                saveItems()
            }
        }

        // Detect when user clicks on "Add" button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Caren", "User clicked on button")
//        }

        // Load data from file into listOfTasks
        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field
        // Allow user to enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get reference to button and set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            // Get text from text field
            val userInputtedTask = inputTextField.text.toString()

            // Add string to list of tasks
            listOfTasks.add(userInputtedTask)

            // Notify the adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Clear the text field
            inputTextField.setText("")

            // Save tasks to data file
            saveItems()
        }
    }

    // Save user inputted data by writing and reading from a file

    // Method to get file we need
    fun getDataFile() : File {
        // Every line in file is a single task
        return File(filesDir, "data.txt")
    }

    // Method to load items from file (read every line)
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Method to save items (write into file)
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}