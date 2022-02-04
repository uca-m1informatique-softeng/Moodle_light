Feature : Questionnaire Creation

    Background :
        Given a teacher of name "Marcel" and with teacher id "123"
        And a module of name "UML S1" with module id "001"

    Scenario : a questionnaire creation
        When "Marcel" creates the questionnaire "UML Quiz 1" in the module "UML S1"
        Then there is the questionnaire "UML Quiz 1" in the right module