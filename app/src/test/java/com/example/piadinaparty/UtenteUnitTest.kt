package com.example.piadinaparty

import org.junit.Test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import com.example.piadinaparty.model.Utente
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue

class UtenteUnitTest {
    @Test
    fun testAddUserToList() {
        // Creiamo una lista di utenti vuota
        val userList = mutableListOf<Utente>()

        // Creiamo un nuovo utente
        val newUser = Utente(
            id = "marcello19800101",
            firstName = "Marcello",
            lastName = "Ciampoli",
            email = "marcello.ciampoli@example.com",
            points = 0
        )

        // Aggiungiamo l'utente alla lista
        userList.add(newUser)

        // Verifichiamo che la lista non sia vuota
        assertNotNull(userList)
        //Verifichiamo che nella lista ci sia un solo utente
        assertEquals(1, userList.size)

        // Verifichiamo che l'utente inserito sia corretto
        val insertedUser = userList[0]
        assertEquals("marcello19800101", insertedUser.id)
        assertEquals("Marcello", insertedUser.firstName)
        assertEquals("Ciampoli", insertedUser.lastName)
        assertEquals("marcello.ciampoli@example.com", insertedUser.email)
        assertEquals(0, insertedUser.points)
    }

    @Test
    fun testAddInvalidUserToList() {
        // Creiamo una lista di utenti vuota
        val userList = mutableListOf<Utente>()

        // Creiamo un nuovo utente con un campo obbligatorio vuoto (ad esempio, email vuota)
        val invalidUser = Utente(
            id = "invalidUser",
            firstName = "Invalid",
            lastName = "User",
            email = "", // Email vuota
            points = 0
        )

        // Aggiungiamo l'utente alla lista solo se l'email non è vuota
        if (invalidUser.email.isNotEmpty()) {
            userList.add(invalidUser)
        }

        // Verifichiamo che la lista sia ancora vuota
        assertTrue(userList.isEmpty())
    }

    @Test
    fun testAddUserWithDifferentExpectedValues() {
        // Creiamo una lista di utenti vuota
        val userList = mutableListOf<Utente>()

        // Creiamo un nuovo utente con valori diversi
        val newUser = Utente(
            id = "marcello19800",
            firstName = "Marcell",
            lastName = "Ciampol",
            email = "marcello.ciampol@example.com",
            points = 3
        )

        // Aggiungiamo l'utente alla lista
        userList.add(newUser)

        // Verifichiamo che l'utente inserito non corrisponda ai valori attesi
        val insertedUser = userList[0]
        assertNotEquals("marcello19800101", insertedUser.id)
        assertNotEquals("Marcello", insertedUser.firstName)
        assertNotEquals("Ciampoli", insertedUser.lastName)
        assertNotEquals("marcello.ciampoli@example.com", insertedUser.email)
        assertNotEquals(0, insertedUser.points)
    }
}