Feature: Object API - Create, Update, and Delete

  Scenario: Create, Update, and Delete an object
    Given I prepare a new object with name "Laptop QA"
    When I send a POST request to "/webhook/api/objects"
    Then the response status should be 200
    And I save the object ID

    When I update the object name to "Laptop QA Updated"
    Then the response status should be 200

    When I delete the object
    Then the response status should be 200