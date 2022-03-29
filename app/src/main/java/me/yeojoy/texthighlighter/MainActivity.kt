package me.yeojoy.texthighlighter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.yeojoy.texthighlighter.util.TextUtil
import me.yeojoy.texthighlighter.util.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewEmpty: TextView

    private lateinit var words: MutableList<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLayout()
        loadWords()
    }

    private fun initLayout() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        textViewEmpty = findViewById(R.id.text_view_empty)

        val editTextKeyword = findViewById<EditText>(R.id.edit_text_keyword)
        editTextKeyword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0)
            }
        })
        editTextKeyword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun search(keyword: CharSequence?) {
        val result = mutableListOf<CharSequence>()

        keyword?.let {
            for (word in words) {
                if (word.contains(it))
                    result.add(TextUtil.highlightKeyword(word, it))
            }

            recyclerView.adapter = TextAdapter(result)
        }
    }

    private fun loadWords() {
        val jsonString = Utils.inputStreamToString(resources.openRawResource(R.raw.words))
        val gson = Gson()
        jsonString?.let {
            val type = object : TypeToken<List<String>>() {}.type
            words = gson.fromJson(jsonString, type)
        }

        recyclerView.adapter = TextAdapter(words)
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}