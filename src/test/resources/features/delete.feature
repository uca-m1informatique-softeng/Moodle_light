Feature: Delete
  Background:
    Given a teacher with login "steve"
    And a student with login "paul"
    And a module named "Gestion-de-projet"

  Scenario: teacher delete student
    Given "steve" delete "paul"
    Then "paul" is not a student

  Scenario: teacher delete cours
    Given a cours named "Gestion"
    When "steve" delete cours "Gestion"
    Then deleteTest last request status is 200

  Scenario: teacher delete Questionaire
    Given a questionnaire named "Quest1"
    When "steve" delete questionaire "Quest1"
    Then deleteTest last request status is 200

  Scenario: teacher enleve Cours du Module
    Given a cours named "Gestion"
    And "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    When "steve" remouve cours "Gestion" de module "Gestion-de-projet"
    Then deleteTest last request status is 200
