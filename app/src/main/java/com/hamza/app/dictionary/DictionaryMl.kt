package com.hamza.app.dictionary

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

import com.hamza.app.R
import kotlinx.android.synthetic.main.activity_main_ml.*

class DictionaryMl : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ml)

        btnTranslate.setOnClickListener {
            if (tvSL.text == getString(R.string.indonesia)) {
                initLanguage(
                        TranslateLanguage.URDU,
                        TranslateLanguage.ENGLISH,
                        etText.text.toString()
                )
            } else {
                initLanguage(
                        TranslateLanguage.ENGLISH,
                        TranslateLanguage.URDU,
                        etText.text.toString()
                )
            }
//            initLanguage(etText.text.toString() );
        }

        // When button swap was clicked swap language
        btnSwap.setOnClickListener {
            if (tvSL.text == getString(R.string.indonesia)) {
                tvSL.text = getString(R.string.english)
                tvTL.text = getString(R.string.indonesia)
            } else {
                tvSL.text = getString(R.string.indonesia)
                tvTL.text = getString(R.string.english)
            }
        }
    }


    private fun initLanguage( idSL: Any?, idTL: Any?, text: String?) {
        val option = TranslatorOptions.Builder()
                .setSourceLanguage(idSL as String)
                .setTargetLanguage(idTL as String)
                .build()
        val textTranslator = Translation.getClient(option)

        // Download model for the first time
        textTranslator.downloadModelIfNeeded()
                .addOnSuccessListener {
                    Log.e("MainActivity", "Download Success")
                }
                .addOnFailureListener {
                    Log.e("MainActivity", "Download Failed: $it")
                }


        // Translate text from source language to target language related with model
        textTranslator.translate(text.toString())
                .addOnSuccessListener {
                    tvResult.text = it
                    Log.e("MainActivity", "Translate Success $it")
                }.addOnFailureListener {
                    Log.e("MainActivity", "Translate Failed: $it")
                }
    }
}