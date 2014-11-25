Narrative:
Transactions from type1 files should be stored in the database.


Scenario: transactions should be stored

Given an input dataset of
HH20140321
GHCLIENT0000
TX0ACC0     20140321000000077.27
TX0ACC1     20140321000000030.68
GF0000000002
FF00000001
When importing the data
Then there should be "2" transactions stored
