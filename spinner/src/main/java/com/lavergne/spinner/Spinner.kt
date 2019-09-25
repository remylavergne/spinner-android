package com.lavergne.spinner

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Spinner
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity


class Spinner : Spinner,
    View.OnTouchListener {


    private var _context: Context? = null
    private var spinnerDialog: SpinnerDialog? = null
    private var _adapter: SpinnerAdapter? = null
    private var itemChoosed: String? = null
    private var currentListPosition: Int? = null
    private var itemsOriginal: List<String>? = null
    private var listener: ISpinner? = null
    private var spinnerItems: MutableList<String>? = null
    private var dialogItems: MutableList<String>? = null
    private var hint: Hint = Hint()

    // Attrs
    private var titleText: String? = null
    private var buttonText: String? = null
    private var customLayout: Int? = null

    private var showHintInList: Boolean = true // Default state


    constructor(context: Context) : super(context) {
        this._context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this._context = context

        // Attrs
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Spinner,
            0, 0
        ).apply {

            try {
                hint.text = getString(R.styleable.Spinner_spinner_hint)
                hint.isSelectable =
                    getBoolean(R.styleable.Spinner_spinner_hint_selectable, true)
                hint.toastMessage = getString(R.styleable.Spinner_spinner_hint_toast_message)
                showHintInList =
                    getBoolean(R.styleable.Spinner_spinner_dialog_show_hint, true)
                titleText = getString(R.styleable.Spinner_spinner_title)
                buttonText = getString(R.styleable.Spinner_spinner_button)
                customLayout =
                    getResourceId(R.styleable.Spinner_spinner_custom_layout, DEFAULT_THEME)
            } finally {
                recycle()
            }
        }

        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this._context = context
        init()
    }

    /**
     * Private Methods
     */

    private fun init() {
        // Create and listen Dialog object
        this.spinnerDialog =
            SpinnerDialog(object : SpinnerDialog.SearchItem {
                override fun onItemClicked(item: String) {
                    itemChoosed = item
                    currentListPosition = spinnerItems?.indexOf(item)
                    currentListPosition?.let {
                        setSelection(
                            currentListPosition ?: NO_VALID_INDEX
                        )
                    }

                    // Notify current listener
                    listener?.onItemSelected(item)
                }
            })

        updateAttrs()
    }

    private fun updateAttrs() {
        this.titleText?.let { title(it) }
        this.buttonText?.let { buttonText(it) }
        this.customLayout?.let { listCustomLayout(it) }
        // If Hint text is set, update Dialog
        this.hint.text?.let { this.spinnerDialog?.updateHint(this.hint) }
    }

    private fun refreshAdapter() {
        this._adapter?.let { adapter(this.itemsOriginal!!) }
    }

    private fun createSpinnerList(items: List<String>): MutableList<String> {

        val updatedList = mutableListOf<String>()
        updatedList.addAll(items)

        this.hint.text?.let { hint ->
            updatedList.add(0, hint)
        }

        return updatedList
    }

    private fun createDialogList(items: List<String>): MutableList<String> {

        val updatedList = mutableListOf<String>()
        updatedList.addAll(items)

        this.hint.text?.let { hint ->
            if (this.showHintInList) {
                updatedList.add(0, hint)
            }
        }

        return updatedList
    }

    /**
     * Public Methods
     */

    // TODO: Implémenter l'injection d'une liste temporaire !


    /**
     * This method allowed to pass many Spinner methods
     *
     * @param changes Pass a Spinner method in Higher Order function
     */
    fun set(changes: com.lavergne.spinner.Spinner.() -> Unit) {
        this.changes()
    }

    /**
     * Create or update existing adapter instance inject in Spinner
     *
     * @param items List shown to user
     * @param customLayout Change spinner layout
     */
    fun adapter(items: List<String>, @LayoutRes customLayout: Int? = null) {

        // Save properties
        this.itemsOriginal = items
        customLayout?.let { this.customLayout = it }

        // Create each items list
        this.spinnerItems = createSpinnerList(items)
        this.dialogItems = createDialogList(items)

        // Update Dialog list
        this.spinnerDialog?.updateItems(this.dialogItems ?: items)

        // Create spinner adapter
        this._adapter = SpinnerAdapter(
            context,
            this.customLayout ?: DEFAULT_THEME,
            this.spinnerItems ?: items
        )

        // Set the adapter
        this.adapter = this._adapter
        setOnTouchListener(this)
    }

    /**
     * Change the first message displayed in the spinner
     * The current adapter is refreshed
     *
     * @param text First text displayed in spinner
     * @param isSelectable Made hint selectable as a value
     */
    fun hint(text: String, isSelectable: Boolean = true) {
        this.hint.text = text
        this.hint.isSelectable = isSelectable
        // Update Hint object in Dialog
        this.spinnerDialog?.updateHint(this.hint)

        refreshAdapter()
    }

    /**
     * Change the first message displayed in the spinner
     * The current adapter will be refreshed
     *
     * @param text First text displayed in spinner
     * @param isSelectable Made hint selectable as a value
     * @param toastMessage Message displayed if hint is visible and not selectable
     */
    fun hint(text: String, isSelectable: Boolean = true, toastMessage: String) {
        this.hint.text = text
        this.hint.isSelectable = isSelectable
        this.hint.toastMessage = toastMessage
        // Update Hint object in Dialog
        this.spinnerDialog?.updateHint(this.hint)

        refreshAdapter()
    }

    /**
     * Display the hint value in items list
     * The current adapater will be refreshed
     *
     * @param state Show or not
     */
    fun showHintInList(state: Boolean) {
        this.showHintInList = state

        refreshAdapter()
    }

    /**
     * Change the Dialog title
     *
     * @param text The title text
     */
    fun title(text: String) {
        this.spinnerDialog?.setTitle(text)
    }

    /**
     * Change the Dialog close button
     *
     * @param text The Dialog close button text
     */
    fun buttonText(text: String) {
        this.spinnerDialog?.setButtonText(text)
    }

    /**
     * Set a custom layout to Dialog items list
     *
     * @param customLayout Resource layout
     */
    fun listCustomLayout(@LayoutRes customLayout: Int) {
        this.spinnerDialog?.updateListLayout(customLayout)
    }

    /**
     * Set a custom layout to Spinner
     * User can apply it to the Dialog items list too
     *
     * @param customLayout Resource layout
     * @param applyToDialog Apply the same layout to the Dialog items list
     */
    fun spinnerCustomLayout(@LayoutRes customLayout: Int, applyToDialog: Boolean = false) {
        this._adapter?.let {
            this.itemsOriginal?.let { items ->
                adapter(items, customLayout)
            }
        }

        if (applyToDialog) {
            listCustomLayout(customLayout)
            currentListPosition?.let { setSelection(it) }
        }
    }

    /**
     * Set listener to item change event
     *
     * @param listener interface ISpinner
     */
    fun setItemSelectedListener(listener: ISpinner) {
        this.listener = listener
    }

    /**
     * Actions
     */

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {

            // Protection against multiple clicks
            this.spinnerDialog?.let {
                if (it.dialogIsAlreadyOpen) {
                    return true
                }
            }

            // Display Dialog (only if necessary)
            (context as? FragmentActivity)?.supportFragmentManager?.let {
                spinnerDialog?.show(
                    it,
                    "TAG"
                )
            }
        }
        return true
    }

    /**
     * Interface
     */

    interface ISpinner {
        fun onItemSelected(item: String)
    }
}