import requests
import os

def downloadAll():
    if (os.path.isdir('./dat')):
        print("Data already downloaded")
    else:
        baseURL = "https://www.kaggle.com/c/transfer-learning-on-stack-exchange-tags/data?"
        fNames = ["cooking.csv.zip"]
        # Kaggle Username and Password
        try:
           uname, passw = (os.environ['uname'], os.environ['passw'])
        except KeyError:
            print("You forgot to set your username and password!!\nExitting!")
            return

        kaggle_info = {'UserName': uname, 'Password': passw}
        print("Attempting to download files!!")
        for fileName in fNames:
            # Attempts to download the CSV file. Gets rejected because we are not logged in.
            r = requests.get(baseURL + fileName)
            print("Downloading: " + r.url)
            # Login to Kaggle and retrieve the data.
            r = requests.post(r.url, data = kaggle_info)
            # Writes the data to a local file one chunk at a time.
            f = open(fileName, 'wb')
            for chunk in r.iter_content(chunk_size = 512 * 1024): # Reads 512KB at a time into memory
                if chunk: # filter out keep-alive new chunks
                    f.write(chunk)
                f.close()


if (__name__ == "__main__"):
    downloadAll()
