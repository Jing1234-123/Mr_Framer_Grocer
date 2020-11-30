package com.example.mr_framer_grocer.Model

class User {
    var name: String? = null
    var gender: String? = null
    var birthDate: String? = null
    var contact: String? = null
    var email: String? = null
    var address: String? = null
    var password: String? = null

    constructor(name: String?, gender: String?, birthDate: String?, contact: String?, email: String?, address: String?, password: String?){
        this.name = name
        this.gender = gender
        this.birthDate = birthDate
        this.contact = contact
        this.email = email
        this.address = address
        this.password = password
    }
}