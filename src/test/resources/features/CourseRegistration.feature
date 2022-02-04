Feature : registration to a course

    Background :
        Given a teacher of name "Marcel" and with teacher id "123"
        And a teacher of name "Jean" and with teacher id "456"
        And a student of name "Kevin" and with student id "0001"
        And a course of name "UML intro" and with course id "001"
        And a module of name "UML S1" with module id "001"

    Scenario : teacher register to a course
        When "Marcel" register to the course "UML intro"
        Then "Marcel" is the teacher of the course "UML intro"

    Scenario : two teacher on the same course
        When "Marcel" register to the course "UML intro"
        And "Jean" register to the course "UML intro"
        Then "Marcel" is the teacher of the course "UML intro"
        And there is an error

    Scenario : student register to a course
        When "Kevin" register to the course "UML intro"
        Then