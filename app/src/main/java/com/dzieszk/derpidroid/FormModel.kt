package com.dzieszk.derpidroid

class FormModel(){
    var utf: String = "✓"
    var auth_token: String = ""
    var user_email: String = ""
    var user_password: String = ""

    fun makeString(): String {
        return "utf8=%E2%9C%93&authenticity_token=" + auth_token +
                "&user%5Bemail%5D=" + user_email +
                "&user%5Bpassword%5D=" + user_password +
                "&user%5Bremember_me%5D=1" +
                "&commit=Sign+in"
    }
}