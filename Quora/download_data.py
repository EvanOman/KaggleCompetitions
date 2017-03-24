#!/usr/local/bin/python3

import requests, os, sys, shutil, zipfile, fire
import ssl

class Download_Data(object): 
    def dl_all(self, uname, passw, optCmd):

        # Clean the data prompts if prompted
        if optCmd is "clean":
                print("Clean: removing ./dat folder\n\n")
                shutil.rmtree("./dat")

        # if the data directory exists, do nothing
        if (os.path.isdir('./dat')):
            print("Data already downloaded")

        # otherwise download teh dataz
        else:

            # website specifics
            # TODO: this really could be refactored into a function which accepts these as params
            baseURL = "https://www.kaggle.com/c/quora-question-pairs/download/"
            fNames = ["test.csv.zip", "train.csv.zip"]
            
            # make the dir
            os.makedirs("dat")
            
            # kaggle user account info 
            kaggle_info = {'UserName': uname, 'Password': passw}

            # download and extract each file
            for fileName in fNames:

                print("##############")
                
                # Attempts to download the CSV file. Gets rejected because we are not logged in.
                r = requests.get(baseURL + fileName)
                
                print("Downloading: " + fileName)
                
                # Login to Kaggle and retrieve the data.
                r = requests.post(r.url, data = kaggle_info)
                
                # Writes the data to a local file one chunk at a time.
                f = open("./dat/" + fileName, 'wb')
                
                # write each chunk to the file
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
    # perform google fire magic
    fire.Fire(Download_Data)