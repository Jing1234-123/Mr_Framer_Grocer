package com.example.mr_framer_grocer.Database

object EndPoints {
        private val URL_ROOT = "https://mr-farmer-grocer.000webhostapp.com/api/"

        // product
        val URL_READ_R_PROD = URL_ROOT + "product/read_category.php"
        val URL_UPDATESTOCK = URL_ROOT + "product/update.php"

        // user
        val URL_VERIFY_USER = URL_ROOT + "user/read_one.php"
        val URL_CREATE_USER = URL_ROOT + "user/create.php"
        val URL_UPDATE_USER = URL_ROOT + "user/update.php"
        val URL_UPDATEPSW_USER = URL_ROOT + "user/updatePsw.php"



    }

