import requests
import os

def downloadAll():

	if (os.path.isdir('./dat'))
		print("Data already downloaded")
	else:
		baseURL = "https://www.kaggle.com/c/santander-product-recommendation/download/"
		fNames = ["sample_submission.csv.zip", "test_ver2.csv.zip", "train_ver2.csv.zip"]

		# Kaggle Username and Password
		kaggle_info = {'UserName': "my_username", 'Password': "my_password"}
		for fileName in fNames:
			# Attempts to download the CSV file. Gets rejected because we are not logged in.
			r = requests.get(baseURL + fileName)

			# Login to Kaggle and retrieve the data.
			r = requests.post(r.url, data = kaggle_info, prefetch = False)
			downloadFile(baseURL+file, file, r)

			# Writes the data to a local file one chunk at a time.
			f = open(local_filename, 'w')
			for chunk in r.iter_content(chunk_size = 512 * 1024): # Reads 512KB at a time into memory
				if chunk: # filter out keep-alive new chunks
					f.write(chunk)
			f.close()
