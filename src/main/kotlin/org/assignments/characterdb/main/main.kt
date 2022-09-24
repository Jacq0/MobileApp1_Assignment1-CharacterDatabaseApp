package org.assignments.characterdb

fun main()
{
    println("initial repo main file.")
}

fun menu() : Int
{
    //basic CLI menu with DB then we can expand it with GUI
    var option: Int
    var input: String? = null

    println("Welcome to the Character Database App v1.0")
    println("1 - Add a Character")
    println("2 - Update existing Character")
    println("3 - View Characters")
    println("4 - Delete a Characer")

    input = readLine()!!
    option = input.toInt()

    return option
}