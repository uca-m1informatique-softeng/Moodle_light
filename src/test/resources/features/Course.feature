Feature: Course

  Background:
    Given a teacher with login "steve"
    And a module named "Gestion-de-projet"
    And a cours named "Gestion"

  Scenario: Teacher create course
    When "steve" creer cours "mycour"
    Then CourseTest last request status is 200 or 500
    And cours "Gestion" has been added

  Scenario: Teacher adds the cours Gestion
    When "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    Then CourseTest last request status is 200 or 500

  Scenario Outline: Teacher adds 3 texts in the content of Gestion
    When "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    And "steve" add to "Gestion" a text <text>
    Then CourseTest last request status is 200 or 500
    Examples:
      | text |
      | "shapter1" |
      | "shapter2" |
      | "shapter3" |

  Scenario Outline: No multiple identical Course
    When "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    And  "steve" add to "Gestion" a text <text>
    Examples:
      | text       |
      | "shapter5" |
      | "shapter5" |


  Scenario: Teacher gets the content of a course
    Then "steve" gets the content of the course "Gestion", then we get:
      | shapter1 |
      | shapter2 |
      | shapter3 |
      | shapter5 |

    Scenario: A teacher creates a course then deletes it
      When "steve" creer cours "Mathematics"
      And "steve" deletes the course "Mathematics"
      Then course "Mathematics" does not exist
