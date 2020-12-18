from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By

from selenium.common import exceptions

import re
import os
import sys
import requests
import time

class Scraper:
    def scrape(self):
        # starting time counter
        start_time = time.time()
        print ("Starting scraper")
        # setting up properties of driver that will be loaded
        binary = r'C:\\Program Files\\Mozilla Firefox\\firefox.exe'
        options = Options()
        options.add_argument('--headless')#will ensure that firefox stays invisible
        options.add_argument('--disable-gpu')
        options.binary = binary
        cap = DesiredCapabilities().FIREFOX
        cap["marionette"] = True #optional - is a driver to control UI elements - works with gecko driver
        # driver is loaded
        driver = webdriver.Firefox(options=options, capabilities=cap)

        # web URL that will be loaded
        webUrl="http://www.constructioncompanylahore.com/house-construction-rates/"

        costs = []
        try:
            print ("loading webpage")
            # driver loads webURL specified earlier
            driver.get(webUrl)
            # getting webelements with this specific span class - i have checked this from the html
            # of webpage
            costsTemp = driver.find_elements_by_xpath('//span[@class = "et_pb_sum"]')
            # print (costsTemp)
            
            # extracting text from web elements
            for i in costsTemp:
                costs.append(i.text)
        except exceptions.StaleElementReferenceException:
             print ("stale exception raised")
        # print (costs)
        driver.quit()
        print ("program took " + str(time.time() - start_time) + " to run")
        return costs
        
def main():
    scraper = Scraper()
    scraper.scrape()


if __name__ == '__main__':
	# FLAGS, unparsed = parser.parse_known_args()
	main()
