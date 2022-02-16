Feature: signup/in User
  Background:
    Given "dave" not exist
    And "paul" not exist


  Scenario: Student good signup
    When "dave" with email "dave@test.fr" and code "password" and is "ROLE_STUDENT" signup
    Then 2 last request status is 200
    And "dave" is registered like user

  Scenario: Teacher good signup
    When "paul" with email "paul@test.fr" and code "password" and is "ROLE_TEACHER" signup
    Then 2 last request status is 200
    And "paul" is registered like user

  Scenario: Student good sign in
    When "dave" with email "dave@test.fr" and code "password" and is "ROLE_STUDENT" signup
    Then user "dave" with password "password" sign in
    Then 2 last request status is 200
