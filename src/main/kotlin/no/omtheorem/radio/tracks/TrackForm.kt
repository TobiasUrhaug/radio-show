package no.omtheorem.radio.tracks

data class TrackForm(var artist: String = "", var title: String = "", var url: String = "") {

    fun isEmpty(): Boolean {
        return artist.isEmpty() && title.isEmpty() && url.isEmpty();
    }
}
