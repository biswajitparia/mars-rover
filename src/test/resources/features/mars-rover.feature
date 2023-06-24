Feature: Rover activities
  Scenario Outline: Rover's landing
    When Rover "<roverCode>" lands in Mars at <x>,<y>,"<direction>"
    Examples: first table
      | roverCode | x   | y   | direction  |
      | R1        | 13  | 14  |  E         |
      | R2        | 23  | 24  |  N         |
      | R3        | 23  | 25  |  S         |
      | R4        | 33  | 34  |  N         |
      | R5        | 3   | 4   |  N         |
      | R6        | 3   | 2   |  S         |

  Scenario Outline: Rover's move
    Then Remote operator moves rover "<roverCode>" with command "<command>"
    Examples: first table
      | roverCode | command           |
      | R5        | r,r,l,r,f,b,f,f,f |


  Scenario Outline: Rover's current position check
    Then Remote operator retrieves rover "<roverCode>". Current position is <x>, <y>, "<direction>"
    Examples: first table
      | roverCode | x   | y   | direction  |
      | R5        | 3   | 3   |  S         |
