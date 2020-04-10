describe('Input form', () => {
  it('has labeled input field for name', () => {
    cy.visit('http://localhost:8080/shows/add')

    cy.get('.new-name-label')
      .should('contain', 'Name:')

    const typedName = 'The first Show!'
    cy.get('.new-name')
      .type(typedName)
      .should('have.value', typedName)
  })
})