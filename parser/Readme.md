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
The row-files name is a hashed unique identifier(ID) that can be used to find the row. The is makes looking a row up
by ID an O(1) operation. You simple check to see if the file `.row/<unique_hash_id>` exists.  




   




