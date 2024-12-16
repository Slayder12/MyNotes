package com.example.mynotes

import java.io.Serializable


class Note(var id: Int? = null, val textNote: String, val date: String, val status: Int): Serializable
