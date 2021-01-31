package com.mirkamalg.edvapp.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mirkamalg.edvapp.model.data.*
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Mirkamal on 25 January 2021
 */

fun String.toDate(
    dateFormat: String = "dd.MM.yyy HH:mm",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun ChequeEntity.toChequeWrapperData(): ChequeWrapperData {
    val items: List<ChequeItemData> = jacksonObjectMapper().readValue(this.items)
    return ChequeWrapperData(
        1,
        ChequeData(
            this.shortDocumentId,
            this.documentID,
            this.factoryNumber,
            this.cashregisterModelName,
            this.cashregisterFactoryNumber,
            this.storePhone,
            this.storeName,
            this.storeAddress,
            this.companyName,
            this.companyTaxNumber,
            this.storeTaxNumber,
            ChequeContentData(
                this.prepaymentSum,
                this.docNumber,
                this.creditSum,
                this.docType?.toInt(),
                listOf(VATAmountsData(this.vatResult, this.vatSum, this.vatPercent)),
                this.sum,
                this.cashSum,
                this.cashboxTaxNumber,
                this.bonusSum,
                this.createdAtUtc,
                this.cashier,
                this.positionInShift?.toInt(),
                this.currency,
                this.globalDocNumber,
                this.cashlessSum,
                items
            )
        )
    )
}

fun Context?.copyToClipboard(text: String) {
    this?.let {
        val clipboard = getSystemService(it, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("documentID", text)
        clipboard?.setPrimaryClip(clip)
    }
}

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
//                textPaint.color = textPaint.linkColor
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = false
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val linkText = links.first().first.toLowerCase(Locale.getDefault())
        val mainText = this.text.toString().toLowerCase(Locale.getDefault())
        val startIndexOfLink = mainText.indexOf(linkText)
        if (startIndexOfLink > -1) {
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun TextView.makeBold(words: List<String>) {
    val spannableString = SpannableString(this.text)
    for (word in words) {
        val styleSpan = StyleSpan(Typeface.BOLD)

        val mainText = this.text.toString().toLowerCase(Locale.getDefault())
        val startIndexOfLink = mainText.indexOf(word)
        if (startIndexOfLink > -1) {
            spannableString.setSpan(
                styleSpan, startIndexOfLink, startIndexOfLink + word.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun TextView.makeColored(words: List<String>, @ColorRes color: Int) {
    val spannableString = SpannableString(this.text)
    for (word in words) {
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(context, color))

        val mainText = this.text.toString().toLowerCase(Locale.getDefault())
        val startIndexOfLink = mainText.indexOf(word)
        if (startIndexOfLink > -1) {
            spannableString.setSpan(
                colorSpan, startIndexOfLink, startIndexOfLink + word.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun Context?.openURL(URL: String) {
    this?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
        startActivity(intent)
    }
}

fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}