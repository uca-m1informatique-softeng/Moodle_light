Feature: Get

  Background:
    Given a user with login "steve"
    And a module named "Gestion-de-projet"
    And a course named "Gestion"
    And "steve" student in "Gestion-de-projet"

  Scenario: user check all modules that he subscribe to
    When "steve" checks his modules
    Then return all modules names
    |Gestion-de-projet|

  Scenario: teacher check list of students subscibed  in a module
    When "steve" checks his module named "Gestion-de-projet"
    Then return all users names
    |{"0":"steve"}|
