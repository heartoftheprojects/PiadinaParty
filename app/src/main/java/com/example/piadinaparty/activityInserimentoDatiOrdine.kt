package com.example.piadinaparty
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class activityInserimentoDatiOrdine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserimentodatiordine)

        // Trova i TextView e gli EditText nel layout
        val informazioniTextView = findViewById<TextView>(R.id.Informazioni)
        val indirizzoTextView = findViewById<TextView>(R.id.Indirizzo)
        val indirizzoEditText = findViewById<EditText>(R.id.indirizzo)
        val orarioTextView = findViewById<TextView>(R.id.Orario)
        val orarioEditText = findViewById<EditText>(R.id.orario)
        val pagamentoTextView = findViewById<TextView>(R.id.Pagamento)
        val pagamentoEditText = findViewById<EditText>(R.id.pagamento)

        // Trova i pulsanti "Conferma" e "Indietro"
        val confermaButton = findViewById<Button>(R.id.Conferma)
        val indietroButton = findViewById<Button>(R.id.Indietro)

        // Gestisci il click del bottone "Conferma"
        confermaButton.setOnClickListener {
            // Ottieni i valori dai campi EditText
            val indirizzo = indirizzoEditText.text.toString()
            val orario = orarioEditText.text.toString()
            val pagamento = pagamentoEditText.text.toString()

            // Puoi fare qualcosa con questi valori, ad esempio mostrarli in un log o inviarli a una nuova activity

            // Naviga a ActivitySchermata4
            val intent = Intent(this, activityRiepilogoOrdine::class.java)

            // Puoi passare i dati a ActivitySchermata4 tramite Intent (opzionale)
            intent.putExtra("indirizzo", indirizzo)
            intent.putExtra("orario", orario)
            intent.putExtra("pagamento", pagamento)

            // Avvia ActivitySchermata4
            startActivity(intent)
        }

        // Gestisci il click del bottone "Indietro"
        indietroButton.setOnClickListener {
            // Quando viene cliccato il bottone "Indietro", torna a ActivitySchermata2
            finish() // Chiama finish per chiudere ActivitySchermata3 e tornare alla precedente
        }
    }
}
