package com.practicum.playlistmaker.settings.domain.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker.settings.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.application_link)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.support_email),
            subject = context.getString(R.string.mailto_support_text),
            text = context.getString(R.string.mailto_support_subject),
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.application_link)
    }
}