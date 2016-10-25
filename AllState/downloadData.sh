if [ ! -d dat ]; then
    if [ ! -f dat.tgz]; then
	echo "getting data"
	wget www.evanoman.com/dat.tgz
    fi
    echo "extracting data"
    tar -xvf dat.tgz
else
    echo "data already downlaoded"
fi
