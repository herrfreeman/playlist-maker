package com.practicum.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var appPreferences: SharedPreferences
    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val topToolbar = findViewById<MaterialToolbar>(R.id.settings_toolbar)
        topToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        themeSwitcher = findViewById(R.id.theme_switcher)
        appPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        val app = applicationContext as App
        themeSwitcher.isChecked = app.darkTheme
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            app.switchTheme(isChecked)
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

    override fun onStop() {
        super.onStop()
        appPreferences.edit().putBoolean(NIGHT_MODE, themeSwitcher.isChecked).apply()
    }

}