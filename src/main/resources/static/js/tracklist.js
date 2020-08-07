function addTrack() {
  let rowNumber = $('#tracklist tr').length;
  let artistCell = '<td><input name="tracks[' + rowNumber + '].artist" class="artist" type="text" value="" data-test="artist"/></td>'
  let titleCell = '<td><input name="tracks[' + rowNumber + '].title" class="title" type="text" value="" data-test="title"/></td>'
  let remixCell = '<td><input name="tracks[' + rowNumber + '].remix" class="remix" type="text" value="" data-test="remix"/></td>'
  let labelCell = '<td><input name="tracks[' + rowNumber + '].label" class="label" type="text" value="" data-test="label"/></td>'
  let urlCell = '<td><input name="tracks[' + rowNumber + '].url" class="url" type="text" value="" data-test="url" /></td>'
  let moveUpBtn = '<td><button type="button" data-test="move-up" class="move-up btn btn-outline-secondary">Move Up</button></td>'
  let moveDownBtn = '<td><button type="button" data-test="move-down" class="move-down btn btn-outline-secondary">Move Down</button></td>'
  let deleteBtn = '<td><button type="button" data-test="delete-track" class="delete btn btn-outline-secondary">Delete</button></td>'

  let row =
    '<tr id="tracks" data-test="tracks">'
    + artistCell
    + titleCell
    + remixCell
    + labelCell
    + urlCell
    + moveUpBtn
    + moveDownBtn
    + deleteBtn
    + '</tr>';
  $('#tracklist').append(row);
  $('.artist').last().focus();
}

$(function(){
  $('#tracklist').on('click', '.move-up', function(e) {
    let row = $(this).closest('tr');
    if (row.index() > 0) {
      swapRows(row, row.prev());
    }
  })
  $('#tracklist').on('click', '.move-down', function(e) {
    let row = $(this).closest('tr');
    if (row.index() < $('#tracklist tr').length - 1) {
      swapRows(row, row.next());
    }
  })
  $('#tracklist').on('click', '.delete', function(e) {
    $(this).closest('tr').remove();
  })
  $('#tracklist').on('keypress', function(e) {
    if (e.keyCode === 13) {
      e.preventDefault()
    }
  })
});

function swapRows(row1, row2) {
  swapValues(row1, row2, '.artist');
  swapValues(row1, row2, '.title');
  swapValues(row1, row2, '.remix');
  swapValues(row1, row2, '.label');
  swapValues(row1, row2, '.url');
}

function swapValues(element1, element2, selector) {
   let temp = $(element1).find(selector).val();
   $(element1).find(selector).val($(element2).find(selector).val())
   $(element2).find(selector).val(temp)
}