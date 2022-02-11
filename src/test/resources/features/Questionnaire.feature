Feature: Questionnaire

  Background:
    Given a teacher with login "steve"
    And a module named "Gestion-de-projet"
    And a questionnaire named "Quest1"

  Scenario: Teacher create course
    When "steve" create questionnaire "Quest1"
    Then QuestionnaireTest last request status is 200 or 500
    And Questionnaire "Quest1" has been added

  Scenario: Teacher add text question to Questionnaire
    When user "steve" create "text" question with question "Enonce1" and with answer "answ1"
    Then Question with question "Enonce1" exist