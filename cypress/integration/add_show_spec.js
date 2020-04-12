describe('Input form', () => {
  beforeEach(() => {
    cy.visit('/shows/add')
  })
  it('has labeled input field for name', () => {
    cy.get('.new-name-label')
      .should('contain', 'Name:')

    const typedName = 'The first Show!'
    cy.get('[data-cy=name-input]')
      .type(typedName)
      .should('have.value', typedName)
  })
  it('has labeled input field for date', () => {
    cy.get('.new-date-label')
      .should('contain', 'Date:')

    const typedDate = '2020-04-10'
    cy.get('[data-cy=date-input]')
      .type(typedDate)
      .should('have.value', typedDate)
  })
  it('has a submit button', () => {
    cy.get('[data-cy=submit]')
  })
})