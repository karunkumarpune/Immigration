package com.immigration.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {

    @SerializedName("main_avatar")
    @Expose
    var mainAvatar: String? = null
    @SerializedName("festival_name1")
    @Expose
    var festivalName1: String? = null
    @SerializedName("festival_name2")
    @Expose
    var festivalName2: String? = null
    @SerializedName("festival_name3")
    @Expose
    var festivalName3: String? = null
    @SerializedName("festival_name4")
    @Expose
    var festivalName4: String? = null
    @SerializedName("festival_name5")
    @Expose
    var festivalName5: String? = null
    @SerializedName("festival_name6")
    @Expose
    var festivalName6: String? = null
    @SerializedName("avatar_1")
    @Expose
    var avatar1: String? = null
    @SerializedName("avatar_2")
    @Expose
    var avatar2: String? = null
    @SerializedName("avatar_3")
    @Expose
    var avatar3: String? = null
    @SerializedName("avatar_4")
    @Expose
    var avatar4: String? = null
    @SerializedName("avatar_5")
    @Expose
    var avatar5: String? = null
    @SerializedName("avatar_6")
    @Expose
    var avatar6: String? = null
}