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
  let moveUpBtn = '<td><button type="button" data-test="move-up" id="move-up">Move Up</button></td>'

  let row = '<tr id="tracks" data-test="tracks">' + artistCell + titleCell + urlCell + moveUpBtn + '</tr>';
  $(row).insertBefore('#inputs');

  $('#artist-input').focus();
}

$(function(){
  $('#tracklist').on('click', '#move-up', function(e) {
    let sourceRow = $(this).closest('tr');
    if (sourceRow.index() != 0) {
     let destinationRow = sourceRow.prev();

     swapValues(sourceRow, destinationRow, '#artist');
     swapValues(sourceRow, destinationRow, '#title');
     swapValues(sourceRow, destinationRow, '#url');
    }
  })
});

function swapValues(element1, element2, selector) {
   let temp = $(element1).find(selector).val();
   $(element1).find(selector).val($(element2).find(selector).val())
   $(element2).find(selector).val(temp)
}