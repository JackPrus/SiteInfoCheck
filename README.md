# SiteInfoCheck
Telegram bot analyzing information on site Football Club Juventus and AC Milano and sending information about tickets availability


Make shure that webdriver installed. In current project is src\main\resources\msedgedriver.exe . On local maschine must be installed Microsoft Edge. 
On heruku this is not obligative. 


In order to run webdriver and browser we need to install them on ubuntu platform taht use heroku. 

Instalation process on heruku we must do by set of configuration. 

On the settigs of deployed application we have to put into configuration: 

CHROMEDRIVER_PATH   -   /app/.chromedriver/bin/chromedriver

GOOGLE_CHROME_BIN   -   /app/.apt/usr/bin/google-chrome

JAVA_TOOL_OPTION    -   -Xmx1024m -Dwebdriver.chrome.driver=/app/.chromedriver/bin/chromedriver SiteInfoCheck-0.0.1-SNAPSHOT.jar.jar


Next we need to add following links of google chrome and chrome driver to buildpack of settings
https://github.com/heroku/heroku-buildpack-google-chrome
https://github.com/heroku/heroku-buildpack-chromedriver
