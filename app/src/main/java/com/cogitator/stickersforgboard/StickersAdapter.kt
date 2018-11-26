package com.cogitator.stickersforgboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

/**
 * @author Ankit Kumar on 26/11/2018
 */

class StickersAdapter// 1
    (private val mContext: Context, private val stickers: Array<Sticker>) : BaseAdapter() {

    // 2
    override fun getCount(): Int {
        return stickers.size
    }

    // 3
    override fun getItemId(position: Int): Long {
        return 0
    }

    // 4
    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val sticker = stickers[position]

        // view holder pattern
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            convertView = layoutInflater.inflate(R.layout.linearlayout_sticker, null)

            val imageViewCoverArt = convertView!!.findViewById(R.id.imageview_cover_art) as ImageView
            val viewHolder = ViewHolder(imageViewCoverArt)
            convertView.tag = viewHolder
        }

        val viewHolder = convertView.tag as ViewHolder
        Picasso.with(mContext).load(sticker.imageResource).into(viewHolder.imageViewCoverArt)

        if (position == HomeActivity.currentSticker) {
            convertView.alpha = 0.5f
        } else {
            convertView.alpha = 1f
        }

        return convertView
    }

    // Your "view holder" that holds references to each subview
    private inner class ViewHolder(private val imageViewCoverArt: ImageView)

}