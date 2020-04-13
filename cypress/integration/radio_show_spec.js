describe('Home page', function() {

  beforeEach(() => {
    cy.visit('/')
  })

  it('Displays welcome message', function() {
    cy.contains('Welcome')
  })

  describe('Radio shows', function() {

    it('Lets users add new shows', () => {
      add_show('A new Show!', '2020-04-10')
      cy.url().should('eq', 'http://localhost:8080/')

      cy.get('[data-test=shows]')
        .first()
        .should('contain', 'A new Show!')
        .and('contain', '2020-04-10')
    })

    function add_show(name, date) {
      cy.get('[data-test=add-show]').click()
      cy.get('[data-test=name-input').type(name)
      cy.get('[data-test=date-input').type(date)
      cy.get('[data-test=submit').click()
    }

  })

})

