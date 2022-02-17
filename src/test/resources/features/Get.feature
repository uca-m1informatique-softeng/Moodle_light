Feature: Get

  Background:
    Given a user with login "steve"
    And a module named "Gestion-de-projet"
    And a module named "BDD"
    And a course named "Gestion"

  Scenario: user check all modules that he subscribe to
    When "steve" checks his modules
    Then return all modules names

  Scenario: A user gets the content of a course
    When "steve" registers to module "Gestion-de-projet"
    Then someone gets the content of the course "Gestion", then we get:
      | "chapter1" |
      | "chapter2" |
      | "chapter3" |
      | "chapter5" |

  Scenario: A user gets his modules
    When "steve" registers to module "Gestion-de-projet"
    And "steve" registers to module "BDD"
    Then "steve" gets his modules, then we get:
      | "Gestion-de-projet" |
      | "BDD" |