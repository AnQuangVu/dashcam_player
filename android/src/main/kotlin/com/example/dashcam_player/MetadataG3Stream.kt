package com.example.dashcam_player

data class SharedMetaData(var metaData: String?) {

    fun getGPSData(): String {
        var gpsData : List<String> = (metaData ?:"").split(": ")
        var gps = ""
        if(gpsData.size > 1) {
            gps = gpsData.last()
        }

        return gps
    }
}