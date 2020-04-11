describe('Home page', function() {

  beforeEach(() => {
    cy.visit('/')
  })

  it('Displays welcome message', function() {
    cy.contains('Welcome')
  })

  describe('Radio shows', function() {

    it('Contains a list of previous shows', function() {
      var radioShows = cy.get('#radio-shows')
      radioShows.find('h2').first().should('contain', 'Radio shows')
      radioShows.get('ul>li').eq(0).should('contain', 'Transmission#1')
      radioShows.get('ul>li').eq(1).should('contain', 'Transmission#2')
    })

    it('Add new show button redirects to add show page', function() {
        cy.get('#radio-shows').contains('Create new show').click()
        cy.url().should('contain', '/shows/add')
    })

  })

})

