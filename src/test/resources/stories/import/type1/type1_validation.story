Narrative:
Type1 files should be 'structurally valid'.

This means that data types should be correct, as well as integrity checks
such as transaction counts, group counts etc.

It does not include other 'content' validation such as reference data checks.


Scenario: a correctly formed file should validate without errors

Given an input dataset of
HH20140321
GHCLIENT0000
TX0ACC0     20140321000000077.27
TX0ACC1     20140321000000030.68
GF0000000002
FF00000001
When importing the data
Then there should be "0" file format errors


Scenario: invalid group count

Given an input dataset of
HH20140321
GHCLIENT0000
TX0ACC0     20140321000000077.27
TX0ACC1     20140321000000032.68
GF0000000003
FF00000001
When importing the data
Then there should be "1" file format error


Scenario: supplying a alpha instead of numeric

Given an input dataset of
HH20140321
GHCLIENT0000
TX0ACC0     20140321000000077.27
TX0ACC1     2014032100000003X.68
GF0000000002
FF00000001
When importing the data
Then there should be "2" file format errors
And file format errors should include "Group footer count of 1 does not match expected 2"
And file format errors should include "Invalid 'transaction' record at line 4"

