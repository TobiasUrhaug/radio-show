describe('Home page', function() {

  beforeEach(() => {
    cy.visit('/')
  })

  it('Displays welcome message', function() {
    cy.contains('Welcome')
  })

  describe('Radio shows', function() {

    it('Contains a list of previous shows', function() {
      add_show('Transmission#1', '2020-01-13')
      add_show('Transmission#2', '2020-02-15')


      const firstRow = cy.get('tr')
        .eq(0)

      firstRow.get('td>span')
        .eq(0)
        .should('contain', 'Transmission#1')

      cy.get('td>span')
        .eq(1)
        .should('contain', '2020-01-13')

//      var radioShows = cy.get('#radio-shows')
//      radioShows.find('h2').first().should('contain', 'Radio shows')
//      radioShows.get('ul>li').eq(0).should('contain', 'Transmission#1')
//      radioShows.get('ul>li').eq(1).should('contain', 'Transmission#2')
    })

    it('Lets users add a new show', () => {
      cy.get('[data-cy=add-show]').click()
      cy.get('[data-cy=name-input').type('A new show!')
      cy.get('[data-cy=date-input').type('2020-04-10')
      cy.get('[data-cy=submit').click()
      cy.url().should('eq', 'http://localhost:8080/')

      cy.get('td>span')
        .eq(0)
        .should('contain', 'A new show!')

      cy.get('td>span')
        .eq(1)
        .should('contain', '2020-04-10')
    })

  })

})

