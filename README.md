[![](https://jitpack.io/v/remylavergne/spinner-android.svg)](https://jitpack.io/#remylavergne/spinner-android)

# Spinner

Spinner API écrite en Kotlin.

## Ajouter dans la vue dans le layout

```xml
<com.lavergne.Spinner
        android:id="@+id/my_spinner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

Options disponibles :

| Attrs   |      Fonction      | Type | Défaut |
|----------|:-------------|:-----------:|:----:|
| **spinner_hint** |  Change le texte qui s'affiche à l'initialisation du Spinner | string | null
| **spinner_hint_selectable** | Permet de sélectionner, ou pas, le hint de la liste | boolean | true
| **spinner_hint_toast_message** | Si le Spinner est présent, et qu'on ne peut pas le sélectionner, nous pouvons afficher un message Toast   | string | null
| **spinner_dialog_show_hint** | Afficher le **hint** dans la liste du **Dialog** | boolean | true
| **spinner_title** | Change le titre du **Dialog**   | string | null
| **spinner_button** | Changer le texte du bouton de fermeture de la **Dialog**   | string | Generic button
| **spinner_custom_layout** | Changer le layout du **Dialog** | reference | android.R.layout.simple_list_item_1

### Higher Order Function

```kotlin

// Binding de la vue
this._mySpinner = findViewById(R.id.my_spinner)

// Configuration du Spinner
this._mySpinner.set {
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
                    "Element 10"
                )/*, R.layout.custom_layout*/
            )

            // Applique un thème à la liste du Dialog
            listCustomLayout(R.layout.custom_layout)

            // Listener pour tous les changements
            setItemSelectedListener(object : Spinner.ISpinner {
                override fun onItemSelected(item: String) {
                    val sentence = "Choix : $item"
                    _result.text = sentence
                }
            })

            // Ajoute un hint à la liste d'objets, et désactive sa sélection
            hint("Hint in High Order Function", false, "Unable")
            // Affiche le hint dans la liste du Dialog
            showHintInList(true)
            // Change le titre du Dialog
            title("Title in High Order Function")
            // Change le text du bouton du Dialog
            buttonText("Close with HOF")
        }
```
