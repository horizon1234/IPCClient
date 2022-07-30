package com.zyh.client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import com.zyh.ipc.Book
import com.zyh.ipc.IBookManager
import com.zyh.ipc.IOnBookListener

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.bindService).setOnClickListener {
            bindService(
                Intent("com.zyh.ipc.BookManagerService").setPackage("com.zyh.ipc"),
                conn,
                BIND_AUTO_CREATE
            )
        }

        findViewById<Button>(R.id.getBook).setOnClickListener {
            val list = bookManager?.bookList
            Log.i("ipc", "onCreate: ${list?.javaClass?.name}")
            for (book in list!!) {
                Log.i("ipc", "onServiceConnected: ${book.BookId} ${book.BookName}")
            }
        }

        findViewById<Button>(R.id.addBook).setOnClickListener {
            bookManager?.addBook(Book(44,"web"))
        }

        findViewById<Button>(R.id.removeListener).setOnClickListener {
            bookManager?.unregisterListener(listener)
        }
    }

    private val conn = object : ServiceConnection{

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("ipc", "onServiceConnected: ")
            bookManager = IBookManager.Stub.asInterface(service)
            bookManager?.registerListener(listener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("ipc", "onServiceDisconnected: ")
        }
    }


    private val  listener = object : IOnBookListener.Stub(){
        override fun onAllBook(books: MutableList<Book>?) {
            for (book in books!!) {
                Log.i("ipc", "onAllBook: ${book.BookId} ${book.BookName}")
            }
        }
    }


    private var bookManager: IBookManager? = null
}