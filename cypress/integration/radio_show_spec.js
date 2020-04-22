describe('Home page', function() {

  beforeEach(() => {
    cy.visit('/')
  })

  it('Displays welcome message', function() {
    cy.contains('Welcome')
  })

  describe('Navbar', function() {

    it('Is visible with a brand', function() {
      cy.get('[data-test=navbar]')
        .should('be.visible')
        .get('.navbar-brand')
        .should('not.be.empty')
    })

    it('Has home element navigating to root', function() {
      cy.get('[data-test=create-show').click()
      cy.get('[data-test=navbar-home').click()
      cy.url()
        .should('eq', 'http://localhost:8080/')
    })

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

      add_show('', '')
      cy.url().should('eq', 'http://localhost:8080/shows/create')
      cy.get('[data-test=name-error]')
        .first()
        .should('not.be.empty')
      cy.get('[data-test=date-error')
        .first()
        .should('not.be.empty')

    })

    describe('Show details', function() {

      it('Lets users see the details of a show', function() {
        add_show('Show me your details', '2020-05-17')
        cy.get('[data-test=show-details').first().click()
        cy.get('[data-test=name]')
          .should('have.value', 'Show me your details')
        cy.get('[data-test=date]')
          .should('have.value', '2020-05-17')
      })

      it('Lets users delete shows', function() {
        add_show('To be Deleted', '2020-04-17')
        cy.get('[data-test=show-details]').first().click()
        cy.get('[data-test=delete]').first().click()
        cy.url().should('eq', 'http://localhost:8080/')
        cy.get('[data-test=shows')
          .should('not.contain', 'To be Deleted')
      })

      it('Lets users edit shows', function() {
            const originalName = 'To be edited'
            const originalDate = '2020-04-18'
            const editedName = 'Edited name'
            const editedDate = '2020-04-19'

            add_show(originalName, originalDate)

            cy.get('[data-test=show-details]').first().click()
            cy.get('[data-test=edit]').first().click()
            cy.url().should('contain', '/shows/update')
            cy.get('[data-test=name-input')
              .should('have.value', originalName)
              .clear()
              .type(editedName)
            cy.get('[data-test=date-input')
              .should('have.value', originalDate)
              .clear()
              .type(editedDate)
            cy.get('[data-test=submit').click()

            cy.url().should('eq', 'http://localhost:8080/')
            cy.get('[data-test=shows')
              .should('not.contain', originalName)
              .and('not.contain', originalDate)
            cy.get('[data-test=show]')
              .first()
              .should('contain', editedName)
              .and('contain', editedDate)
          })

          it('Lets users add tracks to a track list', function() {
            add_show('Show with a track list', '2020-05-18')
            cy.get('[data-test=show-details]').first().click()
            cy.get('[data-test=tracklist]')
              .should('not.be.empty')
          })

    })

    function add_show(name, date) {
      cy.get('[data-test=create-show]').click()
      if (name !== "") {
        cy.get('[data-test=name-input').type(name)
      }
      if (date !== "") {
        cy.get('[data-test=date-input').type(date)
      }
      cy.get('[data-test=submit').click()
    }

  })

})

