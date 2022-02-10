Feature: Questionnaire

  Background:
    Given a teacher with login "steve"
    And a module named "Gestion de projet"
    And a questionnaire named "Quest1"
    And a questionnaire named "Quest1" in the module "Gestion de projet" does not exist

  Scenario: Teacher create course
    When "steve" creer questionnaire "Quest1"
    Then QuestionnaireTest last request status is 200
    And Questionnaire "Quest1" has been added