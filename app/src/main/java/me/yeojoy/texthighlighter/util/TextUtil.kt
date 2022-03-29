package me.yeojoy.texthighlighter.util

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log

object TextUtil {

    private const val TAG = "TextUtil"

    fun highlightKeyword(originalText: CharSequence, keyword: CharSequence?): CharSequence {
        Log.d(TAG, "getSearchedText() originalText: $originalText, keyword: $keyword")
        if (originalText.isEmpty()) return ""
        if (keyword == null || keyword.isEmpty()) return originalText

        val indexList = getIndexListOfKeyword(originalText.toString(), keyword.toString())
        if (indexList.isEmpty()) {
            return originalText
        }

        val keywordLength = keyword.length
        val type = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

        val spannableString = SpannableString(originalText)
        for (index in indexList) {
            // Text color is red, there is a room for color change
            spannableString.setSpan(ForegroundColorSpan(Color.RED), index, index + keywordLength, type)
        }

        return spannableString
    }

    /**
     * Looks for keyword index
     */
    private fun getIndexListOfKeyword(originalText: String, keyword: String): List<Int> {

        val indexList = mutableListOf<Int>()
        val keywordLength = keyword.length
        var startIndex = 0
        while (originalText.indexOf(keyword, startIndex, true) > -1) {
            val index = originalText.indexOf(keyword, startIndex, true)
            indexList.add(index)
            startIndex = index + keywordLength
        }

        return indexList
    }
}