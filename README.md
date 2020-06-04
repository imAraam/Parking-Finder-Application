## Introduction

This is an application that intends to solve the common driver’s problem of wasting time to find and secure a parking spot for their vehicle.

Those problems will be solved by providing the user with an application that scans the target location for available parking spots (within a certain pre-set number of miles/km for the search radius) and displays all the available parking locations in the vicinity. The application was intended to prioritise free parking and display that first, however, due to a difficulty curve with managing the Google Places API, it was not implemented.


## Usability and Guidelines

This application is aimed at any and all users who drive. This application is produced under the assumption that the user of the application is the one with the driving license and not a passenger. There will be no accessibility issues as anyone capable of driving should not require any adjustments in the design of the application in comparison to someone disabled with weaker vision for example.

## Design 


#### Layout

The layout design of the application is consistent across all tabs of the app. It is predictable and allows for the user to be able to easily navigate the application without any confusion over where to press to reach what they need. In addition, users can comfortably use the application across different smartphones and tablets and sustain the same level of consistency for the application’s layout. In addition, the application layout aims to be intuitive with its elements having a very clear purpose to the user. This in turn, allows the UI to be smoothly navigable and the user experience to be pleasant. Which will encourage the user to continue using the application in the future.


#### Style

The style of the application is designed using a mixture of a primary and a secondary colour. The primary colour, which is used for the main body and background of the application, is a dark colour to provide an environment that does not strain the eyes of the users. The secondary colour, which is used for smaller sections of the application such as navigation icons, is a light colour to provide the user with clearness in viewing important information.


#### Components 

The application consists of several components that make it easy to the user to know how to correctly use the app. They allow for the UI to be intuitive and user friendly by making the function and use of these components clear to the user. These components intend to be simplistic yet expressive and informative, the user should not spend much time to figure out how to navigate the application. In addition, they are robust and focused on a specific function to avoid unnecessary complexity in the dynamics of the application. All the components are placed based on a hierarchy of importance and maintain a specific function regarding the aims of the application. For example, upon launching the application the user will be welcome with the parking tab where they can instantly begin searching for free parking at a specific area. At the bottom of the tab will be a navigation bar which will allow the user to switch between tabs and return to the parking tab using the same bottom navigation bar available on all tabs in the application. 
This navigation bar consists of 3 tabs at all times (note that when the user is actively on one of the tabs, the icon of that tab will disappear from the navigation bar and the icon of the previous tab will appear). The tabs that are available are, the parking tab, the history tab, the favourites tab, and the settings tab.



## Improvements

As previously mentioned, this is an application that provides its users with information that guides them to finding free parking in the target area. It utilises the Google Place API to scan a targeted area to see where the user could park their car, after it returns the available places that the user may use to park. 
The application was intended to utilise an algorithm that scans through all the available parking spots and choose the ones that are free of charge. After finding the free of charge parking spots, it would clearly display the area in which the user can park their car and it would display the number of hours the user can park there for free. Should there not be any free parking in the area it will resort to the list of parking spots returned by the API and utilise a sorting algorithm to return the closest and cheapest available parking to guide the user to it. That was however, not implemented. Remaining a challenge to implement in the future.
