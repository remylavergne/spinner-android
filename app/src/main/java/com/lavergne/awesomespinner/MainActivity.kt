package com.lavergne.awesomespinner

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lavergne.spinner.Spinner

class MainActivity : AppCompatActivity() {

    private lateinit var mySpinner: Spinner
    private lateinit var result: TextView
    private lateinit var spinnerLayout: Button

    private val customLayouts = listOf(
        R.layout.custom_layout,
        R.layout.custom_layout_2,
        R.layout.custom_layout_3,
        R.layout.custom_layout_4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mySpinner = findViewById(R.id.my_spinner)
        this.result = findViewById(R.id.show_result)
        this.spinnerLayout = findViewById(R.id.change_spinner_layout)

        higherOrderFunction()

        // OR

        // casualFunction()

        // OR

        // Via XML ! (README)

        // Buttons
        this.spinnerLayout.setOnClickListener {
            this.mySpinner.spinnerCustomLayout(
                this.customLayouts.random(),
                true
            ) // Randomize some layouts
        }
    }

    private fun higherOrderFunction() {
        this.mySpinner.set {
            adapter(
                listOf(
                    "Element 01",
                    "Element 02",
                    "Element 03",
                    "Element 04",
                    "Element 05",
                    "Element 06",
                    "Element 07",
                    "Element 08",
                    "Element 09",
                    "Element 10",
                    "Element 11",
                    "Element 12"
                )/*, R.layout.custom_layout*/
            )

            listCustomLayout(R.layout.custom_layout)

            setItemSelectedListener(object : Spinner.ISpinner {
                override fun onItemSelected(item: String) {
                    val sentence = "Choix : $item"
                    result.text = sentence
                }
            })

            // This override attrs variables
            hint("Hint in High Order Function", false, "Unable")
            showHintInList(true)
            // Override du premier hint
            //hint("Title random", false, "Impossible de sélectionner cette partie")
            title("Title in High Order Function")
            buttonText("Close with HOF")
        }
    }

    private fun casualFunction() {
        this.mySpinner.adapter(
            listOf(
                "Element 1",
                "Element 2",
                "Element 3",
                "Element 4",
                "Element 5",
                "Element 6",
                "Element 7",
                "Element 8",
                "Element 9",
                "Element 10",
                "Element 11",
                "Element 12"
            )
        )

        this.mySpinner.hint("Hint in casual Function")
        this.mySpinner.title("Title in casual Function")
        this.mySpinner.buttonText("Close with casual function")
    }


}
