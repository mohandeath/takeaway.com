# TakeAway


## A quick summary
The Application shows a list of restaurants sorted by :
  - favorites on top ↝ sorted by availability then sorting value
  - the Availability (open ↝ order ahead ↝ closed) : each one sorted by `sortType`
  - sorting value for the rest of the list

↝ Users are able to filter the results by searching in the title of the restaurants
↝ Users are also able to add/remove restaurants to favorites

⚠️ in order to deliver a smooth user experience i've managed to delay the response given by the repository by 1sec
  this way users can see the loading as to get noticed when sortings are changed.


# Technologies
- Kotlin, App is written fully in Kotlin.
- MVVM used for a clean separation of concerns and easy testing.
- RxJava is used for reactive programming between layers also delivering a smooth yet optimized search experience
- Dagger2 used for dependency injection
- Room for persisting the favorite restaurants as requested
- Mockito combined with JUnit used for Unit Testing.

## App Architecture
I used MVVM architecture for this application.
Seperated the layers (View -> ViewModel -> Repository -> DataSource) for scalability and easy testing.
Tried to avoid over engeneering and keep the implementation well-balanced for the scale of this solution
with an eye for scalability in some places (like `IDataSource`).
There are some comments in the code that explains my reasoning.

## Unit Tests
Tred to make the unit tests as generic as possible so that changes in the sample data has the least effects on test

## Wishlist
- Adding a gridlayout for tablets in order to make the view optimised for bigger screen sizes! I've tried to make a clean and simple user interface for mobile devices. However, I've make all `res/diemens` ready to rewrite for other screensizes to make it ready for implement.







# More information
You can email me at `alirezaakian@gmail.com` if you have any question. Thanks.
