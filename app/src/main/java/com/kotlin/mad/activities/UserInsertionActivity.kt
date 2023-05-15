package com.kotlin.mad.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kotlin.mad.models.UserModel
import com.kotlin.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserInsertionActivity : AppCompatActivity() {



    private lateinit var etFName: EditText
    private lateinit var etFType: EditText
    private lateinit var etFMfd: EditText
    private lateinit var etFPrice: EditText
    private lateinit var etFSize: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_insertion)

        etFName = findViewById(R.id.etFName)
        etFType = findViewById(R.id.etFType)
        etFMfd = findViewById(R.id.etFMfd)
        etFPrice = findViewById(R.id.etFPrice)
        etFSize = findViewById(R.id.etFSize)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("UserDB")

        btnSaveData.setOnClickListener {
            saveUserData()
        }

    }

    private fun saveUserData() {

        //Geting Values
        val fName = etFName.text.toString()
        val fType = etFType.text.toString()
        val fMfd = etFMfd.text.toString()
        val fPrice = etFPrice.text.toString()
        val fSize = etFSize.text.toString()

        //validation
        if (fName.isEmpty() || fType.isEmpty() || fMfd.isEmpty() || fPrice.isEmpty() || fSize.isEmpty()) {

            if (fName.isEmpty()) {
                etFName.error = "Please enter Name"
            }
            if (fType.isEmpty()) {
                etFType.error = "Please Enter Address"
            }
            if (fMfd.isEmpty()) {
                etFMfd.error = "Please Enter Phone Number"
            }
            if (fPrice.isEmpty()) {
                etFPrice.error = "Please Enter Email"
            }
            if (fSize.isEmpty()) {
                etFSize.error = "Please Enter NIC"
            }
            Toast.makeText(this, "please check Some areas are not filled", Toast.LENGTH_LONG).show()
        } else {

            //genrate unique ID
            val fId = dbRef.push().key!!

            val payment = UserModel(fId, fName, fType, fMfd, fPrice, fSize)

            dbRef.child(fId).setValue(payment)
                .addOnCompleteListener {
                    Toast.makeText(this, "User insert successfully", Toast.LENGTH_SHORT).show()

                    //clear data after insert
                    etFName.text.clear()
                    etFType.text.clear()
                    etFMfd.text.clear()
                    etFPrice.text.clear()
                    etFSize.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}