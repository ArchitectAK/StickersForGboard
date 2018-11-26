package com.cogitator.stickersforgboard

import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView

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

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            AppIndexingService.enqueueWork(this@HomeActivity)
            val editor = prefs.edit()
            editor.putBoolean("firstTime", true)
            editor.commit()
        }

        val gridView = findViewById<View>(R.id.gridview) as GridView
        val stickersAdapter = StickersAdapter(this, stickers)
        gridView.adapter = stickersAdapter

        image = findViewById<View>(R.id.mainImageView) as ImageView

        val ShareStickerBtn = findViewById<ImageButton>(R.id.shareSticker)
        ShareStickerBtn.setOnClickListener {
            val icon = BitmapFactory.decodeResource(
                this@HomeActivity.getResources(),
                stickers[currentSticker].getImageResource()
            )
            shareImage(icon)
        }

        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val sticker = stickers[position]

            image.setImageResource(sticker.getImageResource())
            currentSticker = position

            // This tells the GridView to redraw itself
            // in turn calling StickerAdapter's getView method again for each cell
            stickersAdapter.notifyDataSetChanged()
        }

    }
}