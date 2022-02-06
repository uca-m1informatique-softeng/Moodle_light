Feature: signup/in User

  Scenario: Student good signup
    When "dave" with email "dave@test.fr" and code "password" and is "ROLE_STUDENT" signup
    Then 2 last request status is 200

  Scenario: Student good signin
