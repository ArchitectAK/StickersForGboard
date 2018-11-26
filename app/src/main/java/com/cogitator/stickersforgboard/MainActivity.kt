package com.cogitator.stickersforgboard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.selected_sticker.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author Ankit Kumar on 21/11/2018
 */
class MainActivity : AppCompatActivity() {

    companion object {
        var currentSticker = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            AppIndexingService().enqueueWork(this@MainActivity)
            val editor = prefs.edit()
            editor.putBoolean("firstTime", true)
            editor.apply()
        }

        val stickersAdapter = StickersAdapter(this, stickers)
        gridview.adapter = stickersAdapter


        val ShareStickerBtn = findViewById<ImageButton>(R.id.shareSticker)
        ShareStickerBtn.setOnClickListener {
            val icon = BitmapFactory.decodeResource(
                this@MainActivity.resources,
                stickers[currentSticker].imageResource
            )
            shareImage(icon)
        }

        gridview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val sticker = stickers[position]

            mainImageView.setImageResource(sticker.imageResource)
            currentSticker = position

            // This tells the GridView to redraw itself
            // in turn calling StickerAdapter's getView method again for each cell
            stickersAdapter.notifyDataSetChanged()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // set back the currently selected sticker we're viewing
        mainImageView.setImageResource(stickers[currentSticker].imageResource)

    }

    private fun shareImage(bitmap: Bitmap) {
        // save bitmap to cache directory
        try {
            val cachePath = File(this.cacheDir, "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream = FileOutputStream(cachePath + "/image.png") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to copy sticker", Toast.LENGTH_SHORT)
                .show()
        }

        val imagePath = File(this.cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        val contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile)

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.type = "image/png"
            startActivity(Intent.createChooser(shareIntent, "Choose an app"))
        }
    }

    var stickers = arrayOf(
        Sticker(
            "logo black",
            R.drawable.pg_logo_black,
            "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Black.png?alt=media&token=e07c50df-697a-40d7-a6a1-a8f5f53f7bba",
            arrayOf("playground", "logo", "black")
        ), Sticker(
            "logo blue",
            R.drawable.pg_logo_blue,
            "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Blue.png?alt=media&token=c1400515-36e9-4f2e-9e78-5171bdfa5339",
            arrayOf("playground", "logo", "blue")
        ), Sticker(
            "logo gold",
            R.drawable.pg_logo_gold,
            "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Gold.png?alt=media&token=0154c57c-ec2f-4f32-b706-f81fd68788d4",
            arrayOf("playground", "logo", "gold")
        ), Sticker(
            "logo green",
            R.drawable.pg_logo_green,
            "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Green.png?alt=media&token=2d5b1d18-230f-4401-a27d-1e035ced180a",
            arrayOf("playground", "logo", "green")
        ), Sticker(
            "logo light blue",
            R.drawable.pg_logo_lightblue,
            "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-LightBlue.png?alt=media&token=0fa1713e-f589-4b1b-8ec7-5b81de8e1c3d",
            arrayOf("playground", "logo", "light blue")
        ), Sticker(
            "logo red",
            R.drawable.pg_logo_red,
            "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Red.png?alt=media&token=be40cb08-09e3-4f4b-808c-e81a0693fcac",
            arrayOf("playground", "logo", "red")
        )
    )
}