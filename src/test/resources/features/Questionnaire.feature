Feature: Questionnaire

  Background:
    Given a teacher with login "steve"
    And a module named "Gestion-de-projet"
    And a questionnaire named "Quest1"

  Scenario: Teacher create course
    When "steve" creates questionnaire "Quest1"
    Then QuestionnaireTest last request status is 200 or 500
    And Questionnaire "Quest1" has been added

  Scenario: Teacher add text question
    When user "steve" creates "text" question with content "Enonce1" and with answer "answ1"
    Then Question with content "Enonce1" exist

  Scenario: Teacher add qcm question
    When user "steve" creates "qcm" question with content "Enonce2", answers "1 ok 2 false" and with answer 1
    Then Question with content "Enonce2" exist

  Scenario: Teacher add multiqcm question
    When user "steve" creates "qcm" multi question with content "Enonce3", answers "1 ok 2 ok 3 false" and with answer
      | 1 |
      | 2 |
    Then Question with content "Enonce3" exist

  Scenario: Teacher gets a questionnaire
    When "steve" creates questionnaire "Quest1"
    And "steve" registers to module "Gestion-de-projet"
    And "steve" adds questionnaire "Quest1" to module "Gestion-de-projet"
    And "steve" sends a get request for questionnaire "Quest1"
    Then "steve" gets the questionnaire with name "Quest1"

  Scenario: Teacher add module to questionaire
    When "steve" creates questionnaire "Quest1"
    And "steve" adds questionnaire "Quest1" to module "Gestion-de-projet"
    Then "steve" finds questionnaire "Quest1" is in module "Gestion-de-projet"

  Scenario: Teacher add qestion to questionaire
    When user "steve" creates "text" question with content "Enonce1" and with answer "answ1"
    And user "steve" add question "Enonce1" to "Quest1"
    And "steve" registers "paul" to module "Gestion-de-projet"
    And "steve" adds questionnaire "Quest1" to module "Gestion-de-projet"
    Then "paul" finds question "Enonce1" is in questionaire "Quest1"
