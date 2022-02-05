
Feature : Adding Question to Questionnaire

    Background :
        Given a teacher of name "Marcel" and with teacher id "123"
        And a module of name "UML S1" with module id "001"

    Scenario : adding a question to a questionnaire
        When "Marcel" add the question "1 + 1 = ?" : "2" to the questionnaire "UML Quiz 1", in the module "UML S1"
        Then there is the question "1 + 1 = ?" : "2" in the questionnaire "UML Quiz 1"