package com.example.eldarwallet.data.local.room

import androidx.room.Embedded
import androidx.room.Relation
import com.example.eldarwallet.data.local.objects.Card
import com.example.eldarwallet.data.local.objects.User

data class UserWithCards(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val cards: List<CardEntity>
) {
    fun toUser(): User {
        val userToReturn = User(user.name, user.surname, user.userName, user.password)
        userToReturn.balance = user.balance
        val cardList = mutableListOf<Card>()
        cards.forEach { cardList.add(it.toCard()) }
        userToReturn.cards = cardList
        return userToReturn
    }
}
