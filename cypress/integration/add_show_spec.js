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
  it('has labeled input field for date', () => {
    cy.visit('http://localhost:8080/shows/add')

    cy.get('.new-date-label')
      .should('contain', 'Date:')

    const typedDate = '2020-04-10'
    cy.get('.new-date')
      .type(typedDate)
      .should('have.value', typedDate)
  })
})