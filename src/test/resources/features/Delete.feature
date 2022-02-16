Feature: Delete
  Background:
    Given a teacher with login "steve"
    And a student with login "paul"
    And a module named "Gestion-de-projet"

  Scenario: teacher delete student
    Given "steve" delete "paul"
    Then "paul" is not a student

  Scenario: teacher delete course
    Given a course named "Gestion"
    When "steve" deletes the course "Gestion"
    Then CourseTest last request status is 200
    And course "Gestion" does not exist

  Scenario: teacher delete Questionnaire
    Given a questionnaire named "Quest1"
    When "steve" delete questionnaire "Quest1"
    Then deleteTest last request status is 200

  Scenario: teacher delete cours that is connected to a module
    Given a course named "Gestion"
    And "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    When "steve" deletes the course "Gestion"
    Then CourseTest last request status is 200
    And course "Gestion" does not exist

  Scenario: teacher delete text from cours

  Scenario: teacher delete question

  Scenario: teacher delete question that is connected to a questionaire