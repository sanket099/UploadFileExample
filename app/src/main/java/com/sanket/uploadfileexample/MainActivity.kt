package com.sanket.uploadfileexample

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sanket.uploadfile.Upload
import com.sanket.uploadfile.Upload.OnTaskCompleted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.io.File


class MainActivity : AppCompatActivity() {
    private val PICK_FILE_REQUEST = 1
    private var filePath: Uri? = null

    lateinit var fileTV : TextView
    lateinit var addUpload : FloatingActionButton
    lateinit var  cancel : FloatingActionButton
    lateinit var tvDesc : TextView
    lateinit var progressBar : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


         fileTV = findViewById<TextView>(R.id.tv_file)
         addUpload = findViewById<FloatingActionButton>(R.id.floatingActionButton)
         cancel = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        progressBar = findViewById(R.id.progress)

        tvDesc = findViewById(R.id.tv_desc)



        addUpload.setOnClickListener {
            if(fileTV.isVisible){
                //ready to upload
                upload()
            }
            else{
                showFileChooser()
                //choosing file

            }
        }


        cancel.setOnClickListener {
            if(filePath != null){
                addFileUI()
            }

        }

        fileTV.setOnClickListener {
            upload()
        }





    }
    private  fun upload(){

        val upload = Upload()


        upload.uploadFile(fileTV.text.toString(), filePath, object : OnTaskCompleted {
            override fun onSuccess(message: String) {

                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()

                addFileUI()
                addAnimation()
                progressBar.visibility = View.GONE

            }

            override fun onFail(message: String) {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }

            override fun onProgress(prog: Int) {
                progressBar.visibility = View.VISIBLE
                progressBar.text = "$prog %"
            }

        })







    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            data.data

            if(filePath != null){
                fileReadyToUploadUI()
            }


        }
    }

    private fun fileReadyToUploadUI(){
        val file = File(filePath.toString())

        fileTV.text = file.nameWithoutExtension.toString()
        fileTV.visibility = View.VISIBLE
        cancel.visibility = View.VISIBLE
        addUpload.setImageResource(R.drawable.ic_baseline_file_upload_24)

        tvDesc.text = "File ready to Upload"
    }
    private fun addFileUI(){
        fileTV.text = ""
        fileTV.visibility = View.GONE
        addUpload.setImageResource(R.drawable.ic_baseline_add_24)

        tvDesc.text = "Go ahead and add a file"
        cancel.visibility = View.GONE
    }
    private fun addAnimation(){
        val viewKonfetti = findViewById<KonfettiView>(R.id.view_konf)

        viewKonfetti.build()
            .addColors(Color.parseColor("#FF8AB7E8"), Color.parseColor("#142850"), Color.parseColor("#4C8FD6"))
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 10f)
            .setFadeOutEnabled(true)
            .setTimeToLive(1000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}