package com.cogitator.stickersforgboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author Ankit Kumar on 21/11/2018
 */
class MainActivity :AppCompatActivity(){

    companion object {
        var currentSticker = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}