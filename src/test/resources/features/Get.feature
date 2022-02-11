Feature: Get

  Background:
    Given a user with login "steve"
    And a module named "Gestion de projet"
    And a cours named "Gestion"

  Scenario: user check all modules that he subsccribe to
    When "steve" check his modules
    Then return all modules names