Feature: Get

  Background:
    Given a user with login "steve"
    And a module named "Gestion de projet"
    And a course named "Gestion"

  Scenario: user check all modules that he subscribe to
    When "steve" checks his modules
    Then return all modules names