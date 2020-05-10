function addTrack() {
  let artist = $('#artist-input').val();
  let title = $('#title-input').val();
  let url = $('#url-input').val();

  $('#artist-input').val('');
  $('#title-input').val('');
  $('#url-input').val('');

  let rowNumber = $('#tracklist tr').length - 1;
  let artistCell = '<td><input name="tracks[' + rowNumber + '].artist" id="artist" type="text" value="' + artist + '" data-test="artist" readonly/></td>'
  let titleCell = '<td><input name="tracks[' + rowNumber + '].title" id="title" type="text" value="' + title + '" data-test="title" readonly/></td>'
  let urlCell = '<td><input name="tracks[' + rowNumber + '].url" id="url" type="text" value="' + url + '" data-test="url" readonly/></td>'

  let row = '<tr id="tracks" data-test="tracks">' + artistCell + titleCell + urlCell + '</tr>';
  $(row).insertBefore('#inputs');

  $('#artist-input').focus();
}
