Feature: Course

  Background:
    Given a teacher with login "steve"
    And a module named "Gestion de projet"
    And a cours named "Gestion"

  Scenario: Teacher create course
    When "steve" creer cours "mycour"
    Then CourseTest last request status is 200
    And cours "Gestion" has been added

  Scenario: Teacher adds the cours Gestion
    When "steve" adds a course with name "Gestion" in module "Gestion de projet"
    Then CourseTest last request status is 200

  Scenario: Teacher adds a text in Gestion
    When "steve" adds a course with name "Gestion" in module "Gestion de projet"
    And "steve" add to "Gestion" a text "shapter1"
    Then CourseTest last request status is 200

  Scenario:
    When "steve" adds a course with name "Gestion" in module "Gestion de projet"
    And  "steve" add to "Gestion" a text "shapter1"
    And  "steve" add to "Gestion" a text "shapter1"
    Then CourseTest last request status is 400