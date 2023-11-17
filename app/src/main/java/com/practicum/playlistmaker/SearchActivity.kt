package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {

    private var searchString = SEARCH_STRING_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setCleanSearchButtonVisibility()

        val topToolbar = findViewById<MaterialToolbar>(R.id.search_toolbar)
        topToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val searchTextEdit = findViewById<TextInputEditText>(R.id.search_edit_text)
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchString = p0.toString()
                setCleanSearchButtonVisibility()
            }
            override fun afterTextChanged(p0: Editable?) {}
        }
        searchTextEdit.addTextChangedListener(searchTextWatcher)

        val searchTextEditLayout = findViewById<TextInputLayout>(R.id.search_edit_text_layout)
        searchTextEditLayout.setEndIconOnClickListener {
//            val searchTextEdit = findViewById<TextInputEditText>(R.id.search_edit_text)
            searchTextEdit.setText(SEARCH_STRING_DEFAULT)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchTextEdit.windowToken, 0)
            searchTextEdit.clearFocus()
        }
    }

    fun setCleanSearchButtonVisibility() {
        val searchTextEditLayout = findViewById<TextInputLayout>(R.id.search_edit_text_layout)
        searchTextEditLayout.isEndIconVisible = !searchString.isEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SEARCH_STRING, SEARCH_STRING_DEFAULT) ?: SEARCH_STRING_DEFAULT
        val searchTextEdit = findViewById<TextInputEditText>(R.id.search_edit_text)
        searchTextEdit.setText(searchString)
    }

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_STRING_DEFAULT = ""
    }
}