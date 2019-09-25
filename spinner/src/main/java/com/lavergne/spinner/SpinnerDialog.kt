package com.lavergne.spinner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment

class SpinnerDialog(private val itemListener: SearchItem) :
    DialogFragment(),
    SearchView.OnQueryTextListener {

    private var searchView: SearchView? = null
    private var listView: ListView? = null

    private var items: List<String> = emptyList()

    private var title: String? = null
    private var closeButton: String? = null
    private var customLayout: Int? = null

    var dialogIsAlreadyOpen: Boolean = false
        private set

    private var _hint: Hint? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val layoutInflater = LayoutInflater.from(activity)

        val rootView = layoutInflater.inflate(R.layout.search_dialog_layout, null)

        // Fill list item in RootView
        setDataInListView(rootView)

        // Create the Dialog
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setView(rootView)

        this.title?.let { alertDialog.setTitle(it) }

        alertDialog.setPositiveButton(
            this.closeButton ?: "Generic positive button"
        ) { dialog, _ ->
            dialog.cancel()
        }

        dialogOpened()

        return alertDialog.create()
    }

    /**
     * Private Methods
     */

    private fun setDataInListView(rootView: View?) {

        this.searchView = rootView?.findViewById(R.id.search_field)
        this.listView = rootView?.findViewById(R.id.items)

        this.listView?.adapter =
            ArrayAdapter(
                requireContext(),
                this.customLayout ?: R.layout.simple_list_item,
                this.items
            )

        // Made the list auto filterable
        this.listView?.isTextFilterEnabled = true
        this.searchView?.setOnQueryTextListener(this)

        // Click event on list
        this.listView?.setOnItemClickListener { _, _, position, _ ->
            this.listView?.adapter?.let { adapter ->
                // Item
                val item = adapter.getItem(position) as String
                // Check Item
                if (hintExist() && item == this._hint?.text && !hintSelectable()) {
                    this._hint?.toastMessage?.let { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    this.itemListener.onItemClicked(item)

                    dialog?.cancel()
                    dialogClosed()
                }
            }
        }
    }

    private fun dialogClosed() {
        this.dialogIsAlreadyOpen = false
    }

    private fun dialogOpened() {
        this.dialogIsAlreadyOpen = true
    }

    private fun hintExist(): Boolean {
        return this._hint != null && !this._hint?.text.isNullOrEmpty()
    }

    private fun hintSelectable(): Boolean {
        return this._hint?.isSelectable ?: true // Selectable by default
    }


    /**
     * Public Methods
     */

    fun setTitle(text: String) {
        this.title = text
    }

    fun setButtonText(text: String) {
        this.closeButton = text
    }

    fun updateItems(items: List<String>) {
        this.items = items
    }

    fun updateListLayout(@LayoutRes layout: Int) {
        this.customLayout = layout
    }

    fun updateHint(hint: Hint) {
        this._hint = hint
    }

    /**
     * Implementations
     */

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        val filter = when {
            !newText.isNullOrEmpty() -> newText
            else -> null
        }

        (this.listView?.adapter as? ArrayAdapter<*>)?.filter?.filter(filter)

        return true
    }

    /**
     * Interfaces
     */

    interface SearchItem {
        fun onItemClicked(item: String)
    }

    /**
     * Lifecycle
     */

    override fun onStop() {
        dialogClosed()
        super.onStop()
    }

    override fun onDestroy() {
        dialogClosed()
        super.onDestroy()
    }
}