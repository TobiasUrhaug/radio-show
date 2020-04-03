describe('Radio test', function() {
  it('Should display welcome message', function() {
    cy.visit('http://localhost:8080')
    cy.contains('Hello!')
  })
})

