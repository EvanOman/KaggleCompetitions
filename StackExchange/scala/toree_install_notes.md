## Make sure Docker is installed

## Make Toree
Run: `make pip-release`

## Install Toree
Run the following:
```
pip install toree-$(BASE_VERSION).tar.gz
jupyter toree install --interpreters=PySpark,SQL,Scala,SparkR
jupyter notebook --ip=* --no-browser
```
