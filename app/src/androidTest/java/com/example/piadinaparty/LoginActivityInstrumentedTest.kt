package com.example.piadinaparty

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.piadinaparty.view.ActivityLogin
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityInstrumentedTest {
    private lateinit var email1: String
    private lateinit var password1: String
    private lateinit var email2: String
    private lateinit var password2: String

    // Regola che viene utilizzata per avviare un'attività
    @get:Rule
    var activityRule: ActivityScenarioRule<ActivityLogin> = ActivityScenarioRule(ActivityLogin::class.java)

    // I dati vengono inizializzati
    @Before
    fun setUp() {
        email1 = "matteogiug@gmail.com"
        password1 = "Giug23"
        email2 = "prova2@gmail.com"
        password2 = "Provaprova.2"
    }

    /** Questo Test andrà a buon fine perchè l'utente email1 password1 è registrato nel database */
    @Test
    fun testLogin1() {
        // Inserimento email
        Espresso.onView(withId(R.id.LoginEmail))
            .perform(ViewActions.typeText(email1), ViewActions.closeSoftKeyboard())
        // Inserimento password
        Espresso.onView(withId(R.id.LoginPassword))
            .perform(ViewActions.typeText(password1), ViewActions.closeSoftKeyboard())
        // Click sul button accedi
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click())

        Thread.sleep(4000)

        // Se viene aperta la MainActivity, quindi l'utente è loggato, il test è andato a buon fine altrimenti è fallito
        Espresso.onView(withId(R.id.frame_container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /** Questo Test non andrà a buon fine perchè l'utente email2 password2 non è registrato nel database */
    @Test
    fun testLogin2() {
        // Inserimento email
        Espresso.onView(withId(R.id.LoginEmail))
            .perform(ViewActions.typeText(email2), ViewActions.closeSoftKeyboard())
        // Inserimento password
        Espresso.onView(withId(R.id.LoginPassword))
            .perform(ViewActions.typeText(password2), ViewActions.closeSoftKeyboard())
        // Click sul button accedi
        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click())

        Thread.sleep(5000)

        // Se viene aperta la MainActivity, quindi l'utente è loggato, il test è andato a buon fine altrimenti è fallito
        Espresso.onView(withId(R.id.frame_container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}