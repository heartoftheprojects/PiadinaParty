package com.example.piadinaparty.view

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.piadinaparty.MainActivity
import com.example.piadinaparty.R
import com.example.piadinaparty.controller.UtenteController
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import com.example.piadinaparty.model.Item
import com.example.piadinaparty.model.Offerta

class ActivityInserimentoDatiOrdine : AppCompatActivity() {

    private lateinit var userController: UtenteController
    private var offerPoints: Int = 0
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserimentodatiordine)

        userController = UtenteController(this)
        userId = FirebaseAuth.getInstance().currentUser?.uid

        // Trova i TextView e gli EditText nel layout
        val indirizzoEditText = findViewById<AutoCompleteTextView>(R.id.indirizzo)
        val orarioButton = findViewById<Button>(R.id.orarioButton)
        val pagamentoSpinner = findViewById<Spinner>(R.id.paymentMethodSpinner)
        val creditCardLayout = findViewById<LinearLayout>(R.id.creditCardLayout)

        // Trova i campi per i dati della carta di credito
        val creditCardNumberEditText = findViewById<EditText>(R.id.creditCardNumberEditText)
        val creditCardExpiryEditText = findViewById<EditText>(R.id.creditCardExpiryEditText)
        val creditCardCvvEditText = findViewById<EditText>(R.id.creditCardCvvEditText)

        // Trova i pulsanti "Conferma" e "Indietro"
        val confermaButton = findViewById<Button>(R.id.Conferma)
        val indietroButton = findViewById<Button>(R.id.Indietro)

        // Popola il Spinner con i metodi di pagamento
        val paymentMethods = arrayOf("Contanti", "Carta di Credito")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pagamentoSpinner.adapter = adapter

        // Gestisci il click del bottone "Seleziona l'orario"
        orarioButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MINUTE, 30) // Aggiungi 30 minuti all'orario corrente
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                if (selectedHour in 19..23) {
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    orarioButton.text = formattedTime
                } else {
                    Toast.makeText(this, "L'orario di consegna deve essere tra le 19:00 e le 23:00", Toast.LENGTH_SHORT).show()
                }
            }, hour, minute, true)

            timePickerDialog.show()
        }

        // Gestisci la visibilità dei campi in base al metodo di pagamento selezionato
        pagamentoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (parent.getItemAtPosition(position).toString()) {
                    "Carta di Credito" -> {
                        creditCardLayout.visibility = View.VISIBLE
                    }
                    else -> {
                        creditCardLayout.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                creditCardLayout.visibility = View.GONE
            }
        }

        // Aggiungi un TextWatcher per formattare il numero di carta di credito
        creditCardNumberEditText.addTextChangedListener(object : TextWatcher {  //addTextChangedListener associa un TextWatcher a creditCardNumberEditText
            private var current = "" // È quello che il codice ha già formattato e impostato sull'EditText (serve per i controlli sulla formattazione) ,
            private val nonDigits = Regex("[^\\d]") //identifica tutti i caratteri non numerici

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} //cattura il testo prima di qualsiasi modifica.

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {} //cattura il testo durante la modifica

            override fun afterTextChanged(s: Editable?) { //cattura il testo dopo l'inserimento del numero di carta e vengono effettuati controlli relativi alla formattazione
                if (s.toString() != current) { //controlla che quello che l'utente sta digitando "s" sia diverso dal numero già formattato e presente nell'editText
                    val userInput = s.toString().replace(nonDigits, "")
                    if (userInput.length <= 16) {
                        val formatted = StringBuilder()
                        for (i in userInput.indices) {
                            if (i > 0 && i % 4 == 0) {
                                formatted.append(' ')
                            }
                            formatted.append(userInput[i])
                        }
                        current = formatted.toString()  //current prende la stringa di numeri formatatta
                        creditCardNumberEditText.setText(current) //setti il testo dell'editText con l'ultima stringa formattata presente in current
                        creditCardNumberEditText.setSelection(current.length) //Si posiziona il cursore alla fine del testo aggiornato
                    } else {
                        current = current.substring(0, current.length - 1) //Se l'utente inserisce più di 16 cifre, il codice elimina l'ultima cifra aggiunta
                        creditCardNumberEditText.setText(current)
                        creditCardNumberEditText.setSelection(current.length)
                    }
                }
            }
        })

        // Aggiungi un TextWatcher per formattare la data di scadenza
        creditCardExpiryEditText.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val mmYY = "MMYY"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(Regex("[^\\d]"), "") //se i caratteri inseriti dall'utente non sono numerici allora non vengono scritti
                    if (userInput.length <= 4) {
                        val formatted = StringBuilder()
                        for (i in userInput.indices) {
                            if (i == 2) {
                                formatted.append('/')
                            }
                            formatted.append(userInput[i])
                        }

                        current = formatted.toString()
                        creditCardExpiryEditText.setText(current)
                        creditCardExpiryEditText.setSelection(current.length)
                    } else {
                        current = current.substring(0, current.length - 1)
                        creditCardExpiryEditText.setText(current)
                        creditCardExpiryEditText.setSelection(current.length)
                    }
                }
            }
        })

        // Recupera i punti dell'offerta dall'intent
        offerPoints = intent.getIntExtra("offerPoints", 0)

        // Gestisci il click del bottone "Conferma"
        confermaButton.setOnClickListener {
            // Ottieni i valori dai campi EditText e Spinner
            val indirizzo = indirizzoEditText.text.toString()
            val orario = orarioButton.text.toString()
            val pagamento = pagamentoSpinner.selectedItem.toString()
            val totalOrder = intent.getDoubleExtra("totalOrder", 0.0)

            // Validazione dei dati
            if (indirizzo.isEmpty()) {
                indirizzoEditText.error = "Inserisci l'indirizzo"
                return@setOnClickListener
            }
            if (orario == "Seleziona l'orario") {
                Toast.makeText(this, "Seleziona l'orario di consegna", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validazione dell'orario di consegna
            val currentTime = Calendar.getInstance() //oggetto che rappresenta data e ora attuali
            val selectedTime = Calendar.getInstance().apply { //oggetto relativo all'orario selezionato dall'utente ma inizalmente settato con data e ora di CurrentTime e modificato tramite apply
                val parts = orario.split(":") //parts prende la stringa orario e la stringa viene divisa in due parti separandola con i :. Come se abbiamo una lista con due elementi (ora e minuti della stringa orario)
                set(Calendar.HOUR_OF_DAY, parts[0].toInt()) //prima parte convertita in int e settata come ora di selectedTime
                set(Calendar.MINUTE, parts[1].toInt()) //seconda parte convertita in int e settata come minuti di selectedTime
            }
            if (selectedTime.before(currentTime.apply { add(Calendar.MINUTE, 30) }) || selectedTime.get(Calendar.HOUR_OF_DAY) !in 19..23) {
                Toast.makeText(this, "L'orario di consegna deve essere tra le 19:00 e le 23:00 e almeno 30 minuti dopo l'orario corrente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pagamento == "Carta di Credito") {
                val cardNumber = creditCardNumberEditText.text.toString()
                val expiryDate = creditCardExpiryEditText.text.toString()
                val cvv = creditCardCvvEditText.text.toString()

                if (cardNumber.isEmpty() || cardNumber.replace(" ", "").length != 16) {
                    creditCardNumberEditText.error = "Il numero di carta deve contenere 16 cifre"
                    return@setOnClickListener
                }
                if (expiryDate.isEmpty()) {
                    creditCardExpiryEditText.error = "Inserisci la data di scadenza"
                    return@setOnClickListener
                }
                if (cvv.isEmpty() || cvv.length !in 3..4) {
                    creditCardCvvEditText.error = "Il CVV deve contenere 3 o 4 cifre"
                    return@setOnClickListener
                }

                // Validazione della data di scadenza
                val parts = expiryDate.split("/")
                if (parts.size != 2) {
                    creditCardExpiryEditText.error = "Formato data non valido"
                    return@setOnClickListener
                }
                val month = parts[0].toIntOrNull()
                val year = parts[1].toIntOrNull()
                if (month == null || year == null || month !in 1..12) {
                    creditCardExpiryEditText.error = "Data di scadenza non valida"
                    return@setOnClickListener
                }
            }

            // Recupera gli elementi selezionati dal Bundle
            val bundle = intent.extras
            val selectedItems: ArrayList<Item>? = bundle?.getParcelableArrayList("selectedItems")
            val selectedOffer: Offerta? = bundle?.getParcelable("selectedOffer")

            // Passa i dati all'ActivityRiepilogoOrdine
            val intent = Intent(this, ActivityRiepilogoOrdine::class.java).apply {
                putExtra("indirizzo", indirizzo)
                putExtra("orario", orario)
                putExtra("pagamento", pagamento)
                putExtra("totalOrder", totalOrder)
                putExtra("offerPoints", offerPoints)
                putExtras(bundle ?: Bundle())
            }
            Log.d("ActivityInserimentoDatiOrdine", "Order details: Indirizzo: $indirizzo, Orario: $orario, Pagamento: $pagamento, TotalOrder: $totalOrder, SelectedItems: $selectedItems")
            startActivity(intent)
        }

        // Gestisci il click del bottone "Indietro"
        indietroButton.setOnClickListener {
            if (offerPoints > 0 && userId != null) {
                userController.getUserPoints(userId!!) { points ->
                    if (points != null) {
                        val newPoints = points + offerPoints //si restituiscono all'utente i punti che avevo utilizzato per sbloccare offerta
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
            } else { //nel caso in cui offerPoints = 0 perchè non è stata sbloccata nessuna offerta
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }
}