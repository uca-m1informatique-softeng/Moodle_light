Feature: Course

  Background: :
    Given a teacher with username "steve"
    And a resource of type cours with id 1
    And and list of texts:
      | shapter1 |
      | shapter2 |
      | shapter3 |
      | shapter4 |

  Scenario: the teacher adds text into the cours
    When "steve" adds text "shapter5" to the cours
    Then last request status is 200
    And "shapter5" has been added to the cours