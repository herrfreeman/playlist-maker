package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val topToolbar = findViewById<MaterialToolbar>(R.id.searchToolbar)
        topToolbar.setNavigationOnClickListener  {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val searchTextEdit = findViewById<TextInputEditText>(R.id.search_edit_text)
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                //nothing
            }


        }

        searchTextEdit.addTextChangedListener(searchTextWatcher)

    }
}