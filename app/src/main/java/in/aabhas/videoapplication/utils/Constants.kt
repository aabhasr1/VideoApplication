package `in`.aabhas.videoapplication.utils

object Constants {
    object IntentConstants {
        const val PICK_VIDEO_FROM_GALLERY = 12
        const val VIDEO_URI = "video_uri"
        const val PERMISSION_ASK = 11
    }

    enum class COMPRESSION_FORMATS(
        val quality: String,
        val bitrate: String,
        val resolution: String
    ) {
        QUALITY_240("240p", "0.64M", "424x240"),
        QUALITY_360("360p", "0.77M", "480x360"),
        QUALITY_432("432p", "1.15M", "768x432"),
        QUALITY_480("480p", "0.96M", "640x480"),
        QUALITY_480_HQ("480p HQ", "1.28M", "640x480"),
        QUALITY_576("576p", "1.47M", "768x576"),
        QUALITY_576_HQ("576p HQ", "1.60M", "768x576"),
        QUALITY_720("720p", "1.92M", "960x720"),
        QUALITY_720_HQ("720p HQ", "2.56M", "960x720");

        fun getFormats(): String {
            return "$quality ($bitrate)"
        }
    }
}