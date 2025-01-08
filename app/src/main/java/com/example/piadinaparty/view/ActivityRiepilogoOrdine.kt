package com.example.piadinaparty.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piadinaparty.MainActivity
import com.example.piadinaparty.R
import com.example.piadinaparty.controller.OrdineController
import com.example.piadinaparty.controller.UtenteController
import com.example.piadinaparty.model.Item
import com.example.piadinaparty.model.Ordine
import com.google.firebase.auth.FirebaseAuth
import com.example.piadinaparty.model.Offerta

class ActivityRiepilogoOrdine : AppCompatActivity() {
    private lateinit var orderController: OrdineController
    private lateinit var userController: UtenteController
    private var offerPoints: Int = 0
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riepilogoordine)

        orderController = OrdineController()
        userController = UtenteController(this)
        userId = FirebaseAuth.getInstance().currentUser?.uid

        // Recupera i dati dall'intent
        val indirizzo = intent.getStringExtra("indirizzo")
        val orario = intent.getStringExtra("orario")
        val pagamento = intent.getStringExtra("pagamento")
        val totalOrder = intent.getDoubleExtra("totalOrder", 0.0)

        // Recupera gli elementi selezionati dal Bundle
        val bundle = intent.extras
        val selectedItems: ArrayList<Item>? = bundle?.getParcelableArrayList("selectedItems")
        val selectedOffer: Offerta? = bundle?.getParcelable("selectedOffer")
        offerPoints = intent.getIntExtra("offerPoints", 0)

        // Se selectedItems è null, impostalo come lista vuota
        val items = selectedItems ?: ArrayList()

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
            if (offerPoints > 0 && userId != null) {
                userController.getUserPoints(userId!!) { points ->
                    if (points != null) {
                        val newPoints = points + offerPoints
                        userController.updateUserPoints(userId!!, newPoints) { success ->
                            if (success) {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Errore nel rollback dei punti", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        // Gestisci il click del bottone "Conferma"
        confermaButton.setOnClickListener {
            val userId = getCurrentUserId()
            val order = Ordine(userId = userId, items = items, frequency = 1, prezzo = totalOrder, offerta = selectedOffer)
            orderController.addOrder(order) { success ->
                if (success) {
                    Toast.makeText(this, "Ordine confermato con successo", Toast.LENGTH_SHORT).show()
                    Log.d("ActivityRiepilogoOrdine", "Order saved: $order")
                    val intent = Intent(this, ActivityConfermaOrdine::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Errore nella conferma dell'ordine", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Gestisci il click del bottone "Indietro"
        indietroButton.setOnClickListener {
            val intent = Intent(this, ActivityInserimentoDatiOrdine::class.java).apply {
                putExtra("totalOrder", totalOrder)
                putExtra("offerPoints", offerPoints)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: ""
    }
}