
![Logo](https://i.pinimg.com/736x/97/51/50/97515045ac221a971e70cacc6e78a3d9.jpg)



# E-commerce shop on Spring framework

"Mironservice-shop" is a pet-project that shows my understanding and abilty to use various popular architecture designs with the most popular technology stack for Java backend development


## Tech Stack
- **Java 17**

- **PostgreSQL**

- **Kafka**

- **Spring MVC**

- **Maven**

- **Docker**

- **Rest**

- **Hibernate**

- **Microservice architecture**

- **Event driven architecture**
## Installation


1. Clone the repository
```bash
  git clone https://github.com/DaniilMyron/eda_spring_shop.git
```
2. Change settings_example.xml to settings.xml and set your configurations in it

3. Deploy core and security modules to your packages repo:

```bash
  PS <path_to_core/security_modules> mvn deploy -U
```
and change links on it in pom.xml in user, product and carting services:

    <repositories>
        <repository>
            <id>github</id>
            <url>https://maven.pkg.github.com/DaniilMyron/eda_spring_shop</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

    <distributionManagement>
        <repository>
            <id>github</id>
            <url>https://maven.pkg.github.com/DaniilMyron/eda_spring_shop</url>
        </repository>
    </distributionManagement>
## Usage

To start the programm you simply need to run the docker-compose:

```bash
docker-compose run --rm app_name
```


## Contact
My LinkedIn: [LinkedIn profile](https://www.linkedin.com/in/danylo-myroshnichenko-a851b7319/)

My Gmail: mironn206@gmail.com

Project link: [https://github.com/DaniilMyron/eda_spring_shop](https://github.com/DaniilMyron/eda_spring_shop)