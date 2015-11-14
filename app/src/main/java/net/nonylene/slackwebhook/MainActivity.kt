package net.nonylene.slackwebhook

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var slackJson: JSONObject = JSONObject()
        set(value) {
            jsonTextView?.text = value.toString(4)
            field = value
        }

    private var webhookEditText: EditText? = null
    private var userNameEditText: EditText? = null
    private var userEmojiEditText: EditText? = null
    private var textEditText: EditText? = null
    private var jsonTextView: TextView? = null
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        jsonTextView = findViewById(R.id.jsonTextView) as TextView

        val watcher = JsonTextWatcher()
        textEditText = (findViewById(R.id.textEditText) as EditText).apply {
            addTextChangedListener(watcher)
            setText(sharedPreferences!!.getString("text_text_key", ""))
        }
        userNameEditText = (findViewById(R.id.userNameEditText) as EditText).apply {
            addTextChangedListener(watcher)
            setText(sharedPreferences!!.getString("name_text_key", ""))
        }
        userEmojiEditText = (findViewById(R.id.userEmojiEditText) as EditText).apply {
            addTextChangedListener(watcher)
            setText(sharedPreferences!!.getString("emoji_text_key", ""))
        }
        webhookEditText = (findViewById(R.id.webhookEditText) as EditText).apply {
            addTextChangedListener(watcher)
            setText(sharedPreferences!!.getString("url_text_key", ""))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.postButton -> {
                postToSlack(slackJson)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun postToSlack(jsonObject: JSONObject) {
        Volley.newRequestQueue(this).add(JsonPostStringRequest(
                Request.Method.POST, webhookEditText!!.text.toString(), jsonObject,
                { response ->
                    Toast.makeText(this@MainActivity, "success! " + response, Toast.LENGTH_LONG).show()
                }, { error ->
            val message = error.networkResponse?.let { " ${it.statusCode}\n${String(it.data)}" } ?: ""
            Toast.makeText(this@MainActivity, error.toString() + message, Toast.LENGTH_LONG).show()
        })
        )
    }

    inner class JsonPostStringRequest(method: Int, url: String, private val jsonObject: JSONObject, listener: ((String) -> Unit),
                                      errorListener: ((VolleyError) -> Unit)) : StringRequest(method, url, listener, errorListener) {

        override fun getBody(): ByteArray? {
            return jsonObject.toString().toByteArray()
        }

        override fun getBodyContentType(): String? {
            return "application/json"
        }
    }

    inner class JsonTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            slackJson = JSONObject(mapOf(
                    "text" to textEditText?.text.toString(),
                    "icon_emoji" to userEmojiEditText?.text.toString(),
                    "username" to userNameEditText?.text.toString()))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onStop() {
        super.onStop()
        sharedPreferences!!.edit()
                .putString("text_text_key", textEditText!!.text.toString())
                .putString("name_text_key", userNameEditText!!.text.toString())
                .putString("emoji_text_key", userEmojiEditText!!.text.toString())
                .putString("url_text_key", webhookEditText!!.text.toString())
                .apply()
    }
}
