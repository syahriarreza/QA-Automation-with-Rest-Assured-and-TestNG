Feature: User API - Register and Login

  Scenario: Register a new user
    Given I prepare a user registration with email "baba.1@example.com" and password "@dmin123"
    When I send a POST request to "/webhook/api/register"
    Then the response status should be 200
    And the response should contain email "baba.1@example.com"

  Scenario: Login with registered user
    Given I have registered user "baba.1@example.com" with password "@dmin123"
    When I send a POST request to "/webhook/api/login"
    Then the response should contain a valid token