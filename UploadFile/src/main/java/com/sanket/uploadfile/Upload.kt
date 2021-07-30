package com.sanket.uploadfile

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class Upload() {

    interface OnTaskCompleted {
        fun onSuccess(message : String)
        fun onFail(message: String)
        fun onProgress(prog : Int)
    }

    var storageReference : StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://FIREBASE_STORAGE_BUCKET")


    fun uploadFile(filename: String, filePath: Uri?, taskCompleted : OnTaskCompleted) {


            try{

                if(filePath != null){

                    val riversRef: StorageReference = storageReference.child("files/${filename}")

                    riversRef.putFile(filePath)
                        .addOnSuccessListener { _ -> // Get a URL to the uploaded content

                            taskCompleted.onSuccess("Upload Successful")


                        }


                        .addOnFailureListener {
                            taskCompleted.onFail("Upload Failed")

                        }
                        .addOnProgressListener {p ->
                            var progress = (100.0 * p.bytesTransferred) / p.totalByteCount
                            taskCompleted.onProgress(progress.toInt())

                        }


                }
                else{
                    taskCompleted.onFail("File Not Found")
                }


            }
            catch (e: Exception){
                taskCompleted.onFail("Error ${e.localizedMessage}")
            }

    }
}