Feature: Questionnaire

  Background: un questionaire avec des question
    Given a teacher with login "steve"
    And a student with login "paul"
    And a module named "Gestion-de-projet"
    And a questionnaire named "Quest1"
    When user "steve" creates "text" question with content "Enonce1" and with answer "answ1"
    And user "steve" add question "Enonce1" to "Quest1"
    And user "steve" creates "qcm" question with content "Enonce2", answers "1 ok 2 false" and with answer 1
    And user "steve" add question "Enonce2" to "Quest1"


  Scenario:
    When user "paul" answer "Enonce1" with "text"
    Then Answer of "paul" is saved in "Enonce1"

  Scenario:
    When
