Feature: Product Management

  Background: Navigate to Test Page
    Given Navigate to Test Page

  @Regression @Smoke
  Scenario Outline: Filter Products by Category
    Given User opens form Filter
    When User filters by "<criteria>" as "<value>"
    Then Verify API response contains 100 objects
    And Verify that filter result is correct with "<criteria>" and "<value>"

    Examples:
      | criteria   | value      |
      | Categories | Category 4 |

