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

    private lateinit var tvFId: TextView
    private lateinit var tvFName: TextView
    private lateinit var tvFType: TextView
    private lateinit var tvFMfd: TextView
    private lateinit var tvFPrice: TextView
    private lateinit var tvFSize: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("fId").toString(),
                intent.getStringExtra("fName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("fId").toString()
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
        tvFId = findViewById(R.id.tvFId)
        tvFName = findViewById(R.id.tvFName)
        tvFType = findViewById(R.id.tvFType)
        tvFMfd = findViewById(R.id.tvFMfd)
        tvFPrice = findViewById(R.id.tvFPrice)
        tvFSize = findViewById(R.id.tvFSize)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvFId.text = intent.getStringExtra("fId")
        tvFName.text = intent.getStringExtra("fName")
        tvFType.text = intent.getStringExtra("fType")
        tvFMfd.text = intent.getStringExtra("fMfd")
        tvFPrice.text = intent.getStringExtra("fPrice")
        tvFSize.text = intent.getStringExtra("fSize")

    }

    private fun openUpdateDialog(
        fId: String,
        fName: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_user_dialog, null)

        mDialog.setView(mDialogView)

        val etFName = mDialogView.findViewById<EditText>(R.id.etFName)
        val etFType = mDialogView.findViewById<EditText>(R.id.etFType)
        val etFMfd = mDialogView.findViewById<EditText>(R.id.etFMfd)
        val etFPrice = mDialogView.findViewById<EditText>(R.id.etFPrice)
        val etFSize = mDialogView.findViewById<EditText>(R.id.etFSize)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etFName.setText(intent.getStringExtra("fName").toString())
        etFType.setText(intent.getStringExtra("fType").toString())
        etFMfd.setText(intent.getStringExtra("fMfd").toString())
        etFPrice.setText(intent.getStringExtra("fPrice").toString())
        etFSize.setText(intent.getStringExtra("fSize").toString())

        mDialog.setTitle("Updating $fName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateUserData(
                fId,
                etFName.text.toString(),
                etFType.text.toString(),
                etFMfd.text.toString(),
                etFPrice.text.toString(),
                etFSize.text.toString()

            )

            Toast.makeText(applicationContext, " Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our text views
            tvFName.text = etFName.text.toString()
            tvFType.text = etFType.text.toString()
            tvFMfd.text = etFMfd.text.toString()
            tvFPrice.text = etFPrice.text.toString()
            tvFSize.text = etFSize.text.toString()

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