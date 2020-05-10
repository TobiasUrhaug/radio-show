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
      const firstShow = {name:'The first show added', date: '2020-04-13'}
      const secondShow = {name:'The second show added', date: '2020-05-15'}

      cy.add_show(firstShow)

      cy.get('[data-test=create-show]').click()
      cy.get('[data-test=name-input').type(secondShow.name)
      cy.get('[data-test=date-input').type(secondShow.date)
      cy.get('[data-test=submit').click()

      cy.get('[data-test=show]')
        .first()
        .should('contain', secondShow.name)
        .and('contain', secondShow.date)
      cy.get('[data-test=show]')
        .eq(1)
        .should('contain', firstShow.name)
        .and('contain', firstShow.date)

      // Submitting an empty form should display error messages
      cy.get('[data-test=create-show]').click()
      cy.get('[data-test=submit').click()
      cy.url().should('eq', 'http://localhost:8080/shows/create')
      cy.get('[data-test=name-error]')
        .first()
        .should('not.be.empty')
      cy.get('[data-test=date-error')
        .first()
        .should('not.be.empty')
      cy.get('[data-test=cancel]').click()
      cy.url().should('eq', 'http://localhost:8080/')

    })

    describe('Show details', function() {

      it('Lets users see the details of a show', function() {
        const show = {name: 'Show me your details', date: '2020-05-17'}
        cy.add_show(show)
        cy.get('[data-test=show-details').first().click()

        cy.get('h1').should('contain.text', show.date).and('contain.text', show.name)
      })

      it('Lets users delete shows', function() {
        const showToBeDeleted = {name: 'To be deleted', date: '2020-05-19'}
        cy.add_show(showToBeDeleted)
        cy.get('[data-test=show-details]').first().click()
        cy.get('[data-test=delete]').first().click()
        cy.url().should('eq', 'http://localhost:8080/')
        cy.get('[data-test=shows')
          .should('not.contain', showToBeDeleted.name)
      })

      it('Lets users edit shows', function() {
        const originalShow = {name: 'To be edited', date: '2020-04-18'}
        const editedShow = {name: 'Edited name', date: '2020-04-19'}
        cy.add_show(originalShow)

        cy.get('[data-test=show-details]').first().click()
        cy.get('[data-test=edit]').first().click()
        cy.url().should('match', /shows\/[0-9]+\/update/)
        cy.get('[data-test=name-input')
          .should('have.value', originalShow.name)
          .clear()
          .type(editedShow.name)
        cy.get('[data-test=date-input')
          .should('have.value', originalShow.date)
          .clear()
          .type(editedShow.date)
        cy.get('[data-test=submit').click()

        cy.url().should('match', /shows\/[0-9]+/)
        cy.get('h1').should('contain.text', editedShow.date).and('contain.text', editedShow.name)

        cy.visit('/')
        cy.get('[data-test=shows')
          .should('not.contain', originalShow.name)
          .and('not.contain', originalShow.dDate)
        cy.get('[data-test=show]')
          .first()
          .should('contain', editedShow.name)
          .and('contain', editedShow.date)

        // Tests that the tracklist of a show is preserved after edit
        cy.get('[data-test=show-details]').first().click()
        cy.get('[data-test=add-tracks]').click()

        cy.get('[data-test=artist-input]').type('Artist')
        cy.get('[data-test=title-input').type('Title')
        cy.get('[data-test=add-track]').click()
        cy.get('[data-test=submit]').click()

        cy.get('[data-test=edit]').first().click()
        cy.get('[data-test=submit]').click()

        cy.get('[data-test=artist]').first().should('contain.text', 'Artist')
        cy.get('[data-test=title]').first().should('contain.text', 'Title')

        // Test cancel of edit
        const editCancelShow = {name: 'Edit was cancelled', date: '2020-04-23'}
        cy.add_show(editCancelShow)
        cy.get('[data-test=show-details]').first().click()
        cy.get('[data-test=edit]').first().click()
        cy.get('[data-test=name-input]')
          .clear()
          .type('This should not be persisted')
        cy.get('[data-test=date-input]')
          .clear()
          .type('2020-04-29')
        cy.get('[data-test=cancel]').click()
        cy.url().should('match', /shows\/[0-9]+/)
        cy.get('h1').should('contain.text', editCancelShow.name).and('contain.text', editCancelShow.date)
      })

      it('Lets users add tracks to the bottom of the tracklist', function() {
        cy.add_show({name: 'Show with a track list', date: '2020-05-18'})
        cy.get('[data-test=show-details]').first().click()
        cy.get('[data-test=add-tracks]').click()
        cy.url().should('match', /shows\/[0-9]+\/tracks\/create/)

        const firstTrack = {artist: 'DJ Great Software', title: 'BDD or go home!', url: 'https://www.example.com'}
        const secondTrack = {artist: 'DJ Second', title: 'Nature!', url: 'https://omtheorem.no'}
        const thirdTrack = {artist: 'DJ Three', title: 'Nuts!', url: 'https://askepott.no'}

        addToTracklist(firstTrack)
        cy.get('[data-test=artist-input]').should('have.value', '')
        cy.get('[data-test=title-input]').should('have.value', '')
        cy.get('[data-test=url-input]').should('have.value', '')

        addToTracklist(thirdTrack)
        addToTracklist(secondTrack)
        cy.get('[data-test=tracks]').eq(2).find('[data-test=move-up]').click()
        cy.get('[data-test=tracks]').eq(1).find('[data-test=move-up]').click()
        cy.get('[data-test=tracks]').eq(0).find('[data-test=move-down]').click()

        // This should not do anything
        cy.get('[data-test=tracks]').first().find('[data-test=move-up]').click()
        cy.get('[data-test=tracks]').last().find('[data-test=move-down]').click()

        cy.get('[data-test=artist]').then(artists => {
            expect(artists[0]).to.have.value(firstTrack.artist)
            expect(artists[1]).to.have.value(secondTrack.artist)
            expect(artists[2]).to.have.value(thirdTrack.artist)
        })
        cy.get('[data-test=title]').then(titles => {
            expect(titles[0]).to.have.value(firstTrack.title)
            expect(titles[1]).to.have.value(secondTrack.title)
            expect(titles[2]).to.have.value(thirdTrack.title)
        })
        cy.get('[data-test=url]').then(urls => {
          expect(urls[0]).to.have.value(firstTrack.url)
          expect(urls[1]).to.have.value(secondTrack.url)
          expect(urls[2]).to.have.value(thirdTrack.url)
        })

        cy.get('[data-test=submit]').click()
        cy.url().should('match', /shows\/[0-9]+/)

        cy.get('[data-test=tracklist').then(tracks => {
          assertRowContainsTrack(tracks[0], firstTrack)
          assertRowContainsTrack(tracks[1], secondTrack)
          assertRowContainsTrack(tracks[2], thirdTrack)
        })

      })

    })

  })

})

function assertRowContainsTrack(row, track) {
  expect(row).to.contain(track.artist)
  expect(row).to.contain(track.title)
  expect(row).to.contain(track.url)
}

function addToTracklist(track) {
  cy.get('[data-test=artist-input]').type(track.artist)
  cy.get('[data-test=title-input').type(track.title)
  cy.get('[data-test=url-input]').type(track.url)
  cy.get('[data-test=add-track]').click()
}

