Feature: Course

  Background:
    Given a teacher with login "steve"
    And a module named "Gestion-de-projet"
    And a course named "Gestion"

  Scenario: Teacher create course
    When "steve" create course "NewCourse"
    Then CourseTest last request status is 200 or 500
    And course "Gestion" has been added

  Scenario: Teacher adds the course Gestion
    When "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    Then CourseTest last request status is 200 or 500
    Then "steve" finds the course "Gestion" is in "Gestion-de-projet"

  Scenario Outline: Teacher adds 3 texts in the content of Gestion
    When "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    And "steve" add to "Gestion" a text <text>
    Then CourseTest last request status is 200 or 500
    Examples:
      | text |
      | "chapter1" |
      | "chapter2" |
      | "chapter3" |

  Scenario Outline: No multiple identical Course
    When "steve" adds a course with name "Gestion" in module "Gestion-de-projet"
    And  "steve" add to "Gestion" a text <text>
    Examples:
      | text       |
      | "chapter5" |
      | "chapter5" |


  Scenario: Teacher gets the content of a course
    Then "steve" gets the content of the course "Gestion", then we get:
      | "chapter1" |
      | "chapter2" |
      | "chapter3" |
      | "chapter5" |

    Scenario: A teacher creates a course then deletes it
      When "steve" create course "Mathematics"
      And "steve" deletes the course "Mathematics"
      Then course "Mathematics" does not exist
