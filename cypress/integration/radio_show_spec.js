describe('Home page', function() {
  it('Displays welcome message', function() {
    cy.visit('http://localhost:8080')
    cy.contains('Welcome')
  })
})

