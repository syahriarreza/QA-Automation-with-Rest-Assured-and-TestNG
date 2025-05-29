Feature: User API - Register and Login

  Scenario: Register a new user
    Given I prepare a user registration with email "TEST_USERNAME" and password "TEST_PASSWORD"
    When I register the user
    Then the response status should be 200
    And the response should contain email "TEST_USERNAME"

  Scenario: Login with registered user
    Given I have registered user "TEST_USERNAME" with password "TEST_PASSWORD"
    When I login
    Then the response should contain a valid token