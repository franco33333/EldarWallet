package com.example.eldarwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eldarwallet.R
import com.example.eldarwallet.data.local.objects.Card
import com.example.eldarwallet.databinding.ItemCardBinding
import com.example.eldarwallet.utils.getCardLogo

class CardsAdapter(private val list: MutableList<Card>, val context: Context): RecyclerView.Adapter<CardsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsAdapter.ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: CardsAdapter.ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)) {

        private val binding = ItemCardBinding.bind(itemView)

        fun bind(data: Card) {
            with(binding) {
                tvCardNumber.text = "**** ${data.number!!.substring(data.number!!.length-5, data.number!!.length-1)}"
                tvExpireDate.text = data.expirationDate
                val cardLogo = getCardLogo(data.number!!)
                if (cardLogo!=null) {
                    Glide.with(root.context).load(
                        AppCompatResources.getDrawable(
                            root.context,
                            cardLogo
                        )).into(ivLogo)
                }
            }
        }
    }
}