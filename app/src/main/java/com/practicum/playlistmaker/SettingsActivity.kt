package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val topToolbar = findViewById<MaterialToolbar>(R.id.settingsToolbar)
        topToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }

        val shareButton = findViewById<TextView>(R.id.share_application)
        shareButton.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.application_link))
            sendIntent.type = "text/*"
            startActivity(sendIntent)
        }

        val writeToSupport = findViewById<TextView>(R.id.write_to_support)
        writeToSupport.setOnClickListener {
            val sendToIntent = Intent(Intent.ACTION_SENDTO)
            sendToIntent.data = Uri.parse("mailto:")
            sendToIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            sendToIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mailto_support_text))
            sendToIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailto_support_subject))
            startActivity(sendToIntent)
        }


        val agreementReader = findViewById<TextView>(R.id.read_agreement)
        agreementReader.setOnClickListener {
            val reedAgreementIntent = Intent(Intent.ACTION_VIEW)
            reedAgreementIntent.data = Uri.parse(getString(R.string.agreement_link))
            startActivity(reedAgreementIntent)
        }
    }
}