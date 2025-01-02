package com.example.piadinaparty.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.piadinaparty.MainActivity
import com.example.piadinaparty.R

class ActivityRiepilogoOrdine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riepilogoordine)

        // Recupera i dati dall'intent
        val indirizzo = intent.getStringExtra("indirizzo")
        val orario = intent.getStringExtra("orario")
        val pagamento = intent.getStringExtra("pagamento")
        val totalOrder = intent.getDoubleExtra("totalOrder", 0.0)

        // Trova i TextView nel layout
        val indirizzoTextView = findViewById<TextView>(R.id.textView2)
        val orarioTextView = findViewById<TextView>(R.id.textView3)
        val pagamentoTextView = findViewById<TextView>(R.id.textView4)
        val totalOrderTextView = findViewById<TextView>(R.id.textView5)

        // Imposta i dati nei TextView
        indirizzoTextView.text = "Indirizzo: $indirizzo"
        orarioTextView.text = "Ora della consegna: $orario"
        pagamentoTextView.text = "Metodo di pagamento: $pagamento"
        totalOrderTextView.text = "Totale: €%.2f".format(totalOrder)

        // Trova i pulsanti nel layout
        val annullaButton = findViewById<Button>(R.id.button2)
        val confermaButton = findViewById<Button>(R.id.button4)
        val indietroButton = findViewById<Button>(R.id.button3)

        // Gestisci il click del bottone "Annulla Ordine"
        annullaButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Gestisci il click del bottone "Conferma"
        confermaButton.setOnClickListener {
            val intent = Intent(this, ActivityConfermaOrdine::class.java)
            startActivity(intent)
        }

        // Gestisci il click del bottone "Indietro"
        indietroButton.setOnClickListener {
            val intent = Intent(this, ActivityInserimentoDatiOrdine::class.java).apply {
                putExtra("totalOrder", totalOrder)
            }
            startActivity(intent)
            finish()
        }
    }
}