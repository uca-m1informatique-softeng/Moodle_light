Feature: Delete
  Background:
    Given a teacher with login "steve"
    And a student with login "paul"
    And a module named "Gestion de projet"

  Scenario: teacher delete student
    Given "steve" delete "paul"
    Then "paul" is not a student