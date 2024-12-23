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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.ProductAdapter

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

        //val rvPiadine = findViewById<RecyclerView>(R.id.recyclerPiadine)
        //rvPiadine.adapter = piadineAdapter

        // RecyclerView per le bibite
        //val rvBibite = findViewById<RecyclerView>(R.id.recyclerBibite)
        //rvBibite.layoutManager = LinearLayoutManager(this)
        //val bibiteAdapter = ProductAdapter(listOf("Coca Cola", "Fanta", "Acqua Naturale", "Acqua Frizzante"))
        //rvBibite.adapter = bibiteAdapter

        //val btnConferma = findViewById<Button>(R.id.button)
        //btnConferma.setOnClickListener {
            // Ottieni gli elementi selezionati da entrambe le liste
            //val piadineSelezionate = piadineAdapter.getSelectedItems()
            //val bibiteSelezionate = bibiteAdapter.getSelectedItems()

            // Mostra i prodotti selezionati (ad esempio con un Toast)
            //val ordine = "Piadine: ${piadineSelezionate.joinToString(", ")}\nBibite: ${bibiteSelezionate.joinToString(", ")}"
            //Toast.makeText(this, ordine, Toast.LENGTH_LONG).show()

            // Crea un Intent per passare ai dati dell'ordine
            //val intent = Intent(this, activityInserimentoDatiOrdine::class.java)

            // Passa i dati (le liste di piadine e bibite selezionate) all'Intent
            //intent.putStringArrayListExtra("piadine", ArrayList(piadineSelezionate))
            //intent.putStringArrayListExtra("bibite", ArrayList(bibiteSelezionate))

            // Avvia la nuova Activity
            //startActivity(intent)
        //}


        //val btnConferma = findViewById<Button>(R.id.button)

        //btnConferma.setOnClickListener {
          //  val intent = Intent(this, activityInserimentoDatiOrdine::class.java)
            //startActivity(intent)
        //}
    }
}