describe('Home page', function() {

  beforeEach(() => {
    cy.visit('/')
  })

  it('Displays welcome message', function() {
    cy.contains('Welcome')
  })

  describe('Radio shows', function() {

    it('Lets users add a new show', () => {
      add_show('A new Show!', '2020-04-10')
      cy.url().should('eq', 'http://localhost:8080/')

      cy.get('td>span')
        .eq(0)
        .should('contain', 'A new Show!')

      cy.get('td>span')
        .eq(1)
        .should('contain', '2020-04-10')
    })

    function add_show(name, date) {
      cy.get('[data-cy=add-show]').click()
      cy.get('[data-cy=name-input').type(name)
      cy.get('[data-cy=date-input').type(date)
      cy.get('[data-cy=submit').click()
    }

  })

})

