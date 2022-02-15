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
    And user "steve" creates "qcm" multi question with content "Enonce3", answers "1 ok 2 ok 3 false" and with answer
      | 1 |
      | 2 |
    And user "steve" add question "Enonce3" to "Quest1"

  Scenario: student answer question text
    When user "paul" answer "Enonce1" with "text"
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
    When user "paul" answer "Enonce1" with "text"
    And user "paul" answer "Enonce2" with 1
    And user "paul" answer multi "Enonce3" with
      | 1 |
      | 2 |
    Then user "paul" validate "Quest1" and get 2 points

