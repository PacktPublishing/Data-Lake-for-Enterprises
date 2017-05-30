# Data Lakes for Enterprises
This is the code repository for [Data Lakes for Enterprises](https://www.packtpub.com/big-data-and-business-intelligence/data-lakes-enterprises?utm_source=github&utm_medium=repository&utm_campaign=9781787281349), published by [Packt](https://www.packtpub.com/?utm_source=github). It contains all the supporting project files necessary to work through the book from start to finish.
## About the Book
The term "Data Lake" has recently emerged as a prominent term in the big data industry. Data scientists can make use of it in deriving meaningful insights that can be used by businesses to redefine or transform the way they operate. Lambda architecture is also emerging as one of the very eminent patterns in the big data landscape, as it not only helps to derive useful information from historical data but also correlates real-time data to enable business to take critical decisions. This book tries to bring these two important aspects — data lake and lambda architecture—together.

This book is divided into three main sections. The first introduces you to the concept of data lakes, the importance of data lakes in enterprises, and getting you up-to-speed with the Lambda architecture. The second section delves into the principal components of building a data lake using the Lambda architecture. It introduces you to popular big data technologies such as Apache Hadoop, Spark, Sqoop, Flume, and ElasticSearch. The third section is a highly practical demonstration of putting it all together, and shows you how an enterprise data lake can be implemented, along with several real-world use-cases. It also shows you how other peripheral components can be added to the lake to make it more efficient.


## Instructions and Navigation
All of the code is organized into folders. Each folder starts with a number followed by the application name. For example, Chapter02.



The code will look like the following:
```
agent.sources = spool-source
agent.sources.spool-source.type=spooldir
agent.sources.spool-source.spoolDir=/home/centos/flume-data
agent.sources.spool-source.interceptors=ts uuid

#Timestamp Interceptor Definition
agent.sources.spool-source.interceptors.ts.type=timestamp

#UUID Interceptor Definition
agent.sources.spool-source.interceptors.uuid.type=org.apache.flume.sink.solr.morphline.UUIDInterceptor$Builder
agent.sources.spool-source.interceptors.uuid.headerName=eventId
```

This book is for developers, architects and product/project owners, for realizing Lambda architecture based Data Lake for Enterprises. This book comprises of working examples to help the reader understand and observe various concepts around Data Lake and its basic implementation. In order to run the examples, one would need access to various open source softwares, required infrastructure and Development IDE. Specific efforts have been made to keep the examples simple and leverage commonly available frameworks and components. The operating system used for running these examples has been CentOS 7, but these examples can run on any flavour of Linux operating system.

## CentOS 7 VM Setup Guide
Below is the link to step-by-step guide to setup the CentOS 7 VM that would be required to run the examples ans source code discussed in this book. 
[CentOS 7 VM Setup Guide](VM-Setup-Guide.MD)

## Related Products
* [Data Lake Development with Big Data](https://www.packtpub.com/big-data-and-business-intelligence/data-lake-development-big-data?utm_source=github&utm_medium=repository&utm_campaign=9781785888083)

* [Spring: Developing Java Applications for the Enterprise](https://www.packtpub.com/web-development/spring-developing-java-applications-enterprise?utm_source=github&utm_medium=repository&utm_campaign=9781787127555)

* [Making Big Data Work for Your Business](https://www.packtpub.com/business/making-big-data-work-your-business?utm_source=github&utm_medium=repository&utm_campaign=9781783000982)

### Suggestions and Feedback
[Click here](https://docs.google.com/forms/d/e/1FAIpQLSe5qwunkGf6PUvzPirPDtuy1Du5Rlzew23UBp2S-P3wB-GcwQ/viewform) if you have any feedback or suggestions.
