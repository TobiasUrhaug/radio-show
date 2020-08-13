package no.omtheorem.radio.tracks

data class TrackForm(
        var artist: String = "",
        var title: String = "",
        var url: String = "",
        var remix: String? = "",
        var label: String? = "")
{

    fun isEmpty(): Boolean {
        return artist.isEmpty() && title.isEmpty() && url.isEmpty();
    }
}
