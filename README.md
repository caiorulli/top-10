# top-10

Choose your top 10 something! A very small library to help you make an ELO ranking of anything at all. I use it to order my top 10 games list. :)
It accompanies a simple terminal application so you can test it with simple CSV files.

## Using it as a library

... to be continued

## Using it as a terminal application

To start a top 10 ranking, think of all possible entries first.
If you are writing a top 10 games like me, think up of many good games you have played through the years.
Then, write them down in a CSV in the same folder as the jar. They should each fill a line, as in the [example](https://github.com/caiorulli/top-10/blob/master/sample.csv).
If you've named it `games.csv`, then run it with the [Clojure CLI tool](https://clojure.org/guides/deps_and_cli):

```bash
clojure -M:run games.csv
```
    
You will be repeatedly prompted to choose between one of random two entries.
After each result, the software will update the ratings using the [ELO Rating](https://www.geeksforgeeks.org/elo-rating-algorithm/).
You will also be able to save your progress into a local `results.csv` file and finish the session printing the resulting Top 10.

```
Which of these do you choose?
1) Game A
2) Game B
3) Save and print top 10
```

Later on, you can reload the saved progress by running the program without arguments.

```bash
clojure -M:run
```

That's about it. Have fun!

## License

Copyright © 2020

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
