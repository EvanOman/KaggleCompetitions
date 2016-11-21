#!/usr/bin/python

import requests, os, sys, shutil, zipfile
import ssl


def downloadAll():
    if (os.path.isdir('./dat')):
        print("Data already downloaded")
    else:
        baseURL = "https://www.kaggle.com/c/transfer-learning-on-stack-exchange-tags/download/"
        fNames = ["cooking.csv.zip", "crypto.csv.zip", "robotics.csv.zip", "biology.csv.zip", 
                  "sample_submission.csv.zip", "travel.csv.zip", "diy.csv.zip", "test.csv.zip"]
        # Kaggle Username and Password
        try:
           uname, passw = (os.environ['uname'], os.environ['passw'])
        except KeyError:
            print("You forgot to set your username and password!!\nExitting!")
            return
        
        # make the dir
        os.makedirs("dat")
        
        kaggle_info = {'UserName': uname, 'Password': passw}
        for fileName in fNames:
            print("##############")
            # Attempts to download the CSV file. Gets rejected because we are not logged in.
            r = requests.get(baseURL + fileName)
            print("Downloading: " + fileName)
            # Login to Kaggle and retrieve the data.
            r = requests.post(r.url, data = kaggle_info)
            # Writes the data to a local file one chunk at a time.
            f = open("./dat/" + fileName, 'wb')
            for chunk in r.iter_content(chunk_size = 512 * 1024): # Reads 512KB at a time into memory
                if chunk: # filter out keep-alive new chunks
                    f.write(chunk)
            f.close()

            # unzip the new file
            print("Unzipping: " + fileName)
            zip_ref = zipfile.ZipFile("./dat/"+fileName)
            zip_ref.extractall("./dat")
            zip_ref.close()

            # delete the zip file
            print("Deleting: " + fileName)
            os.remove("./dat/"+fileName)
            print("##############")

if (__name__ == "__main__"):
    # check to see if we should clean
    if (len(sys.argv) > 1):
        if (sys.argv[1] == "clean"):
            print("Clean: removing ./dat folder\n\n")
            shutil.rmtree("./dat")
        elif (sys.argv[1] == "help"):
            print("Downloads StackExchange Data from Kaggle\nUsage: downloadData.py [clean]\n\tclean: removes downloaded data")
    downloadAll()
