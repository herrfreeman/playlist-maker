package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

class SearchActivity : AppCompatActivity() {

    private var searchString = SEARCH_STRING_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val topToolbar = findViewById<MaterialToolbar>(R.id.searchToolbar)
        topToolbar.setNavigationOnClickListener  {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val searchTextEdit = findViewById<TextInputEditText>(R.id.search_edit_text)
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchString = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        }

        searchTextEdit.addTextChangedListener(searchTextWatcher)
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