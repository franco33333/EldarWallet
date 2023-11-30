package com.example.eldarwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eldarwallet.R
import com.example.eldarwallet.data.local.objects.Card
import com.example.eldarwallet.databinding.ItemCardSelectableBinding
import com.example.eldarwallet.utils.getCardLogo

class CardsSelectableAdapter(private val list: MutableList<Card>, val context: Context): RecyclerView.Adapter<CardsSelectableAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsSelectableAdapter.ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: CardsSelectableAdapter.ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size

    fun getSelectedCard(): Card? {
        list.forEach { if (it.isSelected) return it }
        return null
    }

    inner class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card_selectable, parent, false)) {

        private val binding = ItemCardSelectableBinding.bind(itemView)

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

                if (data.isSelected) {
                    tvCardNumber.setTextColor(root.context.getColor(R.color.white))
                    tvTitleExpireDate.setTextColor(root.context.getColor(R.color.white))
                    tvExpireDate.setTextColor(root.context.getColor(R.color.white))
                    clContainer.setBackgroundColor(root.context.getColor(R.color.color_secondary))
                } else {
                    tvCardNumber.setTextColor(root.context.getColor(R.color.gray_light))
                    tvTitleExpireDate.setTextColor(root.context.getColor(R.color.gray_light))
                    tvExpireDate.setTextColor(root.context.getColor(R.color.gray_light))
                    clContainer.setBackgroundColor(root.context.getColor(R.color.gray))
                }

                root.setOnClickListener {
                    list.forEach { it.isSelected = false }
                    data.isSelected = true
                    notifyItemRangeChanged(0, itemCount)
                }
            }
        }
    }
}