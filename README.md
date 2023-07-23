# BookLibrary

This is a book library service.

## Functionality

This service, exposes REST APIs for borrowing books. At the moment, only one hardcoded book
with ID "f0249d34-945c-4b8d-9e68-6bafb6fae7e0" can be borrowed. The complete CRUD functionality is coming soon.

## Project structure

The project is developed using **Hexagonal** architecutre (see
e.g. [here](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))).

<<img src="https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)" alt="Hexagonal Architecture" style="width:200px;"/>

For such simple projects, one might favor for a typical **technically driven** layered architecture, rather than a *
*domain driven** (Hexagonal) architecture.
However, the Hexagonal structuring of the code here comes for no additional cost (and the typical Hexagonal architecture
advantages) compared to layered architecture.
This is also because a simplified Hexagonal approach has been adpoted. The packages are organized as follows:

```
└── com                                  
    └── fkh                              
        └── bookapp
            ├── BookApp.kt               
            ├── application
            │   └── service
            │       └── FetchBookService.
            ├── infrastructure
            │   ├── Configs.kt           
            │   └── adapters
            │       └── input
            │           └── graphql      
            │               ├── FetchBook
            │               └── GraphqlEr
            └── model
                ├── Book.kt              
                ├── DomainError.kt       
                └── ports
                    ├── AuthorFinder.kt  
                    └── BookStore.kt     
```
