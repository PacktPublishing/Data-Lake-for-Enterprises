# Effective Amazon Machine Learning
This is the code repository for [Effective Amazon Machine Learning](https://www.packtpub.com/big-data-and-business-intelligence/effective-amazon-machine-learning?utm_source=github&utm_medium=repository&utm_campaign=9781785883231), published by [Packt](https://www.packtpub.com/?utm_source=github). It contains all the supporting project files necessary to work through the book from start to finish.
## About the Book
Predictive Analytics is a complex domain requiring coding skills, an understanding of the mathematical concepts underlying Machine Learning algorithms, and the ability to create compelling data visualizations. Following AWS simplifying Machine learning, this book will help you bring predictive analytics projects to fruition in three easy steps: data preparation, model tuning, and model selection.

This book will introduce you to the Amazon Machine Learning platform and will implement core Data Science concepts such as classification, regression, regularization, overfitting, model selection, and evaluation. Furthermore, you will learn to leverage the Amazon Web Service ecosystem for extended access to data sources; implement real- time predictions; and run Amazon Machine Learning projects via the command line and the python SDK. 


## Instructions and Navigation
All of the code is organized into folders. Each folder starts with a number followed by the application name. For example, Chapter02.



The code will look like the following:

Code words in text, database table names, folder names, filenames, file extensions,
pathnames, dummy URLs, user input, and Twitter handles are shown as follows: "Set original weight estimation at w_0 = 100g to initialize and a counter."
A block of code is set as follows:
```
# Create datasource for training
resource = name_id_generation('DS', 'training', trial)
print("Creating datasources for training (%s)"% resource['Name'] )
Any command-line input or output is written as follows:
$ aws s3 cp data/titanic.csv s3://aml.packt/data/ch9/
```

This book requires an AWS account, and the ability to run Python 3 (3.5) code examples. We use the latest Anaconda distribution (conda 4.3). The reader should also be able to run basic shell commands in a terminal. Note that the Amazon Machine Learning service is not part of the free tier AWS offer.

## Related Products
* [Amazon S3 Essentials](https://www.packtpub.com/virtualization-and-cloud/amazon-s3-essentials?utm_source=github&utm_medium=repository&utm_campaign=9781783554898)

* [Getting Started with Amazon Redshift](https://www.packtpub.com/big-data-and-business-intelligence/getting-started-amazon-redshift?utm_source=github&utm_medium=repository&utm_campaign=9781782178088)

* [Amazon SimpleDB Developer Guide](https://www.packtpub.com/application-development/amazon-simpledb-developer-guide?utm_source=github&utm_medium=repository&utm_campaign=9781847197344)

### Suggestions and Feedback
[Click here](https://docs.google.com/forms/d/e/1FAIpQLSe5qwunkGf6PUvzPirPDtuy1Du5Rlzew23UBp2S-P3wB-GcwQ/viewform) if you have any feedback or suggestions.
