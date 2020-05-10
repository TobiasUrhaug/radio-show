describe("Tracklist", function() {

  it("addTrack adds a track to the tracklist", function() {
    loadFixtures('tracklist.html');

    const track = {artist: 'Artist Name', title: 'track title', url: 'url'};
    $('#artist-input').val(track.artist);
    $('#title-input').val(track.title);
    $('#url-input').val(track.url);

    addTrack();

    let addedTrack = $('#tracklist tr').eq(0);
    expect($(addedTrack.find('#artist')).val()).toBe(track.artist);
    expect($(addedTrack.find('#title')).val()).toBe(track.title);
    expect($(addedTrack.find('#url')).val()).toBe(track.url);
  });

  it("addTrack adds the track to the second last row, above the input fields", function() {
    loadFixtures('tracklist.html');

    const track = {artist: 'Artist Name', title: 'track title', url: 'url'};
    $('#artist-input').val(track.artist);
    $('#title-input').val(track.title);
    $('#url-input').val(track.url);

    addTrack();

    expect($('#tracklist tr').eq(1).attr('id')).toBe('inputs');
  });

  it("addTrack clears input fields after adding a track", function() {
    loadFixtures('tracklist.html');

    const track = {artist: 'Artist Name', title: 'track title', url: 'url'};
    $('#artist-input').val(track.artist);
    $('#title-input').val(track.title);
    $('#url-input').val(track.url);

    addTrack();

    expect($('#artist-input').val()).toBe('');
    expect($('#title-input').val()).toBe('');
    expect($('#url-input').val()).toBe('');
  });

  it("addTrack puts focus on artist input", function() {
    loadFixtures('tracklist.html');

    const track = {artist: 'Artist Name', title: 'track title', url: 'url'};
    $('#artist-input').val(track.artist);
    $('#title-input').val(track.title);
    $('#url-input').val(track.url);

    addTrack();

    expect($('#artist-input')).toBeFocused();
  });

});

