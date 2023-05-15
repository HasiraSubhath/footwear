package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kotlin.mad.R
import com.kotlin.mad.models.UserModel
import com.google.firebase.database.FirebaseDatabase

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var tvUId: TextView
    private lateinit var tvUName: TextView
    private lateinit var tvUAddress: TextView
    private lateinit var tvUNumber: TextView
    private lateinit var tvUEmail: TextView
    private lateinit var tvUNic: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("uId").toString(),
                intent.getStringExtra("uName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("uId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("UserDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, " data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, UserFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvUId = findViewById(R.id.tvUId)
        tvUName = findViewById(R.id.tvUName)
        tvUAddress = findViewById(R.id.tvUAddress)
        tvUNumber = findViewById(R.id.tvUNumber)
        tvUEmail = findViewById(R.id.tvUEmail)
        tvUNic = findViewById(R.id.tvUNic)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvUId.text = intent.getStringExtra("uId")
        tvUName.text = intent.getStringExtra("uName")
        tvUAddress.text = intent.getStringExtra("uAddress")
        tvUNumber.text = intent.getStringExtra("uNumber")
        tvUEmail.text = intent.getStringExtra("uEmail")
        tvUNic.text = intent.getStringExtra("uNic")

    }

    private fun openUpdateDialog(
        uId: String,
        uName: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_user_dialog, null)

        mDialog.setView(mDialogView)

        val etUName = mDialogView.findViewById<EditText>(R.id.etUName)
        val etUAddress = mDialogView.findViewById<EditText>(R.id.etUAddress)
        val etUNumber = mDialogView.findViewById<EditText>(R.id.etUNumber)
        val etUEmail = mDialogView.findViewById<EditText>(R.id.etUEmail)
        val etUNic = mDialogView.findViewById<EditText>(R.id.etUNic)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etUName.setText(intent.getStringExtra("uName").toString())
        etUAddress.setText(intent.getStringExtra("uAddress").toString())
        etUNumber.setText(intent.getStringExtra("uNumber").toString())
        etUEmail.setText(intent.getStringExtra("uEmail").toString())
        etUNic.setText(intent.getStringExtra("uNic").toString())

        mDialog.setTitle("Updating $uName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateUserData(
                uId,
                etUName.text.toString(),
                etUAddress.text.toString(),
                etUNumber.text.toString(),
                etUEmail.text.toString(),
                etUNic.text.toString()

            )

            Toast.makeText(applicationContext, " Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our text views
            tvUName.text = etUName.text.toString()
            tvUAddress.text = etUAddress.text.toString()
            tvUNumber.text = etUNumber.text.toString()
            tvUEmail.text = etUEmail.text.toString()
            tvUNic.text = etUNic.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateUserData(
        id: String,
        name: String,
        address: String,
        number: String,
        email: String,
        nic: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("UserDB").child(id)
        val userInfo = UserModel(id, name, address, number, email, nic )
        dbRef.setValue(userInfo)
    }
}