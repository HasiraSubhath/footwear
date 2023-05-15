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



    private lateinit var etUName: EditText
    private lateinit var etUAddress: EditText
    private lateinit var etUNumber: EditText
    private lateinit var etUEmail: EditText
    private lateinit var etUNic: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_insertion)

        etUName = findViewById(R.id.etUName)
        etUAddress = findViewById(R.id.etUAddress)
        etUNumber = findViewById(R.id.etUNumber)
        etUEmail = findViewById(R.id.etUEmail)
        etUNic = findViewById(R.id.etUNic)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("UserDB")

        btnSaveData.setOnClickListener {
            saveUserData()
        }

    }

    private fun saveUserData() {

        //Geting Values
        val uName = etUName.text.toString()
        val uAddress = etUAddress.text.toString()
        val uNumber = etUNumber.text.toString()
        val uEmail = etUEmail.text.toString()
        val uNic = etUNic.text.toString()

        //validation
        if (uName.isEmpty() || uAddress.isEmpty() || uNumber.isEmpty() || uEmail.isEmpty() || uNic.isEmpty()) {

            if (uName.isEmpty()) {
                etUName.error = "Please enter Name"
            }
            if (uAddress.isEmpty()) {
                etUAddress.error = "Please Enter Address"
            }
            if (uNumber.isEmpty()) {
                etUNumber.error = "Please Enter Phone Number"
            }
            if (uEmail.isEmpty()) {
                etUEmail.error = "Please Enter Email"
            }
            if (uNic.isEmpty()) {
                etUNic.error = "Please Enter NIC"
            }
            Toast.makeText(this, "please check Some areas are not filled", Toast.LENGTH_LONG).show()
        } else {

            //genrate unique ID
            val uId = dbRef.push().key!!

            val payment = UserModel(uId, uName, uAddress, uNumber, uEmail, uNic)

            dbRef.child(uId).setValue(payment)
                .addOnCompleteListener {
                    Toast.makeText(this, "User insert successfully", Toast.LENGTH_SHORT).show()

                    //clear data after insert
                    etUName.text.clear()
                    etUAddress.text.clear()
                    etUNumber.text.clear()
                    etUEmail.text.clear()
                    etUNic.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}