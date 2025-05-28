Feature: User API - Register and Login

  Scenario: Register a new user
    Given I prepare a user registration with email "reza.007@example.com" and password "@dmin123"
    When I send a POST request to "/webhook/api/register"
    Then the response status should be 200
    And the response should contain email "reza.007@example.com"

  Scenario: Login with registered user
    Given I have registered user "reza.007@example.com" with password "@dmin123"
    When I send a POST request to "/webhook/api/login"
    Then the response should contain a valid token