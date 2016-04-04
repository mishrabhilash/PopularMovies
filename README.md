# PopularMovies
## Adding Your API keys
 - Open build.gradle file of app module.
 - There in following code:
```sh
    debug {
        buildConfigField "String", "API_KEY", "\"[PASTE-APIKEY-HERE]\""
        }
        release {
            buildConfigField "String", "API_KEY", "\"[PASTE-APIKEY-HERE]\""
        }
```
replace ```[PASTE-APIKEY-HERE]``` with your API key.
