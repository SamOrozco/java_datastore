# Parser
---

*The parsers job is to take a path to a data file, read the contents 
of that file as rows, and then write that data to disc in a way that is conducive to querying.*

**File Format**

```
STB|TITLE|PROVIDER|DATE|REV|VIEW_TIME 
stb1|the matrix|warner bros|2014-04-01|4.00|1:30 
stb1|unbreakable|buena vista|2014-04-03|6.00|2:05 
stb2|the hobbit|warner bros|2014-04-02|8.00|2:45 
stb3|the matrix|warner bros|2014-04-02|4.00|1:05 
```


The parser first initializes, or confirms the initialization, of two directories, `.row` and `.col`.

The **.row** directory contains a single files for each row read from the data file. 
The row-files name is a hashed unique identifier(ID) that can be used to find the row. Tis makes looking a row up
by ID an O(1) operation. You simply check to see if the file `.row/<unique_hash_id>` exists. One key thing about the 
`.row` directory is the **.keys** file. The **.keys** files is a new-line separate file containing every unique row identifier for all rows. 
This file allows us to iterate all rows. 

Row Directory Example: 
```
-.row
-- .keys // important
-- 12111211
-- 14212411
-- 21141111
```
`.keys` file example: 
```
// row Ids
-2022486862
1415143381
-906050746
143403975
-906048824
...
```

Row File example
```
stb3
shrek
netflex
2014-06-20
12.1
2:20
```


The **.col** directory is used for writing the column specific data. The first thing that is done inside the `.col` directory
is to read the headers and create directories for each column to put their data. 

`.col` directory example: 
```
-.col
-- DATE
--- 4242123 // column value files (this is the hashed value)
-- PROVIDER
-- REV
-- STB
-- TITLE
-- VIEW
```

Column value file
```
// row keys
734247363
736121313
729507711
727688538
735051720
731381661
730554240
..
```



While parsing each row the column data is all grouped together by their unique **values**. 
A column value-file is a file with the file-name of the hashed value with the contents of new-line separated rowKeys. 
This makes looking for rows with a certain column value an O(1) operation.


This file structure allows for a persistent storage system that can be quickly queried.

**O(1) row lookup**
```java
if (rowFileExists(".row" + "/" + rowKey)) {
    return contentsAsString();
}
```



**O(1) filter value lookup**
```java
filter=DATE=2014-04-12
//hasfile 
int hash = Objects.hashCode("2014-04-12")
if (valueFileExists(".col" + "/" + "DATE" + "/" + hash)) {
    // read all row keys from here then go read result
}
``` 

The runtime complexity of a query in this case is R + 1,
 R being the size of the result set that need to be iterated and read from disc. 








 






   




