package com.lavergne.spinner

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes

class SpinnerAdapter(context: Context, @LayoutRes layout: Int, items: List<String>) :
    ArrayAdapter<String>(context, layout, items)