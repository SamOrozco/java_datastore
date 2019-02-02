# Parser
---

*The parsers job is to take in a path to a file, read the contents 
of that file as rows, and then write that data to disc in a way that is appropriate for querying.*


Process of the parser:
- read lines from files and add to Threadsafe queue
- spawn a process that will read lines from queue and process
- write data to disk to be queried later


The Parser uses a Blocking queue as the thread safe communication 
between our read thread(main thread reading lines from file), and the process thread.
 
The process thread pull rows out of the queue if any are available. It will split split the data
into their respective columns and prepare the data for writing to disk.

The data is then written to disc in way that is really beneficial for exact match lookups.

The output of the parse should be: 
```
.col
    - dirs for all column name
        - list files with name of values for column
            - each file has a pointer to all rows that have value for column
.row
    .keys // files listing all avail row keys
    - list of files with rowkey as files name
```



   




