package com.example.piadinaparty
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu

class activityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_principale)

        fun showPopup(v: View) {
            val popupMenu: PopupMenu = PopupMenu(this, v)
            popupMenu.menuInflater.inflate(R.menu.opzioni_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.schermata5 ->
                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                            .show()

                    R.id.schermata6 ->
                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                            .show()

                    R.id.schermata7 ->
                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
                            .show()
                }
                true
            }
            popupMenu.show()
        }

        val menuPiadineListView = findViewById<ListView>(R.id.menuPiadineListView)
        val menuBevandeListView = findViewById<ListView>(R.id.menuBevandeListView)

        val piadine = arrayOf("niente", "guerino", "ciccio")
        val bevande = arrayOf("acqua", "vino", "birra")

        val adapterPiadine = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            piadine
        )

        val adapterBevande = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            bevande
        )

        menuPiadineListView.adapter = adapterPiadine
        menuBevandeListView.adapter = adapterBevande

        val btnConferma = findViewById<Button>(R.id.button)

        btnConferma.setOnClickListener {
            val intent = Intent(this, activityInserimentoDatiOrdine::class.java)
            startActivity(intent)
        }
    }
}