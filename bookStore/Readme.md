# BookStore
Bookstore is a simple Java Spring Boot application to store and sell books. It simply stores and sells books.

---
# Functionality And Approach Taken:
BookStore uses in memory H2 database and consists of only one table named: “book”.  The book table consist of following columns: <ul><li>1)isbn_number</li><li> 2)author</li><li> 3)title</li><li> 4)price </li><li>5)total_count</li></ul>‘total_count’ column helps to keep the records of the book in the store.
Jpa/Hibernate is used to communicate with database. In java project the database structure is present in “entity” and "repository" folder.
BookStore  supports following functionality:
<ul>
<li>Addition of book:<ul>
<li> “/api/v1/add-new-book”: If the book is not registered before, it should be registered with unique ISBN number and the amount of books to be added can be specified.</li>
<li> If we try to add the book with same ISBN number that are registered already in the database, it'll throw us Bad Request exception.</li>
</ul>
<li>Get All Books: It gets all the books that are registered. The url for this function is: “api/v1/books/”.</li>
<li>Get books by title and author name: The end point for this function is same as of get all books but we need to append either title or author name as query parameter with this endpoint:”api/v1/books/?title={title}&author={author}”. Here title and author is the query parameter which will return us the results for partial matches too.</li>
<li>Buy a book: The end point is “/api/v1/buy-books/{isbnNumber}/{quantity}” where isbnNumber and quantity is path variable. While updating if “isbnNumber” in the path variable doesn't match any records in the table, it gives exception. Also, if there is a match and if we passed the quantity value greater than the available stock quantity, it gives Exception.</li>
<li>Search media coverage about a book, given its ISBN: The end point is “/api/v1/media-coverage/{isbnNumber}”. Here the isbnNumber is path variable. It will get us the book title from the public rest API "https://jsonplaceholder.typicode.com/posts" for the passed ISBN number </li>
<li>Unit Tests: The unit test is done using Mockito and junit. It has over 89% line code coverage. Jacoco can also be used to generate unit test report. For more information on jacoco look into step 8 of section:Steps to run the application.</li>
</ul>

---
# Tools/Framework
<ul>
<li>H2: H2 is a relational database management system written in Java. It can be embedded in Java applications or run in client-server mode. The software is available as open source software.</li>
<li>JPA: Java Persistence API(JPA) is a Java programming interface specification that describes the management of relational data in applications using Java Platform.</li>
<li>JUNIT/Mockito: Junit is the unit testing framework for the Java programming language. Mockito is a mocking framework.</li>
<li>Jacoco: Jacoco is a Java Code Coverage tool. The jacoco maven plugin is used in this project. This generate unit test code coverage report.</li>
</ul>

---
# Steps To Run The Application
<ul>
<li>There is no specific plugin required to be installed to run this application. This project uses H2 in memory DB. So, it will be automatically created when the application is started. You can open up the H2 console from the following url: http://localhost:8080/h2/login.jsp</li>
<li>Now you can run your application. The table “book” will be automatically created. By default, it runs in port:8080.</li>
<li>You can also use jacoco plugins to generate unit test reports. For that go to the project root folder and run the following two commands in command prompt:
    <ul><li>mvn clean test</li>
    <li>mvn jacoco:report</li>
    </ul>    
</li>
</ul>
