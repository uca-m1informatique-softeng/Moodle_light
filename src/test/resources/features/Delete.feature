Feature: Delete
  Background:
    Given a teacher with login "steve"
    And a student with login "paul"
    And a module named "Gestion-de-projet"
    And a course named "Gestion"

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
    Then course "Quest1" does not exist

  Scenario: teacher delete cours that is connected to a module
    Given a course named "Gestion"
    And "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    When "steve" deletes the course "Gestion"
    Then CourseTest last request status is 200
    And course "Gestion" does not exist

  Scenario: teacher delete question
    When user "steve" creates "text" question with content "Enoncesteve" and with answer "answ1"
    And user "steve" delete question "Enoncesteve"
    Then course "Enoncesteve" does not exist


  Scenario: teacher delete text from cours
    When "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    And "steve" add to "Gestion" a text "stevetext"
    And "steve" delete "stevetext" from cours "Gestion"
    Then "stevetext" not existe in "Gestion"
    
  Scenario: teacher delete question that is connected to a questionaire
    When user "steve" creates "text" question with content "Enoncesteve" and with answer "answ1"
    And user "steve" add question "Enoncesteve" to "Quest1"
    And user "steve" delete question "Enoncesteve"
    Then course "Enoncesteve" does not exist