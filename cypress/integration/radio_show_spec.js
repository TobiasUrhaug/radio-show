describe('Home page', function() {

  beforeEach(() => {
    cy.visit('/')
  })

  it('Displays welcome message', function() {
    cy.contains('Welcome')
  })

  describe('Radio shows', function() {

    it('Lets users add new shows to the top of the list', () => {
      add_show('A new Show!', '2020-04-10')
      cy.url().should('eq', 'http://localhost:8080/')

      cy.get('[data-test=show]')
        .first()
        .should('contain', 'A new Show!')
        .and('contain', '2020-04-10')

      add_show('The second show added', '2020-05-14')
      cy.get('[data-test=show]')
        .first()
        .should('contain', 'The second show added')
        .and('contain', '2020-05-14')
      cy.get('[data-test=show]')
        .eq(1)
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

