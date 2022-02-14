Feature: Register Teacher/Student

  Background:

    Given clean
    And a teacher with login "steve"
    And a teacher with login "sarah"
    And a student with login "paul"
    And a student with login "mari"
    And a module named "Gestion de projet"



  Scenario: Register Teacher
    When "steve" registers to module "Gestion-de-projet"
    Then last request status is 200
    And "steve" is registered to module "Gestion-de-projet"

  Scenario: Register Second Teacher

    When "sarah" registers to module "Gestion-de-projet"
    And "steve" registers to module "Gestion-de-projet"
    Then last request status is 400
    And "sarah" is registered to module "Gestion-de-projet"
    And "steve" is not registered to module "Gestion-de-projet"

    Scenario: Teacher register Student
      When "sarah" registers to module "Gestion-de-projet"
      And "sarah" registers "paul" to module "Gestion-de-projet"
      Then last request status is 200
      And "sarah" is registered to module "Gestion-de-projet"
      And "paul" is registered to module "Gestion-de-projet"

    Scenario: Student register Student
      When "sarah" registers to module "Gestion-de-projet"
      And "sarah" registers "paul" to module "Gestion de projet"
      And "paul" registers "mari" to module "Gestion de projet"
      Then exception in request occurs
      And "sarah" is registered to module "Gestion de projet"
      And "paul" is registered to module "Gestion de projet"
      And "mari" is not registered to module "Gestion de projet"

    Scenario: Teacher register it self again
      When "sarah" registers to module "Gestion de projet"
      And "sarah" registers to module "Gestion de projet"
      Then last request status is 200
      And "sarah" is registered to module "Gestion de projet"

    Scenario: Teacher not registered registre Student
      When "sarah" registers "paul" to module "Gestion de projet"
      Then last request status is 400

    Scenario: Teacher not registered Teacher
      When "sarah" registers "steve" to module "Gestion de projet"
      Then last request status is 400

    Scenario: Teacher add 2 students
      When "sarah" registers to module "Gestion de projet"
      And "sarah" registers "paul" to module "Gestion de projet"
      And "sarah" registers "mari" to module "Gestion de projet"
      Then last request status is 200
      And "sarah" is registered to module "Gestion de projet"
      And "paul" is registered to module "Gestion de projet"
      And "mari" is registered to module "Gestion de projet"
