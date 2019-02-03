# Query

The Query application expects the current working directory looks like this:
 
```
.col
    - dirs for all column name
        - list files with name of values for column
            - each file has a pointer to all rows that have value for column
.row
    .keys // files listing all avail row keys
    - list of files with rowkey as files name
```

The current available options for the query tool: 

Flag: `-s select flag e.g. STB,TITLE` only select STB, TITLE

Flag: `-o order flag e.g. STB,TITLE` order by STB, TITLE

Flag: `-f filter flag e.g. DATE=2014-04-10` select only rows where Date=2014-04-10


The query tool provides a O(1) lookup for rowKey and an O(1) lookup for all rows with a given value for a column.
