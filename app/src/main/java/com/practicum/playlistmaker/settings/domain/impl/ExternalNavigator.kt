package com.practicum.playlistmaker.settings.domain.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.settings.domain.models.EmailData

class ExternalNavigator(private val context: Context) {

    fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        shareIntent.type = "text/*"
        context.startActivity(shareIntent)
    }

    fun shareText(text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        shareIntent.type = "text/*"
        context.startActivity(shareIntent)
    }

    fun openLink(link: String) {
        val openLinkIntent = Intent(Intent.ACTION_VIEW)
        openLinkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        openLinkIntent.data = Uri.parse(link)
        context.startActivity(openLinkIntent)
    }

    fun openEmail(emailData: EmailData) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailData.text)
        context.startActivity(emailIntent)
    }
}