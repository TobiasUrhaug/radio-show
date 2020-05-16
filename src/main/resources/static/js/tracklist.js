function addTrack() {
  let artist = $('#artist-input').val();
  let title = $('#title-input').val();
  let url = $('#url-input').val();

  $('#artist-input').val('');
  $('#title-input').val('');
  $('#url-input').val('');

  let rowNumber = $('#tracklist tr').length - 1;
  let artistCell = '<td><input name="tracks[' + rowNumber + '].artist" id="artist" type="text" value="' + artist + '" data-test="artist"/></td>'
  let titleCell = '<td><input name="tracks[' + rowNumber + '].title" id="title" type="text" value="' + title + '" data-test="title"/></td>'
  let urlCell = '<td><input name="tracks[' + rowNumber + '].url" id="url" type="text" value="' + url + '" data-test="url" /></td>'
  let moveUpBtn = '<td><button type="button" data-test="move-up" id="move-up" class="btn btn-outline-secondary">Move Up</button></td>'
  let moveDownBtn = '<td><button type="button" data-test="move-down" id="move-down" class="btn btn-outline-secondary">Move Down</button></td>'

  let row =
    '<tr id="tracks" data-test="tracks">'
    + artistCell
    + titleCell
    + urlCell
    + moveUpBtn
    + moveDownBtn
    + '</tr>';
  $(row).insertBefore('#inputs');

  $('#artist-input').focus();
}

$(function(){
  $('#tracklist').on('click', '#move-up', function(e) {
    let row = $(this).closest('tr');
    if (row.index() > 0) {
      swapRows(row, row.prev());
    }
  })
  $('#tracklist').on('click', '#move-down', function(e) {
    let row = $(this).closest('tr');
    if (row.index() < $('#inputs').index() - 1) {
      swapRows(row, row.next());
    }
  })
  $('#inputs').on('keypress', function(e) {
    if (e.keyCode === 13) {
      e.preventDefault()
      addTrack()
    }
  })
});

function swapRows(row1, row2) {
  swapValues(row1, row2, '#artist');
  swapValues(row1, row2, '#title');
  swapValues(row1, row2, '#url');
}

function swapValues(element1, element2, selector) {
   let temp = $(element1).find(selector).val();
   $(element1).find(selector).val($(element2).find(selector).val())
   $(element2).find(selector).val(temp)
}