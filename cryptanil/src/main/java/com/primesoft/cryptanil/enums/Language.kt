package com.primesoft.cryptanil.enums

enum class Language(private var id: String) {

    EN("en"),
    DE("de"),
    ES("es"),
    FR("fr"),
    RU("ru");

    companion object {
        fun getById(id: String): Language {
            for (language in values()) {
                if (language.id == id) return language
            }
            return EN
        }
    }

    open fun getId(): String {
        return this.id
    }

}