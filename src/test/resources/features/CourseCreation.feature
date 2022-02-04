Feature: Course Creation

  Background:
    Given a teacher of name "Marcel" and with teacher id "123"

  Scenario: a course creation
    When "Marcel" creates the course "UML intro" in the module "UML S1"
    Then There is the course "UML intro" in the right module