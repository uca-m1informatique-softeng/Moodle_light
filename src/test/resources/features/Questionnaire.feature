Feature: Questionnaire

  Background:
    Given a teacher with login "steve"
    And a student with login "paul"
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


  Scenario: Teacher add qestion to questionaire
    When user "steve" creates "text" question with content "Enonce1" and with answer "answ1"
    And user "steve" add question "Enonce1" to "Quest1"
    And "steve" registers "paul" to module "Gestion-de-projet"
    And "steve" adds questionnaire "Quest1" to module "Gestion-de-projet"
    Then "paul" finds question "Enonce1" is in questionaire "Quest1"

  Scenario: Teacher add questionnaire to module
    When "steve" creates questionnaire "Quest1"
    And "steve" adds questionnaire "Quest1" to module "Gestion-de-projet"
    Then "steve" finds questionnaire "Quest1" is in module "Gestion-de-projet"


  Scenario: student answer question text
    When user "steve" creates "text" question with content "Enonce1" and with answer "answ1"
    And user "paul" answer "Enonce1" with "text"
    Then Answer of "paul" is saved in "Enonce1"

  Scenario: student answer question qcm
    When user "paul" answer "Enonce2" with 1
    Then Answer of "paul" is saved in "Enonce2"

  Scenario: student answer Multianswerquestion
    When user "paul" answer multi "Enonce3" with
      | 1 |
      | 2 |
    Then Answer of "paul" is saved in "Enonce3"

  Scenario: student answer all questions and validate
    When user "paul" answer "Enonce1" with "answ1"
    And user "paul" answer "Enonce2" with 1
    And user "paul" answer multi "Enonce3" with
      | 1 |
      | 2 |
    And user "steve" add question "Enonce1" to "Quest1"
    And user "steve" add question "Enonce2" to "Quest1"
    And user "steve" add question "Enonce3" to "Quest1"
    Then user "paul" validate "Quest1" and get 2 points
