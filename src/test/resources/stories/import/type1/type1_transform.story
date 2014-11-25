Narrative:
Transactions from type1 files should be stored in the database.


Scenario: transactions should be categorized into 'high' and 'low' depending on the amount

Given an input dataset of
HH20140321
GHCLIENT0000
TX0ACC0     20140321000000077.27
TX0ACC1     20140321000000030.68
TY0ACC1     20140321000000060.68
GF0000000003
FF00000001
When importing the data
Then the transactions should be categorized as
|amount|category|
|77.27 | high   |
|60.68 | high   |
|30.68 | low    |
