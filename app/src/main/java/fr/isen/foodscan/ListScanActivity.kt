package fr.isen.foodscan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import fr.isen.foodscan.model.ProductResult
import kotlinx.android.synthetic.main.activity_list_scann.*


class ListScanActivity : AppCompatActivity() {
    private val requestURLStart  = "https://fr.openfoodfacts.org/api/v0/produit/"
    private val requestURLStop  = ".json"


    var tailleDucon = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_scann)


        val maList = findViewById(R.id.MyList1) as ListView
        val myArray = mutableListOf<String>()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("scannedid")


        val adapter1 = ArrayAdapter<String>(this , android.R.layout.simple_list_item_1,myArray)
        maList.adapter = adapter1

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var lastScannedCode = ""
                tailleDucon = dataSnapshot.children.count();
                val value = dataSnapshot.children.forEach {
                    lastScannedCode = it.getValue(String::class.java) ?: ""
                    //Log.d("ListScanActivity", "Value is: " + lastScannedCode)
                    getProductInformation(lastScannedCode,myArray,maList,adapter1)
                }
                //printList(myArray,maList)

                Log.d("ListScanActivity", "Value is END : " + myArray.size)
            }



            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ListScanActivity", "Failed to read value.", error.toException())
            }
        })

    }



    fun printList(myArray: MutableList<String>, maList: ListView) {
        Log.d("ListScanActivity", "Value is END : " + myArray.size + " " + tailleDucon)
        if(tailleDucon==myArray.size){
            val adapter1 = ArrayAdapter<String>(this , android.R.layout.simple_list_item_1,myArray)
            maList.adapter = adapter1
        }
    }


    fun getProductInformation(
        value: String,
        myArray: MutableList<String>,
        maList: ListView,
        adapter1: ArrayAdapter<String>
    ) {
        val myRequest = requestURLStart+value+requestURLStop;
        Log.d("ListScanActivity", "Value is: " + myRequest)
        val queue = Volley.newRequestQueue(this)
        val stringReq = StringRequest(Request.Method.GET, myRequest, Response.Listener {
            Log.d("request", it)
            val gson = Gson()
            val result = gson.fromJson<ProductResult>(it, ProductResult::class.java)
            myArray.add(result.product.generic_name_fr)
            Log.d("ListScanActivity", "Value is: " + myArray.size)
            //adapter1.notifyDataSetChanged()
            text12.setText(result.product.generic_name_fr+"\n"+text12.getText())
            //printList(myArray,maList)
                //add = result.product.generic_name_fr
        }, Response.ErrorListener {
        })
        queue.add(stringReq)
    }
}

