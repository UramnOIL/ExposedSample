package com.uramnoil.exposedsample

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Players : IntIdTable() {
    class Player(id: EntityID<Int>) : IntEntity(id) {
        companion object AccountDAO : IntEntityClass<Player>(Players)
    }
}

object Relations : IntIdTable() {
    class Relation(id: EntityID<Int>) : IntEntity(id) {
        companion object RelationDAO : IntEntityClass<Relation>(Relations) {
            fun isFriends(friend1: Players.Player, friend2: Players.Player): Boolean {
                select { (Relations.offeree eq friend1.id) and (Relations.offerer eq friend2.id) }.singleOrNull() ?: return false
                select { (Relations.offeree eq friend2.id) and (Relations.offerer eq friend1.id) }.singleOrNull() ?: return false
                return true
            }
        }
        var offerer by Players.Player referencedOn Relations.offerer
        var offeree by Players.Player referencedOn Relations.offeree
    }
    val offerer = reference("offerer", Players)
    val offeree = reference("offeree", Players)

    init {
        uniqueIndex(offerer, offeree)
    }
}

fun main() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")

    transaction {
        SchemaUtils.create(Players, Relations)
        val player1 = Players.Player.new {}
        val player2 = Players.Player.new {}

        val relation1 = Relations.Relation.new {
            offerer = player1
            offeree = player2
        }

        val relation2 = Relations.Relation.new {
            offerer = player2
            offeree = player1
        }

        Relations.Relation.all().forEach {
            println("${it.offerer} + ${it.offeree}")
        }

        println("areFriends: " + Relations.Relation.isFriends(player1, player2))
    }
}