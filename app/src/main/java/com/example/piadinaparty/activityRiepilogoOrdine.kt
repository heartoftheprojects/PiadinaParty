package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class activityRiepilogoOrdine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riepilogoordine)

        // Trova i TextView per visualizzare i dati
        val riepilogoTextView = findViewById<TextView>(R.id.textView2)
        val totaleTextView = findViewById<TextView>(R.id.textView3)
        val puntiTextView = findViewById<TextView>(R.id.textView4)

        // Trova i pulsanti "Annulla ordine", "Indietro" e "Conferma"
        val annullaButton = findViewById<Button>(R.id.button2)
        val indietroButton = findViewById<Button>(R.id.button3)
        val confermaButton = findViewById<Button>(R.id.button4)

        // Recupera i dati dall'Intent (passati dalla schermata precedente)
        val indirizzo = intent.getStringExtra("indirizzo")
        val orario = intent.getStringExtra("orario")
        val pagamento = intent.getStringExtra("pagamento")

        // Visualizza i dati nella TextView
        riepilogoTextView.text = "Indirizzo: $indirizzo\nOrario: $orario\nPagamento: $pagamento"

        // Visualizza il totale e i punti
        //totaleTextView.text = "Totale: €$totale"
        //puntiTextView.text = "Punti totalizzati: $punti"

        // Gestisci il click del bottone "Annulla ordine"
        annullaButton.setOnClickListener {
            // Torna alla ActivitySchermata2
            val intent = Intent(this, activityHome::class.java)
            startActivity(intent)
            finish() // Chiude la schermata corrente (Activity4)
        }

        // Gestisci il click del bottone "Indietro"
        indietroButton.setOnClickListener {
            // Torna a ActivitySchermata3
            val intent = Intent(this, activityInserimentoDatiOrdine::class.java)
            startActivity(intent)
            finish() // Chiude la schermata corrente (Activity4)
        }

        // Gestisci il click del bottone "Conferma"
        confermaButton.setOnClickListener {
            // Naviga a ActivitySchermata4_1
            val intent = Intent(this, activityConfermaOrdine::class.java)
            startActivity(intent)
            finish() // Chiude la schermata corrente (Activity4)
        }
    }
}
