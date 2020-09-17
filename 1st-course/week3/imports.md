## Forms of imports

import week3.Rational           // import Rational                      - named imports
import week3.{Rational, Hello}  // import Rational and Hello            - named imports
import week3._                  // import everything in package week3   - wildcard imports

## Automatically imported
- All members of package scala
- All members of package java.lang
- All members of the singleton object scala.Predef

- Example 

|imported| Origin                |
|------  |---------------------  |
|Int     | scala.Int             |
|Boolean | scala.Boolean         |
|Object  | java.lang.Object      |
|require | scala.Predef.require  |
|assert  | scala.Predef.assert   |