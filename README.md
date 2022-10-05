# MinimumTask
1. Two web servers which communicates over HTTP protocol between them.
2. First web server is producer which produces some data on multiple threads (more than 5) and it sends these data from
multiple threds to the second web server.
3. Second server is consumer, which receives and consumes data from first server and populates shared resources, a queue
or stack with received data.
4. Second server also has multiple threads which extracts one element from shered resource and is sending that extracted
data element from second server to the first.
