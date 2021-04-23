# CS2365_Project1
OnlineShoppingSystem Project for CS 2365

### Core
Handles the threads which execute the Online Shopping System and Banking System processes asynchronously.
This is the class that is used to run all the code.
Also contains a reference to the message buffer, allowing transaction requests to be processed synchronously.

### Banking System
Handles i/o for bank data requests.

### Online Shopping System
Handles UI and data management for an online shopping system.
Generates, submits, and receives Banking System data requests but does not directly access bank data.

<b>OrderItem Status Guide:</b></br>
<b>0:</b> In Cart<br/>
<b>1:</b> Purchased and Unfilled<br/>
<b>2:</b> Filled<br/>
<b>3:</b> Shipped<br/>

<b>Order Status Guide:</b></br>
<b>0:</b> Not Purchased<br/>
<b>1:</b> Purchased, not Started<br/>
<b>2:</b> Partially Filled<br/>
<b>3:</b> Filled<br/>
<b>4:</b> Partially Shipped<br/>
<b>5:</b> Shipped<br/>

<b>Order Delivery Guide:</b></br>
<b>0:</b> In-Store Pickup<br/>
<b>1:</b> Mail Delivery<br/>

##To-Do:
Improve navigation with more back/logout/exit/view cart buttons    
    