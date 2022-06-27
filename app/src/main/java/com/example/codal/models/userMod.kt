package com.example.codal.models
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
data class userMod(var uid:String = "",
                   var name : String = "",
                   var imgUrl: String = "",
                   var designation: String = "",
                   var cchef_rating: String = "",
                   var cforces_rating: String = "",
                   var numHacks: String = "",
                   var udescription: String = "",
                   var ques: String = "0"
                   )


// Likes string -> convertibles(Long) -- check