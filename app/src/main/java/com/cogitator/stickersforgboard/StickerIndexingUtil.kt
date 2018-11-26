package com.cogitator.stickersforgboard

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseAppIndexingInvalidArgumentException
import com.google.firebase.appindexing.Indexable
import com.google.firebase.appindexing.builders.Indexables
import com.google.firebase.appindexing.builders.StickerBuilder
import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * @author Ankit Kumar on 26/11/2018
 */
object StickerIndexingUtil {
    private val STICKER_URL_PATTERN = "mystickers://sticker/%s"
    private val STICKER_PACK_URL_PATTERN = "mystickers://sticker/pack/%s"
    private val STICKER_PACK_NAME = "Local Content Pack"
    private val TAG = "AppIndexingUtil"
    val FAILED_TO_INSTALL_STICKERS = "Failed to install stickers"

    //this just adds the stickers
    fun setStickers(context: Context, firebaseAppIndex: FirebaseAppIndex) {
        try {
            val stickers = getIndexableStickers(context)
            val stickerPack = getIndexableStickerPack(context)

            val indexables = ArrayList(stickers)
            indexables.add(stickerPack)

            val task = firebaseAppIndex.update(
                *indexables.toTypedArray()
            )

            task.addOnSuccessListener {
                Toast.makeText(context, "Successfully added stickers", Toast.LENGTH_SHORT)
                    .show()
            }

            task.addOnFailureListener { e ->
                Log.d(TAG, FAILED_TO_INSTALL_STICKERS, e)
                Toast.makeText(context, FAILED_TO_INSTALL_STICKERS, Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Unable to set stickers", e)
            Toast.makeText(context, FAILED_TO_INSTALL_STICKERS, Toast.LENGTH_SHORT)
                .show()
        } catch (e: FirebaseAppIndexingInvalidArgumentException) {
            Log.e(TAG, "Unable to set stickers", e)
            Toast.makeText(context, FAILED_TO_INSTALL_STICKERS, Toast.LENGTH_SHORT).show()
        }

    }

    @Throws(IOException::class)
    private fun getIndexableStickers(context: Context): List<Indexable> {
        val indexableStickers = ArrayList<Indexable>()
        val stickerBuilders = getStickerBuilders(context)

        for (stickerBuilder in stickerBuilders) {
            stickerBuilder
                .setIsPartOf(
                    Indexables.stickerPackBuilder()
                        .setName(STICKER_PACK_NAME)
                )
            indexableStickers.add(stickerBuilder.build())
        }

        return indexableStickers
    }

    // this builds the INDIVIDUAL stickers
    @Throws(IOException::class)
    private fun getStickerBuilders(context: Context): List<StickerBuilder> {
        val stickerBuilders = ArrayList<StickerBuilder>()

        val stickersDir = File(context.filesDir, "stickers")

        if (!stickersDir.exists() && !stickersDir.mkdirs()) {
            throw IOException("Stickers directory does not exist")
        }

        for (i in 0 until HomeActivity.stickers.length) {
            val stickerFilename = getStickerFilename(i)
            val keywords = HomeActivity.stickers[i].getKeywords()

            val stickerBuilder = Indexables.stickerBuilder()
                .setName(stickerFilename)
                // Firebase App Indexing unique key that must match an intent-filter
                // see: Support links to your app content section
                // (e.g. mystickers://sticker/0)
                .setUrl(String.format(STICKER_URL_PATTERN, i))
                // http url or content uri that resolves to the sticker
                // (e.g. http://www.google.com/sticker.png or content://some/path/0)
                .setImage(HomeActivity.stickers[i].getImageUrl())
                .setDescription("content description")
                .setIsPartOf(
                    Indexables.stickerPackBuilder()
                        .setName(STICKER_PACK_NAME)
                )
                .setKeywords(*keywords)
            stickerBuilders.add(stickerBuilder)
        }

        return stickerBuilders
    }

    //this makes the sticker PACK
    @Throws(IOException::class, FirebaseAppIndexingInvalidArgumentException::class)
    private fun getIndexableStickerPack(context: Context): Indexable {
        val stickerBuilders = getStickerBuilders(context)
        val stickersDir = File(context.filesDir, "stickers")

        if (!stickersDir.exists() && !stickersDir.mkdirs()) {
            throw IOException("Stickers directory does not exist")
        }

        // Use the last sticker for category image for the sticker pack.
        val lastIndex = stickerBuilders.size - 1

        // user sticker method here
        val imageUrl = HomeActivity.stickers[lastIndex].getImageUrl()

        val stickerPackBuilder = Indexables.stickerPackBuilder()
            .setName(STICKER_PACK_NAME)
            // Firebase App Indexing unique key that must match an intent-filter.
            // (e.g. mystickers://sticker/pack/0)
            .setUrl(String.format(STICKER_PACK_URL_PATTERN, lastIndex))
            // Defaults to the first sticker in "hasSticker". Used to select between sticker
            // packs so should be representative of the sticker pack.
            .setImage(imageUrl)
            .setHasSticker(*stickerBuilders.toTypedArray())
            .setDescription("content description")
        return stickerPackBuilder.build()
    }

    private fun getStickerFilename(index: Int): String {
        return HomeActivity.stickers[index].getName()
    }

}