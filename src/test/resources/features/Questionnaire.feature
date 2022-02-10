Feature: Course

  Background:
    Given a teacher with login "steve"
    And a module named "Gestion de projet"
    And a questionnaire named "Quest1"

  Scenario: Teacher create course
    When "steve" creer questionnaire "MyQuestionnaire"
    Then QuestionnaireTest last request status is 200
    And Questionnaire "MyQuestionnaire" has been added