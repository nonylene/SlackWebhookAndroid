package net.nonylene.slackwebhook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var webhookEditText: EditText? = null
    private var userNameEditText: EditText? = null
    private var userEmojiEditText: EditText? = null
    private var textEditText: EditText? = null
    private var jsonTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val watcher = JsonTextWatcher()
        textEditText = (findViewById(R.id.textEditText) as EditText).apply {
            addTextChangedListener(watcher)
        }
        userNameEditText = (findViewById(R.id.userNameEditText) as EditText).apply {
            addTextChangedListener(watcher)
        }
        userEmojiEditText = (findViewById(R.id.userEmojiEditText) as EditText).apply {
            addTextChangedListener(watcher)
        }
        webhookEditText = findViewById(R.id.webhookEditText) as EditText
        jsonTextView = findViewById(R.id.jsonTextView) as TextView
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    inner class JsonTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}
