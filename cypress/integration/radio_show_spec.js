describe('Home page', function() {

  it('Displays welcome message', function() {
    cy.visit('http://localhost:8080')
    cy.contains('Welcome')
  })

  it('Contains a list of previous shows', function() {
    cy.visit('http://localhost:8080')

    var radioShows = cy.get('#radio-shows')
    radioShows.find('h2').first().should('contain', 'Radio shows')
    radioShows.get('ul>li').eq(0).should('contain', 'Transmission#1')
    radioShows.get('ul>li').eq(1).should('contain', 'Transmission#2')
  })

})

