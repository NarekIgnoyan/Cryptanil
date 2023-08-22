#  Cryptanil

Cryptanil is a Android library to accept crypto payments

## Installation

### Step 1.
Add the JitPack repository to your build file, in your root build.gradle at the end of repositories:

```gradle
 repositories {
    ...
    maven { url 'https://jitpack.io' } 
 }     
```

### Step 2
Add the dependency:

```gradle
 dependencies { 
    implementation 'com.github.narekignoyan:Cryptanil:1.0.2' 
 }
```
##

## Usage

### Call createOrder method or use [API](https://documenter.getpostman.com/view/6681805/2s8YzXwgGb#261c5b7a-f5d7-4288-848e-69766e1491cb) to get transactionID:
 
```kotlin
 CryptanilApp.createOrder(yourKey, 
        { transactionID ->
            launchCryptanil(transactionID)
        },
        {
           //Handle status if needed
        })
```

### After getting transactionID:
```kotlin
 CryptanilApp.start(this,Language.EN, transactionID,optional_RequestCode)
```

### Or use registerForActivityResult and pass Intent to it:
```kotlin
 yourReceiver.launch(
            CryptanilApp.createIntent(
                this,
                Language.EN,
                transactionID
            )
        )
```

### To get data from result intent use:
```kotlin
CryptanilApp.getOrderInformation(resultIntent)
```
##
### License

[MIT](https://choosealicense.com/licenses/mit/)
